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
package sturesy.core.plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * ClassLoader that loads plugins from jar files
 * 
 * @author w.posdorfer
 * 
 */
public class PluginClassLoader extends URLClassLoader
{
    /**
     * 
     * Creates a new PluginClassLoader
     * 
     * @param folderToScan
     *            the folder containing all the plugin data
     * @throws MalformedURLException
     *             if folderToScan.toURI().toURL() doesn't work
     */
    public PluginClassLoader(File folderToScan) throws MalformedURLException
    {
        super(new URL[] { folderToScan.toURI().toURL() });
        addJarsToClasspath(folderToScan);
    }

    private void addJarsToClasspath(File pluginDir)
    {
        File[] jars = pluginDir.listFiles(new JarFilter());

        if (jars == null)
        {
            return;
        }

        for (File jar : jars)
        {
            try
            {
                addURL(jar.toURI().toURL());
            }
            catch (MalformedURLException e)
            {
            }
        }
    }

    private static final class JarFilter implements FilenameFilter
    {
        public boolean accept(File dir, String name)
        {
            name = name.toLowerCase();
            if (name.endsWith(".jar"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

}
