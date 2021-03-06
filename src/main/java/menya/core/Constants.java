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

package menya.core;

/**
 * This class defines constants to be used throughout MENYA.
 * 
 * @author Max
 * @version $Revision$
 */
public final class Constants {

    /**
     * Convert inch to cm.
     */
    public static final double INCH_TO_CM = 2.54;

    /**
     * Convert cm to inch.
     */
    public static final double CM_TO_INCH = 1 / Constants.INCH_TO_CM;

    /**
     * Convert inch to pt.
     */
    public static final double INCH_TO_PT = 72;

    /**
     * Convert cm to pt.
     */
    public static final double CM_TO_PT = Constants.INCH_TO_PT
            / Constants.INCH_TO_CM;

    private Constants() {
        // Empty on purpose.
    }

}
