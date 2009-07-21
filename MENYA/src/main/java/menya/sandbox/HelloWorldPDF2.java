// CHECKSTYLE:OFF This is a sandbox file.
package menya.sandbox;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class HelloWorldPDF2 {

    private static final float CM_TO_INCH = 1.0f / 2.54f;
    private static final float INCH_TO_PT = 72f;
    private static final float CM_TO_PT = HelloWorldPDF2.CM_TO_INCH
            * HelloWorldPDF2.INCH_TO_PT;

    public void go() {

        final String file = "/tmp/test.pdf";
        final String message = "Hello, World!";

        PDDocument doc = null;
        try {
            doc = new PDDocument();
            final PDPage page = new PDPage();
            page.setMediaBox(new PDRectangle(10.0f * HelloWorldPDF2.CM_TO_PT,
                    10.0f * HelloWorldPDF2.CM_TO_PT));
            doc.addPage(page);
            final Calendar now = Calendar.getInstance();

            final COSDictionary dict = this.getPieceInfo(page);
            final COSDictionary menyadict = this
                    .getAndCreateDict(dict, "Menya");
            page.getCOSDictionary().setDate("LastModified", now);
            menyadict.setDate("LastModified", now);
            final COSDictionary privateDict = this.getAndCreateDict(menyadict,
                    "Private");
            privateDict.setString("Test", "123");

            final org.apache.pdfbox.pdmodel.font.PDFont font = PDType1Font.HELVETICA_BOLD;
            final PDPageContentStream contentStream = new PDPageContentStream(
                    doc, page);
            contentStream.beginText();
            contentStream.setFont(font, 12F);
            contentStream.moveTextPositionByAmount(2 * HelloWorldPDF2.CM_TO_PT,
                    7 * HelloWorldPDF2.CM_TO_PT);
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
            final PDPage page = (PDPage) pages.get(0);
            final COSDictionary dict = this.getPieceInfo(page);
            final COSDictionary menyadict = this
                    .getAndCreateDict(dict, "Menya");
            System.out.println("MenyaLastMod: "
                    + menyadict.getDate("LastModified").getTimeInMillis());
            final COSDictionary privateDict = this.getAndCreateDict(menyadict,
                    "Private");
            System.out.println("MenyaPrivTest: "
                    + privateDict.getString("Test"));

        } catch (final IOException e) {
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

    private COSDictionary getPieceInfo(final PDPage page) {
        return this.getAndCreateDict(page.getCOSDictionary(), "PieceInfo");
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        new HelloWorldPDF2().go();
    }

}
