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

/* $Id$ */

package menya.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class represents the graphical user interface to MENYA. The main class
 * should startup here.
 * 
 * @author Dominik
 * @author Max
 */
public class GUI {

    /**
     * Set to true if we're running under Mac OS X.
     */
    public static final boolean OSX = System.getProperty("mrj.version") != null; //$NON-NLS-1$

    /**
     * Logger for this class
     */
    private static final Log LOGGER = LogFactory.getLog(GUI.class);

    /**
     * is the main entry point of the application.
     * 
     * @param args
     *            arguments to pass
     */
    public static void main(String[] args) {
        // TODO parse arguments
        // TODO create menya core class (respecting switches/flags passed)
        // TODO load file if wanted by argument

        if (GUI.OSX) {
            System.setProperty("apple.laf.useScreenMenuBar", "true"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (final ClassNotFoundException e) {
            GUI.LOGGER.debug(e);
        } catch (final InstantiationException e) {
            GUI.LOGGER.debug(e);
        } catch (final IllegalAccessException e) {
            GUI.LOGGER.debug(e);
        } catch (final UnsupportedLookAndFeelException e) {
            GUI.LOGGER.debug(e);
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final MainFrame mainFrame = new MainFrame();
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setVisible(true);
            }
        });
    }
}
