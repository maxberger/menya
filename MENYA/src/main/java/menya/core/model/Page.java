package menya.core.model;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.util.ArrayDeque;
import java.util.Deque;

import menya.core.Constants;
import menya.core.document.ILayer;
import menya.core.document.IPage;
import menya.core.document.layers.CurveLayer;
import menya.core.document.layers.ImageLayer;

/**
 * Default Implementation of a Document.
 * 
 * @author Max
 * @version $Revision$
 */
public class Page implements IPage {

    private final ArrayDeque<ILayer> layers;

    // 210 mm x 297 mm is A4
    private final Dimension2D size = new Dimension(
            (int) (21.0 * Constants.CM_TO_PT),
            (int) (29.7 * Constants.CM_TO_PT));

    /**
     * Default Constructor.
     */
    public Page() {
        this.layers = new ArrayDeque<ILayer>();
        this.layers.add(new ImageLayer());
    }

    /** {@inheritDoc} */
    public ILayer getBackground() {
        return this.layers.peekFirst();
    }

    /** {@inheritDoc} */
    public Deque<ILayer> getLayers() {
        return this.layers.clone();
    }

    /** {@inheritDoc} */
    public CurveLayer getActiveLayer() {
        final ILayer top = this.layers.peekLast();
        if (top instanceof CurveLayer) {
            return (CurveLayer) top;
        } else {
            final CurveLayer c = new CurveLayer();
            this.layers.add(c);
            return c;
        }
    }

    /** {@inheritDoc} */
    public Dimension2D getPageSize() {
        return this.size;
    }
}
