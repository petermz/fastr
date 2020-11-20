/*
 * Copyright (c) 2014, 2020, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.r.runtime.ffi;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInterface;
import com.oracle.truffle.r.runtime.context.RContext;
import com.oracle.truffle.r.runtime.data.RNull;
import com.oracle.truffle.r.runtime.ffi.DLL.DLLInfo;
import com.oracle.truffle.r.runtime.ffi.DLLRFFI.LibHandle;
import com.oracle.truffle.r.runtime.ffi.RFFIFactory.Type;

/**
 * Support for the {.Call} and {.External} calls.
 */
public interface CallRFFI {
    interface InvokeCallNode extends NodeInterface {

        default Object dispatch(VirtualFrame frame, NativeCallInfo nativeCallInfo, Object[] args) {
            RFFIContext stateRFFI = RContext.getInstance().getStateRFFI();
            DLLInfo dllInfo = nativeCallInfo.dllInfo;
            LibHandle handle = dllInfo == null ? null : dllInfo.handle;
            Type rffiType = handle == null ? stateRFFI.getDefaultRFFIType() : handle.getRFFIType();
            Object before = stateRFFI.beforeDowncall(frame == null ? null : frame.materialize(), rffiType);
            try {
                return execute(nativeCallInfo, args);
            } finally {
                stateRFFI.afterDowncall(before, rffiType, AfterDownCallProfiles.getUncached());
            }
        }

        /**
         * Invoke the native function identified by {@code symbolInfo} passing it the arguments in
         * {@code args}. The values in {@code args} can be any of the types used to represent
         * {@code R} values in the implementation.
         */
        Object execute(NativeCallInfo nativeCallInfo, Object[] args);
    }

    interface InvokeVoidCallNode extends NodeInterface {
        default void dispatch(VirtualFrame frame, NativeCallInfo nativeCallInfo, Object[] args) {
            RFFIContext stateRFFI = RContext.getInstance().getStateRFFI();
            Object before = stateRFFI.beforeDowncall(frame == null ? null : frame.materialize(), nativeCallInfo.dllInfo.handle.getRFFIType());
            try {
                execute(frame, nativeCallInfo, args);
            } finally {
                stateRFFI.afterDowncall(before, nativeCallInfo.dllInfo.handle.getRFFIType(), AfterDownCallProfiles.getUncached());
            }
        }

        /**
         * Variant that does not return a result (primarily for library "init" methods).
         */
        void execute(VirtualFrame frame, NativeCallInfo nativeCallInfo, Object[] args);
    }

    InvokeCallNode createInvokeCallNode();

    InvokeVoidCallNode createInvokeVoidCallNode();

    final class InvokeCallRootNode extends RFFIRootNode<InvokeCallNode> {
        protected InvokeCallRootNode(InvokeCallNode baseRFFINode) {
            super(baseRFFINode);
        }

        @Override
        public Object execute(VirtualFrame frame) {
            Object[] args = frame.getArguments();
            return rffiNode.dispatch(frame, (NativeCallInfo) args[0], (Object[]) args[1]);
        }

        public static CallTarget create(RContext context) {
            CallRFFI callRFFI = context.getRFFI().callRFFI;
            return context.getOrCreateCachedCallTarget(InvokeCallRootNode.class, () -> new InvokeCallRootNode(callRFFI.createInvokeCallNode()).getCallTarget());
        }
    }

    final class InvokeVoidCallRootNode extends RFFIRootNode<InvokeVoidCallNode> {
        protected InvokeVoidCallRootNode(InvokeVoidCallNode wrapper) {
            super(wrapper);
        }

        @Override
        public Object execute(VirtualFrame frame) {
            Object[] args = frame.getArguments();
            rffiNode.dispatch(frame, (NativeCallInfo) args[0], (Object[]) args[1]);
            return RNull.instance; // unused
        }

        public static CallTarget create(RContext context) {
            CallRFFI callRFFI = context.getRFFI().callRFFI;
            return context.getOrCreateCachedCallTarget(InvokeVoidCallNode.class, () -> new InvokeVoidCallRootNode(callRFFI.createInvokeVoidCallNode()).getCallTarget());
        }
    }
}
