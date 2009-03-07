package menya.sandbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.pdfbox.pdfviewer.PageDrawer;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDPage;
import org.pdfbox.pdmodel.common.PDRectangle;

public class ShowPdf extends JComponent {

    private PDDocument doc;
    private int pagenum;
    private Dimension pageDimension;
    private PDPage page;
    private PageDrawer drawer;

    public ShowPdf(final String filename) {
        try {
            this.doc = PDDocument.load(filename);
            this.drawer = new PageDrawer();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        this.loadPage();
    }

    private void loadPage() {
        final List<?> pages = this.doc.getDocumentCatalog().getAllPages();
        this.page = (PDPage) pages.get(this.pagenum);
        final PDRectangle pageSize = this.page.findMediaBox();

        final int rotation = this.page.findRotation();
        this.pageDimension = pageSize.createDimension();
        if (rotation == 90 || rotation == 270) {
            this.pageDimension = new Dimension(this.pageDimension.height,
                    this.pageDimension.width);
        }
        // TODO : check roation = 0 || 180
        this.setPreferredSize(this.pageDimension);
    }

    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        try {
            g2d.setColor(Color.WHITE);
            g2d.fill(new Rectangle2D.Double(0, 0,
                    this.pageDimension.getWidth(), this.pageDimension
                            .getHeight()));
            this.drawer.drawPage(g, this.page, this.pageDimension);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) {
        final JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        final String filename = chooser.getSelectedFile().getAbsolutePath();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final JFrame mainFrame = new JFrame();
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                final JScrollPane contentPane = new JScrollPane();
                mainFrame.setContentPane(contentPane);
                contentPane.setViewportView(new ShowPdf(filename));
                mainFrame.setVisible(true);
            }
        });
    }

}
