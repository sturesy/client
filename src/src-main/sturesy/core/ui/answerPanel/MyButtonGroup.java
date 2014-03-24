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
package sturesy.core.ui.answerPanel;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;

/**
 * ButtonGroup extension which provides a method for the selected index
 * 
 * @author w.posdorfer
 */
public class MyButtonGroup extends ButtonGroup
{
    private static final long serialVersionUID = -577815423056320460L;

    /**
     * Returns the selected index
     * 
     * @return an integer between 0 and sizeOfElements or -1 if no selection
     */
    public int getSelectedIndex()
    {
        ButtonModel selected = getSelection();
        for (int i = 0; i < buttons.size(); i++)
        {
            if (selected == buttons.elementAt(i).getModel())
            {
                return i;
            }
        }
        return -1;
    }
}
