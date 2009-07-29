package menya.core.model;

import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import menya.core.document.IDocument;
import menya.core.document.ILayer;
import menya.core.document.IPage;
import menya.core.document.layers.PDFLayer;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdfviewer.PageDrawer;
import org.apache.pdfbox.pdfwriter.COSWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.util.PDFOperator;

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
    private static final String PDF_PIECE_INFO = "PieceInfo";

    /**
     * 
     */
    private static final String PDF_LAYER_PREFIX = "Layer";

    /**
     * 
     */
    private static final String PDF_PRIVATE = "Private";

    /**
     * 
     */
    private static final String PDF_MENYA_DICT = "Menya";

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
                Document.removeMenyaLayers(pdpage, pddoc);
                Document.createMenyaPage(drawer, d, pdpage);
            } else {
                Document.LOGGER.warning("Unknown Class in PageList: "
                        + pg.getClass());
            }
        }
        return d;
    }

    /**
     * @param pdpage
     */
    private static void removeMenyaLayers(final PDPage pdpage,
            final PDDocument pddoc) throws IOException {
        try {
            final List<?> tokens = pdpage.getContents().getStream()
                    .getStreamTokens();
            final Deque<COSBase> arguments = new LinkedList<COSBase>();
            final PDStream pds = new PDStream(pddoc);
            pds.addCompression();
            final OutputStream os = pds.createOutputStream();
            final COSWriter writer = new COSWriter(os);

            boolean invisa = false;
            for (final Object t : tokens) {
                if (t instanceof COSBase) {
                    arguments.addLast((COSBase) t);
                } else if (t instanceof PDFOperator) {
                    final PDFOperator o = (PDFOperator) t;
                    if ("BDC".equals(o.getOperation())) {
                        // TODO: This filters ALL Layers, but it should only
                        // filter menya layers.
                        arguments.clear();
                        invisa = true;
                    }
                    if (!invisa) {
                        while (!arguments.isEmpty()) {
                            final COSBase b = arguments.removeFirst();
                            b.accept(writer);
                            os.write(COSWriter.SPACE);
                        }
                        os.write(o.getOperation().getBytes());
                        os.write(COSWriter.SPACE);
                    }
                    if ("EMC".equals(o.getOperation())) {
                        invisa = false;
                    }
                } else {
                    Document.LOGGER.warning("Unsupported Token: "
                            + t.getClass());
                }
            }
            writer.close();
            pdpage.setContents(pds);
        } catch (final COSVisitorException e) {
            throw new IOException("Error filtering ContentStream", e);
        }
    }

    /**
     * @param drawer
     * @param d
     * @param pdpage
     */
    private static void createMenyaPage(final PageDrawer drawer,
            final Document d, final PDPage pdpage) throws IOException {
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
        final ILayer pdfLayer = new PDFLayer(drawer, pdpage, pageDimension);
        menyaPage.addLayer(pdfLayer);
        Document.extractSerializedLayers(pdpage, menyaPage);
        d.pages.add(menyaPage);
    }

    /**
     * @param pdpage
     * @param menyaPage
     */
    private static void extractSerializedLayers(final PDPage pdpage,
            final Page menyaPage) throws IOException {
        // TODO: Check LastModified to ensure menya metadata is still current.
        try {
            final COSDictionary pieceInfo = Document.getPieceInfo(pdpage);
            final COSDictionary menyaDict = Document.getAndCreateDict(
                    pieceInfo, Document.PDF_MENYA_DICT);
            final COSDictionary privateDict = Document.getAndCreateDict(
                    menyaDict, Document.PDF_PRIVATE);
            int i = 0;
            COSBase cb = privateDict.getDictionaryObject(Document
                    .getLayerName(i));
            while (cb instanceof COSString) {
                final COSString cs = (COSString) cb;
                final byte[] buf = cs.getBytes();
                final ByteArrayInputStream bis = new ByteArrayInputStream(buf);
                final ObjectInputStream ois = new ObjectInputStream(bis);
                final ILayer layer = (ILayer) ois.readObject();
                menyaPage.addLayer(layer);
                i++;
                cb = privateDict.getDictionaryObject(Document.getLayerName(i));
            }
        } catch (final ClassNotFoundException e) {
            throw new IOException("Failed to read embedded Menya Layer", e);
        }
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
                final List<ILayer> menyaLayers = new ArrayList<ILayer>();
                final PDFLayer pdfLayer = this.findPDFLayer(page, menyaLayers);
                if (pdfLayer == null) {
                    throw new IOException(
                            "Failed to find PDF Layer in all pages");
                }
                Document.storeMenyaLayers(now, menyaLayers, pdfLayer,
                        this.pdfDocument);
            }
            this.pdfDocument.save(filename);
        } catch (final COSVisitorException e) {
            throw new IOException("Failure during save as PDF", e);
        }
    }

    /**
     * @param now
     * @param menyaLayers
     * @param pdfLayer
     * @throws IOException
     */
    private static void storeMenyaLayers(final Calendar now,
            final List<ILayer> menyaLayers, final PDFLayer pdfLayer,
            final PDDocument pddoc) throws IOException {
        final PDPage pdpage = pdfLayer.getPage();
        Document.storeGraphicalMenyaData(menyaLayers, pdpage, pddoc);
        Document.storeSerializedMenyaData(now, menyaLayers, pdpage);
    }

    /**
     * @param menyaLayers
     * @param pdpage
     * @throws IOException
     */
    private static void storeGraphicalMenyaData(final List<ILayer> menyaLayers,
            final PDPage pdpage, final PDDocument pddoc) throws IOException {
        final COSDictionary ocg = Document.createMenyaPDFLayers(pddoc);

        final COSDictionary properties = Document.getAndCreateDict(pdpage
                .getResources().getCOSDictionary(), "Properties");
        properties.setItem("MC0", ocg);

        final PDPageContentStream contentStream = new PDPageContentStream(
                pddoc, pdpage, true, true);
        contentStream.appendRawCommands("/OC /MC0 BDC ");

        // TODO: Add content here.

        contentStream.appendRawCommands(" EMC");
        contentStream.close();

    }

    /**
     * @param pddoc
     */
    private static COSDictionary createMenyaPDFLayers(final PDDocument pddoc) {
        final COSArray intentArray = new COSArray();
        intentArray.add(COSName.getPDFName("View"));
        intentArray.add(COSName.getPDFName("Design"));
        final COSDictionary ocg = new COSDictionary();
        ocg.setName("Type", "OCG");
        ocg.setString("Name", "Layer 1");
        ocg.setItem("Intent", intentArray);

        final COSDictionary ocp = Document.getAndCreateDict(pddoc
                .getDocumentCatalog().getCOSDictionary(), "OCProperties");
        final COSDictionary d = Document.getAndCreateDict(ocp, "D");
        final COSArray order = Document.getAndCreateArray(d, "Order");
        order.add(ocg);
        final COSArray on = Document.getAndCreateArray(d, "ON");
        on.add(ocg);
        final COSArray ocgs = Document.getAndCreateArray(ocp, "OCGs");
        ocgs.add(ocg);
        return ocg;
    }

    /**
     * @param now
     * @param menyaLayers
     * @param pdpage
     * @throws IOException
     */
    private static void storeSerializedMenyaData(final Calendar now,
            final List<ILayer> menyaLayers, final PDPage pdpage)
            throws IOException {
        final COSDictionary pieceInfo = Document.getPieceInfo(pdpage);
        final COSDictionary menyaDict = Document.getAndCreateDict(pieceInfo,
                Document.PDF_MENYA_DICT);
        pdpage.getCOSDictionary().setDate(Document.PDF_LAST_MODIFIED, now);
        menyaDict.setDate(Document.PDF_LAST_MODIFIED, now);
        final COSDictionary privateDict = Document.getAndCreateDict(menyaDict,
                Document.PDF_PRIVATE);

        privateDict.clear();
        for (int i = 0; i < menyaLayers.size(); i++) {
            final String name = Document.getLayerName(i);
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(menyaLayers.get(i));
            oos.close();
            privateDict.setItem(name, new COSString(bos.toByteArray()));
        }
    }

    /**
     * @param index
     * @return
     */
    private static String getLayerName(final int index) {
        return new StringBuilder(Document.PDF_LAYER_PREFIX).append(index)
                .toString();
    }

    /**
     * @param page
     * @param menyaLayers
     * @return
     * @throws IOException
     */
    private PDFLayer findPDFLayer(final IPage page,
            final List<ILayer> menyaLayers) throws IOException {
        PDFLayer pdfLayer = null;
        for (final ILayer layer : page.getLayers()) {
            if (layer instanceof PDFLayer) {
                pdfLayer = (PDFLayer) layer;
            } else if (layer instanceof Serializable) {
                menyaLayers.add(layer);
            } else {
                throw new IOException("Not all layers are Serializable");
            }
        }
        return pdfLayer;
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

    private static COSArray getAndCreateArray(final COSDictionary parent,
            final String name) {
        COSArray a = (COSArray) parent.getItem(COSName.getPDFName(name));
        if (a == null) {
            a = new COSArray();
            parent.setItem(name, a);
        }
        return a;
    }

    private static COSDictionary getPieceInfo(final PDPage page) {
        return Document.getAndCreateDict(page.getCOSDictionary(),
                Document.PDF_PIECE_INFO);
    }

}
