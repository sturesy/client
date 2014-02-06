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
package sturesy.settings.colors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import sturesy.core.Localize;
import sturesy.core.backend.Loader;
import sturesy.core.plugin.ISettingsScreen;
import sturesy.util.Defaults;
import sturesy.util.Settings;

public class ColorSettingController implements ISettingsScreen
{
    public static final String VOTING_TOOLBAR_COLOR = "color.voting.toolbar";
    public static final String VOTING_MAIN_COLOR = "color.voting.main";
    public static final String ANALYSIS_MAIN_COLOR = "color.analysis.main";

    private ColorSettingsUI _ui;
    private JTabbedPane _pane = new JTabbedPane();

    public ColorSettingController()
    {
        _ui = new ColorSettingsUI();
        loadSettings();
        _pane.add(getName(), _ui.getPanel());

        addListeners();
    }

    @Override
    public String getName()
    {
        return Localize.getString("label.colors");
    }

    @Override
    public ImageIcon getIcon()
    {
        return Loader.getImageIcon(Loader.IMAGE_COLORS);
    }

    @Override
    public Component getPanel()
    {
        return _pane;
    }

    private void loadSettings()
    {
        Settings settings = Settings.getInstance();

        _ui.setColorForLink(VOTING_TOOLBAR_COLOR, settings.getColor(VOTING_TOOLBAR_COLOR));
        _ui.setColorForLink(ANALYSIS_MAIN_COLOR, settings.getColor(ANALYSIS_MAIN_COLOR));
        _ui.setColorForLink(VOTING_MAIN_COLOR, settings.getColor(VOTING_MAIN_COLOR));
    }

    @Override
    public void saveSettings() throws Throwable
    {
        Settings settings = Settings.getInstance();
        for (String s : _ui.getComponents().keySet())
        {
            settings.setProperty(s, _ui.getComponents().get(s).getBackground());
        }
        settings.save();
    }

    private void labelMouseClicked(JLabel label, String property)
    {
        ColorPickerFrame frame = new ColorPickerFrame(label.getBackground());

        frame.showDialog(label);

        if (frame.colorWasPicked())
        {
            label.setForeground(frame.getColor());
            label.setBackground(frame.getColor());
            label.revalidate();
            label.repaint();
        }

    }

    private void restoreDefaults()
    {
        Defaults d = Defaults.getInstance();
        _ui.setColorForLink(VOTING_TOOLBAR_COLOR, d.getColor(VOTING_TOOLBAR_COLOR));
        _ui.setColorForLink(VOTING_MAIN_COLOR, d.getColor(VOTING_MAIN_COLOR));
        _ui.setColorForLink(ANALYSIS_MAIN_COLOR, d.getColor(ANALYSIS_MAIN_COLOR));
    }

    private void addListeners()
    {
        for (String s : _ui.getComponents().keySet())
        {
            JLabel label = _ui.getComponents().get(s);
            label.addMouseListener(new MouseClick(label, s)
            {
                public void mouseClicked(MouseEvent e)
                {
                    labelMouseClicked(_label, _property);
                }
            });
        }

        _ui.getRestoreDefaults().addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                restoreDefaults();
            }
        });
    }

    private abstract class MouseClick extends MouseAdapter
    {
        JLabel _label;
        String _property;

        public MouseClick(JLabel label, String property)
        {
            _label = label;
            _property = property;
        }

        public abstract void mouseClicked(MouseEvent e);
    }

}
