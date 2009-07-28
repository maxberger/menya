package menya.core.model;

import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import menya.core.document.IDocument;
import menya.core.document.ILayer;
import menya.core.document.IPage;
import menya.core.document.layers.PDFLayer;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.exceptions.COSVisitorException;
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
public final class Document implements IDocument {

    /**
     * 
     */
    private static final String PDF_LAST_MODIFIED = "LastModified";

    /**
     * Rotated left (or right?).
     */
    private static final int LANDSCAPE_1 = 90;

    /**
     * Rotated right (or left?).
     */
    private static final int LANDSCAPE_2 = 270;

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(Document.class
            .toString());

    private final List<IPage> pages;

    private final PDDocument pdfDocument;

    /**
     * Default Constructor.
     * 
     * @param underlayingDocument
     *            Document this document is based on.
     */
    private Document(final PDDocument underlayingDocument) {
        this.pages = new ArrayList<IPage>();
        this.pdfDocument = underlayingDocument;
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
        PDDocument pddoc;
        try {
            pddoc = new PDDocument();
        } catch (final IOException io) {
            Document.LOGGER.log(Level.WARNING,
                    "Failed to base Document on PDF", io);
            pddoc = null;
        }
        final Document d = new Document(pddoc);
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
        final Document d = new Document(pddoc);

        final List<?> pages = pddoc.getDocumentCatalog().getAllPages();

        for (final Object pg : pages) {
            if (pg instanceof PDPage) {
                final PDPage pdpage = (PDPage) pg;
                final PDRectangle pageSize = pdpage.findMediaBox();
                final int rotation = pdpage.findRotation();
                Dimension pageDimension = pageSize.createDimension();
                if (rotation == Document.LANDSCAPE_1
                        || rotation == Document.LANDSCAPE_2) {
                    pageDimension = new Dimension(pageDimension.height,
                            pageDimension.width);
                    // TODO : check rotation = 0 || 180
                }
                final Page menyaPage = new Page();
                menyaPage.setPageSize(pageDimension);
                final ILayer pdfLayer = new PDFLayer(drawer, pdpage,
                        pageDimension);
                menyaPage.addLayer(pdfLayer);
                d.pages.add(menyaPage);
            } else {
                Document.LOGGER.warning("Unknown Class in PageList: "
                        + pg.getClass());
            }
        }
        return d;
    }

    /** {@inheritDoc} */
    @Override
    public void close() {
        if (this.pdfDocument != null) {
            try {
                this.pdfDocument.close();
            } catch (final IOException e) {
                Document.LOGGER.log(Level.WARNING,
                        "Failed to close PDFDocument", e);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void toPDF(final String filename) throws IOException {
        if (this.pdfDocument == null) {
            throw new IOException("Document is not based on a PDF");
        }
        try {
            final Calendar now = Calendar.getInstance();
            for (final IPage page : this.pages) {
                PDFLayer pdfLayer = null;
                final List<Serializable> menyaLayers = new ArrayList<Serializable>();
                for (final ILayer layer : page.getLayers()) {
                    if (layer instanceof PDFLayer) {
                        pdfLayer = (PDFLayer) layer;
                    } else if (layer instanceof Serializable) {
                        menyaLayers.add((Serializable) layer);
                    }
                }
                if (pdfLayer == null) {
                    throw new IOException(
                            "Failed to find PDF Layer in all pages");
                }
                final PDPage pdpage = pdfLayer.getPage();
                final COSDictionary pieceInfo = Document.getPieceInfo(pdpage);
                final COSDictionary menyaDict = Document.getAndCreateDict(
                        pieceInfo, "Menya");
                pdpage.getCOSDictionary().setDate(Document.PDF_LAST_MODIFIED,
                        now);
                menyaDict.setDate(Document.PDF_LAST_MODIFIED, now);
                final COSDictionary privateDict = Document.getAndCreateDict(
                        menyaDict, "Private");

                privateDict.clear();
                for (int i = 0; i < menyaLayers.size(); i++) {
                    final String name = new StringBuilder("Layer").append(i)
                            .toString();
                    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    final ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(menyaLayers.get(i));
                    oos.close();
                    privateDict.setItem(name, new COSString(bos.toByteArray()));
                }
            }
            this.pdfDocument.save(filename);
        } catch (final COSVisitorException e) {
            throw new IOException("Failure during save as PDF", e);
        }
    }

    private static COSDictionary getAndCreateDict(final COSDictionary parent,
            final String name) {
        COSDictionary dict = (COSDictionary) parent.getDictionaryObject(name);
        if (dict == null) {
            dict = new COSDictionary();
            parent.setItem(name, dict);
        }
        return dict;
    }

    private static COSDictionary getPieceInfo(final PDPage page) {
        return Document.getAndCreateDict(page.getCOSDictionary(), "PieceInfo");
    }

}
