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

import org.apache.commons.collections.CollectionUtils;

import sturesy.core.ui.filetree.FileTreeController;

public class TreeListPair extends SubsettedListPairObservable
{

    private TreeListPairUI _ui;

    /**
     * The controller handling the file tree on the left
     */
    private FileTreeController _fileTree;

    /**
     * the list model which has the content of the right table. This list model
     * will be changed on actions on the left table.
     */
    private DefaultListModel _contentListModel;

    public TreeListPair()
    {
        _fileTree = new FileTreeController();
        _contentListModel = new DefaultListModel();

        _ui = new TreeListPairUI(_fileTree.getPanel(), _contentListModel);
        registerListeners();
    }

    public void setNewSourceTreeRoot(String path)
    {
        System.out.println("setting");
        _fileTree.setNewDirectory(path);

        _fileTree.setSelectedIndex(0);
    }

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

    private void setFirstContentListEntrySelected()
    {
        _ui.setContentListEntrySelected(0);
    }

    public JPanel getPanel()
    {
        return _ui.getPanel();
    }

    public boolean hasSelectedEntries()
    {
        return _fileTree.isNodeSelected() && _ui.isContentListItemSelected();
    }

    public String getContentListItem()
    {
        return _ui.getContentListItem();
    }

    public String getSourceListElement()
    {
        return _fileTree.getLastSelectedFile().getAbsolutePath();
    }

    public void setNewSourceListContent(String directory)
    {
        _fileTree.setNewDirectory(directory);
    }

    private void registerListeners()
    {
        _fileTree.registerListener(folder -> informSourceListChanged(true));

        _ui.getContentList().addKeyListener(new KeyAdapter()
        {
            public void keyReleased(KeyEvent keyEvent)
            {
                informContentListKeyEvent(keyEvent.getKeyCode());
            }
        });
    }

}
