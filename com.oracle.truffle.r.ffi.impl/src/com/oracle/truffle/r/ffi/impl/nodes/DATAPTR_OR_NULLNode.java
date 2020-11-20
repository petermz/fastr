/*
 * Copyright (c) 2020, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.r.ffi.impl.nodes;

import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;
import com.oracle.truffle.api.profiles.ConditionProfile;
import com.oracle.truffle.r.runtime.data.altrep.AltVecClassDescriptor;
import com.oracle.truffle.r.runtime.data.altrep.AltrepUtilities;
import com.oracle.truffle.r.runtime.data.model.RAbstractAtomicVector;
import com.oracle.truffle.r.runtime.ffi.AltrepRFFI;

@GenerateUncached
@ImportStatic(AltrepUtilities.class)
public abstract class DATAPTR_OR_NULLNode extends FFIUpCallNode.Arg1 {
    public static DATAPTR_OR_NULLNode create() {
        return DATAPTR_OR_NULLNodeGen.create();
    }

    @Specialization(guards = "isAltrep(altVec)")
    protected Object doForAltrep(RAbstractAtomicVector altVec,
                    @Cached ConditionProfile hasDataptrOrNullRegisteredProfile,
                    @Cached AltrepRFFI.DataptrOrNullNode dataptrOrNullNode) {
        AltVecClassDescriptor classDescriptor = AltrepUtilities.getAltVecClassDescriptor(altVec);
        if (hasDataptrOrNullRegisteredProfile.profile(classDescriptor.isDataptrOrNullMethodRegistered())) {
            return dataptrOrNullNode.execute(altVec);
        } else {
            return new NullPointer();
        }
    }

    /**
     * For normal vectors we want to return NULL by default, because we do not want to allocate any
     * off-heap native memory.
     * 
     * @return NULL
     */
    @Specialization(guards = "!isAltrep(vector)")
    protected Object doForNormalVectors(@SuppressWarnings("unused") RAbstractAtomicVector vector) {
        return new NullPointer();
    }

    @ExportLibrary(InteropLibrary.class)
    protected static class NullPointer implements TruffleObject {
        @ExportMessage
        public boolean isNull() {
            return true;
        }

        @ExportMessage
        public boolean isPointer() {
            return true;
        }

        @ExportMessage
        public long asPointer() {
            return 0;
        }
    }
}
