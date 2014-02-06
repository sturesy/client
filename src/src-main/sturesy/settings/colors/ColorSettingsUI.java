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

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import sturesy.core.Localize;
import sturesy.core.ui.JGap;

public class ColorSettingsUI
{
    private final int CENTER = GridBagConstraints.CENTER;
    private final int HORIZONTAL = GridBagConstraints.HORIZONTAL;
    private final Insets ZEROINSETS = new Insets(0, 0, 0, 0);
    private final String EMPTYSTRING = "   ";

    private JComponent _panel = new JPanel();

    private Map<String, JLabel> _components = new HashMap<String, JLabel>();

    private int _currentrow = 0;

    private JButton _restoreDefaults;

    public ColorSettingsUI()
    {
        _panel.setLayout(new GridBagLayout());

        _restoreDefaults = new JButton(Localize.getString("button.reset"));

        addComponent(ColorSettingController.VOTING_MAIN_COLOR);
        addComponent(ColorSettingController.VOTING_TOOLBAR_COLOR);
        addComponent(ColorSettingController.ANALYSIS_MAIN_COLOR);

        _panel.add(new JGap(10), new GridBagConstraints(0, _currentrow, 1, 1, 1, 1, CENTER, GridBagConstraints.BOTH,
                ZEROINSETS, 0, 0));

        _panel.add(_restoreDefaults, getConstraints(1, 4));

        JScrollPane pane = new JScrollPane(_panel);
        _panel = pane;
    }

    private void addComponent(String colorlink)
    {
        JLabel label = new JLabel(EMPTYSTRING);
        label.setSize(40, 20);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createLineBorder(Color.black));
        _panel.add(new JLabel(Localize.getString(colorlink)), getConstraints(0, _currentrow));
        _panel.add(label, getConstraints(1, _currentrow));
        _components.put(colorlink, label);
        _currentrow++;
    }

    public JComponent getPanel()
    {
        return _panel;
    }

    private GridBagConstraints getConstraints(int x, int y)
    {
        return new GridBagConstraints(x, y, 1, 1, 1.0, 0.1, CENTER, HORIZONTAL, ZEROINSETS, 0, 0);
    }

    public void setColorForLink(String link, Color c)
    {
        JLabel label = _components.get(link);
        if (label != null)
        {
            label.setForeground(c);
            label.setBackground(c);
        }
    }

    public Map<String, JLabel> getComponents()
    {
        return _components;
    }

    public JButton getRestoreDefaults()
    {
        return _restoreDefaults;
    }
}
