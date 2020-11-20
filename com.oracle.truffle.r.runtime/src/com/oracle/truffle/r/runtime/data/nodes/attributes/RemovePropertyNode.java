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
package com.oracle.truffle.r.runtime.data.nodes.attributes;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.DynamicObjectLibrary;
import com.oracle.truffle.api.object.Location;
import com.oracle.truffle.api.object.Shape;
import com.oracle.truffle.r.runtime.DSLConfig;

@ImportStatic(DSLConfig.class)
public abstract class RemovePropertyNode extends PropertyAccessNode {
    public abstract void execute(DynamicObject attrs, String name);

    public static RemovePropertyNode create() {
        return RemovePropertyNodeGen.create();
    }

    @Specialization(limit = "getCacheSize(3)", //
                    guards = {
                                    "cachedName.equals(name)",
                                    "shapeCheck(shape, attrs)",
                                    "location == null"
                    }, //
                    assumptions = {
                                    "shape.getValidAssumption()"
                    })
    @SuppressWarnings("unused")
    protected static void removeNonExistantAttr(DynamicObject attrs, String name,
                    @Cached("name") String cachedName,
                    @Cached("lookupShape(attrs)") Shape shape,
                    @Cached("lookupLocation(shape, cachedName)") Location location) {
        // do nothing
    }

    @Specialization
    @TruffleBoundary
    protected static void removeAttrFallback(DynamicObject attrs, String name) {
        DynamicObjectLibrary.getUncached().removeKey(attrs, name);
    }
}
