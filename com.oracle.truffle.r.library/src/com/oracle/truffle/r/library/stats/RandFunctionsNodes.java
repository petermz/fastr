/*
 * Copyright (c) 1995, 1996, 1997  Robert Gentleman and Ross Ihaka
 * Copyright (c) 1998-2013, The R Core Team
 * Copyright (c) 2003-2015, The R Foundation
 * Copyright (c) 2016, 2020, Oracle and/or its affiliates
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, a copy is available at
 * https://www.R-project.org/Licenses/
 */

package com.oracle.truffle.r.library.stats;

import static com.oracle.truffle.r.nodes.builtin.CastBuilder.Predef.abstractVectorValue;
import static com.oracle.truffle.r.nodes.builtin.CastBuilder.Predef.missingValue;
import static com.oracle.truffle.r.nodes.builtin.CastBuilder.Predef.nullValue;
import static com.oracle.truffle.r.runtime.RError.SHOW_CALLER;
import static com.oracle.truffle.r.runtime.RError.Message.INVALID_UNNAMED_ARGUMENTS;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

import com.oracle.truffle.api.TruffleLanguage.ContextReference;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.CachedContext;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.profiles.BranchProfile;
import com.oracle.truffle.api.profiles.LoopConditionProfile;
import com.oracle.truffle.r.library.stats.RandFunctionsNodesFactory.ConvertToLengthNodeGen;
import com.oracle.truffle.r.library.stats.RandFunctionsNodesFactory.RandFunction1NodeGen;
import com.oracle.truffle.r.library.stats.RandFunctionsNodesFactory.RandFunction2NodeGen;
import com.oracle.truffle.r.library.stats.RandFunctionsNodesFactory.RandFunction3NodeGen;
import com.oracle.truffle.r.library.stats.RandFunctionsNodesFactory.RandFunctionDoubleExecutorNodeGen;
import com.oracle.truffle.r.library.stats.RandFunctionsNodesFactory.RandFunctionExecutorBaseNodeGen;
import com.oracle.truffle.r.library.stats.RandFunctionsNodesFactory.RandFunctionIntExecutorNodeGen;
import com.oracle.truffle.r.nodes.builtin.NodeWithArgumentCasts.Casts;
import com.oracle.truffle.r.nodes.builtin.RExternalBuiltinNode;
import com.oracle.truffle.r.nodes.profile.VectorLengthProfile;
import com.oracle.truffle.r.nodes.unary.CastIntegerNode;
import com.oracle.truffle.r.runtime.DSLConfig;
import com.oracle.truffle.r.runtime.RError;
import com.oracle.truffle.r.runtime.RRuntime;
import com.oracle.truffle.r.runtime.context.RContext;
import com.oracle.truffle.r.runtime.context.TruffleRLanguage;
import com.oracle.truffle.r.runtime.data.RDataFactory;
import com.oracle.truffle.r.runtime.data.RDoubleVector;
import com.oracle.truffle.r.runtime.data.RIntVector;
import com.oracle.truffle.r.runtime.data.VectorDataLibrary;
import com.oracle.truffle.r.runtime.data.model.RAbstractVector;
import com.oracle.truffle.r.runtime.data.nodes.VectorAccess;
import com.oracle.truffle.r.runtime.data.nodes.VectorAccess.SequentialIterator;
import com.oracle.truffle.r.runtime.nmath.RandomFunctions.RandFunction1_Double;
import com.oracle.truffle.r.runtime.nmath.RandomFunctions.RandFunction2_Double;
import com.oracle.truffle.r.runtime.nmath.RandomFunctions.RandFunction3_DoubleBase;
import com.oracle.truffle.r.runtime.nmath.RandomFunctions.RandomNumberProvider;
import com.oracle.truffle.r.runtime.nodes.RBaseNode;
import com.oracle.truffle.r.runtime.nodes.RBaseNodeWithWarnings;
import com.oracle.truffle.r.runtime.rng.RRNG;

