package menya.sandbox;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.pdfbox.exceptions.COSVisitorException;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDPage;
import org.pdfbox.pdmodel.common.PDRectangle;
import org.pdfbox.pdmodel.edit.PDPageContentStream;
import org.pdfbox.pdmodel.font.PDType1Font;

public class HelloWorldPDF {

    private static final float CM_TO_INCH = 1.0f / 2.54f;
    private static final float INCH_TO_PT = 72f;
    private static final float CM_TO_PT = HelloWorldPDF.CM_TO_INCH
            * HelloWorldPDF.INCH_TO_PT;

    public void go() {

        final String file = "/tmp/test.pdf";
        final String message = "Hello, World!";

        PDDocument doc = null;
        try {
            doc = new PDDocument();
            final PDPage page = new PDPage();
            page.setMediaBox(new PDRectangle(10.0f * HelloWorldPDF.CM_TO_PT,
                    10.0f * HelloWorldPDF.CM_TO_PT));
            doc.addPage(page);
            final org.pdfbox.pdmodel.font.PDFont font = PDType1Font.HELVETICA_BOLD;
            final PDPageContentStream contentStream = new PDPageContentStream(
                    doc, page);
            contentStream.beginText();
            contentStream.setFont(font, 12F);
            contentStream.moveTextPositionByAmount(2 * HelloWorldPDF.CM_TO_PT,
                    7 * HelloWorldPDF.CM_TO_PT);
            contentStream.drawString(message);
            contentStream.endText();

            contentStream.fillRect(1 * HelloWorldPDF.CM_TO_PT,
                    1 * HelloWorldPDF.CM_TO_PT, 1 * HelloWorldPDF.CM_TO_PT,
                    1 * HelloWorldPDF.CM_TO_PT);

            // contentStream.drawLine(144f,144f,288f,288f);

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

    /**
     * @param args
     */
    public static void main(final String[] args) {
        new HelloWorldPDF().go();
    }

}
