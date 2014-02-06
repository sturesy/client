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
package sturesy.util;

import java.awt.event.MouseEvent;

import sturesy.core.Operatingsystem;

/**
 * Class for common Swing Tasks
 * 
 * @author w.posdorfer
 * 
 */
public final class SturesySwingHelper
{

    /**
     * Tests if a Right-Click was made, this can be either the usual
     * "RIGHTMOUSEBUTTON" or on Mac-Systems it is common to use CTRL+LeftClick
     * 
     * @param e
     * @return
     */
    public static boolean isRightClick(MouseEvent e)
    {
        return e.getButton() == MouseEvent.BUTTON3
                || (e.getButton() == MouseEvent.BUTTON1 && e.isControlDown() && Operatingsystem.isMac());
    }

    private SturesySwingHelper()
    {
    }
}