/**
 * Contains infrastructure for R external functions implementing generation of a random value from
 * given random value distribution. To implement such external function, implement one of:
 * {@link RandFunction3_DoubleBase}, {@link RandFunction2_Double} or {@link RandFunction1_Double}.
 */
public final class RandFunctionsNodes {
    private static final RDoubleVector DUMMY_VECTOR = RDataFactory.createDoubleVectorFromScalar(1);

    private RandFunctionsNodes() {
        // static class
    }

    // inspired by the DEFRAND{X}_REAL and DEFRAND{X}_INT macros in GnuR

    /**
     * Converts given value to actual length that should be used as length of the output vector. The
     * argument must be cast using {@link #addLengthCast(Casts)}. Using this node allows us to avoid
     * casting of long vectors to integers if we only need to know their length.
     */
    @ImportStatic(DSLConfig.class)
    protected abstract static class ConvertToLength extends Node {
        public abstract int execute(RAbstractVector value);

        @Specialization(guards = "dataLib.getLength(vector.getData()) == 1")
        public int lengthOne(RAbstractVector vector,
                        @Cached("createNonPreserving()") CastIntegerNode castNode,
                        @CachedLibrary(limit = "getCacheSize(2)") VectorDataLibrary dataLib,
                        @Cached("create()") BranchProfile seenNA) {
            RIntVector vec = (RIntVector) castNode.doCast(vector);
            int result = dataLib.getIntAt(vec.getData(), 0);
            if (RRuntime.isNA(result) || result < 0) {
                seenNA.enter();
                throw RError.error(SHOW_CALLER, INVALID_UNNAMED_ARGUMENTS);
            }
            return result;
        }

        @Specialization(guards = "dataLib.getLength(vector.getData()) != 1", limit = "getVectorAccessCacheSize()")
        public int notSingle(RAbstractVector vector,
                        @CachedLibrary("vector.getData()") VectorDataLibrary dataLib) {
            return dataLib.getLength(vector.getData());
        }

        private static void addLengthCast(Casts casts) {
            casts.arg(0).defaultError(INVALID_UNNAMED_ARGUMENTS).mustBe(abstractVectorValue()).asVector();
        }
    }

    /**
     * Executor node handles the validation, the loop over all vector elements, the creation of the
     * result vector, and similar. The random function is provided as implementation of
     * {@link RandFunction3_DoubleBase}.
     */
    protected abstract static class RandFunctionExecutorBase extends RBaseNode {

        protected final Function<Supplier<? extends RandFunction3_DoubleBase>, RandFunctionIterator> iteratorFactory;
        protected final Supplier<? extends RandFunction3_DoubleBase> functionFactory;

        protected RandFunctionExecutorBase(Function<Supplier<? extends RandFunction3_DoubleBase>, RandFunctionIterator> iteratorFactory, Supplier<? extends RandFunction3_DoubleBase> functionFactory) {
            this.iteratorFactory = iteratorFactory;
            this.functionFactory = functionFactory;
        }

        public abstract RAbstractVector execute(RAbstractVector length, RDoubleVector a, RDoubleVector b, RDoubleVector c, RandomNumberProvider rand);

        @Child private ConvertToLength convertToLength = ConvertToLengthNodeGen.create();
        private final VectorLengthProfile resultVectorLengthProfile = VectorLengthProfile.create();

        @Override
        public final RBaseNode getErrorContext() {
            return RError.SHOW_CALLER;
        }

        protected final RandFunctionIterator createIterator() {
            return iteratorFactory.apply(functionFactory);
        }

        @Specialization(guards = {"randCached.isSame(rand)"})
        protected final RAbstractVector evaluateWithCached(RAbstractVector lengthVec, RDoubleVector a, RDoubleVector b, RDoubleVector c,
                        @SuppressWarnings("unused") RandomNumberProvider rand,
                        @Cached("rand") RandomNumberProvider randCached,
                        @Cached("createIterator()") RandFunctionIterator iterator) {
            int length = resultVectorLengthProfile.profile(convertToLength.execute(lengthVec));
            RBaseNode.reportWork(this, length);
            return iterator.execute(length, a, b, c, randCached);
        }

