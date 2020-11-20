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
public abstract class HasFixedPropertyNode extends PropertyAccessNode {

    protected String name;

    public HasFixedPropertyNode(String name) {
        this.name = name;
    }

    public abstract boolean execute(DynamicObject attr);

    public static HasFixedPropertyNode create(String name) {
        return HasFixedPropertyNodeGen.create(name);
    }

    protected boolean hasProperty(Shape shape) {
        return shape.hasProperty(name);
    }

    @Specialization(limit = "getCacheSize(3)", //
                    guards = {"shapeCheck(shape, attrs)"}, //
                    assumptions = {"shape.getValidAssumption()"})
    protected boolean hasAttrCached(@SuppressWarnings("unused") DynamicObject attrs,
                    @Cached("lookupShape(attrs)") @SuppressWarnings("unused") Shape shape,
                    @Cached("lookupLocation(shape, name)") Location location) {
        return location != null;
    }

    @Specialization(replaces = "hasAttrCached")
    @TruffleBoundary
    protected boolean hasAttrFallback(DynamicObject attrs) {
        return DynamicObjectLibrary.getUncached().containsKey(attrs, name);
    }
}
