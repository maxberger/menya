package menya.gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import menya.core.document.IDocument;
import menya.core.document.IPage;
import menya.core.model.Document;

/**
 * Panel containing multiple pages.
 * 
 * @author Max
 * @version $Revision$
 */
public class PagePanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final IDocument currentDocument;

    private final List<SketchPane> pagePanes = new ArrayList<SketchPane>();

    /**
     * Default constructor.
     */
    public PagePanel() {
        super();
        this.currentDocument = new Document();
        this.reload();
    }

    private void reload() {
        this.pagePanes.clear();
        this.removeAll();
        int y = 0;
        for (final IPage page : this.currentDocument.getPages()) {
            final SketchPane p = new SketchPane(page);
            this.pagePanes.add(p);
            this.add(p);
            p.setLocation(0, y);
            y += p.getHeight();
        }
        this.validate();

    }

    /** {@inheritDoc} */
    @Override
    public Dimension getPreferredSize() {
        int width = 0;
        int height = 0;
        for (final SketchPane p : this.pagePanes) {
            final Dimension d = p.getPreferredSize();
            width = Math.max(width, d.width);
            height += d.height;
        }
        return new Dimension(width, height);
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getMinimumSize() {
        return this.getPreferredSize();
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getMaximumSize() {
        return this.getPreferredSize();
    }

}
