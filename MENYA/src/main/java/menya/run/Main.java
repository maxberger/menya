/*
 * Copyright 2009 - 2009 by Menya Project
 * 
 * This file is part of Menya.
 * 
 * Menya is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Menya is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser Public License along with
 * Menya. If not, see <http://www.gnu.org/licenses/>.
 */
package menya.run;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * A very nice new Main with log4j working.
 * 
 * @author Dominik
 * @version $Revision$
 */
public class Main {
    private static Logger logger = Logger.getLogger(Main.class);
    static {
        DOMConfigurator.configure("log4j-configuration.xml");
    }

    /**
     * is the main entry point of the application.
     * 
     * @param argv
     *            arguments to pass
     */
    public static void main(final String[] argv) {
        Main.logger.debug("working?");
    }
}
