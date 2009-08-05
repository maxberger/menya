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

package menya.gui;

import java.io.File;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * This class represents the graphical user interface to MENYA. The main class
 * should startup here.
 * 
 * @author Dominik
 * @author Max
 * @version $Revision$
 */
public final class GUI {

    /**
     * Set to true if we're running under Mac OS X.
     */
    public static final boolean OSX = GUI.isOSX();

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(GUI.class.toString());

    /**
     * 
     */
    private static final String BASEDIR = "basedir";

    /**
     * 
     */
    private static final String JAVA_LIBRARY_PATH = "java.library.path";

    /**
     * is the empty private constructor.
     */
    private GUI() {
        // Empty on purpose.
    }

    /**
     * retrieves if we are running on OSX.
     * 
     * @return true if OSX, false otherwise
     */
    private static boolean isOSX() {
        //$NON-NLS-1$
        return System.getProperty("mrj.version") != null;
    }

    /**
     * is the main entry point of the application.
     * 
     * @param args
     *            arguments to pass
     */
    public static void main(final String[] args) {
        // TODO parse arguments
        // TODO create menya core class (respecting switches/flags passed)
        // TODO load file if wanted by argument

        GUI.setLibraryPath();

        if (GUI.OSX) {
            //$NON-NLS-1$ //$NON-NLS-2$
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (final ClassNotFoundException e) {
            GUI.LOGGER.log(Level.FINE, e.getMessage(), e);
        } catch (final InstantiationException e) {
            GUI.LOGGER.log(Level.FINE, e.getMessage(), e);
        } catch (final IllegalAccessException e) {
            GUI.LOGGER.log(Level.FINE, e.getMessage(), e);
        } catch (final UnsupportedLookAndFeelException e) {
            GUI.LOGGER.log(Level.FINE, e.getMessage(), e);
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final MainFrame mainFrame = new MainFrame();
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setVisible(true);
            }
        });
    }

    /**
     * Sets java.library.path if basedir is set.
     * <p>
     * This methods employs a terrible hack: java.library.path is only checked
     * when the internal sys_paths variable is null, e.g. once at startup. In
     * this case, it is reset to null to force re-evaluation of the system
     * variable.
     */
    private static void setLibraryPath() {
        final String oldPath = System.getProperty(GUI.JAVA_LIBRARY_PATH);
        final String basedir = System.getProperty(GUI.BASEDIR);
        if (basedir != null) {
            final StringBuilder newPath = new StringBuilder(basedir).append(
                    File.separatorChar).append("jni");
            if (oldPath != null) {
                newPath.append(File.pathSeparatorChar).append(oldPath);
            }
            try {
                final Class<?> clazz = ClassLoader.class;
                final Field field = clazz.getDeclaredField("sys_paths");
                final boolean accessible = field.isAccessible();
                if (!accessible) {
                    field.setAccessible(true);
                }
                field.set(clazz, null);
                field.setAccessible(accessible);
                // CHECKSTYLE:OFF In this case a catch-all is needed due to
                // unpredictable reflection errors!
            } catch (final Throwable e) {
                // CHECKSTYLE:ON
                GUI.LOGGER
                        .log(
                                Level.INFO,
                                "Failed to employ library hack, pen functionality may be inacessible.",
                                e);
            }

            System.setProperty(GUI.JAVA_LIBRARY_PATH, newPath.toString());
        }
    }
}
