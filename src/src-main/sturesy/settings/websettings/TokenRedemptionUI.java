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
package sturesy.settings.websettings;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sturesy.core.Localize;

/**
 * Token Redemption UI Class
 * 
 * @author w.posdorfer
 */
public class TokenRedemptionUI
{
    private JDialog _dialog;
    private JTextField _tokenfield;

    private JButton _okbutton;
    private JButton _cancelButton;

    public TokenRedemptionUI()
    {
        _dialog = new JDialog();
        _dialog.setUndecorated(true);

        JLabel tokenlabel = new JLabel("Token:");
        _tokenfield = new JTextField(30);

        _okbutton = new JButton(Localize.getString("button.redeem"));

        _cancelButton = new JButton(Localize.getString("button.cancel"));

        _dialog.setLayout(new BorderLayout());

        JPanel northpanel = new JPanel();
        northpanel.add(tokenlabel);
        northpanel.add(_tokenfield);

        JPanel centerpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        centerpanel.add(_okbutton);
        centerpanel.add(_cancelButton);

        _dialog.add(northpanel, BorderLayout.NORTH);
        _dialog.add(centerpanel, BorderLayout.CENTER);

    }

    /**
     * @return TextField to input Token
     */
    public JTextField getTokenfield()
    {
        return _tokenfield;
    }

    /**
     * @return the Cancel Button
     */
    public JButton getCancelButton()
    {
        return _cancelButton;
    }

    /**
     * @return the OK Button
     */
    public JButton getOkbutton()
    {
        return _okbutton;
    }

    /**
     * 
     * @return the Dialog
     */
    public JDialog getDialog()
    {
        return _dialog;
    }

    /**
     * Show the dialog relative to Component c
     * 
     * @param c
     */
    public void showDialog(Component c)
    {
        _dialog.pack();
        _dialog.setModal(true);
        _dialog.setLocationRelativeTo(c);
        _dialog.setVisible(true);
    }

}