        @Specialization(replaces = "evaluateWithCached")
        protected final RAbstractVector evaluateFallback(RAbstractVector lengthVec, RDoubleVector a, RDoubleVector b, RDoubleVector c, RandomNumberProvider rand,
                        @Cached("createIterator()") RandFunctionIterator iterator) {
            int length = resultVectorLengthProfile.profile(convertToLength.execute(lengthVec));
            RBaseNode.reportWork(this, length);
            return iterator.execute(length, a, b, c, rand);
        }
    }

    protected abstract static class RandFunctionIterator extends RBaseNodeWithWarnings {

        protected final Supplier<? extends RandFunction3_DoubleBase> functionFactory;
        protected final BranchProfile nanResult = BranchProfile.create();
        protected final BranchProfile nan = BranchProfile.create();
        protected final LoopConditionProfile loopConditionProfile = LoopConditionProfile.createCountingProfile();

        protected RandFunctionIterator(Supplier<? extends RandFunction3_DoubleBase> functionFactory) {
            this.functionFactory = functionFactory;
        }

        protected final RandFunction3_DoubleBase createFunction() {
            return functionFactory.get();
        }

        public abstract RAbstractVector execute(int length, RDoubleVector a, RDoubleVector b, RDoubleVector c, RandomNumberProvider rand);

        static void putRNGState() {
            // Note: we call putRNGState only if we actually changed the state, i.e. called random
            // number generation. We do not need to getRNGState() because the parent wrapper node
            // should do that for us
            RRNG.putRNGState();
        }

        void showNAWarning() {
            warning(RError.Message.NA_PRODUCED);
        }
    }

    protected abstract static class RandFunctionIntExecutorNode extends RandFunctionIterator {

        protected RandFunctionIntExecutorNode(Supplier<? extends RandFunction3_DoubleBase> functionFactory) {
            super(functionFactory);
        }

        @Specialization(guards = {"aAccess.supports(a)", "bAccess.supports(b)", "cAccess.supports(c)"})
        protected RIntVector cached(int length, RDoubleVector a, RDoubleVector b, RDoubleVector c, RandomNumberProvider randProvider,
                        @Cached("createFunction()") RandFunction3_DoubleBase function,
                        @Cached("a.access()") VectorAccess aAccess,
                        @Cached("b.access()") VectorAccess bAccess,
                        @Cached("c.access()") VectorAccess cAccess) {
            try (SequentialIterator aIter = aAccess.access(a); SequentialIterator bIter = bAccess.access(b); SequentialIterator cIter = cAccess.access(c)) {
                if (aAccess.getLength(aIter) == 0 || bAccess.getLength(bIter) == 0 || cAccess.getLength(cIter) == 0) {
                    nanResult.enter();
                    showNAWarning();
                    int[] nansResult = new int[length];
                    Arrays.fill(nansResult, RRuntime.INT_NA);
                    return RDataFactory.createIntVector(nansResult, false);
                }

                boolean nans = false;
                int[] result = new int[length];
                loopConditionProfile.profileCounted(length);
                for (int i = 0; loopConditionProfile.inject(i < length); i++) {
                    aAccess.nextWithWrap(aIter);
                    bAccess.nextWithWrap(bIter);
                    cAccess.nextWithWrap(cIter);
                    double value = function.execute(aAccess.getDouble(aIter), bAccess.getDouble(bIter), cAccess.getDouble(cIter), randProvider);
                    if (Double.isNaN(value) || value <= Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
                        nan.enter();
                        nans = true;
                        result[i] = RRuntime.INT_NA;
                    } else {
                        result[i] = (int) value;
                    }
                }
                putRNGState();
                if (nans) {
                    showNAWarning();
                }
                return RDataFactory.createIntVector(result, !nans);
            }
        }

