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

import java.awt.Dimension;

import javax.swing.JComponent;

/**
 * A Component to create Gaps in Layouts
 * 
 * @author w.posdorfer
 * 
 */
public class JGap extends JComponent
{

    private static final long serialVersionUID = -232316057809130947L;

    /**
     * Creates a gap with width and height
     * 
     * @param w
     *            width
     * @param h
     *            height
     */
    public JGap(int w, int h)
    {
        Dimension min = new Dimension(w, h);
        setMinimumSize(min);
        setPreferredSize(min);
        setMaximumSize(min);
    }

    /**
     * Creates a gap with width == height
     * 
     * @param size
     */
    public JGap(int size)
    {
        this(size, size);
    }

}
