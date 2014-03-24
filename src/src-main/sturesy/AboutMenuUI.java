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

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;

import sturesy.core.backend.Loader;
import sturesy.core.ui.JGap;
import sturesy.settings.about.About;
import sturesy.util.Settings;

/**
 * Basic Aboutmenu
 * 
 * @author w.posdorfer
 * 
 */
public class AboutMenuUI extends JFrame
{

    private static final String SETTINGS_WINDOW_SIZE_PROPERTY = "settings.window.size";
    private static final long serialVersionUID = -7803978813785904826L;

    private AboutMenuUI()
    {
        this(Settings.getInstance().getDimension(SETTINGS_WINDOW_SIZE_PROPERTY));
    }

    private AboutMenuUI(Dimension d)
    {
        About about = new About(d);
        setTitle("About");
        setLayout(new BorderLayout());
        add(about.getPanel(), BorderLayout.CENTER);
        add(new JLabel(Loader.getImageIcon(Loader.IMAGE_STURESY)), BorderLayout.NORTH);
        add(new JGap(25), BorderLayout.SOUTH);
    }

    /**
     * Shows the About-Window
     */
    public static void showMenu()
    {
        Dimension d = new Dimension(600, 600);
        AboutMenuUI abme = new AboutMenuUI(d);
        abme.setSize(d);
        abme.setLocationRelativeTo(null);
        abme.setVisible(true);
    }
}