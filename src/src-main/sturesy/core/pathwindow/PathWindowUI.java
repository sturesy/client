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
package sturesy.core.pathwindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sturesy.core.Localize;
import sturesy.core.backend.Loader;
import sturesy.core.ui.HTMLLabel;
import sturesy.core.ui.JGap;

/**
 * The UI for the PathWindow<br>
 * it consists of a message a textfield and a select-button
 * 
 * @author w.posdorfer
 * 
 */
public class PathWindowUI
{

    private JDialog _dialog;
    private JTextField _textfield;
    private JButton _chooseButton;
    private JButton _okButton;
    private JButton _cancelButton;
    private JLabel _errorLabel;

    /**
     * Creates a new UI
     */
    public PathWindowUI()
    {
        _dialog = new JDialog();
        _dialog.setTitle("StuReSy");
        _dialog.setLayout(new GridBagLayout());

        JLabel longtext = new HTMLLabel(Localize.getString("label.provide.maindir"));

        JLabel homedirLabel = new JLabel(Localize.getString("label.maindir"));
        _textfield = new JTextField("", 10);
        _chooseButton = new JButton(Localize.getString("button.browse"));

        _errorLabel = new JLabel(" ");
        _errorLabel.setHorizontalAlignment(JLabel.CENTER);
        _errorLabel.setForeground(Color.RED);
        _errorLabel.setFont(_errorLabel.getFont().deriveFont(Font.BOLD));

        JPanel center = new JPanel(new BorderLayout());
        center.add(homedirLabel, BorderLayout.WEST);
        center.add(_textfield, BorderLayout.CENTER);
        center.add(_chooseButton, BorderLayout.EAST);

        _okButton = new JButton(Localize.getString("button.apply"));
        _cancelButton = new JButton(Localize.getString("button.cancel"));

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(_cancelButton);
        south.add(_okButton);

        Insets insets = new Insets(0, 0, 0, 0);
        _dialog.add(longtext, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, insets, 0, 0));

        _dialog.add(center, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, insets, 0, 0));

        _dialog.add(_errorLabel, new GridBagConstraints(1, 2, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, insets, 0, 0));

        _dialog.add(south, new GridBagConstraints(1, 3, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, insets, 0, 0));

        _dialog.add(new JLabel(Loader.getImageIcon(Loader.IMAGE_STURESY)), new GridBagConstraints(0, 0, 1, 4, 1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0));
        _dialog.add(new JGap(20), new GridBagConstraints(2, 0, 1, 4, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, insets, 0, 0));

        _dialog.pack();

        _dialog.setSize(_dialog.getWidth(), _dialog.getHeight() + 40);

    }

    /**
     * Show this dialog
     * 
     * @param relativeTo
     *            to this component
     */
    public void showDialog(Component relativeTo)
    {
        _dialog.setLocationRelativeTo(relativeTo);
        _dialog.setModal(true);
        _dialog.setVisible(true);
    }

    /**
     * Hide this dialog
     */
    public void hideDialog()
    {
        _dialog.setModal(false);
        _dialog.dispose();
    }

    public JButton getChooseButton()
    {
        return _chooseButton;
    }

    public JButton getCancelButton()
    {
        return _cancelButton;
    }

    public JButton getOkButton()
    {
        return _okButton;
    }

    public JTextField getTextField()
    {
        return _textfield;
    }

    public JDialog getDialog()
    {
        return _dialog;
    }

    public JLabel getErrorLabel()
    {
        return _errorLabel;
    }
}
