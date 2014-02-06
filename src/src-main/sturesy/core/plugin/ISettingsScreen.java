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

import java.awt.Component;

import javax.swing.ImageIcon;

/**
 * Interface for all the Settings to display
 * 
 * @author w.posdorfer
 * 
 */
public interface ISettingsScreen
{

    /**
     * Returns the Name of these settings
     * 
     * @return short name of these settings
     */
    public String getName();

    /**
     * Returns the Icon for these settings
     * 
     * @return {@link ImageIcon} 64x64 pixel
     */
    public ImageIcon getIcon();

    /**
     * Returns the Panel for these settings
     */
    public Component getPanel();

    /**
     * This Method will be called when the user presses the "Save" Button<br>
     * make sure to save all your settings inside this method
     * 
     * @throws Throwable
     *             should throw an Exception or Error, when a setting cannot be
     *             stored, or a field is not filled in with valid informations,
     *             etc...
     */
    public void saveSettings() throws Throwable;

}
