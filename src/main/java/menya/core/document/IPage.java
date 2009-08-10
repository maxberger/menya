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

/* $Id$ */

package menya.core.document;

import java.awt.geom.Dimension2D;
import java.util.Deque;
import java.util.List;

import menya.core.model.IWorkingTool;

/**
 * This interfaces represents a page in a MENYA Document.
 * 
 * @author Dominik
 * @author Max
 * @version $Revision$
 */
public interface IPage {

	/**
	 * @return the size of the page in Point.
	 */
	public Dimension2D getPageSize();

	/**
	 * retrieves the background layer of this page. Basically would it be the
	 * lowest (=first) layer. Thus it is the same as if we would call
	 * getLayers().peekFirst().
	 * 
	 * @return the background of this document or null if no layers are present.
	 */
	public ILayer getBackground();

	/**
	 * retrieves all layers of this document.
	 * 
	 * @return the document layers as stack
	 */
	public Deque<ILayer> getLayers();

	/**
	 * Retrieves the currently active layer. It is not guaranteed that the layer
	 * returned by this method is actually a editable layer.
	 * 
	 * @return the active layer.
	 */
	public ILayer getActiveLayer();

	/**
	 * sets the currently active layer. If the layer passed as argument is not a
	 * valid layer for this page, a {@link IllegalArgumentException} will be
	 * thrown.
	 * 
	 * @param layer
	 * @throws IllegalArgumentException
	 */
	public void setActiveLayer(ILayer layer) throws IllegalArgumentException;

	/**
	 * retrieves the default pen for the currently active layer. This represents
	 * a convenience method that is deprecated. Please note that this method may
	 * be removed in future versions. Access the pens through the layers!
	 * 
	 * @return
	 */
	@Deprecated
	public IWorkingTool getDefaultPen();

	/**
	 * retrieves the currently active pen for the currently active layer. This
	 * represents a convenience method that is deprecated. Please note that this
	 * method may be removed in future versions. Access the pens through the
	 * layers!
	 * 
	 * @return
	 */
	@Deprecated
	public IWorkingTool getActivePen();

	/**
	 * sets the currently active pen for the currently active layer. This
	 * represents a convenience method that is deprecated. Please note that this
	 * method may be removed in future versions. Access the pens through the
	 * layers!
	 * 
	 * @param pen
	 * @throws IllegalArgumentException
	 */
	@Deprecated
	public void setActivePen(IWorkingTool pen) throws IllegalArgumentException;

	/**
	 * retrieves all pens of each layer on this page. This represents a
	 * convenience method that is deprecated. Please note that this method may
	 * be removed in future versions. Access the pens through the layers!
	 * 
	 * @return
	 */
	@Deprecated
	public List<IWorkingTool> getAllPens();
}
