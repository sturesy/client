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
package sturesy.qgen;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * FocusListener who remembers who had the focus last
 * 
 * @author w.posdorfer
 * 
 */
public class RememberingFocusListener implements FocusListener
{

    private Component _focus;

    /**
     * Returns the Component that has Focus
     * 
     * @return some Component
     */
    public Component getComponent()
    {
        return _focus;
    }

    @Override
    public void focusGained(FocusEvent e)
    {
        _focus = e.getComponent();
    }

    @Override
    public void focusLost(FocusEvent e)
    {
    }


}
