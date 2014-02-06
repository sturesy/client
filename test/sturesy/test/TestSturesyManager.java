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
package sturesy.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.junit.Test;

import sturesy.SturesyManager;
import sturesy.core.plugin.IPlugin;
import sturesy.core.plugin.ISettingsScreen;
import sturesy.util.Settings;

public class TestSturesyManager
{
    private String _expectedPath = File.separator + "test";
    private String _path = "/test";

    @Test
    public void testGetSettings()
    {
        Settings settings = SturesyManager.getSettings();
        assertNotNull(settings);
        Settings settings2 = SturesyManager.getSettings();
        assertNotNull(settings2);
        assertSame(settings, settings2);
    }

    @Test
    public void testMaindirectory()
    {
        SturesyManager.setMainDirectory(_path);
        File mainDirectory = SturesyManager.getMainDirectory();
        assertEquals(_expectedPath, mainDirectory.getPath());
    }

    @Test
    public void testGetPluginsDirectory()
    {
        SturesyManager.setMainDirectory(_path);
        File pluginsDirectory = SturesyManager.getPluginsDirectory();
        File pluginsDirectory2 = SturesyManager.getPluginsDirectory();
        assertEquals(pluginsDirectory, pluginsDirectory2);
        assertEquals(_expectedPath + File.separator + "plugins", pluginsDirectory.getPath());
    }

    @Test
    public void testGetLecturesDirectory()
    {
        SturesyManager.setMainDirectory(_path);
        File lecturesDirectory = SturesyManager.getLecturesDirectory();
        File lecturesDirectory2 = SturesyManager.getLecturesDirectory();
        assertEquals(lecturesDirectory, lecturesDirectory2);
        assertEquals(_expectedPath + File.separator + "lectures", lecturesDirectory.getPath());
    }

    @Test
    public void testGetSettingsScreens()
    {
        Collection<IPlugin> plugins = new ArrayList<IPlugin>();
        IPlugin firstPlugin = mock(IPlugin.class);
        IPlugin secondPlugin = mock(IPlugin.class);
        ISettingsScreen settingsScreen = mock(ISettingsScreen.class);
        plugins.add(firstPlugin);
        plugins.add(secondPlugin);
        when(firstPlugin.getSettingsScreen()).thenReturn(null);
        when(secondPlugin.getSettingsScreen()).thenReturn(settingsScreen);
        SturesyManager.setLoadedPlugins(plugins);
        Set<ISettingsScreen> settingsScreens = SturesyManager.getSettingsScreens();
        assertTrue(1 == settingsScreens.size());
        assertSame(settingsScreen, settingsScreens.iterator().next());
    }
}
