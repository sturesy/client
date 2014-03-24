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
package sturesy.test.loaddialog;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import sturesy.core.ui.loaddialog.LoadButtonBar;
import sturesy.core.ui.loaddialog.LoadDialog;
import sturesy.core.ui.loaddialog.LoadDialogListener;
import sturesy.core.ui.loaddialog.SelectedDirectorySource;
import sturesy.core.ui.loaddialog.SubsettedJListPair;
import sturesy.core.ui.loaddialog.ui.LoadDialogUI;

@RunWith(MockitoJUnitRunner.class)
public class TestLoadDialog
{
    private static final String DEFAULT_PATH = "defaultPath";

    private LoadDialog _loadDialog;

    private String _internalDirectoryPath = null;

    @Mock
    private FileFilter _filter;

    @Mock
    private LoadDialogUI _ui;

    private LoadButtonBar _loadButtonBar;

    @Spy
    private SubsettedJListPair _listPair;

    private String _selectedContentItem = "selected subsetted item";

    private String _selectedSourceItem = "selected subset source item";

    @Mock
    private SelectedDirectorySource _selectedDirectorySource;

    @Mock
    private LoadDialogListener _loadDialogListener;

    @Before
    public void beforeEachTest()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetFileFilter()
    {
        createLoadDialogSpy(DEFAULT_PATH);
        _loadDialog.setFileFilter(_filter);
        verify(_loadButtonBar, times(1)).setFileFilter(_filter);
    }

    @Test
    public void testReplaceNorthernPanel()
    {
        JPanel panelMock = mock(JPanel.class);
        createLoadDialogSpy(DEFAULT_PATH);
        _loadDialog.replaceNorthernPanel(panelMock);
        verify(_ui, times(1)).replaceNorthPanel(panelMock);
    }

    @Test
    public void testTriggerContentListKeyEventIsEnterAndButtonEnabled()
    {
        createLoadDialogSpy(DEFAULT_PATH);
        when(_listPair.hasSelectedEntries()).thenReturn(true);
        _listPair.informContentListKeyEvent(KeyEvent.VK_ENTER);

        verify(_loadButtonBar, times(1)).loadInternalFile();
        verify(_loadButtonBar, times(1)).setInternalLoadButtonEnabled(true);
    }

    @Test
    public void testTriggerContentListKeyEventIsEnterButButtonNotEnabled()
    {
        createLoadDialogSpy(DEFAULT_PATH);
        when(_listPair.hasSelectedEntries()).thenReturn(false);
        _listPair.informContentListKeyEvent(KeyEvent.VK_ENTER);

        verify(_loadButtonBar, times(0)).loadInternalFile();
        verify(_loadButtonBar, times(1)).setInternalLoadButtonEnabled(false);
    }

    @Test
    public void testTriggerContentListKeyEventIsNotEnterAndButtonNotEnabled()
    {
        createLoadDialogSpy(DEFAULT_PATH);
        when(_listPair.hasSelectedEntries()).thenReturn(false);
        _listPair.informContentListKeyEvent(KeyEvent.VK_UP);

        verify(_loadButtonBar, times(0)).loadInternalFile();
        verify(_loadButtonBar, times(1)).setInternalLoadButtonEnabled(false);
    }

    @Test
    public void testTriggerContentListKeyEventIsNotEnterButButtonEnabled()
    {
        createLoadDialogSpy(DEFAULT_PATH);
        when(_listPair.hasSelectedEntries()).thenReturn(true);
        _listPair.informContentListKeyEvent(KeyEvent.KEY_FIRST);

        verify(_loadButtonBar, times(0)).loadInternalFile();
        verify(_loadButtonBar, times(1)).setInternalLoadButtonEnabled(true);
    }

    @Test
    public void testTriggerSourceListKeyEventIsEnterInternalLoadButtonEnabled()
    {
        createLoadDialogSpy(DEFAULT_PATH);
        when(_listPair.hasSelectedEntries()).thenReturn(true);
        _listPair.informSourceListKeyEvent(KeyEvent.VK_ENTER);

        verify(_loadButtonBar, times(1)).loadInternalFile();
        verify(_loadButtonBar, times(1)).setInternalLoadButtonEnabled(true);
    }

    @Test
    public void testTriggerSourceListKeyEventIsEnterInternalLoadButtonNotEnabled()
    {
        createLoadDialogSpy(DEFAULT_PATH);
        when(_listPair.hasSelectedEntries()).thenReturn(false);
        _listPair.informSourceListKeyEvent(KeyEvent.VK_ENTER);

        verify(_loadButtonBar, times(0)).loadInternalFile();
        verify(_loadButtonBar, times(1)).setInternalLoadButtonEnabled(false);

    }

