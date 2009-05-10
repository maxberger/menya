package menya.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import menya.core.document.IDocument;
import menya.core.document.ILayer;
import menya.core.document.layers.CurveLayer;
import menya.core.model.Curve;
import menya.core.model.Document;
import menya.core.model.GraphicalData;
import menya.core.model.Point;

/**
 * The pane where one can draw on.
 * 
 * @author Max
 * @version $Revision$
 */
public class SketchPane extends JComponent implements MouseListener,
        MouseMotionListener {

    private final IDocument currentDocument;

    private final CurveLayer activeLayer;

    private Curve currentCurve;

    /**
     * Default constructor.
     */
    public SketchPane() {
        this.currentDocument = new Document();
        this.activeLayer = this.currentDocument.getActiveLayer();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    /** {@inheritDoc} */
    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        for (final ILayer l : this.currentDocument.getLayers()) {
            for (final GraphicalData gd : l.getGraphicalData()) {
                gd.draw(g2d);
            }
        }
        if (this.currentCurve != null) {
            this.currentCurve.draw(g2d);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getMinimumSize() {
        // TODO: Find sensible values.
        return new Dimension(50, 50);
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getPreferredSize() {
        // TODO: Find sensible values.
        return new Dimension(800, 600);
    }

    /** {@inheritDoc} */
    public void mouseClicked(final MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    /** {@inheritDoc} */
    public void mouseEntered(final MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    /** {@inheritDoc} */
    public void mouseExited(final MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    /** {@inheritDoc} */
    public void mousePressed(final MouseEvent arg0) {
        final Point p = this.toPoint(arg0);
        this.currentCurve = new Curve();
        this.currentCurve.add(p);
    }

    private Point toPoint(final MouseEvent arg0) {
        // TODO: Improve!
        return new Point(arg0.getX(), arg0.getY(), 3.0);
    }

    /** {@inheritDoc} */
    public void mouseReleased(final MouseEvent arg0) {
        if (this.currentCurve == null) {
            return;
        }
        this.currentCurve.add(this.toPoint(arg0));
        // TODO: Smooth Path
        this.activeLayer.addCurve(this.currentCurve);
        this.currentCurve = null;
        // TODO: Maybe erase path that was drawn during drag.
        this.repaint();
    }

    /** {@inheritDoc} */
    public void mouseDragged(final MouseEvent arg0) {
        if (this.currentCurve == null) {
            return;
        }
        final Point newP = this.toPoint(arg0);
        // final Point old = this.currentPath.get(this.currentPath.size() - 1);
        // if (newP.distanceTo(old) > 10.0) {
        this.currentCurve.add(newP);
        // }
        this.repaint();
    }

    /** {@inheritDoc} */
    public void mouseMoved(final MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

}
