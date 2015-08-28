/*
 * This material is distributed under the GNU General Public License
 * Version 2. You may review the terms of this license at
 * http://www.gnu.org/licenses/gpl-2.0.html
 *
 * Copyright (c) 1995-2012, The R Core Team
 * Copyright (c) 2003, The R Foundation
 * Copyright (c) 2014, 2015, Oracle and/or its affiliates
 *
 * All rights reserved.
 */
package com.oracle.truffle.r.runtime.rng;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.r.runtime.*;
import com.oracle.truffle.r.runtime.RError.RErrorException;
import com.oracle.truffle.r.runtime.context.*;
import com.oracle.truffle.r.runtime.data.*;
import com.oracle.truffle.r.runtime.env.*;
import com.oracle.truffle.r.runtime.ffi.*;
import com.oracle.truffle.r.runtime.rng.mm.*;
import com.oracle.truffle.r.runtime.rng.mt.*;
import com.oracle.truffle.r.runtime.rng.user.*;

/**
 * Facade class to the R random number generators, (see src/main/RNG.c in GnuR). The individual
 * generators are implemented in their own class. Currently there are only two implemented, the
 * default, {@link MersenneTwister} and {@link MarsagliaMulticarry}.
 *
 * The fact that the R programmer can set {@code .Random.seed} explicitly, as opposed to the
 * recommended approach of calling {@code set.seed}, is something of a pain as it changes the
 * {@link Kind}, the {@link NormKind} and the actual seeds all in one go and in a totally
 * uncontrolled way, which then has to be checked. Currently we do not support reading it, although
 * we do create/update it when the seed/kind is changed, primarily as a debugging aid. N.B. GnuR
 * updates it on <i>every</i> random number generation!
 */
public class RRNG {
    /**
     * The standard kinds provided by GnuR, where the ordinal value corresponds to the argument to
     * {@link RRNG#doSetSeed}.
     */
    public enum Kind {
        WICHMANN_HILL(false),
        MARSAGLIA_MULTICARRY(true) {
            @Override
            Kind setGenerator() {
                generator = new MarsagliaMulticarry();
                return this;
            }
        },
        SUPER_DUPER(false),
        MERSENNE_TWISTER(true) {
            @Override
            Kind setGenerator() {
                generator = new MersenneTwister();
                return this;
            }
        },
        KNUTH_TAOCP(false),
        USER_UNIF(true) {
            @Override
            Kind setGenerator() {
                generator = new UserRNG();
                return this;
            }
        },
        KNUTH_TAOCP2(false),
        LECUYER_CMRG(false);

        @CompilationFinal static final Kind[] VALUES = values();

        private final boolean available;
        GeneratorPrivate generator;

        /**
         * Lazy setting of the actual generator.
         */
        Kind setGenerator() {
            assert false;
            return this;
        }

        Kind(boolean available) {
            this.available = available;
        }

    }

    public enum NormKind {
        BUGGY_KINDERMAN_RAMAGE,
        AHRENS_DIETER,
        BOX_MULLER,
        USER_NORM,
        INVERSION,
        KINDERMAN_RAMAGE;

        @CompilationFinal static final NormKind[] VALUES = values();

    }

    public static final int NO_KIND_CHANGE = -2; // internal value
    public static final int RESET_KIND = -1; // comes from RNG.R
    public static final Integer RESET_SEED = null;
    private static final Kind DEFAULT_KIND = Kind.MERSENNE_TWISTER;
    private static final NormKind DEFAULT_NORM_KIND = NormKind.INVERSION;
    private static final String RANDOM_SEED = ".Random.seed";
    public static final double I2_32M1 = 2.3283064365386963e-10;
    private static final double UINT_MAX = (double) Integer.MAX_VALUE * 2;
    @CompilationFinal private static final int[] NO_SEEDS = new int[0];

    /**
     * The (logically private) interface that a random number generator must implement.
     */
    public interface GeneratorPrivate {
        void init(int seed) throws RNGException;

        void fixupSeeds(boolean initial);

        int[] getSeeds();

        double genrandDouble();
    }

    /**
     * R Errors and warnings are possible during operations like {@link #doSetSeed}, which are all
     * passed back to the associated builtin.
     */
    public static class RNGException extends RErrorException {
        private static final long serialVersionUID = 1L;

        private final boolean isError;

        public RNGException(RError.Message msg, boolean isError, Object... args) {
            super(msg, args);
            this.isError = isError;
        }

        public boolean isError() {
            return isError;
        }

        @TruffleBoundary
        public static RNGException raise(RError.Message message, boolean isError, Object... args) throws RNGException {
            throw new RNGException(message, isError, args);
        }
    }

    public static final class ContextStateImpl implements RContext.ContextState {
        private Kind currentKind;
        private NormKind currentNormKind;

        private ContextStateImpl(Kind currentKind, NormKind currentNormKind) {
            this.currentKind = currentKind;
            this.currentNormKind = currentNormKind;
        }

        void updateCurrentKind(Kind kind) {
            currentKind = kind;
        }

        void updateCurrentNormKind(NormKind normKind) {
            currentNormKind = normKind;
        }

        public static ContextStateImpl newContext(@SuppressWarnings("unused") RContext context) {
            int seed = timeToSeed();
            try {
                initGenerator(DEFAULT_KIND.setGenerator(), seed);
            } catch (RNGException ex) {
                Utils.fail("failed to initialize default random number generator");
            }
            return new ContextStateImpl(DEFAULT_KIND, DEFAULT_NORM_KIND);
        }
    }

