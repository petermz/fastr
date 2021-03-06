/*
 * Copyright (C) 2001-3 Paul Murrell
 * Copyright (c) 1998-2013, The R Core Team
 * Copyright (c) 2017, 2020, Oracle and/or its affiliates
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, a copy is available at
 * https://www.R-project.org/Licenses/
 */
/*
 * Copyright (c) 2001-2015, The R Core Team
 * Copyright (c) 2017, Oracle and/or its affiliates
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, a copy is available at
 * https://www.R-project.org/Licenses/
 */
package com.oracle.truffle.r.library.fastrGrid;

import static com.oracle.truffle.r.library.fastrGrid.GridUtils.fmax;
import static com.oracle.truffle.r.library.fastrGrid.GridUtils.fmin;
import static com.oracle.truffle.r.library.fastrGrid.GridUtils.getDataAtMod;
import static com.oracle.truffle.r.library.fastrGrid.TransformMatrix.multiply;
import static com.oracle.truffle.r.library.fastrGrid.TransformMatrix.transLocation;
import static com.oracle.truffle.r.library.fastrGrid.TransformMatrix.translation;
import static com.oracle.truffle.r.library.fastrGrid.device.DrawingContext.INCH_TO_POINTS_FACTOR;
import static com.oracle.truffle.r.nodes.builtin.CastBuilder.Predef.abstractVectorValue;
import static com.oracle.truffle.r.nodes.builtin.CastBuilder.Predef.numericValue;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.profiles.ConditionProfile;
import com.oracle.truffle.r.library.fastrGrid.EdgeDetection.Rectangle;
import com.oracle.truffle.r.library.fastrGrid.Unit.UnitConversionContext;
import com.oracle.truffle.r.library.fastrGrid.device.DrawingContext;
import com.oracle.truffle.r.library.fastrGrid.device.GridDevice;
import com.oracle.truffle.r.nodes.builtin.NodeWithArgumentCasts.Casts;
import com.oracle.truffle.r.runtime.RInternalError;
import com.oracle.truffle.r.runtime.data.RDataFactory;
import com.oracle.truffle.r.runtime.data.RDoubleVector;
import com.oracle.truffle.r.runtime.data.RList;
import com.oracle.truffle.r.runtime.data.RNull;
import com.oracle.truffle.r.runtime.data.model.RAbstractContainer;
import com.oracle.truffle.r.runtime.data.RStringVector;
import com.oracle.truffle.r.runtime.data.model.RAbstractVector;
import com.oracle.truffle.r.runtime.nodes.RBaseNode;

/**
 * Implements what is in the original grid code implemented by {@code gridText} function.
 *
 * Note: the third parameter contains sequences {@code 1:max(length(x),length(y))}, where the
 * 'length' dispatches to S3 method giving us unit length like
 * {@link com.oracle.truffle.r.library.fastrGrid.Unit#getLength(RAbstractContainer)}.
 */
public final class GridTextNode extends RBaseNode {

    private final ConditionProfile checkOverlapProfile = ConditionProfile.createBinaryProfile();
    private final boolean draw;

    static void addGridTextCasts(Casts casts) {
        casts.arg(0).asStringVector();
        casts.arg(1).mustBe(abstractVectorValue());
        casts.arg(2).mustBe(abstractVectorValue());
        casts.arg(3).mustBe(numericValue()).asDoubleVector();
        casts.arg(4).mustBe(numericValue()).asDoubleVector();
        casts.arg(5).mustBe(numericValue()).asDoubleVector();
    }

    private GridTextNode(boolean draw) {
        this.draw = draw;
    }

    public static GridTextNode createDraw() {
        return new GridTextNode(true);
    }

    public static GridTextNode createCalculateBounds() {
        return new GridTextNode(false);
    }

