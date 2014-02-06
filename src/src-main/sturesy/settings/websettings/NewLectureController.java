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

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Collection;

import javax.swing.JOptionPane;

import sturesy.core.Localize;
import sturesy.core.ui.MessageWindow;
import sturesy.items.LectureID;
import sturesy.util.Settings;

public class NewLectureController
{

    private NewLectureUI _ui;
    private Collection<LectureID> _lectureIDs;
    private Settings _settings;

    public NewLectureController(Collection<LectureID> lectureIds)
    {
        _settings = Settings.getInstance();
        _ui = new NewLectureUI();
        _lectureIDs = lectureIds;
        registerListeners();
    }

    public void show(Component relativeToComponent)
    {
        _ui.show(relativeToComponent);
    }

    private void cancelAction()
    {
        closeWindow();
    }

    /**
     * Closes this Dialog
     */
    private void closeWindow()
    {
        WindowEvent wev = new WindowEvent(_ui.getDialog(), WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }

    /**
     * Stores the Password in the Settings
     */
    private void storePassword()
    {
        String password = new String(_ui.getPasswortTextFieldText());
        String idtext = _ui.getIdTextFieldText();
        String serveradress = _settings.getString(Settings.SERVERADDRESS);

        LectureID toadd = new LectureID(idtext, password, serveradress);

        LectureID duplicate = findDuplicateFor(toadd);

        if (duplicate != null)
        {

            int result = JOptionPane.showConfirmDialog(_ui.getDialog(),
                    Localize.getString("message.websettings.duplicate", idtext),
                    Localize.getString("label.lecture.duplicate.id"), JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION)
            {
                _lectureIDs.remove(duplicate);
                _lectureIDs.add(toadd);
            }
        }
        else
        {
            _lectureIDs.add(toadd);
        }
    }

    private LectureID findDuplicateFor(LectureID tofind)
    {
        for (LectureID id : _lectureIDs)
        {
            if (id.getLectureID().equals(tofind.getLectureID()) && id.getHost().equals(tofind.getHost()))
            {
                // LectureID == LectureID
                // Host == Host
                return id;
            }
        }
        return null;
    }

    private void confirmAction()
    {

        char[] passwordTextFieldText = _ui.getPasswortTextFieldText();
        String password = new String(passwordTextFieldText);
        String id = _ui.getIdTextFieldText();
        if (id.length() != 0 && password.length() != 0)
        {

            String host = _settings.getString(Settings.SERVERADDRESS);
            if (host == null || !host.matches("https?://.*"))
            {
                MessageWindow.showMessageWindowError(Localize.getString("error.lectureid.no.host"), 1500);
                return;
            }

            storePassword();
            closeWindow();

        }
        else
        { // Password or ID empty
            if (id.length() == 0)
            {
                MessageWindow.showMessageWindowError(Localize.getString("error.lectureid.empty"), 1500);
            }
            else
            {
                MessageWindow.showMessageWindowError(Localize.getString("error.password.empty"), 1500);
            }
        }
    }

    public void registerListeners()
    {
        _ui.getConfirmButton().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                confirmAction();
            }
        });
        _ui.getCancelButton().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                cancelAction();
            }
        });

        KeyAdapter enterKeyAdapter = new KeyAdapter()
        {
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    confirmAction();
                }
                else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    cancelAction();
                }
            }
        };
        
        _ui.getIdTextfield().addKeyListener(enterKeyAdapter);
        _ui.getPasswordTextfield().addKeyListener(enterKeyAdapter);
    }
}
