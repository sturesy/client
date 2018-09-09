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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.collections4.CollectionUtils;

import sturesy.core.ui.loaddialog.ui.SubsettedJListPairUI;

/**
 * Subsetted List Pair is a pair of two lists. The Content of the right list is
 * dependent from the content of the left list. Whenever the left list changes
 * the client has to provide a new content for the list on the right.
 * 
 * @author jens.dallmann
 * 
 */
public class SubsettedJListPair extends SubsettedListPairObservable
{
    /**
     * the ui for the two lists
     */
    private SubsettedJListPairUI _ui;
    /**
     * the list model which has the content of the left table which is the
     * trigger for the refill of the right table.
     */
    private DefaultListModel _sourceListModel;
    /**
     * the list model which has the content of the right table. This list model
     * will be changed on actions on the left table.
     */
    private DefaultListModel _contentListModel;

    /**
     * Initialize the SubsettedJListPair with two new DefaultListModels which
     * are passed to the UI
     */
    public SubsettedJListPair()
    {
        _sourceListModel = new DefaultListModel();
        _contentListModel = new DefaultListModel();
        _ui = new SubsettedJListPairUI(_sourceListModel, _contentListModel);
        registerListeners();
    }

    /**
     * Initialize the SubsettedJListPair with the two given ListModel and the ui
     * 
     * @param sourceListModel
     * @param subettedListModel
     * @param ui
     */
    public SubsettedJListPair(DefaultListModel sourceListModel, DefaultListModel contentListModel,
            SubsettedJListPairUI ui)
    {
        _sourceListModel = sourceListModel;
        _contentListModel = contentListModel;
        _ui = ui;
    }

    /**
     * handles the new content for the subset source list. If null or empty list
     * is passed the model will be cleared but not refilled.
     * 
     * @param newContent
     *            the new list content
     */
    public void setNewSourceListContent(List<String> newContent)
    {
        _sourceListModel.clear();
        if (CollectionUtils.isNotEmpty(newContent))
        {
            for (String newElement : newContent)
            {
                _sourceListModel.addElement(newElement);
            }
            setFirstSourceListEntrySelected();
        }
    }

    /**
     * handles the new content for the subsetted list. If null or empty list is
     * passed the model will be cleared but not refilled.
     * 
     * @param newContent
     *            the new list content
     */
    public void setNewContentListContent(List<String> newContent)
    {
        _contentListModel.clear();
        if (CollectionUtils.isNotEmpty(newContent))
        {
            for (String newElement : newContent)
            {
                _contentListModel.addElement(newElement);
            }

            setFirstContentListEntrySelected();
        }
    }

    private void setFirstSourceListEntrySelected()
    {
        _ui.setSourceListEntrySelected(0);
    }

    private void setFirstContentListEntrySelected()
    {
        _ui.setContentListEntrySelected(0);
    }

    /**
     * Return the JPanel on which the two lists are drawn
     * 
     * @return JPanel the panel of the two lists
     */
    public JPanel getSubsettedListPairUI()
    {
        return _ui.getPanel();
    }

    /**
     * Returns the selected subsetted list item
     * 
     * @return String the entry name.
     */
    public String getContentListItem()
    {
        return _ui.getSelectedContentListElement().toString();
    }

    /**
     * Return the selected subset source list item
     * 
     * @return String the entry name
     */
    public String getSourceListElement()
    {
        return _ui.getSelectedSourceListElement().toString();
    }

    public void subsetSourceListKeyEvent(int keyCode)
    {
        if (isKeyUpOrDown(keyCode))
        {
            informSourceListChanged(true);
        }
        else if (keyCode == KeyEvent.VK_RIGHT)
        {
            _ui.requestFocusContentList();
        }
        informSourceListKeyEvent(keyCode);
    }

    private boolean isKeyUpOrDown(int keyCode)
    {
        return keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN;
    }

    public void subsetContentListKeyEvent(int keyCode)
    {
        if (keyCode == KeyEvent.VK_LEFT)
        {
            _ui.requestFocusSourceList();
        }
        informContentListKeyEvent(keyCode);
    }

    /**
     * returns if in both lists one entry is selected
     * 
     * @return true if in both list one entry is selected false else
     */
    public boolean hasSelectedEntries()
    {
        return _ui.hasSelectedEntries();
    }

    private void registerListeners()
    {
        _ui.getSourceList().addKeyListener(new KeyAdapter()
        {
            public void keyReleased(KeyEvent keyEvent)
            {
                subsetSourceListKeyEvent(keyEvent.getKeyCode());
            }
        });
        _ui.getContentList().addKeyListener(new KeyAdapter()
        {
            public void keyReleased(KeyEvent keyEvent)
            {
                subsetContentListKeyEvent(keyEvent.getKeyCode());
            }
        });
        _ui.getSourceList().addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent evnt)
            {
                informSourceListChanged(evnt.getValueIsAdjusting());
            }
        });
    }
}
