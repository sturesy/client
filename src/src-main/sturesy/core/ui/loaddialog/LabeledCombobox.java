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

import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import sturesy.core.ui.loaddialog.ui.LabeledComboboxUI;

/**
 * A Combobox shown with a label next to it.
 * 
 * @author jens.dallmann
 * 
 * @param <ContentItem>
 *            the type of the content which is added to the combobox
 */
public class LabeledCombobox<ContentItem>
{
    /**
     * the ui class for the labeled combobox
     */
    private LabeledComboboxUI<ContentItem> _ui;

    /**
     * 
     * 
     * @param ressourceKey
     *            the text of the label next to the combobox
     * @param content
     *            the content of the combobox
     * @param renderer
     *            the renderer for the content to get a string to display in
     *            cell
     */
    public LabeledCombobox(String ressourceKey, Vector<ContentItem> content, ListCellRenderer renderer)
    {
        _ui = new LabeledComboboxUI<ContentItem>(ressourceKey, content, renderer);
    }

    /**
     * return the selected item in the combobox
     * 
     * @return content item
     */
    public ContentItem getSelectedItem()
    {
        return _ui.getSelectedItem();
    }

    /**
     * return the jpanel which combines label and combobox
     * 
     * @return JPanel
     */
    public JPanel getPanel()
    {
        return _ui.getPanel();
    }

    /**
     * Adds the specified key listener to receive key events from the combobox.
     * If l is null, no exception is thrown and no action is performed.
     * 
     * @param l
     *            the KeyListener
     */
    public void addKeyListener(KeyListener l)
    {
        _ui.addKeyListener(l);
    }
}
