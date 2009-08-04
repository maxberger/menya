/*
 * Copyright 2009 - 2009 by Menya Project
 * 
 * This file is part of Menya.
 * 
 * Menya is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Menya is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser Public License along with
 * Menya. If not, see <http://www.gnu.org/licenses/>.
 */
package menya.core.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.PathIterator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import menya.core.document.IPage;
import menya.gui.PathShape;

import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.pdfwriter.COSWriter;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

/**
 * Implements the necessary functionality to be a drawn curve within a document.
 * 
 * @author Dominik
 * @author Max
 * @version $Revision$
 */
public class Curve implements GraphicalData, Serializable {

    /**
     * 
     */
    private static final int MAX_DATA_PER_POINT = 6;

    private static final long serialVersionUID = 1L;

    private static final int ONE_DATA_POINT = 2;

    private static final int TWO_DATA_POINTS = 4;

    private static final int THREE_DATA_POINTS = 6;

    private final List<Point> path = new ArrayList<Point>();

    /**
     * Default constructor.
     */
    public Curve() {
    }

    /** {@inheritDoc} */
    public void draw(final Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.fill(new PathShape(this.path));
    }

    /**
     * Adds a new Point to a curve.
     * 
     * @param p
     *            The point to add.
     */
    public void add(final Point p) {
        this.path.add(p);
    }

    /**
     * Draw the Curve onto a PDF.
     * 
     * @param contentStream
     *            the pdf content Stream
     * @param page
     *            The page this curve is drawn on.
     * @throws IOException
     *             if the curve can not be written.
     */
    public void toPdf(final PDPageContentStream contentStream, final IPage page)
            throws IOException {
        contentStream.setStrokingColor(Color.BLACK);
        final PathShape shape = new PathShape(this.path);
        final PathIterator it = shape.getPathIterator(null);
        final float[] points = new float[Curve.MAX_DATA_PER_POINT];
        final float height = (float) page.getPageSize().getHeight();
        while (!it.isDone()) {
            final int op = it.currentSegment(points);
            switch (op) {
            case PathIterator.SEG_CUBICTO:
                contentStream.appendRawCommands(this.floatToPDFString(points,
                        Curve.THREE_DATA_POINTS, height));
                contentStream.appendRawCommands("c ");
                break;
            case PathIterator.SEG_LINETO:
                contentStream.appendRawCommands(this.floatToPDFString(points,
                        Curve.ONE_DATA_POINT, height));
                contentStream.appendRawCommands("l ");
                break;
            case PathIterator.SEG_MOVETO:
                contentStream.appendRawCommands(this.floatToPDFString(points,
                        Curve.ONE_DATA_POINT, height));
                contentStream.appendRawCommands("m ");
                break;
            case PathIterator.SEG_QUADTO:
                contentStream.appendRawCommands(this.floatToPDFString(points,
                        Curve.TWO_DATA_POINTS, height));
                contentStream.appendRawCommands("y ");
                break;
            case PathIterator.SEG_CLOSE:
                contentStream.appendRawCommands("h\n");
                break;
            default:
                break;
            }
            it.next();
        }

        if (it.getWindingRule() == PathIterator.WIND_EVEN_ODD) {
            contentStream.appendRawCommands("f*\n");
        } else {
            contentStream.appendRawCommands("f\n");
        }
    }

    /**
     * @param points
     * @param cpoints
     * @throws IOException
     */
    private byte[] floatToPDFString(final float[] points, final int count,
            final float height) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (int i = 0; i < count; i++) {
            float point = points[i];
            if (i % 2 == 1) {
                // Y-Values are switched on PDF vs. G2d
                point = height - point;
            }
            final COSFloat f = new COSFloat(point);
            f.writePDF(bos);
            bos.write(COSWriter.SPACE);
        }
        return bos.toByteArray();
    }
}