        @Specialization(replaces = "cached")
        protected RIntVector generic(int length, RDoubleVector a, RDoubleVector b, RDoubleVector c, RandomNumberProvider randProvider,
                        @Cached("createFunction()") RandFunction3_DoubleBase function) {
            return cached(length, a, b, c, randProvider, function, a.slowPathAccess(), b.slowPathAccess(), c.slowPathAccess());
        }
    }

    protected abstract static class RandFunctionDoubleExecutorNode extends RandFunctionIterator {

        protected RandFunctionDoubleExecutorNode(Supplier<? extends RandFunction3_DoubleBase> functionFactory) {
            super(functionFactory);
        }

        @Specialization(guards = {"aAccess.supports(a)", "bAccess.supports(b)", "cAccess.supports(c)"})
        protected RDoubleVector cached(int length, RDoubleVector a, RDoubleVector b, RDoubleVector c, RandomNumberProvider randProvider,
                        @Cached("createFunction()") RandFunction3_DoubleBase function,
                        @Cached("a.access()") VectorAccess aAccess,
                        @Cached("b.access()") VectorAccess bAccess,
                        @Cached("c.access()") VectorAccess cAccess) {
            try (SequentialIterator aIter = aAccess.access(a); SequentialIterator bIter = bAccess.access(b); SequentialIterator cIter = cAccess.access(c)) {
                if (aAccess.getLength(aIter) == 0 || bAccess.getLength(bIter) == 0 || cAccess.getLength(cIter) == 0) {
                    nanResult.enter();
                    showNAWarning();
                    double[] nansResult = new double[length];
                    Arrays.fill(nansResult, RRuntime.DOUBLE_NA);
                    return RDataFactory.createDoubleVector(nansResult, false);
                }

                boolean nans = false;
                double[] result = new double[length];
                loopConditionProfile.profileCounted(length);
                for (int i = 0; loopConditionProfile.inject(i < length); i++) {
                    aAccess.nextWithWrap(aIter);
                    bAccess.nextWithWrap(bIter);
                    cAccess.nextWithWrap(cIter);
                    double value = function.execute(aAccess.getDouble(aIter), bAccess.getDouble(bIter), cAccess.getDouble(cIter), randProvider);
                    if (Double.isNaN(value) || RRuntime.isNA(value)) {
                        nan.enter();
                        nans = true;
                    }
                    result[i] = value;
                }
                putRNGState();
                if (nans) {
                    showNAWarning();
                }
                return RDataFactory.createDoubleVector(result, !nans);
            }
        }

        @Specialization(replaces = "cached")
        protected RDoubleVector generic(int length, RDoubleVector a, RDoubleVector b, RDoubleVector c, RandomNumberProvider randProvider,
                        @Cached("createFunction()") RandFunction3_DoubleBase function) {
            return cached(length, a, b, c, randProvider, function, a.slowPathAccess(), b.slowPathAccess(), c.slowPathAccess());
        }

    }

    public abstract static class RandFunction3Node extends RExternalBuiltinNode.Arg4 {
        @Child private RandFunctionExecutorBase inner;

        protected RandFunction3Node(RandFunctionExecutorBase inner) {
            this.inner = inner;
        }

        public static RandFunction3Node createInt(Supplier<RandFunction3_DoubleBase> function) {
            return RandFunction3NodeGen.create(RandFunctionExecutorBaseNodeGen.create(RandFunctionIntExecutorNodeGen::create, function));
        }

        // Note: for completeness of the API
        public static RandFunction3Node createDouble(Supplier<RandFunction3_DoubleBase> function) {
            return RandFunction3NodeGen.create(RandFunctionExecutorBaseNodeGen.create(RandFunctionDoubleExecutorNodeGen::create, function));
        }

        static {
            Casts casts = new Casts(RandFunction3Node.class);
            ConvertToLength.addLengthCast(casts);
            casts.arg(1).mustBe(nullValue().not(), RError.Message.INVALID_UNNAMED_ARGUMENTS).mustBe(missingValue().not(), RError.Message.ARGUMENT_MISSING, "a").asDoubleVector();
            casts.arg(2).mustBe(nullValue().not(), RError.Message.INVALID_UNNAMED_ARGUMENTS).mustBe(missingValue().not(), RError.Message.ARGUMENT_MISSING, "b").asDoubleVector();
            casts.arg(3).mustBe(nullValue().not(), RError.Message.INVALID_UNNAMED_ARGUMENTS).mustBe(missingValue().not(), RError.Message.ARGUMENT_MISSING, "c").asDoubleVector();
        }

