/*
 * Copyright (c) 2014, 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.r.nodes.access;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.source.*;
import com.oracle.truffle.api.utilities.*;
import com.oracle.truffle.r.nodes.*;
import com.oracle.truffle.r.nodes.access.variables.*;
import com.oracle.truffle.r.nodes.access.variables.ReadVariableNode.ReadKind;
import com.oracle.truffle.r.nodes.function.*;
import com.oracle.truffle.r.runtime.*;
import com.oracle.truffle.r.runtime.RDeparse.State;
import com.oracle.truffle.r.runtime.data.*;

/**
 * An {@link RNode} that handles accesses to components of the variadic argument (..1, ..2, etc.).
 */
public class ReadVariadicComponentNode extends RNode implements RSyntaxNode {

    @Child private ReadVariableNode lookup = ReadVariableNode.create("...", RType.Any, ReadKind.Silent);
    @Child private PromiseHelperNode promiseHelper;

    private final int index;

    private final BranchProfile errorBranch = BranchProfile.create();
    private final BranchProfile promiseBranch = BranchProfile.create();

    public ReadVariadicComponentNode(SourceSection src, int index) {
        this.index = index;
        assignSourceSection(src);
    }

    @Override
    public Object execute(VirtualFrame frame) {
        Object args = lookup.execute(frame);
        if (args == null) {
            errorBranch.enter();
            throw RError.error(this, RError.Message.NO_DOT_DOT, index + 1);
        }
        RArgsValuesAndNames argsValuesAndNames = (RArgsValuesAndNames) args;
        if (argsValuesAndNames.isEmpty()) {
            errorBranch.enter();
            throw RError.error(this, RError.Message.NO_LIST_FOR_CDR);
        }

        if (argsValuesAndNames.getLength() <= index) {
            errorBranch.enter();
            throw RError.error(this, RError.Message.DOT_DOT_SHORT, index + 1);
        }
        Object ret = argsValuesAndNames.getArgument(index);
        if (ret instanceof RPromise) {
            promiseBranch.enter();
            // This might be the case, as lookup only checks for "..." to be a promise and forces it
            // eventually, NOT (all) of its content
            if (promiseHelper == null) {
                CompilerDirectives.transferToInterpreterAndInvalidate();
                promiseHelper = insert(new PromiseHelperNode());
            }
            ret = promiseHelper.evaluate(frame, (RPromise) ret);
        }
        return ret == null ? RMissing.instance : ret;
    }

    @Override
    public void deparse(State state) {
        state.append("..");
        state.append(Integer.toString(index + 1));
    }
}
