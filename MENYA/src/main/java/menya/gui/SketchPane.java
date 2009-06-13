package menya.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import jpen.PButton;
import jpen.PButtonEvent;
import jpen.PKind;
import jpen.PLevelEvent;
import jpen.Pen;
import jpen.PenManager;
import jpen.PLevel.Type;
import jpen.event.PenAdapter;
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
public class SketchPane extends JComponent {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final int DEFAULT_SIZE_Y = 600;

    private static final int DEFAULT_SIZE_X = 800;

    private static final int MIN_SIZE_Y = 50;

    private static final int MIN_SIZE_X = 50;

    private static final float MAX_PEN_WIDTH = 5.0f;

    private static final float DEFAULT_PRESSURE = 0.5f;

    private final IDocument currentDocument;

    private final CurveLayer activeLayer;

    private Curve currentCurve;

    /**
     * Default constructor.
     */
    public SketchPane() {
        this.currentDocument = new Document();
        this.activeLayer = this.currentDocument.getPages().get(0)
                .getActiveLayer();
        // this.addMouseListener(this);
        // this.addMouseMotionListener(this);

        final PenManager penManager = new PenManager(this);
        penManager.pen.addListener(new PenAdapter() {
            @Override
            public void penLevelEvent(final PLevelEvent ev) {
                SketchPane.this.penLevelEvent(ev);
            }

            @Override
            public void penButtonEvent(final PButtonEvent ev) {
                SketchPane.this.penButtonEvent(ev);
            }
        });

    }

    /** {@inheritDoc} */
    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        for (final ILayer l : this.currentDocument.getPages().get(0)
                .getLayers()) {
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
        return new Dimension(SketchPane.MIN_SIZE_X, SketchPane.MIN_SIZE_Y);
    }

    /** {@inheritDoc} */
    @Override
    public Dimension getPreferredSize() {
        // TODO: Find sensible values.
        return new Dimension(SketchPane.DEFAULT_SIZE_X,
                SketchPane.DEFAULT_SIZE_Y);
    }

    /** {@inheritDoc} */
    public void penButtonEvent(final PButtonEvent ev) {
        final PButton button = ev.button;
        if (PButton.Type.LEFT.equals(button.getType())) {
            final Point p = this.toPoint(ev.pen);

            if (button.value) {
                this.startCurve(p);
            } else {
                this.stopCurve(p);
            }
        }
    }

    private void stopCurve(final Point p) {
        if (this.currentCurve == null) {
            return;
        }
        this.currentCurve.add(p);
        // TODO: Smooth Path
        this.activeLayer.addCurve(this.currentCurve);
        this.currentCurve = null;
        // TODO: Maybe erase path that was drawn during drag.
        this.repaint();
    }

    private void startCurve(final Point p) {
        this.currentCurve = new Curve();
        this.currentCurve.add(p);
    }

    private Point toPoint(final Pen pen) {
        final float posx = pen.getLevelValue(Type.X);
        final float posy = pen.getLevelValue(Type.Y);
        final float pressure;
        if (PKind.Type.STYLUS.equals(pen.getKind().getType())) {
            pressure = pen.getLevelValue(Type.PRESSURE);
        } else {
            pressure = SketchPane.DEFAULT_PRESSURE;
        }
        // TODO: Improve!
        return new Point(posx, posy, pressure * SketchPane.MAX_PEN_WIDTH);
    }

    /** {@inheritDoc} */
    public void penLevelEvent(final PLevelEvent ev) {
        if (this.currentCurve == null) {
            return;
        }
        final Point newP = this.toPoint(ev.pen);
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