    @Test
    public void testTriggerSourceListKeyEventIsNotEnterInternalLoadButtonEnabled()
    {
        createLoadDialogSpy(DEFAULT_PATH);
        when(_listPair.hasSelectedEntries()).thenReturn(true);
        _listPair.informSourceListKeyEvent(KeyEvent.KEY_FIRST);

        verify(_loadButtonBar, times(0)).loadInternalFile();
        verify(_loadButtonBar, times(1)).setInternalLoadButtonEnabled(true);
    }

    @Test
    public void testTriggerSourceListKeyEventIsNotEnterInternalLoadButtonNotEnabled()
    {
        createLoadDialogSpy(DEFAULT_PATH);
        when(_listPair.hasSelectedEntries()).thenReturn(false);
        _listPair.informSourceListKeyEvent(KeyEvent.KEY_FIRST);

        verify(_loadButtonBar, times(0)).loadInternalFile();
        verify(_loadButtonBar, times(1)).setInternalLoadButtonEnabled(false);

    }

    @Test
    public void testTriggerSourceListKeyEventValueIsAdjustingSourceHasCharacters()
    {
        createLoadDialogSpy(DEFAULT_PATH);
        doReturn(true).when(_listPair).hasSelectedEntries();
        doReturn("MANY CHARACTERS").when(_listPair).getSourceListElement();
        _loadDialog.registerListener(_loadDialogListener);
        _listPair.informSourceListChanged(true);
        verify(_loadDialogListener, times(1)).subsetSourceListChanged(Mockito.<File> any());
        verify(_loadButtonBar, times(1)).setInternalLoadButtonEnabled(true);
    }

    @Test
    public void testTriggerSourceListKeyEventValueIsAdjustingSourceNull()
    {
        createLoadDialogSpy(null);
        doReturn(null).when(_listPair).getSourceListElement();
        doReturn(true).when(_listPair).hasSelectedEntries();

        _loadDialog.registerListener(_loadDialogListener);
        _listPair.informSourceListChanged(true);

        verify(_loadDialogListener, times(0)).internalFileLoaded(null);
        verify(_loadButtonBar, times(1)).setInternalLoadButtonEnabled(true);
    }

    @Test
    public void testTriggerSourceListKeyEventValueIsAdjustingSourceHasNoCharacters()
    {
        createLoadDialogSpy("");
        doReturn("MANY CHARACTERS").when(_listPair).getSourceListElement();
        doReturn(true).when(_listPair).hasSelectedEntries();

        _loadDialog.registerListener(_loadDialogListener);
        _listPair.informSourceListChanged(true);

        verify(_loadDialogListener, times(0)).internalFileLoaded(Mockito.<File> any());
        verify(_loadButtonBar, times(1)).setInternalLoadButtonEnabled(true);
    }

    @Test
    public void testTriggerSubsetSourceListKeyEventValueIsNotAdjusting()
    {
        createLoadDialogSpy("");
        doReturn("MANY CHARACTERS").when(_listPair).getSourceListElement();
        doReturn(true).when(_listPair).hasSelectedEntries();

        _loadDialog.registerListener(_loadDialogListener);
        _listPair.informSourceListChanged(false);

        verify(_loadDialogListener, times(0)).internalFileLoaded(Mockito.<File> any());
        verify(_loadButtonBar, times(1)).setInternalLoadButtonEnabled(true);
    }

    @Test
    public void testTriggerLoadedInternalFile()
    {
        createLoadDialogSpy(DEFAULT_PATH);
        File file = mock(File.class);

        _loadDialog.registerListener(_loadDialogListener);
        _loadButtonBar.informLoadListenerLoadInternalFile(file);

        verify(_loadDialogListener, times(1)).internalFileLoaded(file);
        verify(_ui, times(1)).closeDialog();
    }

    @Test
    public void testTriggerLoadedExternalFile()
    {
        createLoadDialogSpy(DEFAULT_PATH);
        File file = mock(File.class);

        _loadDialog.registerListener(_loadDialogListener);
        _loadButtonBar.informLoadListenerLoadExternalFile(file);

        verify(_loadDialogListener, times(1)).externalFileLoaded(file);
        verify(_ui, times(1)).closeDialog();
    }

    @Test
    public void testGetDirectoryAbsolutePathInternalPathIsNull()
    {
        createLoadDialogSpy(null);

        String directoryAbsolutePath = _loadDialog.getDirectoryAbsolutePath();
        assertEquals(_selectedSourceItem, directoryAbsolutePath);
    }

