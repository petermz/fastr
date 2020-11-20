/*
 * Copyright (c) 2020, 2020, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.r.runtime.data.nodes;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.r.runtime.data.AbstractContainerLibrary;
import com.oracle.truffle.r.runtime.data.model.RAbstractContainer;
import com.oracle.truffle.r.runtime.nodes.RBaseNode;

/**
 * Copies a container and all its attributes (shallow copy). The reference counts of the attributes
 * is set accordingly. The copying preserves the order of the attributes and does not do any
 * validation of the attribute values.
 */
public final class CopyWithAttributes extends RBaseNode {
    private CopyWithAttributes() {
    }

    public static CopyWithAttributes create() {
        return new CopyWithAttributes();
    }

    @SuppressWarnings("static-method")
    public RAbstractContainer execute(AbstractContainerLibrary containerLibrary, RAbstractContainer container) {
        RAbstractContainer result = containerLibrary.duplicate(container, false);
        setAttributes(container, result);
        return result;
    }

    // TODO: this should be a node
    @TruffleBoundary
    private static void setAttributes(RAbstractContainer container, RAbstractContainer result) {
        container.setAttributes(result);
    }
}
