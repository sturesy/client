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
package sturesy.core.error;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
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
import sturesy.core.ui.JGap;

/**
 * Graphical Unit of the PluginErrorController
 * 
 * @author w.posdorfer
 * 
 */
public class ErrorUI
{

    private JDialog _dialog;
    private JButton _okButton;

    private JList _pluginsList;

    /**
     * Constructs the UI
     */
    public ErrorUI()
    {
        _dialog = new JDialog();
        _dialog.setTitle(Localize.getString("error.error"));
        _dialog.setIconImage(Loader.getImageIcon(Loader.IMAGE_STURESY).getImage());
        _dialog.setLayout(new BorderLayout());
        JLabel errorImage = new JLabel(Loader.getImageIcon(Loader.IMAGE_STURESY_ERROR));
        _dialog.add(errorImage, BorderLayout.NORTH);

        _okButton = new JButton("OK");

        JPanel okpanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        okpanel.add(_okButton);
        _dialog.add(okpanel, BorderLayout.SOUTH);

        _pluginsList = new JList(new DefaultListModel());
        _pluginsList.setCellRenderer(new ErrorListCellRenderer());
        JScrollPane pane = new JScrollPane(_pluginsList);
        pane.setSize(415, 350);
        _dialog.add(pane, BorderLayout.CENTER);

        _dialog.add(new JGap(20, 1), BorderLayout.WEST);
        _dialog.add(new JGap(20, 1), BorderLayout.EAST);

        _dialog.setSize(430, 350);
    }

    /**
     * Shows the modal Dialog
     */
    public void showDialog()
    {
        _dialog.setModalityType(ModalityType.APPLICATION_MODAL);
        _dialog.setLocationRelativeTo(null);
        _dialog.setVisible(true);
    }

    /**
     * Hides the Dialog
     */
    public void hideDialog()
    {
        _dialog.setVisible(false);
    }

    /**
     * Returns the JDialog
     */
    public JDialog getDialog()
    {
        return _dialog;
    }

    /**
     * Returns the OK-Button at the bottom
     */
    public JButton getOkButton()
    {
        return _okButton;
    }

    /**
     * List of Pair&lt;String,String&gt;
     * 
     * @return returns the List holding the Plugin errors and descriptions
     */
    public JList getPluginsList()
    {
        return _pluginsList;
    }
}
