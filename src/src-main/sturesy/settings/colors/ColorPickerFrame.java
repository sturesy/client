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
package sturesy.settings.colors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import sturesy.core.Localize;
import sturesy.core.ui.ColorPick;

public class ColorPickerFrame
{

    private ColorPick _pick;
    private JDialog _dialog;

    private boolean _colorWasPicked = false;

    public ColorPickerFrame(Color c)
    {
        _pick = new ColorPick(false, c);
        _dialog = new JDialog();
        _dialog.setUndecorated(true);
        _dialog.setLayout(new BorderLayout());
        _dialog.add(_pick, BorderLayout.CENTER);

        JButton saveButton = new JButton(Localize.getString("button.save"));
        JButton cancelButton = new JButton(Localize.getString("button.cancel"));

        JPanel southpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southpanel.add(saveButton);
        southpanel.add(cancelButton);
        _dialog.add(southpanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> buttonClicked(true));
        cancelButton.addActionListener(e -> buttonClicked(false));
    }

    private void buttonClicked(boolean save)
    {
        _colorWasPicked = save;
        hideDialog();
    }

    public void showDialog(Component relativeTo)
    {
        _dialog.pack();
        _dialog.setModal(true);
        _dialog.setLocationRelativeTo(relativeTo);
        _dialog.setVisible(true);
    }

    public void hideDialog()
    {
        _dialog.setModal(false);
        _dialog.dispose();
    }

    public Color getColor()
    {
        return _pick.getColor();
    }

    public boolean colorWasPicked()
    {
        return _colorWasPicked;
    }

}
