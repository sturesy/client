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
package sturesy.core.ui.loaddialog.ui;

import java.awt.FlowLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import sturesy.core.Localize;

/**
 * The LoadButtonBarUI displays the button bar and calls on actions the
 * operations provided by the interaction
 * 
 * @author student
 * 
 */
public class LoadButtonBarUI
{
    private JButton _loadInternalFileButton;
    private JButton _loadExternalFileButton;
    private JPanel _buttonBar;

    public LoadButtonBarUI()
    {
        _loadInternalFileButton = new JButton(Localize.getString("button.load"));
        _loadInternalFileButton.setToolTipText(Localize.getString("tool.questionload.load"));
        _loadInternalFileButton.setEnabled(false);
        _loadExternalFileButton = new JButton(Localize.getString("button.loadexternal"));
        _loadExternalFileButton.setToolTipText(Localize.getString("tool.questionload.load.external"));
        _buttonBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        _buttonBar.add(_loadInternalFileButton);
        _buttonBar.add(_loadExternalFileButton);
    }

    /**
     * Opens a file chooser and returns the selected file
     * 
     * @param filter
     *            if null it will be ignored. No Exception expected
     * @return File from user, or <code>null</code> if no selection has been
     *         made
     */
    public File acceptFileFromUser(FileFilter filter)
    {
        JFileChooser fc = new JFileChooser(System.getProperty("user.home"));
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (filter != null)
        {
            fc.setFileFilter(filter);
        }
        int result = fc.showOpenDialog(null);

        File selectedFile = null;

        if (result == JFileChooser.APPROVE_OPTION)
        {
            selectedFile = fc.getSelectedFile();
        }
        return selectedFile;
    }

    public JButton getLoadExternalFileButton()
    {
        return _loadExternalFileButton;
    }

    public JButton getLoadInternalFileButton()
    {
        return _loadInternalFileButton;
    }

    /**
     * adds a button at the passed position
     * 
     * @param button
     *            adds a button to the panel
     * @param index
     */
    public void addButton(JButton button, int index)
    {
        _buttonBar.add(button, index);
    }

    /**
     * returns the button bar panel.
     * 
     * @return JPanel the button bar panel
     */
    public JPanel getButtonBar()
    {
        return _buttonBar;
    }

    /**
     * Sets the internal laod button enabled or disabled
     * 
     * @param isEnabled
     *            button state
     */
    public void setInternalLoadButtonEnabled(boolean isEnabled)
    {
        _loadInternalFileButton.setEnabled(isEnabled);
    }
}
