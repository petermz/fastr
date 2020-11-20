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
package com.oracle.truffle.r.runtime.context;

import com.oracle.truffle.api.TruffleLogger;
import com.oracle.truffle.r.runtime.RLogger;
import com.oracle.truffle.r.runtime.data.altrep.AltComplexClassDescriptor;
import com.oracle.truffle.r.runtime.data.altrep.AltIntegerClassDescriptor;
import com.oracle.truffle.r.runtime.data.altrep.AltLogicalClassDescriptor;
import com.oracle.truffle.r.runtime.data.altrep.AltRawClassDescriptor;
import com.oracle.truffle.r.runtime.data.altrep.AltRealClassDescriptor;
import com.oracle.truffle.r.runtime.data.altrep.AltStringClassDescriptor;
import org.graalvm.collections.EconomicMap;

/**
 * A context that stores all the class descriptors for ALTREP. TODO: Currently this class is not
 * useful. Use it for serialization.
 */
public final class AltRepContext implements RContext.ContextState {
    private static final TruffleLogger logger = RLogger.getLogger(RLogger.LOGGER_ALTREP);
    EconomicMap<String, AltIntegerClassDescriptor> altIntDescriptors = EconomicMap.create();
    EconomicMap<String, AltRealClassDescriptor> altRealDescriptors = EconomicMap.create();

    private AltRepContext() {
    }

    public static AltRepContext newContextState() {
        return new AltRepContext();
    }

    public AltIntegerClassDescriptor registerNewAltIntClass(String className, String packageName) {
        AltIntegerClassDescriptor altIntClassDescr = new AltIntegerClassDescriptor(className, packageName);
        altIntDescriptors.put(altIntClassDescr.toString(), altIntClassDescr);
        logger.fine(() -> "Registered ALTINT class: " + altIntClassDescr.toString());
        return altIntClassDescr;
    }

    public AltRealClassDescriptor registerNewAltRealClass(String className, String packageName) {
        AltRealClassDescriptor altRealClassDescr = new AltRealClassDescriptor(className, packageName);
        altRealDescriptors.put(altRealClassDescr.toString(), altRealClassDescr);
        logger.fine(() -> "Registered ALTREAL class: " + altRealClassDescr.toString());
        return altRealClassDescr;
    }

    public static AltComplexClassDescriptor registerNewAltComplexClass(String className, String packageName) {
        return new AltComplexClassDescriptor(className, packageName);
    }

    public static AltLogicalClassDescriptor registerNewAltLogicalClass(String className, String packageName) {
        return new AltLogicalClassDescriptor(className, packageName);
    }

    public static AltStringClassDescriptor registerNewAltStringClass(String className, String packageName) {
        return new AltStringClassDescriptor(className, packageName);
    }

    public static AltRawClassDescriptor registerNewAltRawClass(String className, String packageName) {
        return new AltRawClassDescriptor(className, packageName);
    }
}
