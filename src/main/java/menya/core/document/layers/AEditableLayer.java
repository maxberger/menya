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
package menya.core.document.layers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import menya.core.document.IEditableLayer;
import menya.core.document.IPage;
import menya.core.model.Curve;
import menya.core.model.GraphicalData;
import menya.core.model.tools.StandardPen;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

/**
 * @author dominik
 * 
 */
public abstract class AEditableLayer extends ALayer implements IEditableLayer,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3212596772643865807L;

	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = Logger.getLogger(AEditableLayer.class
			.toString());

	private List<Curve> curves;

	private transient boolean hasChanged;

	public AEditableLayer() {
		// this is a editable layer, hence add a pen
		addPen(new StandardPen());
	}

	private List<Curve> getCurves() {
		if (curves == null) {
			curves = new ArrayList<Curve>();
		}
		return curves;
	}

	/** {@inheritDoc} */
	@Override
	public final IEditableLayer castEditableLayer() {
		return this;
	}

	/** {@inheritDoc} */
	public void addCurve(final Curve curve) {
		LOGGER.info("adding curve");
		this.hasChanged = true;
		getCurves().add(curve);
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	public Iterable<GraphicalData> getGraphicalData() {
		final Iterable<Curve> i = getCurves();
		final Iterable d = i;
		this.hasChanged = false;
		if (d == null) {
			LOGGER.warning("getGraphicalData() will return null");
		}
		return d;
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasChanged() {
		return this.hasChanged;
	}

	/** {@inheritDoc} */
	@Override
	public void toPdf(final PDPageContentStream contentStream, final IPage page)
			throws IOException {
		for (final Curve curve : getCurves()) {
			curve.toPdf(contentStream, page);
		}
	}
}
