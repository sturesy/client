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
package sturesy.qgen.qimport;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import sturesy.core.Localize;
import sturesy.core.backend.Loader;
import sturesy.qgen.gui.QuestionListCellRenderer;

/**
 * Graphical Component for QuestionImportController
 * 
 * @author w.posdorfer
 * 
 */
public class QuestionImportUI
{

    private JDialog _dialog;
    private JList _list;
    private JButton _loadButton;
    private JButton _cancelButton;

    /**
     * Creates a new QuestionImportUI
     */
    public QuestionImportUI()
    {
        _dialog = new JDialog();
        _dialog.setIconImage(Loader.getImageIcon(Loader.IMAGE_STURESY).getImage());

        _dialog.setLayout(new BorderLayout());

        _loadButton = new JButton(Localize.getString("button.load"));
        _cancelButton = new JButton(Localize.getString("button.cancel"));

        _list = new JList(new DefaultListModel());
        _list.setCellRenderer(new QuestionListCellRenderer(35));
        JScrollPane pane = new JScrollPane(_list);

        JLabel label = new JLabel(Localize.getString("label.select.questions"));

        _dialog.add(label, BorderLayout.NORTH);
        _dialog.add(pane, BorderLayout.CENTER);

        JPanel southpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southpanel.add(_loadButton);
        southpanel.add(_cancelButton);

        _dialog.add(southpanel, BorderLayout.SOUTH);
        _dialog.setSize(300, 350);
    }

    /**
     * Returns the JDialog of this UI
     * 
     * @return JDialog
     */
    public JDialog getDialog()
    {
        return _dialog;
    }

    /**
     * List of QuestionModel
     * 
     * @return JList&lt;QuestionModel&gt;
     */
    public JList getList()
    {
        return _list;
    }

    /**
     * Returns the LoadButton
     * 
     * @return JButton
     */
    public JButton getLoadButton()
    {
        return _loadButton;
    }

    /**
     * Returns the CancelButton
     * 
     * @return JButton
     */
    public JButton getCancelButton()
    {
        return _cancelButton;
    }

    /**
     * Returns the Underlying ListModel of the JList
     * 
     * @return DefaultListModel&lt;QuestionModel&gt;
     */
    public DefaultListModel getListModel()
    {
        return (DefaultListModel) _list.getModel();
    }

    /**
     * Shows the Dialog
     * 
     * @param relativeTo
     *            telative to what component
     * @param modal
     *            use a modal JDialog?
     */
    public void show(Component relativeTo, boolean modal)
    {
        _dialog.setLocationRelativeTo(relativeTo);
        _dialog.setModal(modal);
        _dialog.setVisible(true);
    }
}
