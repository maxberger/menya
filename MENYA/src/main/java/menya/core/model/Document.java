package menya.core.model;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import menya.core.document.IDocument;
import menya.core.document.ILayer;
import menya.core.document.IPage;
import menya.core.document.layers.PDFLayer;

import org.apache.pdfbox.pdfviewer.PageDrawer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

/**
 * Default Implementation of a Document.
 * 
 * @author Max
 * @version $Revision$
 */
public class Document implements IDocument {

    private final List<IPage> pages;

    /**
     * Default Constructor.
     */
    private Document() {
        this.pages = new ArrayList<IPage>();
    }

    /** {@inheritDoc} */
    public List<IPage> getPages() {
        return Collections.unmodifiableList(this.pages);
    }

    /**
     * Create an empty document.
     * 
     * @return an empty Document with one page.
     */
    public static Document emptyDocument() {
        final Document d = new Document();
        d.pages.add(new Page());
        return d;
    }

    /**
     * Create a new Document from a given PDF file.
     * 
     * @param filename
     *            file to open
     * @return a Document instance.
     * @throws IOException
     *             if the document could not be loaded.
     */
    public static Document fromPDF(final String filename) throws IOException {
        final PDDocument pddoc = PDDocument.load(filename);
        final PageDrawer drawer = new PageDrawer();
        final Document d = new Document();

        final List<?> pages = pddoc.getDocumentCatalog().getAllPages();

        for (final Object pg : pages) {
            if (pg instanceof PDPage) {
                final PDPage pdpage = (PDPage) pg;
                final PDRectangle pageSize = pdpage.findMediaBox();
                final int rotation = pdpage.findRotation();
                Dimension pageDimension = pageSize.createDimension();
                if (rotation == 90 || rotation == 270) {
                    pageDimension = new Dimension(pageDimension.height,
                            pageDimension.width);
                    // TODO : check rotation = 0 || 180
                }
                final Page menyaPage = new Page();
                menyaPage.setPageSize(pageDimension);
                final ILayer pdfLayer = new PDFLayer(pddoc, drawer, pdpage,
                        pageDimension);
                menyaPage.addLayer(pdfLayer);
                d.pages.add(menyaPage);
            } else {
                // TODO: Log!
            }
        }
        return d;
    }
}
