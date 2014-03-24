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

/**
 * Delegation Class for SingleAnswerPanelController to get information about
 * togglestates.<br>
 * A ToggleDelegate is passed to every instantiated SingleAnswerPanelController,
 * to be able to ask its ParentController if a button selection should trigger
 * enabling or disabling of the components of the SingleAnswerPanelController.<br>
 * For more information read <a
 * href="http://en.wikipedia.org/wiki/Delegation_pattern">Delegation Pattern</a>
 * 
 * @author w.posdorfer
 * 
 */
public interface ToggleDelegate
{

    /**
     * A ToggleButton asking for permission to change its state
     * 
     * @param number
     *            the index at which this button is located
     * @param newState
     *            the new state, which the button has aquired<br>
     *            <li><code>true</code> = selected</li><li><code>false</code> =
     *            unselected</li>
     * @return if permission is granted to switch state returns
     *         <code>true</code>
     */
    public boolean amIAllowedToToggle(int number, boolean newState);
}
