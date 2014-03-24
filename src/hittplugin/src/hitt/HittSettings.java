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

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import sturesy.core.plugin.ISettingsScreen;
import sturesy.core.plugin.proxy.PSettings;

public class HittSettings implements ISettingsScreen
{
    public static final String SETTINGS_DEVICE = "plugin.hitt.device";
    public static final String SETTINGS_BAUD = "plugin.hitt.baudrate";
    public static final String SETTINGS_ENABLED = "plugin.hitt.enabled";

    HittSettingsComponent _panel;
    JTabbedPane _component;

    public HittSettings()
    {
        _component = new JTabbedPane();
        _panel = new HittSettingsComponent();
        _component.add(getName(), _panel);
    }

    @Override
    public String getName()
    {
        return "H-iTT Clicker";
    }

    @Override
    public ImageIcon getIcon()
    {
        ImageIcon ico = new ImageIcon(MainEntry.PATHOFPLUGIN + "/hitt-logo.png");
        return new ImageIcon(ico.getImage().getScaledInstance(64, 64, Image.SCALE_FAST));
    }

    @Override
    public Component getPanel()
    {
        return _component;
    }

    @Override
    public void saveSettings() throws Throwable
    {
        _panel.stopPolling();

        if (_panel.getPluginEnabled())
        {
            if (_panel.getBaudRateText().isEmpty() || _panel.getBaudRate() < 1000)
            {
                throw new Exception(Localizer.getString("error.baudrate"));
            }
            if (_panel.getDeviceName().length() == 0)
            {
                throw new Exception(Localizer.getString("error.devicename"));
            }
        }

        PSettings.setProperty(SETTINGS_BAUD, _panel.getBaudRate());
        PSettings.setProperty(SETTINGS_DEVICE, _panel.getDeviceName());
        PSettings.setProperty(SETTINGS_ENABLED, _panel.getPluginEnabled());
    }

    /**
     * Is this Plugin currently enabled
     * 
     * @return true if enabled
     */
    public boolean isPluginEnabled()
    {
        return _panel.getPluginEnabled();
    }

}
