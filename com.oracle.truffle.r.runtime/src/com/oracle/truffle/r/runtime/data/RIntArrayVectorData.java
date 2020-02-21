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

import static com.oracle.truffle.r.runtime.data.VectorDataLibrary.initInputNACheck;
import static com.oracle.truffle.r.runtime.data.model.RAbstractVector.ENABLE_COMPLETE;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Cached.Shared;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.profiles.LoopConditionProfile;
import com.oracle.truffle.r.runtime.RRuntime;
import com.oracle.truffle.r.runtime.RType;
import com.oracle.truffle.r.runtime.data.VectorDataLibrary.RandomAccessIterator;
import com.oracle.truffle.r.runtime.data.VectorDataLibrary.RandomAccessWriteIterator;
import com.oracle.truffle.r.runtime.data.VectorDataLibrary.SeqIterator;
import com.oracle.truffle.r.runtime.data.VectorDataLibrary.Iterator;
import com.oracle.truffle.r.runtime.data.VectorDataLibrary.SeqWriteIterator;
import com.oracle.truffle.r.runtime.data.model.RAbstractVector;
import com.oracle.truffle.r.runtime.ops.na.InputNACheck;

import java.util.Arrays;

@ExportLibrary(VectorDataLibrary.class)
class RIntArrayVectorData implements TruffleObject, VectorDataWithOwner {
    private final int[] data;
    // this flag is used only to initialize the complete flag in the owner,
    // from then on, we read/write the owner's complete flag
    private final boolean dataInitiallyComplete;
    private RIntVector owner;

    RIntArrayVectorData(int[] data, boolean complete) {
        this.data = data;
        this.dataInitiallyComplete = complete && ENABLE_COMPLETE;
    }

    @Override
    public void setOwner(RAbstractVector newOwner) {
        owner = (RIntVector) newOwner;
        owner.setComplete(dataInitiallyComplete);
    }

    @ExportMessage
    public final RType getType() {
        return RType.Integer;
    }

    @ExportMessage
    public int getLength() {
        return data.length;
    }

    @ExportMessage
    public RIntArrayVectorData materialize() {
        return this;
    }

    @ExportMessage
    public boolean isWriteable() {
        return true;
    }

    @ExportMessage
    public RIntArrayVectorData copy(@SuppressWarnings("unused") boolean deep) {
        return new RIntArrayVectorData(Arrays.copyOf(data, data.length), isComplete());
    }

    @ExportMessage
    public RIntArrayVectorData copyResized(int newSize, @SuppressWarnings("unused") boolean deep, boolean fillNA) {
        int[] newData = Arrays.copyOf(data, newSize);
        if (fillNA) {
            Arrays.fill(newData, data.length, newData.length, RRuntime.INT_NA);
        }
        return new RIntArrayVectorData(newData, isComplete());
    }

    @ExportMessage
    public boolean isComplete() {
        return owner != null && owner.isComplete() && ENABLE_COMPLETE;
    }

    @ExportMessage
    public int[] getReadonlyIntData() {
        return data;
    }

    @ExportMessage
    public int[] getIntDataCopy() {
        return Arrays.copyOf(data, data.length);
    }

    // Read access to the elements:

    @ExportMessage
    public SeqIterator iterator(@Shared("SeqItLoopProfile") @Cached("createCountingProfile()") LoopConditionProfile loopProfile) {
        SeqIterator it = new SeqIterator(data, data.length);
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
        return new RandomAccessIterator(data);
    }

    @ExportMessage
    public int getIntAt(int index) {
        return data[index];
    }

    @ExportMessage
    public int getNextInt(SeqIterator it) {
        return getStore(it)[it.getIndex()];
    }

    @ExportMessage
    public int getInt(RandomAccessIterator it, int index) {
        return getStore(it)[index];
    }

    // Write access to the elements:

    @ExportMessage
    public SeqWriteIterator writeIterator(boolean inputIsComplete, @Shared("inputNACheck") @Cached InputNACheck naCheck) {
        initInputNACheck(naCheck, inputIsComplete, isComplete());
        return new SeqWriteIterator(data, data.length, inputIsComplete);
    }

    @ExportMessage
    public RandomAccessWriteIterator randomAccessWriteIterator(boolean inputIsComplete, @Shared("inputNACheck") @Cached InputNACheck naCheck) {
        initInputNACheck(naCheck, inputIsComplete, isComplete());
        return new RandomAccessWriteIterator(data, inputIsComplete);
    }

    @ExportMessage
    public void commitWriteIterator(SeqWriteIterator iterator, @Shared("inputNACheck") @Cached InputNACheck naCheck) {
        iterator.commit();
        commitWrites(naCheck, iterator.inputIsComplete);
    }

    @ExportMessage
    public void commitRandomAccessWriteIterator(RandomAccessWriteIterator iterator, @Shared("inputNACheck") @Cached InputNACheck naCheck) {
        iterator.commit();
        commitWrites(naCheck, iterator.inputIsComplete);
    }

    private void commitWrites(InputNACheck naCheck, boolean inputIsComplete) {
        if (naCheck.needsResettingCompleteFlag() && !inputIsComplete) {
            owner.setComplete(false);
        }
    }

    @ExportMessage
    public void setIntAt(int index, int value, InputNACheck naCheck) {
        naCheck.check(value);
        data[index] = value;
        if (naCheck.needsResettingCompleteFlag()) {
            owner.setComplete(false);
        }
    }

    @ExportMessage
    public void setNextInt(SeqWriteIterator it, int value, @Shared("inputNACheck") @Cached InputNACheck naCheck) {
        naCheck.check(value);
        getStore(it)[it.getIndex()] = value;
        // complete flag will be updated in commit method
    }

    @ExportMessage
    public void setInt(RandomAccessWriteIterator it, int index, int value, @Shared("inputNACheck") @Cached InputNACheck naCheck) {
        naCheck.check(value);
        getStore(it)[index] = value;
        // complete flag will be updated in commit method
    }

    // Utility methods:

    private static int[] getStore(Iterator it) {
        return (int[]) it.getStore();
    }
}
