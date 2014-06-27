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
package sturesy.core.ui.filetree;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeSelectionModel;

import sturesy.core.AbstractObservable;

/**
 * The FileTreeController provides a mechanism for selecting folders from the
 * filesystem.
 * 
 * @author w.posdorfer
 *
 */
public class FileTreeController extends AbstractObservable<FolderSelectedObserver>
{

    private JPanel _panel;
    private JTree _fileTree;
    private FileSystemModel _fileSystemModel;

    private static final int ICONSIZE = 16;
    private static final int SCALING = Image.SCALE_FAST;

    private File _lastSelectedFile;

    /**
     * Starts the root-node at the user home directory
     */
    public FileTreeController()
    {
        this(System.getProperty("user.home"));
    }

    /**
     * Starts the root-node at given directory
     * 
     * @param directory
     *            root-node start point
     */
    public FileTreeController(String directory)
    {
        _fileSystemModel = new FileSystemModel(new File(directory));
        _fileTree = new JTree(_fileSystemModel);
        _fileTree.setEditable(true);
        _fileTree.addTreeSelectionListener(new TreeSelectionListener()
        {
            public void valueChanged(TreeSelectionEvent event)
            {
                File file = (File) _fileTree.getLastSelectedPathComponent();
                if (file != null && file.isDirectory())
                {
                    _lastSelectedFile = file;
                    for (FolderSelectedObserver fso : _listeners)
                    {
                        fso.folderHasBeenSelected(file);
                    }
                }
                else if (file == null)
                {
                    _lastSelectedFile = null;
                }
            }
        });

        JScrollPane scroll = new JScrollPane(_fileTree);

        _panel = new JPanel(new BorderLayout());
        _panel.add(scroll, BorderLayout.CENTER);

        FileTreeCellRenderer renderer = new FileTreeCellRenderer();

        renderer.setLeafIcon(renderer.getOpenIcon());

        _fileTree.setCellRenderer(renderer);
        _fileTree.setRootVisible(false);
        _fileTree.setEditable(false);
        _fileTree.setShowsRootHandles(true);
        _fileTree.setSelectionModel(new DefaultTreeSelectionModel()
        {
            {
                this.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            }
        });

    }

    public boolean isNodeSelected()
    {
        return _fileTree.getSelectionCount() != 0;
    }

    /**
     * Changes the root-node
     * 
     * @param directory
     */
    public void setNewDirectory(String directory)
    {
        _fileSystemModel = new FileSystemModel(new File(directory));
        _fileTree.setModel(_fileSystemModel);
    }

    public File getLastSelectedFile()
    {
        return _lastSelectedFile;
    }

    public JPanel getPanel()
    {
        return _panel;
    }
}