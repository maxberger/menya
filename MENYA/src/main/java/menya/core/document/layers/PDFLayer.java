/**
 * 
 */
package menya.core.document.layers;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import menya.core.model.GraphicalData;
import menya.gui.GUI;

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

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(GUI.class.toString());

    private final PageDrawer drawer;
    private final PDPage page;
    private final Dimension dimension;

    /**
     * Create a new PDF based layer.
     * 
     * @param pddoc
     *            PDDocument
     * @param pgdrawer
     *            page drawer object
     * @param pdpage
     *            page object
     * @param pageDimension
     *            page dimensions
     */
    public PDFLayer(final PDDocument pddoc, final PageDrawer pgdrawer,
            final PDPage pdpage, final Dimension pageDimension) {
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

}
