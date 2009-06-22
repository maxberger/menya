package menya.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import menya.core.document.IDocument;
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

    private final List<SketchPane> pagePanes;

    /**
     * Default constructor.
     */
    public PagePanel() {
        super();
        this.setLayout(new GridBagLayout());
        this.currentDocument = new Document();
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        this.pagePanes = new ArrayList<SketchPane>(this.currentDocument
                .getPages().size());

        final SketchPane p = new SketchPane(this.currentDocument.getPages()
                .get(0));
        this.pagePanes.add(p);
        this.add(p, gbc);
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
