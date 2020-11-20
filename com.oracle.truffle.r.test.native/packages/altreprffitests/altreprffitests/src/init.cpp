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
#include <R.h>
#include <Rinternals.h>
#include <R_ext/Rdynload.h>
#include "TrivialClass.hpp"
#include "altrep_classes.hpp"
#include "GeneratorClass.hpp"
#include "FirstCharChangerClass.hpp"

static SEXP is_altrep(SEXP x);
static SEXP altrep_get_data1(SEXP x);
static SEXP altrep_get_data2(SEXP x);
static SEXP integer_no_na(SEXP x);
static SEXP real_no_na(SEXP x);
static SEXP is_sorted(SEXP x);
extern "C" SEXP my_test(SEXP vec);

static const R_CallMethodDef CallEntries[] = {
        {"is_altrep", (DL_FUNC) &is_altrep, 1},
        {"altrep_get_data1", (DL_FUNC) &altrep_get_data1, 1},
        {"altrep_get_data2", (DL_FUNC) &altrep_get_data2, 1},
        {"integer_no_na", (DL_FUNC) &integer_no_na, 1},
        {"real_no_na", (DL_FUNC) &real_no_na, 1},
        {"is_sorted", (DL_FUNC) &is_sorted, 1},
        {"trivial_class_create_instance", (DL_FUNC) &TrivialClass::createInstance, 0},
        {"simple_vec_wrapper_create_instance", (DL_FUNC) &VecWrapper::createInstance, 9},
        {"logging_vec_wrapper_create_instance", (DL_FUNC) &LoggingVecWrapper::createInstance, 9},
        {"logging_vec_wrapper_was_method_called", (DL_FUNC) &LoggingVecWrapper::wasMethodCalled, 2},
        {"logging_vec_wrapper_clear_called_methods", (DL_FUNC) &LoggingVecWrapper::clearCalledMethods, 0},
        {"generator_class_new", (DL_FUNC)&GeneratorClass::createInstance, 3},
        {"first_char_changer_class_new", (DL_FUNC)&FirstCharChangerClass::createInstance, 2},
        {NULL, NULL, 0}
};

extern "C" void R_init_altreprffitests(DllInfo *dll)
{
    R_registerRoutines(dll, NULL, CallEntries, NULL, NULL);
}

static SEXP is_altrep(SEXP x)
{
    return ScalarLogical(ALTREP(x));
}

static SEXP altrep_get_data1(SEXP x)
{
    return R_altrep_data1(x);
}

static SEXP altrep_get_data2(SEXP x)
{
    return R_altrep_data2(x);
}

static SEXP integer_no_na(SEXP x)
{
    int no_na = INTEGER_NO_NA(x);
    return ScalarLogical(no_na);
}

static SEXP real_no_na(SEXP x)
{
    int no_na = REAL_NO_NA(x);
    return ScalarLogical(no_na);
}

static SEXP is_sorted(SEXP x)
{
    int sortedness = UNKNOWN_SORTEDNESS;
    switch (TYPEOF(x)) {
        case INTSXP:
            sortedness = INTEGER_IS_SORTED(x);
            break;
        case REALSXP:
            sortedness = REAL_IS_SORTED(x);
            break;
        case LGLSXP:
            sortedness = LOGICAL_IS_SORTED(x);
            break;
        case STRSXP:
            sortedness = STRING_IS_SORTED(x);
            break;
        default:
            Rf_error("is_sorted: Unknown type");
    }

    if (KNOWN_INCR(sortedness)) {
        return ScalarLogical(TRUE);
    }
    else {
        return ScalarLogical(FALSE);
    }
}
