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

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import sturesy.core.Localize;

/**
 * UI Class for a subsetted JList Pair. Displays two JLists with content and
 * provide methods to refill the content of both. Triggers Key Listeners on both
 * lists and informs about the list selection event on the source list to inform
 * that new content in the right table is needed
 * 
 * @author jens dallmann
 * 
 */
public class SubsettedJListPairUI
{
    /**
     * The JList in which the content is which subset the second list.
     */
    private JList _sourceList;
    /**
     * The JList in which the subsetted content is shown
     */
    private JList _contentList;
    /**
     * The panel which contains the both lists.
     */
    private JPanel _panel;

    /**
     * Constructor which creates the lists with two given models and one
     * interaction object
     * 
     * @param interaction
     *            a interaction for loose coupled communication between
     *            InteractionComponent and FunctionalComponent
     * @param listModel
     *            the listmodel of the subsetSourceList
     * @param subsettedListModel
     *            the listmodel of the subsettedList
     */
    public SubsettedJListPairUI(ListModel sourceListModel, ListModel contentListModel)
    {
        _panel = new JPanel(new GridLayout(1, 2));
        JScrollPane sourceListScrollPane = createSourceList(sourceListModel, Localize.getString("label.lecture"));
        JScrollPane contentListScrollPane = createContentList(contentListModel, Localize.getString("label.questions"));
        _panel.add(sourceListScrollPane);
        _panel.add(contentListScrollPane);
        _panel.setPreferredSize(new Dimension(350, 200));
    }

    /**
     * Creates the Contentlist using the ListModel and Title
     * 
     * @param listModel
     *            Listmodel to use
     * @param title
     *            Title for TitleLayout of ScrollPane
     * @return JScrollPane
     */
    private JScrollPane createContentList(ListModel listModel, String title)
    {
        _contentList = createList(listModel, title);
        return createScrollPane(_contentList, title);
    }

    /**
     * Creates the Sourcelist using the ListModel and Title
     * 
     * @param listModel
     *            Listmodel to use
     * @param title
     *            Title for TitleLayout of ScrollPane
     * @return JScrollPane
     */
    private JScrollPane createSourceList(ListModel listModel, String title)
    {
        _sourceList = createList(listModel, title);
        return createScrollPane(_sourceList, title);
    }

    /**
     * Puts the given JList in a ScrollPane and sets the Title
     * 
     * @param list
     *            JList to place in ScrollPane
     * @param title
     *            Title for TitledLayout
     * @return JScrollPane
     */
    private JScrollPane createScrollPane(JList list, String title)
    {
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(new TitledBorder(title));
        return scrollPane;
    }

    private JList createList(ListModel listModel, String titleString)
    {
        JList list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return list;
    }

    /**
     * Returns the Source List on the left side
     * 
     * @return JList
     */
    public JList getSourceList()
    {
        return _sourceList;
    }

    /**
     * Returns the Content List on the right side
     * 
     * @return JList
     */
    public JList getContentList()
    {
        return _contentList;
    }

    /**
     * Return the selected element of the subset source list
     * 
     * @return returns the selected value
     */
    /**
     * Returns the selected element of the source list
     * 
     * @return Object
     */
    public Object getSelectedSourceListElement()
    {
        return _sourceList.getSelectedValue();
    }

    /**
     * Return the selected element of the subsetted list
     * 
     * @return returns the selected value
     */
    /**
     * Returns the selected element of the content list
     * 
     * @return Object
     */
    public Object getSelectedContentListElement()
    {
        return _contentList.getSelectedValue();
    }

    /**
     * set the selection of the source list.
     * 
     * @param index
     */
    /**
     * Selects the given index in the sourcelist
     * 
     * @param index
     *            index to select
     */
    public void setSourceListEntrySelected(int index)
    {
        _sourceList.setSelectedIndex(index);
    }

    /**
     * set the selection of the subsetted list.
     * 
     * @param index
     */
    /**
     * Selects the given index in the contentlist
     * 
     * @param index
     *            index to select
     */
    public void setContentListEntrySelected(int index)
    {
        _contentList.setSelectedIndex(index);
    }

    /**
     * Returns the panel on which the lists are shown
     * 
     * @return JPanel the panel on which the lists are shown
     */
    public JPanel getPanel()
    {
        return _panel;
    }

    /**
     * Do both the Sourcelist and the Contentlist have selected entries?
     * 
     * @return <code>true</code> if both lists has a selection, false else
     */
    public boolean hasSelectedEntries()
    {
        int sourceSelectionIndex = _sourceList.getSelectedIndex();
        int contentSelectionIndex = _contentList.getSelectedIndex();
        return sourceSelectionIndex != -1 && contentSelectionIndex != -1;
    }

    /**
     * Tells the Sourcelist to request focus
     */
    public void requestFocusSourceList()
    {
        _sourceList.requestFocus();
    }

    /**
     * Tells the Content list to request focus
     */
    public void requestFocusContentList()
    {
        _contentList.requestFocus();
    }

}
