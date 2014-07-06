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
package sturesy.core;

import javax.swing.JFrame;

import sturesy.core.ui.SFrame;

/**
 * Abstract class for UI that is a JFrame
 * 
 * @author w.posdorfer
 *
 */
public abstract class FrameView
{

    protected JFrame _frame;

    /**
     * Creates a View with a JFrame
     */
    public FrameView()
    {
        this(false);
    }

    /**
     * Creates a View that is a {@link JFrame} or an {@link SFrame}
     * 
     * @param frameShouldCloseOnCtrlW
     *            if <code>true</code> frame will be an {@link SFrame}
     */
    public FrameView(boolean frameShouldCloseOnCtrlW)
    {
        if (frameShouldCloseOnCtrlW)
        {
            _frame = new SFrame();
        }
        else
        {
            _frame = new JFrame();
        }
    }

    /**
     * Returns this Frame
     * 
     * @return JFrame
     */
    public JFrame getFrame()
    {
        return _frame;
    }
}
