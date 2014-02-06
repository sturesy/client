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
package sturesy.settings;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import sturesy.core.Localize;
import sturesy.core.backend.Loader;

/**
 * The SettingsWindow displaying all available Settings<br>
 * Consisting of a List of Topics on the Left and the matching Settings on the
 * Right
 * 
 * @author w.posdorfer
 * 
 */
public class SettingsUI
{
    private JFrame _frame;
    private JList _iconsList;
    private JButton _saveButton;
    private JButton _saveAndCloseButton;
    private JButton _cancelButton;

    /**
     * ISettingsScreens will be displayed inside this JPanel
     */
    private JPanel _settingsPanel;

    public SettingsUI(Object[] array)
    {
        _settingsPanel = new JPanel(new BorderLayout());
        _iconsList = new JList(array);
        _frame = new JFrame(Localize.getString(Localize.SETTINGS));
        _frame.setIconImage(Loader.getImageIcon(Loader.IMAGE_STURESY).getImage());
        _frame.setLayout(new BorderLayout());

        _saveButton = new JButton(Localize.getString("button.save"));
        _saveAndCloseButton = new JButton(Localize.getString("button.save.close"));
        _cancelButton = new JButton(Localize.getString("button.cancel"));

        JPanel buttonpanel = new JPanel(new GridLayout(1, 3));
        buttonpanel.add(_cancelButton);
        buttonpanel.add(_saveButton);
        buttonpanel.add(_saveAndCloseButton);

        _frame.add(configureList(), BorderLayout.WEST);
        _frame.add(_settingsPanel, BorderLayout.CENTER);

        JPanel southpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southpanel.add(buttonpanel);
        _frame.add(southpanel, BorderLayout.SOUTH);

    }

    public JButton getSaveButton()
    {
        return _saveButton;
    }

    public JButton getSaveAndCloseButton()
    {
        return _saveAndCloseButton;
    }

    public JButton getCancelButton()
    {
        return _cancelButton;
    }

    public JFrame getFrame()
    {
        return _frame;
    }

    public Dimension getFrameSize()
    {
        return _frame.getSize();
    }

    public void setIcons(Object[] array)
    {
        _iconsList = new JList(array);
        configureList();

    }

    private JScrollPane configureList()
    {
        _iconsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _iconsList.setBackground(_settingsPanel.getBackground());
        JScrollPane scrollpane = new JScrollPane(_iconsList);
        scrollpane.setBorder(null);
        scrollpane.setBackground(_settingsPanel.getBackground());
        scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        _iconsList.setSelectedIndex(0);
        _iconsList.setCellRenderer(new CustomListCellRenderer());

        return scrollpane;
    }

    public Object getSelectedSettingsScreenValue()
    {
        return _iconsList.getSelectedValue();
    }

    public void setNewSettingsPanel(Component p)
    {
        _settingsPanel.removeAll();
        _settingsPanel.add(p);
        _settingsPanel.repaint();
        _settingsPanel.revalidate();
    }

    public JList getIconList()
    {
        return _iconsList;
    }

    public void show(WindowListener listener, Dimension size, Component relativToComponent)
    {
        _frame.pack();
        _frame.setSize(size);
        _frame.setLocationRelativeTo(relativToComponent);
        _frame.addWindowListener(listener);
        _frame.setVisible(true);
    }
}
