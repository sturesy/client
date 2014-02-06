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
package sturesy.settings.websettings;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import sturesy.core.Localize;
import sturesy.core.backend.Loader;

/**
 * Displays a Dialog asking for LectureID and Password to save it in the local
 * database
 * 
 * @author w.posdorfer
 * 
 */
public class NewLectureUI
{

    private JTextField _idtext;
    private JPasswordField _passtext;
    private JDialog _dialog;

    private JButton _confirmButton;
    private JButton _cancelButton;

    public NewLectureUI()
    {
        _dialog = new JDialog();
        _dialog.setLayout(new BorderLayout());
        _dialog.setTitle(Localize.getString("button.add.new.lectureid"));
        _dialog.setIconImage(Loader.getImageIcon(Loader.IMAGE_STURESY).getImage());

        JPanel centerpanel = new JPanel(new GridBagLayout());
        JPanel southpanel = new JPanel(new FlowLayout());

        _dialog.add(centerpanel, BorderLayout.CENTER);
        _dialog.add(southpanel, BorderLayout.SOUTH);

        JLabel labelID = new JLabel("ID:");
        labelID.setHorizontalAlignment(JLabel.RIGHT);
        JLabel labelpass = new JLabel(Localize.getString("label.password"));
        labelpass.setHorizontalAlignment(JLabel.RIGHT);
        _idtext = new JTextField(20);
        _passtext = new JPasswordField(20);
        centerpanel.add(labelID, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1));
        centerpanel.add(_idtext, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1));
        centerpanel.add(labelpass, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1));
        centerpanel.add(_passtext, new GridBagConstraints(1, 1, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1));

        _confirmButton = new JButton(Localize.getString("button.save"));
        _cancelButton = new JButton(Localize.getString("button.cancel"));
        southpanel.add(_confirmButton);
        southpanel.add(_cancelButton);
    }

    public JTextField getIdTextfield()
    {
        return _idtext;
    }

    public JPasswordField getPasswordTextfield()
    {
        return _passtext;
    }

    public String getIdTextFieldText()
    {
        return _idtext.getText();
    }

    public char[] getPasswortTextFieldText()
    {
        return _passtext.getPassword();
    }

    public JButton getConfirmButton()
    {
        return _confirmButton;
    }

    public JButton getCancelButton()
    {
        return _cancelButton;
    }

    public void show(Component relativeToComponent)
    {
        _dialog.pack();
        _dialog.setModal(true);
        _dialog.setLocationRelativeTo(relativeToComponent);
        _dialog.setVisible(true);
    }

    public Window getDialog()
    {
        return _dialog;
    }
}
