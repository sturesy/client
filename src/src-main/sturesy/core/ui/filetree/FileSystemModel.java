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

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * A Treemodel for Files.
 * 
 * @author w.posdorfer
 *
 */
final class FileSystemModel implements TreeModel
{
    private File _rootNode;

    private Set<TreeModelListener> _treemodellisteners = new HashSet<TreeModelListener>();

    private FileFilter _filter = pathname -> pathname.isDirectory();

    public FileSystemModel(File rootDirectory)
    {
        _rootNode = rootDirectory;
    }

    public Object getRoot()
    {
        return _rootNode;
    }

    public Object getChild(Object parent, int index)
    {
        File directory = (File) parent;
        String[] children = directory.list();
        return new File(directory, children[index]);
    }

    public int getChildCount(Object parent)
    {
        File file = (File) parent;
        if (file != null && file.isDirectory())
        {
            String[] fileList = file.list();
            if (fileList != null)
                return fileList.length;
        }
        return 0;
    }

    /**
     * Checks if the file is a directory and if it has more subdirs
     * 
     * @param f
     *            a potential directory
     * @return <code>true</code> if file has subdirs
     */
    public boolean hasSubDirs(File f)
    {
        if (f == null || f.isFile())
        {
            return false;
        }
        File[] listFile = f.listFiles(_filter);
        return listFile != null && listFile.length > 0;
    }

    public boolean isLeaf(Object node)
    {
        File file = (File) node;

        return file == null || file.isFile() || (file.isDirectory() && !hasSubDirs(file));
    }

    public int getIndexOfChild(Object parent, Object child)
    {
        File directory = (File) parent;
        File file = (File) child;
        String[] children = directory.list();
        for (int i = 0; i < children.length; i++)
        {
            if (file.getName().equals(children[i]))
            {
                return i;
            }
        }
        return -1;

    }

    public void valueForPathChanged(TreePath path, Object value)
    {
        File oldFile = (File) path.getLastPathComponent();
        String fileParentPath = oldFile.getParent();
        String newFileName = (String) value;
        File targetFile = new File(fileParentPath, newFileName);
        oldFile.renameTo(targetFile);
        File parent = new File(fileParentPath);
        int[] changedChildrenIndices = { getIndexOfChild(parent, targetFile) };
        Object[] changedChildren = { targetFile };
        notifyNodesHaveChanged(path.getParentPath(), changedChildrenIndices, changedChildren);
    }

    private void notifyNodesHaveChanged(TreePath parentPath, int[] indices, Object[] children)
    {
        TreeModelEvent event = new TreeModelEvent(this, parentPath, indices, children);
        for (TreeModelListener listener : _treemodellisteners)
        {
            listener.treeNodesChanged(event);
        }
    }

    public void addTreeModelListener(TreeModelListener listener)
    {
        _treemodellisteners.add(listener);
    }

    public void removeTreeModelListener(TreeModelListener listener)
    {
        _treemodellisteners.remove(listener);
    }
}