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
package sturesy.core.ui.loaddialog;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import sturesy.core.backend.filter.filechooser.XMLFileFilter;
import sturesy.core.ui.loaddialog.ui.LoadDialogUI;

/**
 * The load dialog provides handling for file loading. It provides two methods
 * for loading. The first method is to load internal files. For this a internal
 * directory is needed. The files will be allocated by two lists. The first list
 * is the directory and the second list is the filename+type. The second method
 * is external file loading. It provides a FileChooser which shown files and
 * directories can be subsetted by a FileFilter. If more possibilities for
 * loading are needed an external button can be injected, but the client has two
 * handle ui interactions for this button The Northpanel is free and there can
 * be injected any component
 * 
 * @author jens.dallmann
 * 
 */
public class LoadDialog extends LoadDialogObservable implements SelectedDirectorySource
{
    /**
     * The button bar for the load buttons
     */
    private LoadButtonBar _loadButtonBar;

    /**
     * the subsetted j list pair
     */
    private final SubsettedJListPair _subsettedListPair;

    /**
     * Internal directory path which points to the directories shown in the left
     * list
     */
    private final String _internalDirectoryPath;

    /**
     * the ui for the load dialog to layout the elements and provide user
     * interaction
     */
    private LoadDialogUI _loadDialogUI;

    /**
     * Initialize the load dialog with an internal directory path and a title.
     * The default filefilter is a XMLFileFilter which subsets to .xml files and
     * directories in the external filechoser
     * 
     * @param internalDirectoryPath
     * @param title
     */
    public LoadDialog(String internalDirectoryPath, String title)
    {
        this(internalDirectoryPath, title, new XMLFileFilter());
    }

    /**
     * Initialize the load dialog with an internal directory path, a title and a
     * file filter
     * 
     * @param internalDirectoryPath
     * @param title
     * @param filter
     */
    public LoadDialog(String internalDirectoryPath, String title, FileFilter filter)
    {
        _internalDirectoryPath = internalDirectoryPath;
        _loadButtonBar = new LoadButtonBar(this, filter);
        _subsettedListPair = new SubsettedJListPair();
        _loadDialogUI = new LoadDialogUI(title, null, _subsettedListPair.getSubsettedListPairUI(),
                _loadButtonBar.getButtonBar());
        registerLoadListener();
    }

    /**
     * Initialize the load dialog with an internal directory path, a title, a
     * file filter, the ui and the two components displayed in the laod dialog
     * by default.
     * 
     * @param internalDirectoryPath
     * @param ui
     * @param loadbuttonbar
     * @param listpair
     */
    public LoadDialog(String internalDirectoryPath, LoadDialogUI ui, LoadButtonBar loadButtonBar,
            SubsettedJListPair listPair)
    {
        _internalDirectoryPath = internalDirectoryPath;
        _loadButtonBar = loadButtonBar;
        _subsettedListPair = listPair;
        _loadDialogUI = ui;
        registerLoadListener();
    }

    /**
     * overwrite the old file filter with a new one
     * 
     * @param filter
     */
    public void setFileFilter(FileFilter filter)
    {
        _loadButtonBar.setFileFilter(filter);
    }

    /**
     * replace the northern panel with a passed panel
     * 
     * @param panel
     */
    public void replaceNorthernPanel(JPanel panel)
    {
        _loadDialogUI.replaceNorthPanel(panel);
    }

    private void registerLoadListener()
    {
        _subsettedListPair.registerListener(new SubsettedListPairListener()
        {
            @Override
            public void subsettedListKeyEvent(int keyCode)
            {
                if (keyCode == KeyEvent.VK_ENTER)
                {
                    loadInternalFile();
                }
            }

            @Override
            public void subsetSourceListKeyEvent(int keyCode)
            {
                if (keyCode == KeyEvent.VK_ENTER)
                {
                    loadInternalFile();
                }
            }

            @Override
            public void subsetSourceListChanged(boolean valueIsAdjusting)
            {
                if (valueIsAdjusting)
                {
                    String newSubsetSourceListValue = getDirectoryAbsolutePath();
                    if (newSubsetSourceListValue != null && newSubsetSourceListValue.length() > 0)
                    {
                        File subsetSourceDirectory = new File(newSubsetSourceListValue);

                        informSubsetSourceListChanged(subsetSourceDirectory);
                    }
                }
                setNewInternalLoadButtonState();
            }
        });
        _loadButtonBar.registerListener(new LoadButtonBarListener()
        {
            @Override
            public void loadedInternalFile(File f)
            {
                loadInternalFile(f);
            }

            @Override
            public void loadedExternalFile(File file)
            {
                externalFileLoaded(file);
            }
        });
    }

    /**
     * sets the internal load button state to enabled or disabled
     */
    private void setNewInternalLoadButtonState()
    {
        _loadButtonBar.setInternalLoadButtonEnabled(isInternalLoadButtonEnabled());
    }

    /**
     * returns if the load button for internal files is enabled. the internal
     * load button is enabled if in both list one entry is selected. Else it
     * returns false.
     * 
     * @return hasSelectedEntries of subsettedListPair
     */
    private boolean isInternalLoadButtonEnabled()
    {
        return _subsettedListPair.hasSelectedEntries();
    }

    /**
     * informs that an external file has been loaded and closes the dialog
     * 
     * @param file
     */
    private void externalFileLoaded(File file)
    {
        informExternalFileLoaded(file);
        _loadDialogUI.closeDialog();
    }

    /**
     * informs that an internal file has been loaded and closes the dialog
     * 
     * @param file
     */
    private void loadInternalFile(File file)
    {
        informInternalFileLoaded(file);
        _loadDialogUI.closeDialog();
    }

    @Override
    public String getFileName()
    {
        return _subsettedListPair.getContentListItem();
    }

    @Override
    public String getDirectoryAbsolutePath()
    {
        String subsetSourceListElement = _subsettedListPair.getSourceListElement();
        if (_internalDirectoryPath != null)
        {
            return _internalDirectoryPath + File.separator + subsetSourceListElement;
        }
        return subsetSourceListElement;
    }

    /**
     * show the dialog
     */
    public void show()
    {
        _loadDialogUI.showDialog();
    }

    /**
     * sets new content for the subset source list.
     * 
     * @param newContent
     */
    public void setNewSourceListContent(List<String> newContent)
    {
        _subsettedListPair.setNewSourceListContent(newContent);
        setNewInternalLoadButtonState();
    }

    /**
     * sets the new content for the subsetted list.
     * 
     * @param newContent
     */
    public void setNewContentListContent(List<String> newContent)
    {
        _subsettedListPair.setNewContentListContent(newContent);
        setNewInternalLoadButtonState();
    }

    /**
     * shows an error message on the screen
     * 
     * @param resourceKey
     */
    public void showErrorMessage(String resourceKey)
    {
        _loadDialogUI.showErrorMessage(resourceKey);
    }

    /**
     * closes the dialog
     */
    public void closeDialog()
    {
        _loadDialogUI.closeDialog();
    }

    public void loadInternalFile()
    {
        if (isInternalLoadButtonEnabled())
        {
            _loadButtonBar.loadInternalFile();
        }
        setNewInternalLoadButtonState();
    }

    /**
     * adds an extra button on the first position
     * 
     * @param extraButton
     */
    public void addExtraButton(JButton extraButton)
    {
        _loadButtonBar.addExtraButtonOnFirstPosition(extraButton);
    }

    /**
     * sets if the load dialog should be modal or not
     * 
     * @param isModal
     */
    public void setModal(boolean isModal)
    {
        _loadDialogUI.setModal(isModal);
    }
}
