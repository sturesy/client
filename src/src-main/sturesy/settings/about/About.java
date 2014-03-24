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
package sturesy.settings.about;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import sturesy.core.backend.Loader;
import sturesy.core.plugin.ISettingsScreen;
import sturesy.util.Version;

/**
 * Aboutmenu in Settings
 * 
 * @author w.posdorfer
 */
public class About implements ISettingsScreen
{

    private Panel _panel;

    public About(Dimension size)
    {
        _panel = new Panel(size);
    }

    @Override
    public String getName()
    {
        return "About";
    }

    @Override
    public ImageIcon getIcon()
    {
        return Loader.getImageIcon(Loader.IMAGE_COPYRIGHT);
    }

    @Override
    public Component getPanel()
    {
        return _panel;
    }

    @Override
    public void saveSettings()
    {
    }

    private class Panel extends JPanel
    {
        private static final long serialVersionUID = 9079003718528881051L;

        public Panel(Dimension size)
        {
            setLayout(new BorderLayout());
            JPanel northpanel = new JPanel(new BorderLayout());

            JLabel name = new JLabel("<html><center>StuReSy - Version " + Version.CURRENTVERSION
                    + "<br>\u00A9 2012-2013 StuReSy-Team</center></html>");
            name.setFont(name.getFont().deriveFont(20.0f));
            name.setHorizontalAlignment(JLabel.CENTER);

            JLabel contributors = new JLabel("<html><body><center>" + getContributors() + "<br></center></body></html>");
            contributors.setHorizontalAlignment(JLabel.CENTER);

            Color c = name.getBackground();
            String color = c.getRed() + "," + c.getGreen() + "," + c.getBlue();

            JLabel label = new JLabel("<html><body bgcolor=\"rgb(" + color + ")\"><center>" + getLicense()
                    + "</center></body></html>");
            label.setBackground(c);
            label.setOpaque(true);
            label.setHorizontalAlignment(JLabel.CENTER);

            JScrollPane scroll = new JScrollPane(label);
            scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            scroll.setBorder(null);

            Dimension d = size;

            scroll.setPreferredSize(new Dimension(50, d.height - 200));

            northpanel.add(name, BorderLayout.NORTH);
            northpanel.add(contributors, BorderLayout.CENTER);

            add(northpanel, BorderLayout.NORTH);
            add(scroll, BorderLayout.CENTER);
        }

        public String getLicense()
        {
            return getContentsOfStreamBy(Loader.LICENSE_HTML);
        }

        public String getContributors()
        {
            return getContentsOfStreamBy(Loader.CONTRIBUTORS_TEXT);
        }

        public String getContentsOfStreamBy(String resource)
        {
            InputStream instream = Loader.getResourceAsStream(resource);
            InputStreamReader instreamreader = new InputStreamReader(instream);
            BufferedReader bufread = new BufferedReader(instreamreader);

            String line = "";

            StringBuffer buffer = new StringBuffer();
            try
            {
                while ((line = bufread.readLine()) != null)
                {
                    buffer.append(line);
                }
                bufread.close();
                instreamreader.close();
                instream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    if (bufread != null)
                    {
                        bufread.close();
                    }
                    if (instreamreader != null)
                    {
                        instreamreader.close();
                    }
                    if (instream != null)
                    {
                        instream.close();
                    }
                }
                catch (IOException e)
                {
                }
            }

            String result = buffer.toString();
            return result;
        }
    }

}