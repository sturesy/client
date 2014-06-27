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

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import sturesy.core.Localize;
import sturesy.core.PanelView;

public class TreeListPairUI extends PanelView
{

    private JList _contentList;

    public TreeListPairUI(JPanel treepanel, DefaultListModel contentModel)
    {
        _panel = new JPanel(new GridLayout(1, 2));
        _panel.add(treepanel);
        _panel.add(createContentList(contentModel, Localize.getString("label.questions")));
        _panel.setPreferredSize(new Dimension(350, 200));
    }

    private JScrollPane createContentList(ListModel listModel, String title)
    {
        _contentList = new JList(listModel);
        _contentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(_contentList);
        scrollPane.setBorder(new TitledBorder(title));

        return scrollPane;
    }

    public void setContentListEntrySelected(int index)
    {
        _contentList.setSelectedIndex(index);
    }

    /**
     * Tells the Sourcelist to request focus
     */
    public void requestFocusSourceList()
    {
        _panel.requestFocus();
    }

    /**
     * Tells the Content list to request focus
     */
    public void requestFocusContentList()
    {
        _contentList.requestFocus();
    }

    public boolean isContentListItemSelected()
    {
        return _contentList.getSelectedIndex() != -1;
    }

    public String getContentListItem()
    {
        return (String) _contentList.getSelectedValue();
    }

    public JList getContentList()
    {
        return _contentList;
    }

}
