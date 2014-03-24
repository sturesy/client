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

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import sturesy.core.ui.loaddialog.LoadButtonBar;
import sturesy.core.ui.loaddialog.SelectedDirectorySource;
import sturesy.core.ui.loaddialog.ui.LoadButtonBarUI;

@RunWith(MockitoJUnitRunner.class)
public class TestLoadButtonBar
{
    private LoadButtonBar _loadButtonBar;

    @Mock
    private LoadButtonBarUI _ui;

    @Mock
    private SelectedDirectorySource _directorySource;

    @Mock
    private FileFilter _fileFilter;

    @Mock
    private JButton _loadInternalFileButton;

    @Mock
    private JButton _loadExternalFileButton;

    private static final String DIRECTORY = "anypath";

    private static final String FILENAME = "something.xml";

    private static final File _expectedFile = new File(DIRECTORY + File.separator + FILENAME);

    @Before
    public void beforeEachTest()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadButtonBarInternalFile()
    {
        createLoadButtonBar();
        configureSources();
        _loadButtonBar.loadInternalFile();
        verify(_directorySource, times(1)).getDirectoryAbsolutePath();
        verify(_loadButtonBar, times(1)).informLoadListenerLoadInternalFile(eq(_expectedFile));
    }

    @Test
    public void testLoadButtonBarExternalFileFileIsSelected()
    {
        createLoadButtonBar();
        when(_ui.acceptFileFromUser(Mockito.<FileFilter> any())).thenReturn(_expectedFile);
        _loadButtonBar.loadExternalFile();

        verify(_loadButtonBar, times(1)).informLoadListenerLoadExternalFile(_expectedFile);
    }

    @Test
    public void testLoadButtonBarExternalFileNoFileSelected()
    {
        createLoadButtonBar();
        when(_ui.acceptFileFromUser(Mockito.<FileFilter> any())).thenReturn(null);
        _loadButtonBar.loadExternalFile();

        verify(_loadButtonBar, times(0)).informLoadListenerLoadExternalFile(_expectedFile);
    }

    @Test
    public void testLoadButtonBarAddExtraButtonFirstPosition()
    {
        createLoadButtonBar();
        JButton button = mock(JButton.class);
        _loadButtonBar.addExtraButtonOnFirstPosition(button);
        verify(_ui, times(1)).addButton(button, 0);
    }

    @Test
    public void testLoadButtonBarGetButtonBar()
    {
        createLoadButtonBar();
        JPanel button = new JPanel();
        when(_ui.getButtonBar()).thenReturn(button);
        JPanel buttonBar = _loadButtonBar.getButtonBar();
        assertSame(buttonBar, button);
        verify(_ui, times(1)).getButtonBar();
    }

    @Test
    public void testSetInternalButtonEnabled()
    {
        createLoadButtonBar();
        _loadButtonBar.setInternalLoadButtonEnabled(true);
        verify(_ui, times(1)).setInternalLoadButtonEnabled(true);
    }

    private void configureSources()
    {
        when(_directorySource.getFileName()).thenReturn(FILENAME);
        when(_directorySource.getDirectoryAbsolutePath()).thenReturn(DIRECTORY);
    }

    @SuppressWarnings("deprecation")
    private void createLoadButtonBar()
    {
        when(_ui.getLoadExternalFileButton()).thenReturn(_loadExternalFileButton);
        when(_ui.getLoadInternalFileButton()).thenReturn(_loadInternalFileButton);
        LoadButtonBar loadButtonBar = new LoadButtonBar(_directorySource, _fileFilter, _ui);
        _loadButtonBar = spy(loadButtonBar);
        configureLoadButtonBar();
    }

    private void configureLoadButtonBar()
    {
        doNothing().when(_loadButtonBar).informLoadListenerLoadInternalFile(Mockito.<File> any());
        doNothing().when(_loadButtonBar).informLoadListenerLoadExternalFile(Mockito.<File> any());
    }
}