    @TruffleBoundary
    public Object gridText(RStringVector textVec, RAbstractVector x, RAbstractVector y, RDoubleVector hjustVec, RDoubleVector vjustVec, RDoubleVector rotationVec,
                    boolean checkOverlapIn, double theta) {
        if (textVec.getLength() == 0) {
            return RNull.instance;
        }

        boolean checkOverlap = checkOverlapProfile.profile(checkOverlapIn);
        GridContext ctx = GridContext.getContext();
        GridDevice dev = ctx.getCurrentDevice();

        RList currentVP = ctx.getGridState().getViewPort();
        GPar gpar = GPar.create(ctx.getGridState().getGpar());
        ViewPortTransform vpTransform = ViewPortTransform.get(currentVP, dev);
        ViewPortContext vpContext = ViewPortContext.fromViewPort(currentVP);
        UnitConversionContext conversionCtx = new UnitConversionContext(vpTransform.size, vpContext, dev, gpar);

        int length = GridUtils.maxLength(x, y);

        // following variables will hold the (intermediate) results of bounds checking
        int boundsCount = 0;
        Point edge = null;
        double xmin = Double.MAX_VALUE;
        double xmax = -Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE;
        double ymax = -Double.MAX_VALUE;
        int ntxt = 0;   // number of texts that were actually used for bounds computation
        EdgeDetection.Rectangle[] bounds = null;
        if (checkOverlap || !draw) {
            bounds = new EdgeDetection.Rectangle[length];
        }

        for (int i = 0; i < length; i++) {
            Point loc = Point.fromUnits(x, y, i, conversionCtx);
            if (draw) {
                // transformation not necessary for bounds calculation
                loc = transLocation(loc, vpTransform.transform);
            }

            String text = textVec.getDataAt(i % textVec.getLength());
            double hjust = getDataAtMod(hjustVec, i);
            double vjust = getDataAtMod(vjustVec, i);
            double rotation = getDataAtMod(rotationVec, i);

            // update bounds if necessary
            boolean doDraw = true;
            Rectangle trect = null;
            if (checkOverlap || !draw) {
                trect = textRect(loc, hjust, vjust, rotation, text, gpar.getDrawingContext(i), dev);
                for (int j = 0; j < boundsCount; j++) {
                    if (trect.intersects(bounds[j])) {
                        doDraw = false;
                        break;
                    }
                }
                if (doDraw) {
                    bounds[boundsCount++] = trect;
                }
            }

            // actual drawing
            if (draw && doDraw) {
                text(loc.x, loc.y, text, hjust, vjust, rotation + vpTransform.rotationAngle, gpar.getDrawingContext(i), dev);
            }

            // or bounds checking
            if (!draw) {
                if (Double.isFinite(loc.x) && Double.isFinite(loc.y)) {
                    xmin = fmin(xmin, trect.x);
                    xmax = fmax(xmax, trect.x);
                    ymin = fmin(ymin, trect.y);
                    ymax = fmax(ymax, trect.y);
                    double[] xxx = new double[4];
                    double[] yyy = new double[4];
                    for (int j = 0; j < 4; j++) {
                        xxx[j] = trect.x[3 - j];
                        yyy[j] = trect.y[3 - j];
                    }
                    // Calculate edgex and edgey for case where this is the only rect
                    edge = EdgeDetection.polygonEdge(xxx, yyy, 4, theta);
                    ntxt++;
                }
            }
        }

        if (!draw && ntxt > 0) {
            // If there is more than one text, just produce edge based on bounding rect of all text
            if (ntxt > 1) {
                // Produce edge of rect bounding all text
                edge = EdgeDetection.rectEdge(xmin, ymin, xmax, ymax, theta);
            }

            double scale = GridContext.getContext().getGridState().getScale();
            double[] result = new double[4];
            result[0] = edge.x / scale;
            result[1] = edge.y / scale;
            result[2] = (xmax - xmin) / scale;
            result[3] = (ymax - ymin) / scale;
            return RDataFactory.createDoubleVector(result, RDataFactory.COMPLETE_VECTOR);
        }

        // NULL is OK result even for bound checking if there was no text actually "drawn", the R
        // wrapper deals with NULL in such case. For actual drawing case, we should always return
        // NULL
        return RNull.instance;
    }

    // transcribed from utils.c

    private static EdgeDetection.Rectangle textRect(Point loc, double xadj, double yadj, double rotation, String text, DrawingContext drawingCtx, GridDevice device) {
        // TODO: for expressions the h and w are calculated differently
        double h = device.getStringHeight(drawingCtx, text);
        double w = device.getStringWidth(drawingCtx, text);

        double[][] thisJustification = translation(-xadj * w, -yadj * h);
        double[][] thisRotation = TransformMatrix.rotation(rotation);
        double[][] transform = multiply(multiply(thisJustification, thisRotation), translation(loc.x, loc.y));

        Point bl = transLocation(new Point(0, 0), transform);
        Point br = transLocation(new Point(w, 0), transform);
        Point tr = transLocation(new Point(w, h), transform);
        Point tl = transLocation(new Point(0, h), transform);
        return new Rectangle(bl, br, tr, tl);
    }

    // transcribed from engine.c

    private static void text(double x, double y, String text, double xadjIn, double yadj, double rotationDegrees, DrawingContext drawingCtx, GridDevice device) {
        if (!Double.isFinite(yadj)) {
            throw RInternalError.unimplemented("'exact' vertical centering, see engine.c:1700");
        }
        double xadj = Double.isFinite(xadjIn) ? xadjIn : 0.5;

        double radRotation = Math.toRadians(rotationDegrees);
        double cosRot = Math.cos(radRotation);
        double sinRot = Math.sin(radRotation);
        String[] lines = text.split("\n");
        for (int lineIdx = 0; lineIdx < lines.length; lineIdx++) {
            double xoff;
            double yoff;
            if (lines.length == 1) {
                // simplification for single line
                xoff = x;
                yoff = y;
            } else {
                yoff = (1 - yadj) * (lines.length - 1) - lineIdx;
                // TODO: in the original the following formula uses "dd->dev->cra[1]"
                yoff *= (drawingCtx.getFontSize() * drawingCtx.getLineHeight()) / INCH_TO_POINTS_FACTOR;
                xoff = -yoff * sinRot;
                yoff = yoff * cosRot;
                xoff = x + xoff;
                yoff = y + yoff;
            }

            double xleft = xoff;
            double ybottom = yoff;
            // now determine bottom-left for THIS line
            if (xadj != 0.0 || yadj != 0.0) {
                // otherwise simply the initial values for xleft and ybottom are OK
                double width = device.getStringWidth(drawingCtx, lines[lineIdx]);
                double height = device.getStringHeight(drawingCtx, lines[lineIdx]);
                xleft = xoff - (xadj) * width * cosRot + yadj * height * sinRot;
                ybottom = yoff - (xadj) * width * sinRot - yadj * height * cosRot;
            }

            device.drawString(drawingCtx, xleft, ybottom, radRotation, lines[lineIdx]);
        }
    }
}
