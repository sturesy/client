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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;

import javax.swing.border.LineBorder;

import sturesy.core.ui.MessageWindow;
import sturesy.items.LectureID;
import sturesy.util.Settings;
import sturesy.util.web.WebCommands2;

/**
 * Token Redemption class
 * 
 * @author w.posdorfer
 * 
 */
public class TokenRedemption
{

    private final TokenRedemptionUI _ui;

    private boolean _success = false;

    private final Collection<LectureID> _lectureIds;

    /**
     * Creates a TokenRedemption Window
     * 
     * @param lectureIds
     *            a Collection of LectureIDs where to insert the redeemed
     *            LectureID
     */
    public TokenRedemption(Collection<LectureID> lectureIds)
    {
        _lectureIds = lectureIds;
        _ui = new TokenRedemptionUI();
        addListeners();
    }

    public void showDialog(Component c)
    {
        _ui.showDialog(c);
    }

    /**
     * Was token redemption successful?
     * 
     * @return succesful token redemption
     */
    public boolean isSuccessful()
    {
        return _success;
    }

    /**
     * tries to redeem the token
     */
    private void okButtonAction()
    {
        if (_ui.getTokenfield().getText().length() != 0)
        {
            Settings settings = Settings.getInstance();
            String serverAddress = settings.getString(Settings.SERVERADDRESS);
            String result = WebCommands2.redeemToken(serverAddress, _ui.getTokenfield().getText());
            try
            {
                String name = result.split(";")[0];
                String pw = result.split(";")[1];

                if (name.length() < 30)
                {
                    _lectureIds.add(new LectureID(name, pw, serverAddress));
                    _success = true;
                }
                else
                {
                    throw new Exception();
                }
            }
            catch (Exception ex)
            {
                MessageWindow.showMessageWindowError("Token Redemption Unsuccessfull", 2000);
                _success = false;
            }
            _ui.getDialog().dispose();
        }
        else
        {
            _ui.getTokenfield().setBorder(new LineBorder(Color.RED, 2));
        }
    }

    private void cancelButtonAction()
    {
        _success = false;
        _ui.getDialog().dispose();
    }

    private void addListeners()
    {
        _ui.getTokenfield().addKeyListener(new KeyAdapter()
        {
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    okButtonAction();
                }
                else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    cancelButtonAction();
                }
            }
        });
        _ui.getOkbutton().addActionListener(e -> okButtonAction());
        _ui.getCancelButton().addActionListener(e -> cancelButtonAction());
    }

}
