/*
 * Copyright (c) 2017, 2020, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.r.runtime.nmath;

import com.oracle.truffle.r.runtime.context.RContext;
import com.oracle.truffle.r.runtime.nmath.distr.SExp;
import com.oracle.truffle.r.runtime.nmath.distr.SNorm;
import com.oracle.truffle.r.runtime.nodes.RBaseNode;
import com.oracle.truffle.r.runtime.nodes.RBaseNodeWithWarnings;
import com.oracle.truffle.r.runtime.rng.RRNG;
import com.oracle.truffle.r.runtime.rng.RRNG.NormKind;
import com.oracle.truffle.r.runtime.rng.RandomNumberGenerator;

/**
 * Defines common interface for math functions generating a random scalar value, which is used to
 * implement common code for the vectorized versions.
 */
public class RandomFunctions {
    public interface RandFunction3_DoubleBase {
        double execute(double a, double b, double c, RandomNumberProvider rand);
    }

    public abstract static class RandFunction3_DoubleWithWarnings extends RBaseNodeWithWarnings implements RandFunction3_DoubleBase {

    }

    public abstract static class RandFunction3_Double extends RBaseNode implements RandFunction3_DoubleBase {

    }

    public abstract static class RandFunction2_Double extends RandFunction3_Double {
        public abstract double execute(double a, double b, RandomNumberProvider rand);

        @Override
        public final double execute(double a, double b, double c, RandomNumberProvider rand) {
            return execute(a, b, rand);
        }
    }

    public abstract static class RandFunction1_Double extends RandFunction3_Double {
        public abstract double execute(double a, RandomNumberProvider rand);

        @Override
        public final double execute(double a, double b, double c, RandomNumberProvider rand) {
            return execute(a, rand);
        }
    }

    /**
     * Convenient wrapper of the current random number generator and current "norm kind" value.
     */
    public static final class RandomNumberProvider {
        final RandomNumberGenerator generator;
        final NormKind normKind;

        public RandomNumberProvider(RandomNumberGenerator generator, NormKind normKind) {
            this.generator = generator;
            this.normKind = normKind;
        }

        public static RandomNumberProvider fromCurrentRNG(RContext ctx) {
            return new RandomNumberProvider(RRNG.currentGenerator(ctx), RRNG.currentNormKind(ctx));
        }

        public static RandomNumberProvider fromCurrentRNG() {
            return fromCurrentRNG(RContext.getInstance());
        }

        public boolean isSame(RandomNumberProvider other) {
            return this.generator == other.generator && this.normKind == other.normKind;
        }

        public double unifRand() {
            return generator.genrandDouble();
        }

        public double normRand() {
            return SNorm.normRand(generator, normKind);
        }

        public double expRand() {
            return SExp.expRand(generator);
        }
    }
}
