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
package sturesy.core.ui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JWindow;

/**
 * Displays a JWindow in the Center, as a sort of Notification Area
 * 
 * @author w.posdorfer
 * 
 */
public class MessageWindow
{

    /**
     * Show an error Message for given duration with RED text
     * 
     * @param text
     * @param duration
     *            in milliseconds
     */
    public static void showMessageWindowError(String text, int duration)
    {
        showMessageWindow(text, duration, Color.RED);
    }

    /**
     * Show a success Message for fiven duration with Green text
     * 
     * @param text
     * @param duration
     *            in milliseconds
     */
    public static void showMessageWindowSuccess(String text, int duration)
    {
        showMessageWindow(text, duration, new Color(0, 100, 0));
    }

    /**
     * Shows a MessageWindow with specified Text in specified Color for
     * specified duration, the Window closes automatically after duration or by
     * clicking it
     * 
     * @param text
     * @param duration
     *            in milliseconds
     * @param color
     *            some color or <code>null</code> for Color.BLACK
     */
    public static void showMessageWindow(String text, int duration, Color color)
    {

        if (color == null)
        {
            color = Color.BLACK;
        }
        final JWindow frame = new JWindow();

        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(42.0f));
        label.setForeground(color);
        frame.setAlwaysOnTop(true);
        frame.add(label);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        label.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                frame.dispose();
            }
        });

        TimerTask timertask = new TimerTask()
        {
            public void run()
            {
                frame.dispose();
            }
        };

        Timer timer = new Timer();
        timer.schedule(timertask, duration);
    }
}
