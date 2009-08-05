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
package menya.core.model;

import java.awt.Graphics2D;

/**
 * represents all possible graphical data that one may could draw / render in
 * (YET) not specified way.
 * 
 * @author Dominik
 * @version $Revision$
 */
public interface GraphicalData {

    /**
     * Draw the represented object.
     * 
     * @param g2d
     *            Graphics context.
     */
    void draw(Graphics2D g2d);
}
