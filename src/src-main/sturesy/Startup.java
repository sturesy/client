/*
 * StuReSy - Student Response System
 * Copyright (C) 2012-2013  StuReSy-Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sturesy;

import java.io.File;

import javax.swing.JFileChooser;

import sturesy.core.pathwindow.PathWindowController;
import sturesy.core.plugin.PluginLoader;
import sturesy.util.Settings;

/**
 * Main Entrypoint
 * 
 * @author w.posdorfer
 * 
 */
public class Startup
{

    public static void main(String[] args)
    {
        Macintosh.executeMacOnlyCode();
        Windows.setLookAndFeelWindows();

        cleanupUpdateLeftovers();

        loadSettings();

        loadPlugins();

        // Start Mainscreen
        MainScreen screen = new MainScreen();
        screen.getJFrame().pack();
        screen.getJFrame().setLocationRelativeTo(null);
        screen.getJFrame().setVisible(true);
    }

    private static void cleanupUpdateLeftovers()
    {
        File updateFolder = new File("update");
        if (updateFolder.exists())
        {
            for (File f : updateFolder.listFiles())
            {
                f.delete();
            }
            updateFolder.delete();
        }
    }

    private static void loadSettings()
    {
        // Load Settings
        Settings settings = SturesyManager.getSettings();
        String maindir = settings.getString(Settings.MAINDIRECTORY);

        if (maindir.length() == 0)
        {
            maindir = getMainDir().getAbsolutePath();
            settings.setProperty(Settings.MAINDIRECTORY, maindir);
        }
        SturesyManager.setMainDirectory(maindir);
    }

    private static void loadPlugins()
    {
        // Loads the plugins
        PluginLoader plugload = new PluginLoader(SturesyManager.getPluginsDirectory(),
                SturesyManager.getInternalPluginsDirectory());
        plugload.loadPlugins();
        SturesyManager.setLoadedPlugins(plugload.getListOfLoadedPlugins());
    }

    /**
     * Displays a Dialog asking to provide the Main-Directory in the following
     * {@link JFileChooser}
     */
    private static File getMainDir()
    {
        PathWindowController controller = new PathWindowController();

        return controller.getSelectedFolder();
    }

}
