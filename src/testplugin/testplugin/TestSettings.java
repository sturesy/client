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
package testplugin;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import sturesy.core.backend.Loader;
import sturesy.core.plugin.ISettingsScreen;
import sturesy.core.plugin.proxy.PLoader;

public class TestSettings implements ISettingsScreen
{
    public void saveSettings()
    {
    }

    public Component getPanel()
    {

        JLabel label = new JLabel(
                "<html><center><b>Example</b></center><br><br>This is an example SettingsScreen for the Testplugin</html>");

        label.setHorizontalAlignment(JLabel.CENTER);

        return label;
    }

    public String getName()
    {
        return "Testplugin";
    }

    public ImageIcon getIcon()
    {
        return PLoader.getImageIconResized(Loader.IMAGE_YELLOW_BAR, 64, 64, 2);
    }
}
