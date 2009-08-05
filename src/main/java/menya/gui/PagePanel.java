/*
 * This file is part of Menya.
 * 
 * Menya is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Menya is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Menya. If not, see <http://www.gnu.org/licenses/>.
 */
package menya.gui;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
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

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(PagePanel.class
            .toString());

    private IDocument currentDocument;

    private final List<SketchPane> pagePanes = new ArrayList<SketchPane>();

    /**
     * Default constructor.
     */
    public PagePanel() {
        super();
        this.currentDocument = Document.emptyDocument();
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

    /**
     * Loads a given file into this panel.
     * 
     * @param filename
     *            the File to load.
     */
    public void load(final String filename) {
        if (this.currentDocument != null) {
            this.currentDocument.close();
        }
        try {
            this.currentDocument = Document.fromPDF(filename);
        } catch (final IOException e) {
            this.currentDocument = Document.emptyDocument();
        }
        this.reload();
    }

    /**
     * Saves a file from this panel.
     *
     * @param filename
     *            the File to save as.
     */
    public void save(final String filename) {
        if (this.currentDocument != null) {
            try {
                this.currentDocument.toPDF(filename);
            } catch (final IOException e) {
                LOGGER.log(Level.WARNING, "Error saving file", e);
            }
        }
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
