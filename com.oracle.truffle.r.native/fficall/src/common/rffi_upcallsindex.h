/*
 * Copyright (c) 2018, 2020, Oracle and/or its affiliates. All rights reserved.
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

// GENERATED by com.oracle.truffle.r.ffi.codegen.FFIUpCallsIndexCodeGen class; DO NOT EDIT
// This file can be regenerated by running 'mx rfficodegen'
#ifndef RFFI_UPCALLSINDEX_H
#define RFFI_UPCALLSINDEX_H

#define ALTREP_x 0
#define ATTRIB_x 1
#define BODY_x 2
#define CAAR_x 3
#define CAD4R_x 4
#define CADDDR_x 5
#define CADDR_x 6
#define CADR_x 7
#define CAR_x 8
#define CDAR_x 9
#define CDDDR_x 10
#define CDDR_x 11
#define CDR_x 12
#define CLOENV_x 13
#define COMPLEX_x 14
#define COMPLEX_ELT_x 15
#define COMPLEX_GET_REGION_x 16
#define DATAPTR_OR_NULL_x 17
#define DUPLICATE_ATTRIB_x 18
#define DispatchPRIMFUN_x 19
#define ENCLOS_x 20
#define FASTR_DATAPTR_x 21
#define FASTR_getConnectionChar_x 22
#define FORMALS_x 23
#define GetRNGstate_x 24
#define INTEGER_x 25
#define INTEGER_ELT_x 26
#define INTEGER_GET_REGION_x 27
#define INTEGER_IS_SORTED_x 28
#define INTEGER_NO_NA_x 29
#define IS_S4_OBJECT_x 30
#define LENGTH_x 31
#define LEVELS_x 32
#define LOGICAL_x 33
#define LOGICAL_ELT_x 34
#define LOGICAL_GET_REGION_x 35
#define LOGICAL_IS_SORTED_x 36
#define LOGICAL_NO_NA_x 37
#define NAMED_x 38
#define OBJECT_x 39
#define PRCODE_x 40
#define PRENV_x 41
#define PRINTNAME_x 42
#define PRSEEN_x 43
#define PRVALUE_x 44
#define PutRNGstate_x 45
#define RAW_x 46
#define RAW_ELT_x 47
#define RAW_GET_REGION_x 48
#define RDEBUG_x 49
#define REAL_x 50
#define REAL_ELT_x 51
#define REAL_GET_REGION_x 52
#define REAL_IS_SORTED_x 53
#define REAL_NO_NA_x 54
#define RSTEP_x 55
#define R_BaseEnv_x 56
#define R_BaseNamespace_x 57
#define R_BindingIsLocked_x 58
#define R_CHAR_x 59
#define R_CleanUp_x 60
#define R_ExternalPtrAddr_x 61
#define R_ExternalPtrProtected_x 62
#define R_ExternalPtrTag_x 63
#define R_FindNamespace_x 64
#define R_GetConnection_x 65
#define R_GlobalContext_x 66
#define R_GlobalEnv_x 67
#define R_Home_x 68
#define R_HomeDir_x 69
#define R_Interactive_x 70
#define R_LockBinding_x 71
#define R_MakeActiveBinding_x 72
#define R_MakeExternalPtr_x 73
#define R_MakeWeakRef_x 74
#define R_MakeWeakRefC_x 75
#define R_MethodsNamespace_x 76
#define R_NamespaceRegistry_x 77
#define R_NewHashedEnv_x 78
#define R_ParseVector_x 79
#define R_PreserveObject_x 80
#define R_PromiseExpr_x 81
#define R_ProtectWithIndex_x 82
#define R_ReadConnection_x 83
#define R_ReleaseObject_x 84
#define R_Reprotect_x 85
#define R_SetExternalPtrAddr_x 86
#define R_SetExternalPtrProtected_x 87
#define R_SetExternalPtrTag_x 88
#define R_TempDir_x 89
#define R_ToplevelExec_x 90
#define R_WeakRefKey_x 91
#define R_WeakRefValue_x 92
#define R_WriteConnection_x 93
#define R_alloc_x 94
#define R_altrep_data1_x 95
#define R_altrep_data2_x 96
#define R_altrep_inherits_x 97
#define R_compute_identical_x 98
#define R_do_MAKE_CLASS_x 99
#define R_do_new_object_x 100
#define R_do_slot_x 101
#define R_do_slot_assign_x 102
#define R_forceAndCall_x 103
#define R_getClassDef_x 104
#define R_getContextCall_x 105
#define R_getContextEnv_x 106
#define R_getContextFun_x 107
#define R_getContextSrcRef_x 108
#define R_getGlobalFunctionContext_x 109
#define R_getParentFunctionContext_x 110
#define R_has_slot_x 111
#define R_insideBrowser_x 112
#define R_isEqual_x 113
#define R_isGlobal_x 114
#define R_lsInternal3_x 115
#define R_make_altcomplex_class_x 116
#define R_make_altinteger_class_x 117
#define R_make_altlogical_class_x 118
#define R_make_altraw_class_x 119
#define R_make_altreal_class_x 120
#define R_make_altstring_class_x 121
#define R_nchar_x 122
#define R_new_altrep_x 123
#define R_new_custom_connection_x 124
#define R_set_altcomplex_Elt_method_x 125
#define R_set_altcomplex_Get_region_method_x 126
#define R_set_altinteger_Elt_method_x 127
#define R_set_altinteger_Get_region_method_x 128
#define R_set_altinteger_Is_sorted_method_x 129
#define R_set_altinteger_Max_method_x 130
#define R_set_altinteger_Min_method_x 131
#define R_set_altinteger_No_NA_method_x 132
#define R_set_altinteger_Sum_method_x 133
#define R_set_altlogical_Elt_method_x 134
#define R_set_altlogical_Get_region_method_x 135
#define R_set_altlogical_Is_sorted_method_x 136
#define R_set_altlogical_No_NA_method_x 137
#define R_set_altlogical_Sum_method_x 138
#define R_set_altraw_Elt_method_x 139
#define R_set_altraw_Get_region_method_x 140
#define R_set_altreal_Elt_method_x 141
#define R_set_altreal_Get_region_method_x 142
#define R_set_altreal_Is_sorted_method_x 143
#define R_set_altreal_Max_method_x 144
#define R_set_altreal_Min_method_x 145
#define R_set_altreal_No_NA_method_x 146
#define R_set_altreal_Sum_method_x 147
#define R_set_altrep_Coerce_method_x 148
#define R_set_altrep_DuplicateEX_method_x 149
#define R_set_altrep_Duplicate_method_x 150
#define R_set_altrep_Inspect_method_x 151
#define R_set_altrep_Length_method_x 152
#define R_set_altrep_Serialized_state_method_x 153
#define R_set_altrep_UnserializeEX_method_x 154
#define R_set_altrep_Unserialize_method_x 155
#define R_set_altrep_data1_x 156
#define R_set_altrep_data2_x 157
#define R_set_altstring_Elt_method_x 158
#define R_set_altstring_Is_sorted_method_x 159
#define R_set_altstring_No_NA_method_x 160
#define R_set_altstring_Set_elt_method_x 161
#define R_set_altvec_Dataptr_method_x 162
#define R_set_altvec_Dataptr_or_null_method_x 163
#define R_set_altvec_Extract_subset_method_x 164
#define R_tryEval_x 165
#define R_unLockBinding_x 166
#define Rf_GetOption1_x 167
#define Rf_NonNullStringMatch_x 168
#define Rf_PairToVectorList_x 169
#define Rf_PrintValue_x 170
#define Rf_ScalarComplex_x 171
#define Rf_ScalarInteger_x 172
#define Rf_ScalarLogical_x 173
#define Rf_ScalarRaw_x 174
#define Rf_ScalarReal_x 175
#define Rf_ScalarString_x 176
#define Rf_VectorToPairList_x 177
#define Rf_allocArray_x 178
#define Rf_allocList_x 179
#define Rf_allocMatrix_x 180
#define Rf_allocSExp_x 181
#define Rf_allocVector_x 182
#define Rf_any_duplicated_x 183
#define Rf_any_duplicated3_x 184
#define Rf_asChar_x 185
#define Rf_asCharacterFactor_x 186
#define Rf_asInteger_x 187
#define Rf_asLogical_x 188
#define Rf_asReal_x 189
#define Rf_asS4_x 190
#define Rf_bessel_i_x 191
#define Rf_bessel_i_ex_x 192
#define Rf_bessel_j_x 193
#define Rf_bessel_j_ex_x 194
#define Rf_bessel_k_x 195
#define Rf_bessel_k_ex_x 196
#define Rf_bessel_y_x 197
#define Rf_bessel_y_ex_x 198
#define Rf_beta_x 199
#define Rf_choose_x 200
#define Rf_classgets_x 201
#define Rf_coerceVector_x 202
#define Rf_cons_x 203
#define Rf_copyListMatrix_x 204
#define Rf_copyMatrix_x 205
#define Rf_copyMostAttrib_x 206
#define Rf_cospi_x 207
#define Rf_dbeta_x 208
#define Rf_dbinom_x 209
#define Rf_dcauchy_x 210
#define Rf_dchisq_x 211
#define Rf_defineVar_x 212
#define Rf_dexp_x 213
#define Rf_df_x 214
#define Rf_dgamma_x 215
#define Rf_dgeom_x 216
#define Rf_dhyper_x 217
#define Rf_digamma_x 218
#define Rf_dlnorm_x 219
#define Rf_dlogis_x 220
#define Rf_dnbeta_x 221
#define Rf_dnbinom_x 222
#define Rf_dnbinom_mu_x 223
#define Rf_dnchisq_x 224
#define Rf_dnf_x 225
#define Rf_dnorm4_x 226
#define Rf_dnt_x 227
#define Rf_dpois_x 228
#define Rf_dpsifn_x 229
#define Rf_dsignrank_x 230
#define Rf_dt_x 231
#define Rf_dunif_x 232
#define Rf_duplicate_x 233
#define Rf_duplicated_x 234
#define Rf_dweibull_x 235
#define Rf_dwilcox_x 236
#define Rf_error_x 237
#define Rf_errorcall_x 238
#define Rf_eval_x 239
#define Rf_findFun_x 240
#define Rf_findVar_x 241
#define Rf_findVarInFrame_x 242
#define Rf_findVarInFrame3_x 243
#define Rf_fprec_x 244
#define Rf_ftrunc_x 245
#define Rf_gammafn_x 246
#define Rf_getAttrib_x 247
#define Rf_gsetVar_x 248
#define Rf_inherits_x 249
#define Rf_install_x 250
#define Rf_installChar_x 251
#define Rf_isNull_x 252
#define Rf_isObject_x 253
#define Rf_isString_x 254
#define Rf_lbeta_x 255
#define Rf_lchoose_x 256
#define Rf_lengthgets_x 257
#define Rf_lgamma1p_x 258
#define Rf_lgammafn_x 259
#define Rf_lgammafn_sign_x 260
#define Rf_log1pexp_x 261
#define Rf_log1pmx_x 262
#define Rf_logspace_add_x 263
#define Rf_logspace_sub_x 264
#define Rf_mkCharLenCE_x 265
#define Rf_namesgets_x 266
#define Rf_ncols_x 267
#define Rf_nrows_x 268
#define Rf_pbeta_x 269
#define Rf_pbinom_x 270
#define Rf_pcauchy_x 271
#define Rf_pchisq_x 272
#define Rf_pentagamma_x 273
#define Rf_pexp_x 274
#define Rf_pf_x 275
#define Rf_pgamma_x 276
#define Rf_pgeom_x 277
#define Rf_phyper_x 278
#define Rf_plnorm_x 279
#define Rf_plogis_x 280
#define Rf_pnbeta_x 281
#define Rf_pnbinom_x 282
#define Rf_pnbinom_mu_x 283
#define Rf_pnchisq_x 284
#define Rf_pnf_x 285
#define Rf_pnorm5_x 286
#define Rf_pnorm_both_x 287
#define Rf_pnt_x 288
#define Rf_ppois_x 289
#define Rf_protect_x 290
#define Rf_psigamma_x 291
#define Rf_psignrank_x 292
#define Rf_pt_x 293
#define Rf_ptukey_x 294
#define Rf_punif_x 295
#define Rf_pweibull_x 296
#define Rf_pwilcox_x 297
#define Rf_qbeta_x 298
#define Rf_qbinom_x 299
#define Rf_qcauchy_x 300
#define Rf_qchisq_x 301
#define Rf_qexp_x 302
#define Rf_qf_x 303
#define Rf_qgamma_x 304
#define Rf_qgeom_x 305
#define Rf_qhyper_x 306
#define Rf_qlnorm_x 307
#define Rf_qlogis_x 308
#define Rf_qnbeta_x 309
#define Rf_qnbinom_x 310
#define Rf_qnbinom_mu_x 311
#define Rf_qnchisq_x 312
#define Rf_qnf_x 313
#define Rf_qnorm5_x 314
#define Rf_qnt_x 315
#define Rf_qpois_x 316
#define Rf_qsignrank_x 317
#define Rf_qt_x 318
#define Rf_qtukey_x 319
#define Rf_qunif_x 320
#define Rf_qweibull_x 321
#define Rf_qwilcox_x 322
#define Rf_rbeta_x 323
#define Rf_rbinom_x 324
#define Rf_rcauchy_x 325
#define Rf_rchisq_x 326
#define Rf_rexp_x 327
#define Rf_rf_x 328
#define Rf_rgamma_x 329
#define Rf_rgeom_x 330
#define Rf_rhyper_x 331
#define Rf_rlnorm_x 332
#define Rf_rlogis_x 333
#define Rf_rmultinom_x 334
#define Rf_rnbinom_x 335
#define Rf_rnbinom_mu_x 336
#define Rf_rnchisq_x 337
#define Rf_rnorm_x 338
#define Rf_rpois_x 339
#define Rf_rsignrank_x 340
#define Rf_rt_x 341
#define Rf_runif_x 342
#define Rf_rweibull_x 343
#define Rf_rwilcox_x 344
#define Rf_setAttrib_x 345
#define Rf_setVar_x 346
#define Rf_sign_x 347
#define Rf_sinpi_x 348
#define Rf_str2type_x 349
#define Rf_tanpi_x 350
#define Rf_tetragamma_x 351
#define Rf_trigamma_x 352
#define Rf_unprotect_x 353
#define Rf_unprotect_ptr_x 354
#define Rf_warning_x 355
#define Rf_warningcall_x 356
#define Rprintf_x 357
#define SETCAD4R_x 358
#define SETCADDDR_x 359
#define SETCADDR_x 360
#define SETCADR_x 361
#define SETCAR_x 362
#define SETCDR_x 363
#define SETLENGTH_x 364
#define SETLEVELS_x 365
#define SET_ATTRIB_x 366
#define SET_BODY_x 367
#define SET_CLOENV_x 368
#define SET_ENCLOS_x 369
#define SET_FORMALS_x 370
#define SET_NAMED_FASTR_x 371
#define SET_OBJECT_x 372
#define SET_RDEBUG_x 373
#define SET_RSTEP_x 374
#define SET_S4_OBJECT_x 375
#define SET_STRING_ELT_x 376
#define SET_SYMVALUE_x 377
#define SET_TAG_x 378
#define SET_TRUELENGTH_x 379
#define SET_TYPEOF_x 380
#define SET_VECTOR_ELT_x 381
#define STRING_ELT_x 382
#define STRING_IS_SORTED_x 383
#define STRING_NO_NA_x 384
#define SYMVALUE_x 385
#define TAG_x 386
#define TRUELENGTH_x 387
#define TYPEOF_x 388
#define UNSET_S4_OBJECT_x 389
#define VECTOR_ELT_x 390
#define exp_rand_x 391
#define forceSymbols_x 392
#define gdActivate_x 393
#define gdCircle_x 394
#define gdClip_x 395
#define gdClose_x 396
#define gdDeactivate_x 397
#define gdFlush_x 398
#define gdHold_x 399
#define gdLine_x 400
#define gdLocator_x 401
#define gdMetricInfo_x 402
#define gdMode_x 403
#define gdNewPage_x 404
#define gdOpen_x 405
#define gdPath_x 406
#define gdPolygon_x 407
#define gdPolyline_x 408
#define gdRaster_x 409
#define gdRect_x 410
#define gdSize_x 411
#define gdText_x 412
#define gdcSetColor_x 413
#define gdcSetFill_x 414
#define gdcSetFont_x 415
#define gdcSetLine_x 416
#define getCCallable_x 417
#define getConnectionClassString_x 418
#define getEmbeddingDLLInfo_x 419
#define getOpenModeString_x 420
#define getStrWidth_x 421
#define getSummaryDescription_x 422
#define isSeekable_x 423
#define match5_x 424
#define norm_rand_x 425
#define octsize_x 426
#define registerCCallable_x 427
#define registerRoutines_x 428
#define restoreHandlerStacks_x 429
#define setDotSymbolValues_x 430
#define unif_rand_x 431
#define useDynamicSymbols_x 432

#define UPCALLS_TABLE_SIZE 433

#endif // RFFI_UPCALLSINDEX_H
