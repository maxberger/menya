// CHECKSTYLE:OFF This is a sandbox file.
package menya.sandbox;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.PDFOperator;

public class HelloWorldPDF3 {

    private static final float CM_TO_INCH = 1.0f / 2.54f;
    private static final float INCH_TO_PT = 72f;
    private static final float CM_TO_PT = HelloWorldPDF3.CM_TO_INCH
            * HelloWorldPDF3.INCH_TO_PT;

    public void go() {

        final String file = "/tmp/test.pdf";
        final String message = "Hello, World!";

        PDDocument doc = null;
        try {
            doc = new PDDocument();
            final PDPage page = new PDPage();
            page.setMediaBox(new PDRectangle(10.0f * HelloWorldPDF3.CM_TO_PT,
                    10.0f * HelloWorldPDF3.CM_TO_PT));
            doc.addPage(page);
            final org.apache.pdfbox.pdmodel.font.PDFont font = PDType1Font.HELVETICA_BOLD;
            final PDPageContentStream contentStream = new PDPageContentStream(
                    doc, page);
            contentStream.beginText();
            contentStream.setFont(font, 12F);
            contentStream.moveTextPositionByAmount(2 * HelloWorldPDF3.CM_TO_PT,
                    7 * HelloWorldPDF3.CM_TO_PT);
            contentStream.drawString(message);
            contentStream.endText();
            contentStream.close();
            doc.save(file);
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final COSVisitorException e) {
            e.printStackTrace();
        } finally {
            if (doc != null) {
                try {
                    doc.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        doc = null;
        try {
            doc = PDDocument.load(file);
            final List<?> pages = doc.getDocumentCatalog().getAllPages();
            System.out.println("NumPages: " + pages.size());

            final COSArray intentArray = new COSArray();
            intentArray.add(COSName.getPDFName("View"));
            intentArray.add(COSName.getPDFName("Design"));
            final COSDictionary ocg = new COSDictionary();
            ocg.setName("Type", "OCG");
            ocg.setString("Name", "Layer 1");
            ocg.setItem("Intent", intentArray);

            final COSDictionary ocp = this.getAndCreateDict(doc
                    .getDocumentCatalog().getCOSDictionary(), "OCProperties");
            final COSDictionary d = this.getAndCreateDict(ocp, "D");
            final COSArray order = this.getAndCreateArray(d, "Order");
            order.add(ocg);
            final COSArray on = this.getAndCreateArray(d, "ON");
            on.add(ocg);
            final COSArray ocgs = this.getAndCreateArray(ocp, "OCGs");
            ocgs.add(ocg);

            final PDPage page = (PDPage) pages.get(0);

            final COSDictionary properties = this.getAndCreateDict(page
                    .getResources().getCOSDictionary(), "Properties");
            properties.setItem("MC0", ocg);

            final PDPageContentStream contentStream = new PDPageContentStream(
                    doc, page, true, true);
            contentStream.appendRawCommands("/OC /MC0 BDC ");

            contentStream.fillRect(2 * HelloWorldPDF3.CM_TO_PT,
                    7 * HelloWorldPDF3.CM_TO_PT, 6 * HelloWorldPDF3.CM_TO_PT,
                    1 * HelloWorldPDF3.CM_TO_PT);
            contentStream.appendRawCommands(" EMC");
            contentStream.close();
            doc.save(file);
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final COSVisitorException e) {
            e.printStackTrace();
        } finally {
            if (doc != null) {
                try {
                    doc.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        doc = null;
        try {
            doc = PDDocument.load(file);
            final List<?> pages = doc.getDocumentCatalog().getAllPages();
            final PDPage page = (PDPage) pages.get(0);
            final List<?> tokens = page.getContents().getStream()
                    .getStreamTokens();
            final Deque<COSBase> arguments = new LinkedList<COSBase>();
            final StringBuilder newStream = new StringBuilder();
            boolean invisa = false;
            for (final Object t : tokens) {
                if (t instanceof COSBase) {
                    arguments.addLast((COSBase) t);
                } else if (t instanceof PDFOperator) {
                    final PDFOperator o = (PDFOperator) t;
                    if ("BDC".equals(o.getOperation())) {
                        invisa = true;
                    }
                    if (!invisa) {
                        while (!arguments.isEmpty()) {
                            final COSBase b = arguments.removeFirst();
                            if (b instanceof COSNumber) {
                                newStream.append(((COSNumber) b).doubleValue());
                            } else if (b instanceof COSString) {
                                newStream.append('(').append(
                                        ((COSString) b).getString())
                                        .append(')');
                            } else if (b instanceof COSName) {
                                newStream.append('/').append(
                                        ((COSName) b).getName());
                            } else {
                                System.out.println("Cannot push: " + b);
                            }
                            newStream.append(' ');
                        }
                        newStream.append(o.getOperation()).append(' ');
                    }
                    if ("EMC".equals(o.getOperation())) {
                        invisa = false;
                    }
                }
            }
            final PDPage page2 = new PDPage();
            doc.addPage(page2);
            final PDPageContentStream contentStream = new PDPageContentStream(
                    doc, page2);
            contentStream.appendRawCommands(newStream.toString());
            contentStream.close();
            doc.save(file);
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final COSVisitorException e) {
            e.printStackTrace();
        } finally {
            if (doc != null) {
                try {
                    doc.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            Desktop.getDesktop().open(new File(file));
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private COSDictionary getAndCreateDict(final COSDictionary parent,
            final String name) {
        COSDictionary dict = (COSDictionary) parent.getDictionaryObject(name);
        if (dict == null) {
            dict = new COSDictionary();
            parent.setItem(name, dict);
        }
        return dict;
    }

    private COSArray getAndCreateArray(final COSDictionary parent,
            final String name) {
        COSArray a = (COSArray) parent.getItem(COSName.getPDFName(name));
        if (a == null) {
            a = new COSArray();
            parent.setItem(name, a);
        }
        return a;
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        new HelloWorldPDF3().go();
    }

}
