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
package sturesy.core.ui;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import sturesy.core.Operatingsystem;

/**
 * Class that adds CTRL+W /CMD+W - closing capability to every frame.
 * 
 * @author w.posdorfer
 */
public class SFrame extends JFrame
{

    private static final long serialVersionUID = 3894079095117174687L;

    public SFrame()
    {
        registerListeners();
    }

    private void registerListeners()
    {
        InputMap inputMap = this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getRootPane().getActionMap();

        AbstractAction action = new AbstractAction()
        {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e)
            {
                WindowEvent wev = new WindowEvent(SFrame.this, WindowEvent.WINDOW_CLOSING);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
            }
        };

        int downMask = Operatingsystem.isMac() ? InputEvent.META_DOWN_MASK : InputEvent.CTRL_DOWN_MASK;

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, downMask), "dispose");
        actionMap.put("dispose", action);
    }

}
