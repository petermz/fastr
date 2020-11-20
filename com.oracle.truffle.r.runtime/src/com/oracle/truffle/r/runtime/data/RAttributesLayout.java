/*
 * Copyright (c) 2016, 2020, Oracle and/or its affiliates. All rights reserved.
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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.CompilerDirectives.ValueType;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.DynamicObjectFactory;
import com.oracle.truffle.api.object.DynamicObjectLibrary;
import com.oracle.truffle.api.object.Layout;
import com.oracle.truffle.api.object.ObjectType;
import com.oracle.truffle.api.object.Property;
import com.oracle.truffle.api.object.Shape;
import com.oracle.truffle.r.runtime.RRuntime;

/**
 * The facade to facilitate manipulation with attributes in FastR. The attributes in FastR are
 * implemented on top of the Truffle object model, in particular, on top of class
 * {@link DynamicObject}. This class contains methods for creating new instances of attributes,
 * copying, clearing and iterating attributes.
 * <p>
 * This class serves also as a register of known sets of attributes used in FastR. Each set is
 * represented by an instance of {@link RAttributesLayout.AttrsLayout} that encapsulates, among
 * other things, the pre-loaded list of properties of the given attribute set. This plays role when
 * optimizing the iteration over attributes of an object (by means of special nodes).
 */
public final class RAttributesLayout {

    public static class RAttributesType extends ObjectType {
    }

    private static final AttrsLayout EMPTY_ATTRS_LAYOUT = new AttrsLayout();
    private static final AttrsLayout CLASS_ATTRS_LAYOUT = new AttrsLayout(RRuntime.CLASS_ATTR_KEY);
    private static final AttrsLayout NAMES_ATTRS_LAYOUT = new AttrsLayout(RRuntime.NAMES_ATTR_KEY);
    private static final AttrsLayout DIM_ATTRS_LAYOUT = new AttrsLayout(RRuntime.DIM_ATTR_KEY);
    private static final AttrsLayout DIMNAMES_ATTRS_LAYOUT = new AttrsLayout(RRuntime.DIMNAMES_ATTR_KEY);
    private static final AttrsLayout NAMES_AND_DIMNAMES_ATTRS_LAYOUT = new AttrsLayout(RRuntime.NAMES_ATTR_KEY, RRuntime.DIMNAMES_ATTR_KEY);
    private static final AttrsLayout ROWNAMES_ATTRS_LAYOUT = new AttrsLayout(RRuntime.ROWNAMES_ATTR_KEY);
    private static final AttrsLayout TSP_ATTRS_LAYOUT = new AttrsLayout(RRuntime.TSP_ATTR_KEY);
    private static final AttrsLayout COMMENT_ATTRS_LAYOUT = new AttrsLayout(RRuntime.COMMENT_ATTR_KEY);
    private static final AttrsLayout NAMES_AND_DIM_ATTRS_LAYOUT = new AttrsLayout(RRuntime.NAMES_ATTR_KEY, RRuntime.DIM_ATTR_KEY);
    private static final AttrsLayout DIM_AND_DIMNAMES_ATTRS_LAYOUT = new AttrsLayout(RRuntime.DIM_ATTR_KEY, RRuntime.DIMNAMES_ATTR_KEY);
    private static final AttrsLayout NAMES_AND_DIM_AND_DIMNAMES_ATTRS_LAYOUT = new AttrsLayout(RRuntime.NAMES_ATTR_KEY, RRuntime.DIM_ATTR_KEY, RRuntime.DIMNAMES_ATTR_KEY);
    private static final AttrsLayout CLASS_AND_CONNID_ATTRS_LAYOUT = new AttrsLayout(RRuntime.CLASS_ATTR_KEY, RRuntime.CONN_ID_ATTR_KEY);

    public static final AttrsLayout[] LAYOUTS = {EMPTY_ATTRS_LAYOUT, CLASS_ATTRS_LAYOUT, NAMES_ATTRS_LAYOUT, DIM_ATTRS_LAYOUT, DIMNAMES_ATTRS_LAYOUT, ROWNAMES_ATTRS_LAYOUT,
                    NAMES_AND_DIM_ATTRS_LAYOUT, DIM_AND_DIMNAMES_ATTRS_LAYOUT};

    private RAttributesLayout() {
    }

    public static DynamicObject createRAttributes() {
        return EMPTY_ATTRS_LAYOUT.factory.newInstance();
    }

    public static DynamicObject createClass(Object cls) {
        return CLASS_ATTRS_LAYOUT.factory.newInstance(cls);
    }

    public static DynamicObject createNames(Object names) {
        return NAMES_ATTRS_LAYOUT.factory.newInstance(names);
    }

    public static DynamicObject createDim(Object dim) {
        return DIM_ATTRS_LAYOUT.factory.newInstance(dim);
    }

    public static DynamicObject createDimNames(Object dimNames) {
        return DIMNAMES_ATTRS_LAYOUT.factory.newInstance(dimNames);
    }

    public static DynamicObject createNamesAndDimNames(Object names, Object dimNames) {
        return NAMES_AND_DIMNAMES_ATTRS_LAYOUT.factory.newInstance(names, dimNames);
    }

    public static DynamicObject createRowNames(Object rowNames) {
        return ROWNAMES_ATTRS_LAYOUT.factory.newInstance(rowNames);
    }

    public static DynamicObject createTsp(Object tsp) {
        return TSP_ATTRS_LAYOUT.factory.newInstance(tsp);
    }

    public static DynamicObject createComment(Object comment) {
        return COMMENT_ATTRS_LAYOUT.factory.newInstance(comment);
    }

