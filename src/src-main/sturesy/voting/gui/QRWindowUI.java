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
package sturesy.voting.gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Class to display a QRCode in an undecorated JFrame
 */
public class QRWindowUI extends JFrame
{
    private static final long serialVersionUID = -4749327622536612930L;
    private JLabel _label;

    /**
     * Creates a borderless Frame displaying the ImageIcon in the Center of it,
     * at a maxiumsize of <code>ScreenSize.height-60</code>
     * 
     * @param qrCode
     *            an QRCode ImageIcon
     */
    public QRWindowUI(ImageIcon qrCode)
    {
        super();
        setUndecorated(true);

        Toolkit t = Toolkit.getDefaultToolkit();
        int h = t.getScreenSize().height - 60;

        _label = new JLabel(new ImageIcon(qrCode.getImage().getScaledInstance(h, h, Image.SCALE_FAST)));
        add(_label);

        registerListeners();

    }

    /**
     * Packs this JFrame, sets it location to center of screen and makes it
     * visible
     */
    public void showQRWindow()
    {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Register Listeners to close the QRWindow
     */
    private void registerListeners()
    {
        _label.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                setVisible(false);
            }
        });
        addFocusListener(new FocusAdapter()
        {
            public void focusLost(FocusEvent e)
            {
                setVisible(false);
            }
        });
    }
}