    @Test
    public void testGetDirectoryAbsolutePathInternalPathNotNull()
    {
        createLoadDialogSpy(DEFAULT_PATH);

        String directoryAbsolutePath = _loadDialog.getDirectoryAbsolutePath();
        assertEquals(DEFAULT_PATH + File.separator + _selectedSourceItem, directoryAbsolutePath);
    }

    @Test
    public void testGetFileName()
    {
        createLoadDialogSpy(DEFAULT_PATH);

        String fileName = _loadDialog.getFileName();
        assertEquals(_selectedContentItem, fileName);
    }

    @Test
    public void testShowDialog()
    {
        createLoadDialogSpy(DEFAULT_PATH);

        _loadDialog.show();
        verify(_ui, times(1)).showDialog();
    }

    @Test
    public void testSetNewsubsetSourceListContent()
    {
        List<String> newContent = new ArrayList<String>();
        newContent.add("Hello");
        newContent.add("World");
        newContent.add("!");
        doReturn(true).when(_listPair).hasSelectedEntries();
        createLoadDialogSpy(DEFAULT_PATH);

        _loadDialog.setNewSourceListContent(newContent);
        verify(_listPair).setNewSourceListContent(newContent);
        verify(_loadButtonBar).setInternalLoadButtonEnabled(true);
    }

    @Test
    public void testSetNewSubsettedListContent()
    {
        List<String> newContent = new ArrayList<String>();
        newContent.add("Hello");
        newContent.add("World");
        newContent.add("!");
        createLoadDialogSpy(DEFAULT_PATH);
        doReturn(false).when(_listPair).hasSelectedEntries();

        _loadDialog.setNewContentListContent(newContent);

        verify(_listPair).setNewContentListContent(newContent);
        verify(_loadButtonBar).setInternalLoadButtonEnabled(false);
    }

    @Test
    public void testShowErrorMessage()
    {
        createLoadDialogSpy(DEFAULT_PATH);
        _loadDialog.showErrorMessage("ressKey");
        verify(_ui, times(1)).showErrorMessage("ressKey");
    }

    @Test
    public void testCloseDialog()
    {
        createLoadDialogSpy(DEFAULT_PATH);

        _loadDialog.closeDialog();
        verify(_ui, times(1)).closeDialog();
    }

    @Test
    public void testAddExtraButton()
    {
        createLoadDialogSpy(DEFAULT_PATH);
        JButton button = mock(JButton.class);
        _loadDialog.addExtraButton(button);

        verify(_loadButtonBar, times(1)).addExtraButtonOnFirstPosition(button);
    }

    @Test
    public void testSetModal()
    {
        createLoadDialogSpy(DEFAULT_PATH);
        _loadDialog.setModal(true);
        verify(_ui, times(1)).setModal(true);
    }

    private void createLoadDialogSpy(String internalDirectoryPath)
    {
        _internalDirectoryPath = internalDirectoryPath;
        LoadButtonBar loadButtonBar = new LoadButtonBar(_selectedDirectorySource);
        _loadButtonBar = spy(loadButtonBar);
        LoadDialog loadDialog = new LoadDialog(_internalDirectoryPath, _ui, _loadButtonBar, _listPair);
        _loadDialog = spy(loadDialog);
        configureLoadDialogSpy();
        configureSubsettedListSpy();
        configureLoadButtonBarSpy();
    }

    private void configureLoadDialogSpy()
    {
        doNothing().when(_loadDialog).informExternalFileLoaded(Mockito.<File> any());
        doNothing().when(_loadDialog).informInternalFileLoaded(Mockito.<File> any());
        doNothing().when(_loadDialog).informSubsetSourceListChanged(Mockito.<File> any());
    }

    private void configureSubsettedListSpy()
    {
        doReturn(true).when(_listPair).hasSelectedEntries();
        doReturn(_selectedContentItem).when(_listPair).getContentListItem();
        doReturn(_selectedSourceItem).when(_listPair).getSourceListElement();
        doNothing().when(_listPair).setNewSourceListContent(anyListOf(String.class));
        doNothing().when(_listPair).setNewContentListContent(anyListOf(String.class));
    }

    private void configureLoadButtonBarSpy()
    {
        doNothing().when(_loadButtonBar).setFileFilter(Mockito.<FileFilter> any());
        doNothing().when(_loadButtonBar).loadInternalFile();
        doNothing().when(_loadButtonBar).loadExternalFile();
        doNothing().when(_loadButtonBar).setInternalLoadButtonEnabled(anyBoolean());
        doNothing().when(_loadButtonBar).addExtraButtonOnFirstPosition(Mockito.<JButton> any());
    }
}
