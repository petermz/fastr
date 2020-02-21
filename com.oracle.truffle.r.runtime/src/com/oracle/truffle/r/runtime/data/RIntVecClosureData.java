/*
 * Copyright (c) 2019, 2020, Oracle and/or its affiliates. All rights reserved.
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

package com.oracle.truffle.r.runtime.data;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Cached.Shared;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.profiles.LoopConditionProfile;
import com.oracle.truffle.r.runtime.RType;
import com.oracle.truffle.r.runtime.data.VectorDataLibrary.RandomAccessIterator;
import com.oracle.truffle.r.runtime.data.VectorDataLibrary.SeqIterator;
import com.oracle.truffle.r.runtime.data.closures.RClosure;
import com.oracle.truffle.r.runtime.data.model.RAbstractVector;
import com.oracle.truffle.r.runtime.data.nodes.VectorAccess;
import com.oracle.truffle.r.runtime.data.nodes.VectorAccess.RandomIterator;

@ExportLibrary(VectorDataLibrary.class)
public class RIntVecClosureData implements RClosure, TruffleObject {
    private final RAbstractVector vector;

    public RIntVecClosureData(RAbstractVector vector) {
        this.vector = vector;
    }

    @ExportMessage
    public final RType getType() {
        return RType.Integer;
    }

    @ExportMessage
    public int getLength() {
        return vector.getLength();
    }

    @ExportMessage
    public RIntArrayVectorData materialize() {
        return new RIntArrayVectorData(getIntDataCopy(), false);
    }

    @ExportMessage
    public RIntVecClosureData copy(@SuppressWarnings("unused") boolean deep) {
        // TOD new closure or also new vector?
        return new RIntVecClosureData(this.vector);
    }

    @ExportMessage
    public RIntArrayVectorData copyResized(int newSize, boolean deep, boolean fillNA) {
        throw new RuntimeException("TODO?");
    }

    @ExportMessage
    public int[] getIntDataCopy() {
        int[] res = new int[getLength()];
        for (int i = 0; i < res.length; i++) {
            res[i] = getIntAt(i);
        }
        return res;
    }

    // TODO: the accesses may be done more efficiently with nodes and actually using the "store" in
    // the iterator object

    @ExportMessage
    public SeqIterator iterator(@Shared("SeqItLoopProfile") @Cached("createCountingProfile()") LoopConditionProfile loopProfile) {
        SeqIterator it = new SeqIterator(null, getLength());
        it.initLoopConditionProfile(loopProfile);
        return it;
    }

    @ExportMessage
    public boolean next(SeqIterator it, boolean withWrap,
                    @Shared("SeqItLoopProfile") @Cached("createCountingProfile()") LoopConditionProfile loopProfile) {
        return it.next(loopProfile, withWrap);
    }

    @ExportMessage
    public RandomAccessIterator randomAccessIterator() {
        return new RandomAccessIterator(null);
    }

    @ExportMessage
    public Object getDataAtAsObject(int index) {
        return getIntAt(index);
    }

    @ExportMessage
    public int getIntAt(int index) {
        VectorAccess access = vector.slowPathAccess();
        RandomIterator it = access.randomAccess(vector);
        return access.getInt(it, index);
    }

    @ExportMessage
    public int getNextInt(SeqIterator it) {
        return getIntAt(it.getIndex());
    }

    @ExportMessage
    public int getInt(@SuppressWarnings("unused") RandomAccessIterator it, int index) {
        return getIntAt(index);
    }

    // RClosure overrides:

    @Override
    public Object getDelegateDataAt(int idx) {
        return vector.getDataAtAsObject(idx);
    }

    @Override
    public RAbstractVector getDelegate() {
        return vector;
    }
}