        @Specialization
        protected RAbstractVector evaluate(RAbstractVector length, RDoubleVector a, RDoubleVector b, RDoubleVector c,
                        @CachedContext(TruffleRLanguage.class) ContextReference<RContext> ctxRef) {
            RRNG.getRNGState();
            return inner.execute(length, a, b, c, RandomNumberProvider.fromCurrentRNG(ctxRef.get()));
        }
    }

    public abstract static class RandFunction2Node extends RExternalBuiltinNode.Arg3 {
        @Child private RandFunctionExecutorBase inner;

        protected RandFunction2Node(RandFunctionExecutorBase inner) {
            this.inner = inner;
        }

        public static RandFunction2Node createInt(Supplier<RandFunction2_Double> function) {
            return RandFunction2NodeGen.create(RandFunctionExecutorBaseNodeGen.create(RandFunctionIntExecutorNodeGen::create, function));
        }

        public static RandFunction2Node createDouble(Supplier<RandFunction2_Double> function) {
            return RandFunction2NodeGen.create(RandFunctionExecutorBaseNodeGen.create(RandFunctionDoubleExecutorNodeGen::create, function));
        }

        static {
            Casts casts = new Casts(RandFunction2Node.class);
            ConvertToLength.addLengthCast(casts);
            casts.arg(1).mustBe(nullValue().not(), RError.Message.INVALID_UNNAMED_ARGUMENTS).mustBe(missingValue().not(), RError.Message.ARGUMENT_MISSING, "a").asDoubleVector();
            casts.arg(2).mustBe(nullValue().not(), RError.Message.INVALID_UNNAMED_ARGUMENTS).mustBe(missingValue().not(), RError.Message.ARGUMENT_MISSING, "b").asDoubleVector();
        }

        @Specialization
        protected Object evaluate(RAbstractVector length, RDoubleVector a, RDoubleVector b,
                        @CachedContext(TruffleRLanguage.class) ContextReference<RContext> ctxRef) {
            RRNG.getRNGState();
            return inner.execute(length, a, b, DUMMY_VECTOR, RandomNumberProvider.fromCurrentRNG(ctxRef.get()));
        }
    }

    public abstract static class RandFunction1Node extends RExternalBuiltinNode.Arg2 {
        @Child private RandFunctionExecutorBase inner;

        protected RandFunction1Node(RandFunctionExecutorBase inner) {
            this.inner = inner;
        }

        public static RandFunction1Node createInt(Supplier<RandFunction1_Double> function) {
            return RandFunction1NodeGen.create(RandFunctionExecutorBaseNodeGen.create(RandFunctionIntExecutorNodeGen::create, function));
        }

        public static RandFunction1Node createDouble(Supplier<RandFunction1_Double> function) {
            return RandFunction1NodeGen.create(RandFunctionExecutorBaseNodeGen.create(RandFunctionDoubleExecutorNodeGen::create, function));
        }

        static {
            Casts casts = new Casts(RandFunction1Node.class);
            ConvertToLength.addLengthCast(casts);
            casts.arg(1).mustBe(nullValue().not(), RError.Message.INVALID_UNNAMED_ARGUMENTS).mustBe(missingValue().not(), RError.Message.ARGUMENT_MISSING, "a").asDoubleVector();
        }

        @Specialization
        protected Object evaluate(RAbstractVector length, RDoubleVector a,
                        @CachedContext(TruffleRLanguage.class) ContextReference<RContext> ctxRef) {
            RRNG.getRNGState();
            return inner.execute(length, a, DUMMY_VECTOR, DUMMY_VECTOR, RandomNumberProvider.fromCurrentRNG(ctxRef.get()));
        }
    }
}
