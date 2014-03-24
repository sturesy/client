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
package sturesy.core.ui.loaddialog.ui;

import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import sturesy.core.Localize;

/**
 * UI Class for the labeled combobox Layout the combobox next to the label
 * 
 * @author jens.dallmann
 * 
 * @param <ContentItem>
 *            the type of the item added to the combobox
 */
public class LabeledComboboxUI<ContentItem>
{
    /**
     * The Combobox with items
     */
    private JComboBox _combobox;
    /**
     * the panel for combobox and labels
     */
    private JPanel _panel;

    /**
     * Initialize the Combobox, set the renderer and
     * 
     * @param ressourceKey
     * @param content
     * @param renderer
     *            the cell renderer for the combobox to show the correct string
     *            of the ContentItem
     */
    public LabeledComboboxUI(String ressourceKey, Vector<ContentItem> content, ListCellRenderer renderer)
    {
        JLabel descriptionLabel = new JLabel(Localize.getString(ressourceKey));
        _combobox = new JComboBox(content);
        _combobox.setRenderer(renderer);
        _panel = new JPanel();
        _panel.add(descriptionLabel);
        _panel.add(_combobox);
        if (content.size() > 1)
        {
            _combobox.setSelectedIndex(1);
        }
    }

    @SuppressWarnings("unchecked")
    public ContentItem getSelectedItem()
    {
        return (ContentItem) _combobox.getSelectedItem();
    }

    public JPanel getPanel()
    {
        return _panel;
    }

    /**
     * Adds the specified key listener to receive key events from this
     * component. If l is null, no exception is thrown and no action is
     * performed.
     * 
     * @param l
     *            the KeyListener
     */
    public void addKeyListener(KeyListener l)
    {
        _combobox.addKeyListener(l);
    }
}
