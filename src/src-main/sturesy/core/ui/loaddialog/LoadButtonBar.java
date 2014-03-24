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
package sturesy.core.ui.loaddialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import sturesy.core.backend.filter.filechooser.XMLFileFilter;
import sturesy.core.ui.loaddialog.ui.LoadButtonBarUI;

/**
 * LoadButtonBar represents the logic of the LoadButtonBar. It process the
 * loaded file and informs the listener
 * 
 * @author jens dallman
 */
public class LoadButtonBar extends LoadButtonBarObservable
{

    /**
     * source to retrieve the absolute directory path for internal file loading.
     */
    private final SelectedDirectorySource _directorySource;
    /**
     * the file filter which subset the shown entries on external file loading.
     */
    private FileFilter _filter;
    /**
     * ButtonBar UI Class.
     */
    private final LoadButtonBarUI _ui;

    /**
     * Initialize the dialog with a directory and file source for internal file
     * loading.
     * 
     * @param directorySource
     *            the directory source
     */
    public LoadButtonBar(SelectedDirectorySource directorySource)
    {
        this(directorySource, new XMLFileFilter());
    }

    /**
     * Initialize the dialog with a directory and file source for internal file
     * loading.
     * 
     * 
     * @param directorySource
     *            the directory source
     * @param filter
     *            the filefilter which subsets the shown entries on external
     *            file loading
     */
    public LoadButtonBar(SelectedDirectorySource directorySource, FileFilter filter)
    {
        _filter = new XMLFileFilter();
        _ui = new LoadButtonBarUI();
        _directorySource = directorySource;
        registerListener();
    }

    /**
     * Initialize the dialog with a directory and file source for internal file
     * loading.
     * 
     * 
     * @param directorySource
     *            the directory source
     * @param fileSource
     *            the source for a file name
     * @param filter
     *            the filefilter which subsets the shown entries on external
     *            file loading
     * @param the
     *            ui class
     */
    @Deprecated
    public LoadButtonBar(SelectedDirectorySource directorySource, FileFilter filter, LoadButtonBarUI ui)
    {
        _filter = new XMLFileFilter();
        _ui = ui;
        _directorySource = directorySource;
        registerListener();
    }

    public void loadInternalFile()
    {
        String directory = _directorySource.getDirectoryAbsolutePath();
        String fileName = _directorySource.getFileName();
        String filepath = directory + File.separator + fileName;

        File file = new File(filepath);
        informLoadListenerLoadInternalFile(file);
    }

    public void loadExternalFile()
    {
        File file = _ui.acceptFileFromUser(_filter);
        if (file != null)
        {
            informLoadListenerLoadExternalFile(file);
        }
    }

    /**
     * adds an extra button in the button bar on the first position. the button
     * handling is not provided by this method.
     * 
     * @param button
     *            the button to add to the button bar
     */
    public void addExtraButtonOnFirstPosition(JButton button)
    {
        _ui.addButton(button, 0);
    }

    /**
     * Returns the button bar panel
     * 
     * @return JPanel
     */
    public JPanel getButtonBar()
    {
        return _ui.getButtonBar();
    }

    /**
     * Enables or Disables the internal load button
     * 
     * @param isEnabled
     */
    public void setInternalLoadButtonEnabled(boolean isEnabled)
    {
        _ui.setInternalLoadButtonEnabled(isEnabled);
    }

    /**
     * Set the file filter.
     * 
     * @param filter
     *            the filefilter which subsets the shown entries.
     */
    public void setFileFilter(FileFilter filter)
    {
        _filter = filter;
    }
    
    /**
     * adds action listeners to the internal and external load button
     */
    private void registerListener()
    {
       _ui.getLoadInternalFileButton().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                loadInternalFile();
            }
        });
        _ui.getLoadExternalFileButton().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                loadExternalFile();
            }
        });
    }
}
