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
package sturesy.core.ui.loaddialog;

/**
 * A listener for the the subsetted list pair which informs about what happens
 * on the ui interaction
 * 
 * @author jens.dallmann
 */
public interface SubsettedListPairListener
{
    /**
     * triggered when a key released on the subsetted list.
     * 
     * @param keyCode
     *            as described in the KeyEvent
     */
    public void subsettedListKeyEvent(int keyCode);

    /**
     * triggered when a key released on the subset source list
     * 
     * @param keyCode
     *            as described in the java core class KeyEvent.class
     */
    public void subsetSourceListKeyEvent(int keyCode);

    /**
     * triggered when the list selection changed and new content for the
     * subsetted list is needed
     * 
     * @param valueIsAdjusting
     */
    public void subsetSourceListChanged(boolean valueIsAdjusting);
}
