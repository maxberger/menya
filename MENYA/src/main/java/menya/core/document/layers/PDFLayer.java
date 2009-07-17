/**
 * 
 */
package menya.core.document.layers;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import menya.core.model.GraphicalData;

import org.apache.pdfbox.pdfviewer.PageDrawer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

/**
 * A layer based on a PDF document.
 * 
 * @author Max
 * @version $Revision$
 */
public class PDFLayer extends ALayer {

    private final PageDrawer drawer;
    private final PDPage page;
    private final Dimension dimension;

    /**
     * Create a new PDF based layer.
     * 
     * @param pddoc
     *            PDDocument
     * @param drawer
     *            page drawer object
     * @param pdpage
     *            page object
     * @param pageDimension
     *            page dimensions
     */
    public PDFLayer(final PDDocument pddoc, final PageDrawer drawer,
            final PDPage pdpage, final Dimension pageDimension) {
        this.drawer = drawer;
        this.page = pdpage;
        this.dimension = pageDimension;
    }

    private class PDFGraphics implements GraphicalData {

        /** {@inheritDoc} */
        @Override
        public void draw(final Graphics2D g2d) {
            try {
                PDFLayer.this.drawer.drawPage(g2d, PDFLayer.this.page,
                        PDFLayer.this.dimension);
            } catch (final IOException io) {
                // TODO: Log.
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

}
