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
package hitt;

import java.io.File;

import sturesy.SturesyManager;
import sturesy.core.plugin.IPlugin;
import sturesy.core.plugin.IPollPlugin;
import sturesy.core.plugin.ISettingsScreen;
import sturesy.core.plugin.proxy.PSettings;
import sturesy.core.ui.MessageWindow;

/**
 * Mainentrypoint for HiTT-Plugin
 * 
 * @author w.posdorfer
 */
public class MainEntry implements IPlugin
{

    public static String PATHOFPLUGIN;

    @Override
    public void onLoad()
    {

        PATHOFPLUGIN = findPathOfPlugin(SturesyManager.getPluginsDirectory());
        if (PATHOFPLUGIN == null)
        {
            PATHOFPLUGIN = findPathOfPlugin(SturesyManager.getInternalPluginsDirectory());
        }

        if (PATHOFPLUGIN != null)
        {
            System.load(PATHOFPLUGIN + "/" + getLibraryForOS());
        }
        else
        {
            MessageWindow.showMessageWindowError(Localizer.getString("message.damaged"), 3500);
        }
    }

    private String findPathOfPlugin(File folder)
    {
        for (File folders : folder.listFiles())
        {
            if (folders.isDirectory())
            {
                for (File file : folders.listFiles())
                {
                    if (file.getName().equals("hitt") || file.getName().equals("hitt-logo.png"))
                    {
                        return folders.getAbsolutePath();
                    }
                }
            }
        }
        return null;
    }

    private String getLibraryForOS()
    {
        String result = "";

        String osname = System.getProperty("os.name").toLowerCase();
        int arch = Integer.parseInt(System.getProperty("sun.arch.data.model"));

        if (osname.contains("mac os x"))
        {
            result = "librxtxSerial.jnilib";
        }
        else if (osname.contains("windows"))
        {
            if (arch == 32)
            {
                result = "rxtxSerial.dll";
            }
            else if (arch == 64)
            {
                result = "rxtxSerial_x64.dll";
            }
        }
        else
        {
            // TODO generate Linux files
            // result = "rxtxSerial_linux.so, lib, whatever
        }

        return result;
    }

    @Override
    public IPollPlugin getPollPlugin()
    {
        boolean enabled = PSettings.getBoolean(HittSettings.SETTINGS_ENABLED);
        if (enabled)
        {
            return new HittPolling();
        }
        else
        {
            return null;
        }
    }

    @Override
    public ISettingsScreen getSettingsScreen()
    {
        return new HittSettings();
    }

    @Override
    public boolean equals(Object obj)
    {
        return (obj instanceof MainEntry);
    }

}
