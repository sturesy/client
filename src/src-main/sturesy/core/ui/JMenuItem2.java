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

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

/**
 * The whole purpose of this is to save 10 characters ;-)<br>
 * instead of<br>
 * <code> JMenuItem it = new JMenuItem(sometext); it.addActionListener(a);</code>
 * <br>
 * you can write: <br>
 * <code> JMenuItem2 it = new JMenuItem2(sometext,a)</code>
 * 
 * @author w.posdorfer
 */
public final class JMenuItem2 extends JMenuItem
{
    private static final long serialVersionUID = 2362324286935476741L;

    public JMenuItem2(String name, ActionListener a)
    {
        super(name);
        addActionListener(a);
    }
}