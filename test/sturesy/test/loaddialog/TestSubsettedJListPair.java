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
package sturesy.test.loaddialog;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import sturesy.core.ui.loaddialog.SubsettedJListPair;
import sturesy.core.ui.loaddialog.ui.SubsettedJListPairUI;

@RunWith(MockitoJUnitRunner.class)
public class TestSubsettedJListPair
{

    private SubsettedJListPair _subsettedJListPair;

    @Mock
    private DefaultListModel _subsetSourceListModel;
    @Mock
    private DefaultListModel _subsettedListModel;
    @Mock
    private SubsettedJListPairUI _ui;

    @Spy
    private JList _contentList;

    @Spy
    private JList _sourceList;

    @Test
    public void testSetNewSubsetSourceListContentOneEntry()
    {
        List<String> expectedListEntries = new ArrayList<String>();
        expectedListEntries.add("Hello World");
        createSubsettedJListPair();
        _subsettedJListPair.setNewSourceListContent(expectedListEntries);

        verify(_subsetSourceListModel, times(1)).clear();
        for (String oneExpectedString : expectedListEntries)
        {
            verify(_subsetSourceListModel, times(1)).addElement(oneExpectedString);
        }
        verify(_ui, times(1)).setSourceListEntrySelected(0);

    }

    @Test
    public void testSetNewSubsetSourceListContentSomeEntries()
    {
        List<String> expectedListEntries = new ArrayList<String>();
        expectedListEntries.add("Hello");
        expectedListEntries.add("World");
        expectedListEntries.add("Will");
        expectedListEntries.add("Ever");
        expectedListEntries.add("Be");
        expectedListEntries.add("The");
        expectedListEntries.add("First");
        expectedListEntries.add("Example");

        createSubsettedJListPair();
        _subsettedJListPair.setNewSourceListContent(expectedListEntries);

        verify(_subsetSourceListModel, times(1)).clear();
        for (String oneExpectedString : expectedListEntries)
        {
            verify(_subsetSourceListModel, times(1)).addElement(oneExpectedString);
        }
        verify(_ui, times(1)).setSourceListEntrySelected(0);
    }

    @Test
    public void testSetNewSubsetSourceListContentNull()
    {
        createSubsettedJListPair();
        _subsettedJListPair.setNewSourceListContent(null);

        verify(_subsetSourceListModel, times(1)).clear();
        verify(_subsetSourceListModel, times(0)).addElement(Mockito.<String> any());
        verify(_ui, times(0)).setSourceListEntrySelected(0);
    }

    @Test
    public void testSetNewSubsetSourceListContentEmptyContent()
    {
        createSubsettedJListPair();
        _subsettedJListPair.setNewSourceListContent(new ArrayList<String>());

        verify(_subsetSourceListModel, times(1)).clear();
        verify(_subsetSourceListModel, times(0)).addElement(Mockito.<String> any());
        verify(_ui, times(0)).setSourceListEntrySelected(0);
    }

    @Test
    public void testSetNewSubsettedListContentOneEntry()
    {
        List<String> expectedContent = new ArrayList<String>();
        expectedContent.add("Hello World");
        createSubsettedJListPair();
        _subsettedJListPair.setNewContentListContent(expectedContent);

        verify(_subsettedListModel, times(1)).clear();
        for (String oneString : expectedContent)
        {
            verify(_subsettedListModel, times(1)).addElement(oneString);
        }
        verify(_ui, times(1)).setContentListEntrySelected(0);
    }

    @Test
    public void testSetNewSubsettedListContentManyEntries()
    {
        List<String> expectedListEntries = new ArrayList<String>();
        expectedListEntries.add("Hello");
        expectedListEntries.add("World");
        expectedListEntries.add("Will");
        expectedListEntries.add("Ever");
        expectedListEntries.add("Be");
        expectedListEntries.add("The");
        expectedListEntries.add("First");
        expectedListEntries.add("Example");

        createSubsettedJListPair();
        _subsettedJListPair.setNewContentListContent(expectedListEntries);

        verify(_subsettedListModel, times(1)).clear();
        for (String oneString : expectedListEntries)
        {
            verify(_subsettedListModel, times(1)).addElement(oneString);
        }
        verify(_ui, times(1)).setContentListEntrySelected(0);
    }

