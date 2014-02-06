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
package sturesy.core.plugin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import sturesy.core.Log;
import sturesy.core.error.ErrorController;

/**
 * The {@link PluginLoader} loads all plugins from inside the Plugindirectory
 * 
 * @author w.posdorfer
 * 
 */
public class PluginLoader
{
    private ArrayList<IPlugin> _plugins = new ArrayList<IPlugin>();
    private File _internalPluginsDirectory;
    private final File _externalPluginsDirectory;

    private ErrorController _pluginerror;

    public PluginLoader(File externalPluginsDirectory, File internalPluginsDirectory)
    {
        _pluginerror = new ErrorController();
        _externalPluginsDirectory = externalPluginsDirectory;
        _internalPluginsDirectory = internalPluginsDirectory;
    }

    public void loadPlugins()
    {
        // Load Internal First
        loadPlugins(_internalPluginsDirectory);

        // Do not load plugins again, if internal and maindir are referencing
        // same directory
        if (!isCanonicalPathEqual(_externalPluginsDirectory, _internalPluginsDirectory))
        {
            loadPlugins(_externalPluginsDirectory);
        }

        // Call onLoad everywhere
        callOnLoadInPlugins();

        if (_pluginerror.errorsOccured())
        {
            _pluginerror.show();
        }
    }

    private boolean isCanonicalPathEqual(File f1, File f2)
    {
        try
        {
            return f1.getCanonicalPath().equals(f2.getCanonicalPath());
        }
        catch (IOException e)
        {
            return false;
        }
    }

    private void loadPlugins(File folder)
    {
        if (folder.exists() && folder.isDirectory() && folder.canRead())
            for (File pluginfolder : folder.listFiles())
            {
                if (pluginfolder.isDirectory())
                {
                    try
                    {
                        loadPluginFromFolder(pluginfolder);
                    }
                    catch (XmlPullParserException e)
                    {
                        Log.error("XML Plugin Read error", e);
                    }
                    catch (IOException e)
                    {
                        Log.error("IOEXCEPTION", e);
                    }
                }
            }
    }

    private void loadPluginFromFolder(File folder) throws XmlPullParserException, IOException
    {

        boolean containsPluginXML = false;

        File pluginXML = null;

        for (File f : folder.listFiles())
        {
            if (f.getName().equals("plugin.xml"))
            {
                containsPluginXML = true;
                pluginXML = f;
                break;
            }
        }

        if (!containsPluginXML)
            return; // jump out if theres no plugin.xml

        XmlPullParser parser = new MXParser();
        FileReader reader = new FileReader(pluginXML);
        parser.setInput(reader);

        int eventType = parser.getEventType();

        String entrypointclass = "";
        boolean isJar = false;
        do
        {
            if (eventType == XmlPullParser.START_TAG)
            {
                // process start
                String currentTag = parser.getName();
                if (currentTag.equals("entry"))
                {
                    // Saving the Entry-Point class in here
                    entrypointclass = parser.nextText();
                    eventType = XmlPullParser.END_DOCUMENT; // lets stop looping
                }
                else if (currentTag.equals("jar"))
                {
                    isJar = true;
                    entrypointclass = parser.nextText();
                    eventType = XmlPullParser.END_DOCUMENT; // lets stop looping
                }
            }
            eventType = parser.next();
        }
        while (eventType != XmlPullParser.END_DOCUMENT);

        try
        {

            ClassLoader loader = null;
            if (isJar)
            {
                loader = new PluginClassLoader(folder);
            }
            else
            {
                loader = new FileClassLoader(folder.getAbsolutePath());
            }

            IPlugin plug = (IPlugin) loader.loadClass(entrypointclass).newInstance();

            if (!_plugins.contains(plug))
            {
                _plugins.add(plug);
            }

        }
        catch (ClassNotFoundException e)
        {
            Log.error("Plugin class in plugin.xml not found while trying to load", e);
            putErrorInController(entrypointclass, e);
        }
        catch (InstantiationException e)
        {
            Log.error("MainEntry-Class is probably not implementing IPlugin", e);
            putErrorInController(entrypointclass, e);
        }
        catch (IllegalAccessException e)
        {
            Log.error("Can't access the MainEntry-Class", e);
            putErrorInController(entrypointclass, e);
        }
        catch (Throwable t)
        {
            Log.error("An unexpected error occured", t);
            putErrorInController(entrypointclass, t);
        }

    }

    private void callOnLoadInPlugins()
    {
        ArrayList<IPlugin> removelater = new ArrayList<IPlugin>();

        for (IPlugin plug : _plugins)
        {
            String name = plug.getClass().getName();
            try
            {
                plug.onLoad();
            }
            catch (Throwable t)
            {
                Log.error("Error loading plugin " + name, t);
                removelater.add(plug);
                putErrorInController(name, t);
            }
        }

        _plugins.removeAll(removelater);

    }

    private void putErrorInController(String name, Throwable t)
    {
        _pluginerror.insertError(name, t);
    }

    /**
     * Returns the original List containing all loaded Plugins
     */
    public List<IPlugin> getListOfLoadedPlugins()
    {
        return _plugins;
    }
}