    public static DynamicObject createNamesAndDim(Object names, Object dim) {
        return NAMES_AND_DIM_ATTRS_LAYOUT.factory.newInstance(names, dim);
    }

    public static DynamicObject createDimAndDimNames(Object dim, Object dimNames) {
        return DIM_AND_DIMNAMES_ATTRS_LAYOUT.factory.newInstance(dim, dimNames);
    }

    public static DynamicObject createNamesAndDimAndDimNames(Object names, Object dim, Object dimNames) {
        return NAMES_AND_DIM_AND_DIMNAMES_ATTRS_LAYOUT.factory.newInstance(names, dim, dimNames);
    }

    public static DynamicObject createClassWithConnId(Object cls, Object connId) {
        return CLASS_AND_CONNID_ATTRS_LAYOUT.factory.newInstance(cls, connId);
    }

    public static boolean isRAttributes(Object attrs) {
        return (attrs instanceof DynamicObject) && isRAttributes((DynamicObject) attrs);
    }

    public static boolean isRAttributes(DynamicObject attrs) {
        return isRAttributes(attrs.getShape().getObjectType());
    }

    public static boolean isRAttributes(ObjectType objectType) {
        return objectType instanceof RAttributesType;
    }

    public static RAttributeIterable asIterable(DynamicObject attrs) {
        return new RAttributeIterable(attrs, attrs.getShape().getProperties());
    }

    public static RAttributeIterable asIterable(DynamicObject attrs, AttrsLayout attrsLayout) {
        assert attrsLayout.shape.check(attrs);
        return new RAttributeIterable(attrs, attrsLayout.propertyList);
    }

    @TruffleBoundary
    public static DynamicObject copy(DynamicObject attrs) {
        assert isRAttributes(attrs);
        DynamicObject result = attrs.getShape().newInstance();
        Shape shape = result.getShape();
        for (Object key : shape.getKeys()) {
            Object value = DynamicObjectLibrary.getUncached().getOrDefault(attrs, key, null);
            DynamicObjectLibrary.getUncached().put(result, key, value);
            if (RSharingAttributeStorage.isShareable(value)) {
                // There is no simple way to determine the correct reference count here and since
                // the value will end up in two attributes collections, it will end up being shared
                // most likely anyway.
                ((RSharingAttributeStorage) value).makeSharedPermanent();
            }
        }
        return result;
    }

    @TruffleBoundary
    public static void clear(DynamicObject attrs) {
        assert isRAttributes(attrs);

        for (Property p : attrs.getShape().getProperties()) {
            DynamicObjectLibrary.getUncached().removeKey(attrs, p.getKey());
        }
    }

    public static final class AttrsLayout {
        private final Layout layout = Layout.newLayout().build();
        private final Shape.Allocator allocator = layout.createAllocator();
        public final Shape shape;
        public final List<Property> propertyList;
        public final Property[] properties;
        public final DynamicObjectFactory factory;

        private AttrsLayout(String... attrNames) {
            this.properties = new Property[attrNames.length];
            Shape s = layout.createShape(new RAttributesType());
            for (int i = 0; i < attrNames.length; i++) {
                Property p = Property.create(attrNames[i], allocator.locationForType(Object.class), 0);
                this.properties[i] = p;
                s = s.addProperty(p);
            }
            shape = s;
            propertyList = s.getPropertyList();
            factory = s.createFactory();
        }
    }

    public static final class ConstantShapesAndProperties {
        private static final Shape[] EMPTY_SHAPES_ARRAY = new Shape[0];
        private static final Property[] EMPTY_PROPERTY_ARRAY = new Property[0];

        public static final ConstantShapesAndProperties EMPTY = new ConstantShapesAndProperties(EMPTY_SHAPES_ARRAY, EMPTY_PROPERTY_ARRAY);

        private final Shape[] constantShapes;
        private final Property[] constantProperties;

        private ConstantShapesAndProperties(Shape[] constantShapes, Property[] constantProperties) {
            this.constantShapes = constantShapes;
            this.constantProperties = constantProperties;
        }

        public Shape[] getConstantShapes() {
            return constantShapes;
        }

        public Property[] getConstantProperties() {
            return constantProperties;
        }
    }

    public static final class RAttributeIterable implements Iterable<RAttributesLayout.RAttribute> {

        public static final RAttributeIterable EMPTY = new RAttributeIterable(null, null);

        private final DynamicObject attrs;
        private final Iterable<Property> properties;

        RAttributeIterable(DynamicObject attrs, Iterable<Property> properties) {
            this.attrs = attrs;
            this.properties = properties;
        }

        @Override
        public Iterator<RAttributesLayout.RAttribute> iterator() {
            if (attrs == null || properties == null) {
                return Collections.emptyIterator();
            } else {
                return new Iter(attrs, properties.iterator());
            }
        }
    }

    public interface RAttribute {
        String getName();

        Object getValue();
    }

    @ValueType
    public static final class AttrInstance implements RAttribute {
        private final String name;
        private final Object value;

        public AttrInstance(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public String toString() {
            return name + "=" + value;
        }
    }

    static class Iter implements Iterator<RAttribute> {
        private final Iterator<Property> iter;
        private final DynamicObject attrs;
        private final Shape shape;

        Iter(DynamicObject attrs, Iterator<Property> propertyIterator) {
            this.attrs = attrs;
            this.iter = propertyIterator;
            this.shape = attrs.getShape();
        }

        @Override
        @TruffleBoundary
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        @TruffleBoundary
        public RAttribute next() {
            Property p = iter.next();
            Object value = p.get(attrs, shape);
            return new AttrInstance((String) p.getKey(), value);
        }
    }
}