    @Test
    public void testSetNewSubsettedListContentEmptyContent()
    {
        createSubsettedJListPair();
        _subsettedJListPair.setNewContentListContent(new ArrayList<String>());

        verify(_subsettedListModel, times(1)).clear();
        verify(_subsettedListModel, times(0)).addElement(Mockito.<String> any());
        verify(_ui, times(0)).setContentListEntrySelected(0);
    }

    @Test
    public void testSetNewSubsettedListContentNull()
    {
        createSubsettedJListPair();
        _subsettedJListPair.setNewContentListContent(null);

        verify(_subsettedListModel, times(1)).clear();
        verify(_subsettedListModel, times(0)).addElement(Mockito.<String> any());
        verify(_ui, times(0)).setContentListEntrySelected(0);
    }

    @Test
    public void testGetSubsettedListPairUI()
    {
        createSubsettedJListPair();
        _subsettedJListPair.getSubsettedListPairUI();
        verify(_ui, times(1)).getPanel();
    }

    @Test
    public void testSubsettedListItem()
    {
        createSubsettedJListPair();
        String selectedElementInList = "SelectedElement";
        when(_ui.getSelectedContentListElement()).thenReturn(selectedElementInList);
        String subsettedListItem = _subsettedJListPair.getContentListItem();
        assertEquals(selectedElementInList, subsettedListItem);
    }

    @Test
    public void testGetSourceListElement()
    {
        createSubsettedJListPair();
        String selectedElementInList = "SelectedElement";
        when(_ui.getSelectedSourceListElement()).thenReturn(selectedElementInList);
        String subsetSourceListElement = _subsettedJListPair.getSourceListElement();
        assertEquals(selectedElementInList, subsetSourceListElement);
    }

    @Test
    public void testContentListKeyEvent()
    {
        createSubsettedJListPair();
        _subsettedJListPair.informContentListKeyEvent(KeyEvent.VK_UP);
        verify(_subsettedJListPair).informContentListKeyEvent(KeyEvent.VK_UP);
    }

    @Test
    public void testSourceListKeyEventUP()
    {
        createSubsettedJListPair();
        doNothing().when(_subsettedJListPair).informSourceListChanged(true);
        _subsettedJListPair.subsetSourceListKeyEvent(KeyEvent.VK_UP);
        verify(_subsettedJListPair, times(1)).informSourceListChanged(true);
        verify(_subsettedJListPair, times(1)).informSourceListKeyEvent(KeyEvent.VK_UP);
    }

    @Test
    public void testSourceListKeyEventDOWN()
    {
        createSubsettedJListPair();
        doNothing().when(_subsettedJListPair).informSourceListChanged(true);
        _subsettedJListPair.subsetSourceListKeyEvent(KeyEvent.VK_DOWN);
        verify(_subsettedJListPair, times(1)).informSourceListChanged(true);
        verify(_subsettedJListPair, times(1)).informSourceListKeyEvent(KeyEvent.VK_DOWN);
    }

    @Test
    public void testSourceListKeyEventOTHER()
    {
        createSubsettedJListPair();
        doNothing().when(_subsettedJListPair).informSourceListChanged(true);
        _subsettedJListPair.informSourceListKeyEvent(KeyEvent.VK_0);
        verify(_subsettedJListPair, times(0)).informSourceListChanged(true);
        verify(_subsettedJListPair, times(1)).informSourceListKeyEvent(KeyEvent.VK_0);
    }

    @Test
    public void testHasSelectedEntries()
    {
        createSubsettedJListPair();
        _subsettedJListPair.hasSelectedEntries();
        verify(_ui, times(1)).hasSelectedEntries();
    }

    private void createSubsettedJListPair()
    {
        when(_ui.getContentList()).thenReturn(_contentList);
        when(_ui.getSourceList()).thenReturn(_sourceList);
        SubsettedJListPair subsettedJListPair = new SubsettedJListPair(_subsetSourceListModel, _subsettedListModel, _ui);
        _subsettedJListPair = spy(subsettedJListPair);
        doNothing().when(_subsettedJListPair).informSourceListChanged(Mockito.anyBoolean());
        doNothing().when(_subsettedJListPair).informSourceListKeyEvent(Mockito.anyInt());
        doNothing().when(_subsettedJListPair).informContentListKeyEvent(Mockito.anyInt());
    }
}
