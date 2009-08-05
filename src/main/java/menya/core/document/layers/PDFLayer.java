/*
 * This file is part of Menya.
 * 
 * Menya is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Menya is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Menya. If not, see <http://www.gnu.org/licenses/>.
 */
package menya.core.document.layers;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import menya.core.document.IPage;
import menya.core.model.GraphicalData;

import org.apache.pdfbox.pdfviewer.PageDrawer;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

/**
 * A layer based on a PDF document.
 * 
 * @author Max
 * @version $Revision$
 */
public class PDFLayer extends ALayer {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(PDFLayer.class
            .toString());

    private final PageDrawer drawer;
    private final PDPage page;
    private final Dimension dimension;

    /**
     * Create a new PDF based layer.
     * 
     * @param pgdrawer
     *            page drawer object
     * @param pdpage
     *            page object
     * @param pageDimension
     *            page dimensions
     */
    public PDFLayer(final PageDrawer pgdrawer, final PDPage pdpage,
            final Dimension pageDimension) {
        this.drawer = pgdrawer;
        this.page = pdpage;
        this.dimension = pageDimension;
    }

    private class PDFGraphics implements GraphicalData {

        protected PDFGraphics() {
            // Empty on purpose.
        }

        /** {@inheritDoc} */
        @Override
        public void draw(final Graphics2D g2d) {
            try {
                PDFLayer.this.drawer.drawPage(g2d, PDFLayer.this.page,
                        PDFLayer.this.dimension);
            } catch (final IOException io) {
                PDFLayer.LOGGER.log(Level.WARNING, "Failed to render page", io);
            }
        }
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public Iterable<GraphicalData> getGraphicalData() {
        final List l = Collections.singletonList(new PDFGraphics());
        final List<GraphicalData> g = l;
        return g;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasChanged() {
        return false;
    }

    /**
     * Getter for PDF page object.
     * 
     * @return the page.
     */
    public PDPage getPage() {
        return this.page;
    }

    /** {@inheritDoc} */
    @Override
    public void toPdf(final PDPageContentStream contentStream, final IPage ipage)
            throws IOException {
        throw new IOException("Unsupported Operation: toPDF on PDFLayer");
    }

}
