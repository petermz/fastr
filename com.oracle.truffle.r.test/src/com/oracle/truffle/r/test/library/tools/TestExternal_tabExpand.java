/*
 * Copyright (c) 2016, 2018, Oracle and/or its affiliates. All rights reserved.
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

package com.oracle.truffle.r.test.library.tools;

import org.junit.Test;

import com.oracle.truffle.r.test.TestBase;

public class TestExternal_tabExpand extends TestBase {
    @Test
    public void testtabExpand() {
        assertEval(".Call(tools:::C_doTabExpand,\"a\tb\",1L)");
        assertEval(".Call(tools:::C_doTabExpand,c(\"a\tb\tc\",\"x\ty\tz\"),c(0L,1L))");
        assertEval(".Call(tools:::C_doTabExpand,\"a\tb\",NULL)");
        assertEval(".Call(tools:::C_doTabExpand,\"a\tb\",1.1)");
        assertEval(".Call(tools:::C_doTabExpand,123,1L)");
        assertEval(".Call(tools:::C_doTabExpand,.Call(tools:::C_doTabExpand,c(1,2),c(0L,1L)),1L)");
    }
}