    private static ContextStateImpl getContextState() {
        return RContext.getInstance().stateRNG;
    }

    public static int currentKindAsInt() {
        return getContextState().currentKind.ordinal();
    }

    public static int currentNormKindAsInt() {
        return getContextState().currentNormKind.ordinal();
    }

    private static Kind currentKind() {
        return getContextState().currentKind;
    }

    static GeneratorPrivate currentGenerator() {
        return getContextState().currentKind.generator;
    }

    private static NormKind currentNormKind() {
        return getContextState().currentNormKind;
    }

    /**
     * Ask the current generator for a random double. (cf. {@code unif_rand} in RNG.c.
     */
    public static double unifRand() {
        return currentGenerator().genrandDouble();
    }

    /**
     * Set the seed and optionally the RNG kind and norm kind.
     *
     * @param seed {@link #RESET_SEED} to reset, else new seed
     * @param kindAsInt {@link #NO_KIND_CHANGE} for no change, else ordinal value of new
     *            {@link Kind}.
     * @param normKindAsInt {@link #NO_KIND_CHANGE} for no change, else ordinal value of new
     *            {@link NormKind}.
     */
    public static void doSetSeed(Integer seed, int kindAsInt, int normKindAsInt) throws RNGException {
        int newSeed = seed == RESET_SEED ? timeToSeed() : seed;
        changeKindsAndInitGenerator(newSeed, kindAsInt, normKindAsInt);
        updateDotRandomSeed();
    }

    /**
     * Set the kind and optionally the norm kind, called from R builtin {@code RNGkind}. GnuR
     * chooses the new seed from the previous RNG.
     */
    public static void doRNGKind(int kindAsInt, int normKindAsInt) throws RNGException {
        int newSeed = (int) (unifRand() * UINT_MAX);
        changeKindsAndInitGenerator(newSeed, kindAsInt, normKindAsInt);
        updateDotRandomSeed();
    }

    @TruffleBoundary
    private static void changeKindsAndInitGenerator(int newSeed, int kindAsInt, int normKindAsInt) throws RNGException {
        Kind kind = changeKinds(kindAsInt, normKindAsInt);
        initGenerator(kind, newSeed);
        getContextState().updateCurrentKind(kind);
    }

    private static Kind changeKinds(int kindAsInt, int normKindAsInt) throws RNGException {
        Kind kind;
        @SuppressWarnings("unused")
        NormKind normKind;
        if (kindAsInt != NO_KIND_CHANGE) {
            if (kindAsInt == RESET_KIND) {
                kind = DEFAULT_KIND;
            } else {
                kind = intToKind(kindAsInt);
                if (!kind.available) {
                    throw RNGException.raise(RError.Message.RNG_BAD_KIND, true, kind);
                }
            }
        } else {
            kind = currentKind();
        }
        if (normKindAsInt != NO_KIND_CHANGE) {
            if (normKindAsInt == RESET_KIND) {
                normKind = DEFAULT_NORM_KIND;
            } else {
                normKind = intToNormKind(normKindAsInt);
            }
        }
        return kind;
    }

    public static double fixup(double x) {
        /* transcribed from GNU R, RNG.c (fixup) */
        /* ensure 0 and 1 are never returned */
        if (x <= 0.0) {
            return 0.5 * I2_32M1;
        }
        // removed fixup for 1.0 since x is in [0,1).
        // TODO Since GnuR does include this is should we not for compatibility (probably).
        // if ((1.0 - x) <= 0.0) return 1.0 - 0.5 * I2_32M1;
        return x;
    }

    private static Kind intToKind(int kindAsInt) throws RNGException {
        if (kindAsInt < 0 || kindAsInt >= Kind.VALUES.length) {
            throw RNGException.raise(RError.Message.RNG_NOT_IMPL_KIND, true, kindAsInt);
        }
        return Kind.VALUES[kindAsInt];

    }

    private static NormKind intToNormKind(int normKindAsInt) {
        return NormKind.VALUES[normKindAsInt];
    }

    private static void initGenerator(Kind kind, int aSeed) throws RNGException {
        // Initial scrambling, common to all, from RNG_Init in src/main/RNG.c
        int seed = aSeed;
        for (int i = 0; i < 50; i++) {
            seed = (69069 * seed + 1);
        }
        if (kind.generator == null) {
            kind.setGenerator();
        }
        kind.generator.init(seed);
    }

    @SuppressWarnings("unused")
    private static Object getDotRandomSeed(VirtualFrame frame) {
        // TODO try to find .Random.seed in R_GlobalEnv
        throw RInternalError.unimplemented("getDotRandomSeed");
    }

    private static void updateDotRandomSeed() {
        int[] seeds = currentKind().generator.getSeeds();
        if (seeds == null) {
            seeds = NO_SEEDS;
        }
        int[] data = new int[seeds.length + 1];
        data[0] = currentKind().ordinal() + 100 * currentNormKind().ordinal();
        for (int i = 0; i < seeds.length; i++) {
            data[i + 1] = seeds[i];
        }
        RIntVector vector = RDataFactory.createIntVector(data, RDataFactory.COMPLETE_VECTOR);
        REnvironment.globalEnv().safePut(RANDOM_SEED, vector);
    }

    /**
     * Create a random integer.
     */
    public static Integer timeToSeed() {
        int pid = RFFIFactory.getRFFI().getBaseRFFI().getpid();
        int millis = (int) (System.currentTimeMillis() & 0xFFFFFFFFL);
        return (millis << 16) ^ pid;
    }

}
