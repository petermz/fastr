# Copyright (c) 2017, 2018, Oracle and/or its affiliates. All rights reserved.
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
#
# This code is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License version 3 only, as
# published by the Free Software Foundation.
#
# This code is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
# version 3 for more details (a copy is included in the LICENSE file that
# accompanied this code).
#
# You should have received a copy of the GNU General Public License version
# 3 along with this work; if not, write to the Free Software Foundation,
# Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
#
# Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
# or visit www.oracle.com if you need additional information or have any
# questions.

eval(expression({
# this function is replaced with a primitive because it is expected to
# modify its argument in-place, which can clash with argument refcount handling
`slot<-` <- .fastr.methods.slotassign

new <- function (Class, ...) {
    if (.fastr.option("hostLookup") && is.character(Class) && !isClass(Class)) {
        javaClass <- java.type(Class, silent=TRUE)
        if(!is.null(javaClass)) {
            Class <- javaClass
        }
    }
    if(is.polyglot.value(Class)) {
        .fastr.interop.new(Class, ...)
    } else {
        ClassDef <- getClass(Class, where = topenv(parent.frame()))
        value <- .Call(C_new_object, ClassDef)
        initialize(value, ...)
    }
}
}), asNamespace("methods"))
