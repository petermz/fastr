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
package com.oracle.truffle.r.ffi.impl.upcalls;

import com.oracle.truffle.r.ffi.impl.nodes.PreserveObjectNode;
import com.oracle.truffle.r.ffi.impl.nodes.ProtectNode;
import com.oracle.truffle.r.ffi.impl.nodes.ProtectWithIndexNode;
import com.oracle.truffle.r.ffi.impl.nodes.ReleaseObjectNode;
import com.oracle.truffle.r.ffi.impl.nodes.ReprotectNode;
import com.oracle.truffle.r.ffi.impl.nodes.UnprotectNode;
import com.oracle.truffle.r.ffi.impl.nodes.UnprotectPtrNode;
import com.oracle.truffle.r.ffi.processor.RFFICpointer;
import com.oracle.truffle.r.ffi.processor.RFFIUpCallNode;

public interface MemoryUpCallsRFFI {
    // Checkstyle: stop method name check

    Object R_MakeWeakRef(Object key, Object val, Object fin, long onexit);

    Object R_MakeWeakRefC(Object key, Object val, @RFFICpointer Object finFunction, int onexit);

    Object R_WeakRefKey(Object w);

    Object R_WeakRefValue(Object w);

    @RFFIUpCallNode(PreserveObjectNode.class)
    void R_PreserveObject(Object obj);

    @RFFIUpCallNode(ReleaseObjectNode.class)
    void R_ReleaseObject(Object obj);

    @RFFIUpCallNode(ProtectNode.class)
    Object Rf_protect(Object x);

    @RFFIUpCallNode(UnprotectNode.class)
    void Rf_unprotect(int x);

    @RFFIUpCallNode(ProtectWithIndexNode.class)
    int R_ProtectWithIndex(Object x);

    @RFFIUpCallNode(ReprotectNode.class)
    void R_Reprotect(Object x, int y);

    @RFFIUpCallNode(UnprotectPtrNode.class)
    void Rf_unprotect_ptr(Object x);

    @RFFICpointer
    Object R_alloc(int n, int size);
}
