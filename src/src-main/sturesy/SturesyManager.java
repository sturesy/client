/*
 * StuReSy - Student Response System
 * Copyright (C) 2012-2014  StuReSy-Team
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import sturesy.core.error.ErrorController;
import sturesy.core.error.XMLException;
import sturesy.core.plugin.IPlugin;
import sturesy.core.plugin.IPollPlugin;
import sturesy.core.plugin.ISettingsScreen;
import sturesy.export.LectureIDExport;
import sturesy.items.LectureID;
import sturesy.util.Defaults;
import sturesy.util.Settings;
import sturesy.util.web.WebVotingHandler;

public class SturesyManager
{

    private static final String PLUGINS_FOLDER = "/plugins";
    private static final String LECTURES_FOLDER = "/lectures";

    private static File MAINDIRECTORY;

    public static Defaults _defaults = Defaults.getInstance();
    private static Settings _settings;
    private static Set<IPlugin> _loadedPlugins = new HashSet<IPlugin>();

    private static Collection<LectureID> _lectureIDs = null;

    public static Settings getSettings()
    {
        if (_settings == null)
        {
            _settings = Settings.getInstance();
        }

        return _settings;
    }

    /**
     * Returns a Set of all the custom Setting-Screens (i.e. Plugins)
     */
    public static Set<ISettingsScreen> getSettingsScreens()
    {
        HashSet<ISettingsScreen> result = new HashSet<ISettingsScreen>();
        for (IPlugin p : _loadedPlugins)
        {
            if (p.getSettingsScreen() != null)
            {
                result.add(p.getSettingsScreen());
            }
        }
        return result;
    }

    public static void setLoadedPlugins(Collection<IPlugin> plugins)
    {
        _loadedPlugins.addAll(plugins);
    }

    /**
     * Returns an unmodifiable Set of all the loaded Plugins
     */
    public static Set<IPlugin> getLoadedPlugins()
    {
        return Collections.unmodifiableSet(_loadedPlugins);
    }

    /**
     * Returns a new Set of loaded {@link IPollPlugin}
     */
    public static Set<IPollPlugin> getLoadedPollPlugins()
    {
        LinkedHashSet<IPollPlugin> result = new LinkedHashSet<IPollPlugin>();

        if (getSettings().getBoolean(Settings.WEB_PLUGIN_ENABLED))
        {
            result.add(new WebVotingHandler());
        }

        for (IPlugin plug : getLoadedPlugins())
        {
            IPollPlugin pollPlugin = plug.getPollPlugin();
            if (pollPlugin != null)
            {
                result.add(pollPlugin);
            }
        }

        return result;
    }

    public static Collection<LectureID> getLectureIDs()
    {
        if (_lectureIDs == null)
        {
            try
            {
                _lectureIDs = LectureIDExport.unmarshallLectureIDs(LectureIDExport.FILENAME);
            }
            catch (XMLException e)
            {
                ErrorController con = new ErrorController();
                con.insertError("LectureID " + e.getClass().getSimpleName(), e);
                con.show();
                return new ArrayList<LectureID>();
            }
        }

        return _lectureIDs;
    }

    /**
     * Returns the Main StuReSy Directory
     * 
     * @return directory
     */
    public static File getMainDirectory()
    {
        return new File(MAINDIRECTORY + "");
    }

    /**
     * Returns the Directory where all Lectures are saved <br>
     * <code>/Users/XYZ/maindir/lectures</code>
     * 
     * @return directory
     */
    public static File getLecturesDirectory()
    {
        File lecturedir = new File(MAINDIRECTORY + LECTURES_FOLDER);
        if (!lecturedir.exists())
        {
            lecturedir.mkdirs();
        }

        return lecturedir;
    }

    /**
     * The Pluginsdirectory located inside the Maindirectory
     */
    public static File getPluginsDirectory()
    {
        File plugdir = new File(MAINDIRECTORY + PLUGINS_FOLDER);
        if (!plugdir.exists())
        {
            plugdir.mkdirs();
        }
        return plugdir;
    }

    /**
     * The Pluginsdirectory located next to the sturesy.jar
     */
    public static File getInternalPluginsDirectory()
    {
        File plugdir = new File(Folder.getBaseFolder(), PLUGINS_FOLDER);
        if (!plugdir.exists())
        {
            plugdir.mkdir();
        }
        return plugdir;
    }

    public synchronized static void setMainDirectory(String path)
    {
        MAINDIRECTORY = new File(path);
    }

}
