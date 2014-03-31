/*
 * Copyright (c) 2013, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.r.nodes.builtin.base;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.dsl.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.r.nodes.builtin.*;
import com.oracle.truffle.r.nodes.unary.*;
import com.oracle.truffle.r.runtime.*;
import com.oracle.truffle.r.runtime.data.*;
import com.oracle.truffle.r.runtime.data.model.*;
import com.oracle.truffle.r.runtime.ops.na.*;

@RBuiltin("diag<-")
public abstract class UpdateDiag extends RBuiltinNode {

    private final NACheck naCheck = NACheck.create();

    @Child protected CastDoubleNode castDouble;

    @Override
    public final boolean getVisibility() {
        return false;
    }

    public static boolean isMatrix(RAbstractVector vector) {
        return vector.hasDimensions() && vector.getDimensions().length == 2;
    }

    // FIXME The following two are workarounds for a Truffle-DSL bug.

    public static boolean isMatrix(RAbstractIntVector vector) {
        return isMatrix((RAbstractVector) vector);
    }

    public static boolean isMatrix(RAbstractDoubleVector vector) {
        return isMatrix((RAbstractVector) vector);
    }

    public static boolean correctReplacementLength(RAbstractVector matrix, RAbstractVector replacement) {
        return replacement.getLength() == 1 || Math.min(matrix.getDimensions()[0], matrix.getDimensions()[1]) == replacement.getLength();
    }

    @SuppressWarnings("unused")
    @Specialization(order = 0, guards = "!isMatrix")
    public RIntVector updateDiagNoMatrix(RAbstractVector vector, RAbstractVector valueVector) {
        controlVisibility();
        throw RError.getOnlyMatrixDiagonals(this.getEncapsulatingSourceSection());
    }

    @SuppressWarnings("unused")
    @Specialization(order = 1, guards = {"isMatrix", "!correctReplacementLength"})
    public RIntVector updateDiagReplacementDiagonalLength(RAbstractVector vector, RAbstractVector valueVector) {
        controlVisibility();
        throw RError.getReplacementDiagonalLength(this.getEncapsulatingSourceSection());
    }

    @Specialization(order = 11, guards = {"isMatrix", "correctReplacementLength"})
    public RAbstractIntVector updateDiag(RIntVector vector, RAbstractIntVector valueVector) {
        controlVisibility();
        RIntVector resultVector = vector;
        if (vector.isShared()) {
            resultVector = (RIntVector) vector.copy();
        }
        int size = Math.min(resultVector.getDimensions()[0], resultVector.getDimensions()[1]);
        int nrow = resultVector.getDimensions()[0];
        int pos = 0;
        naCheck.enable(!resultVector.isComplete());
        for (int i = 0; i < size; i++) {
            resultVector.updateDataAt(pos, valueVector.getDataAt(i % valueVector.getLength()), naCheck);
            pos += nrow + 1;
        }
        return resultVector;
    }

    @Specialization(order = 12, guards = {"isMatrix", "correctReplacementLength"})
    public RAbstractDoubleVector updateDiag(RDoubleVector vector, RAbstractDoubleVector valueVector) {
        controlVisibility();
        RDoubleVector resultVector = vector;
        if (vector.isShared()) {
            resultVector = (RDoubleVector) vector.copy();
        }
        int size = Math.min(resultVector.getDimensions()[0], resultVector.getDimensions()[1]);
        int nrow = resultVector.getDimensions()[0];
        int pos = 0;
        naCheck.enable(!resultVector.isComplete());
        for (int i = 0; i < size; i++) {
            resultVector.updateDataAt(pos, valueVector.getDataAt(i % valueVector.getLength()), naCheck);
            pos += nrow + 1;
        }
        return resultVector;
    }

    @Specialization(order = 13, guards = {"isMatrix", "correctReplacementLength"})
    public RAbstractDoubleVector updateDiag(VirtualFrame frame, RIntVector vector, RAbstractDoubleVector valueVector) {
        controlVisibility();
        if (castDouble == null) {
            CompilerDirectives.transferToInterpreter();
            castDouble = insert(CastDoubleNodeFactory.create(null, false, false));
        }
        RDoubleVector resultVector = (RDoubleVector) castDouble.executeDoubleVector(frame, vector);
        resultVector.copyAttributesFrom(vector);
        return updateDiag(resultVector, valueVector);
    }
}
