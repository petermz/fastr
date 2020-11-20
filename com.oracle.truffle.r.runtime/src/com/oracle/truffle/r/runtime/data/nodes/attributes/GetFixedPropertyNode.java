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
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.DynamicObjectLibrary;
import com.oracle.truffle.api.object.Location;
import com.oracle.truffle.api.object.Shape;
import com.oracle.truffle.r.runtime.DSLConfig;
import com.oracle.truffle.r.runtime.RInternalError;
import com.oracle.truffle.r.runtime.RRuntime;
import com.oracle.truffle.r.runtime.data.nodes.attributes.GetFixedPropertyNodeGen.GetClassPropertyNodeGen;
import com.oracle.truffle.r.runtime.data.nodes.attributes.GetFixedPropertyNodeGen.GetCommentPropertyNodeGen;
import com.oracle.truffle.r.runtime.data.nodes.attributes.GetFixedPropertyNodeGen.GetDimNamesPropertyNodeGen;
import com.oracle.truffle.r.runtime.data.nodes.attributes.GetFixedPropertyNodeGen.GetDimPropertyNodeGen;
import com.oracle.truffle.r.runtime.data.nodes.attributes.GetFixedPropertyNodeGen.GetGenericPropertyNodeGen;
import com.oracle.truffle.r.runtime.data.nodes.attributes.GetFixedPropertyNodeGen.GetNamesPropertyNodeGen;
import com.oracle.truffle.r.runtime.data.nodes.attributes.GetFixedPropertyNodeGen.GetRowNamesPropertyNodeGen;
import com.oracle.truffle.r.runtime.data.nodes.attributes.GetFixedPropertyNodeGen.GetTspPropertyNodeGen;

/**
 * Retrieves fixed property (passed in constructor) from dynamic object.
 */
@ImportStatic(DSLConfig.class)
@GenerateUncached
public abstract class GetFixedPropertyNode extends PropertyAccessNode {

    public abstract Object execute(DynamicObject obj);

    protected String getPropertyName() {
        throw RInternalError.shouldNotReachHere();
    }

    public static GetGenericPropertyNode create(String propertyName) {
        return GetGenericPropertyNodeGen.create(propertyName);
    }

    public static GetClassPropertyNode createClass() {
        return GetClassPropertyNodeGen.create();
    }

    public static GetNamesPropertyNode createNames() {
        return GetNamesPropertyNodeGen.create();
    }

    public static GetDimPropertyNode createDim() {
        return GetDimPropertyNodeGen.create();
    }

    public static GetDimNamesPropertyNode createDimNames() {
        return GetDimNamesPropertyNodeGen.create();
    }

    public static GetRowNamesPropertyNode createRowNames() {
        return GetRowNamesPropertyNodeGen.create();
    }

    public static GetTspPropertyNode createTsp() {
        return GetTspPropertyNodeGen.create();
    }

    public static GetCommentPropertyNode createComment() {
        return GetCommentPropertyNodeGen.create();
    }

    protected boolean hasProperty(Shape shape) {
        return shape.hasProperty(getPropertyName());
    }

    @Specialization(limit = "getCacheSize(3)", //
                    guards = {"shapeCheck(shape, attrs)"}, //
                    assumptions = {"shape.getValidAssumption()"})
    protected Object getAttrCached(DynamicObject attrs,
                    @Cached("lookupShape(attrs)") Shape shape,
                    @Cached("lookupLocation(shape, getPropertyName())") Location location) {
        return location == null ? null : location.get(attrs, shape);
    }

    @Specialization(replaces = "getAttrCached")
    @TruffleBoundary
    protected Object getAttrFallback(DynamicObject attrs) {
        return DynamicObjectLibrary.getUncached().getOrDefault(attrs, getPropertyName(), null);
    }

    @GenerateUncached
    public abstract static class GetClassPropertyNode extends GetFixedPropertyNode {
        @Override
        protected String getPropertyName() {
            return RRuntime.CLASS_ATTR_KEY;
        }
    }

    @GenerateUncached
    public abstract static class GetNamesPropertyNode extends GetFixedPropertyNode {
        @Override
        protected String getPropertyName() {
            return RRuntime.NAMES_ATTR_KEY;
        }
    }

    @GenerateUncached
    public abstract static class GetDimPropertyNode extends GetFixedPropertyNode {
        @Override
        protected String getPropertyName() {
            return RRuntime.DIM_ATTR_KEY;
        }
    }

    @GenerateUncached
    public abstract static class GetDimNamesPropertyNode extends GetFixedPropertyNode {
        @Override
        protected String getPropertyName() {
            return RRuntime.DIMNAMES_ATTR_KEY;
        }
    }

    @GenerateUncached
    public abstract static class GetRowNamesPropertyNode extends GetFixedPropertyNode {
        @Override
        protected String getPropertyName() {
            return RRuntime.ROWNAMES_ATTR_KEY;
        }
    }

    @GenerateUncached
    public abstract static class GetTspPropertyNode extends GetFixedPropertyNode {
        @Override
        protected String getPropertyName() {
            return RRuntime.TSP_ATTR_KEY;
        }
    }

    @GenerateUncached
    public abstract static class GetCommentPropertyNode extends GetFixedPropertyNode {
        @Override
        protected String getPropertyName() {
            return RRuntime.COMMENT_ATTR_KEY;
        }
    }

    public abstract static class GetGenericPropertyNode extends GetFixedPropertyNode {
        private final String propertyName;

        public GetGenericPropertyNode(String propertyName) {
            this.propertyName = propertyName;
        }

        @Override
        protected String getPropertyName() {
            return propertyName;
        }
    }
}
