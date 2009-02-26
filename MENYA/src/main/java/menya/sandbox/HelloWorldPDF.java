package menya.sandbox;

import java.io.IOException;

import org.pdfbox.exceptions.COSVisitorException;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDPage;
import org.pdfbox.pdmodel.edit.PDPageContentStream;
import org.pdfbox.pdmodel.font.PDType1Font;

public class HelloWorldPDF {

	public void go() {

		String file = "/tmp/test.pdf";
		String message = "Hello, World!";

		PDDocument doc = null;
		try {
			doc = new PDDocument();
			PDPage page = new PDPage();
			doc.addPage(page);
			org.pdfbox.pdmodel.font.PDFont font = PDType1Font.HELVETICA_BOLD;
			PDPageContentStream contentStream = new PDPageContentStream(doc,
					page);
			contentStream.beginText();
			contentStream.setFont(font, 12F);
			contentStream.moveTextPositionByAmount(100F, 700F);
			contentStream.drawString(message);
			contentStream.endText();
			
			contentStream.fillRect(72F, 72F, 72F,72F);
			
			//contentStream.drawLine(144f,144f,288f,288f);
			
			contentStream.close();
			doc.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (COSVisitorException e) {
			e.printStackTrace();
		} finally {
			if (doc != null)
				try {
					doc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new HelloWorldPDF().go();
	}

}
