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
package sturesy.core.ui.loaddialog;

import sturesy.core.AbstractObservable;

public class SubsettedListPairObservable extends AbstractObservable<SubsettedListPairListener>
{

    public void informContentListKeyEvent(int keyCode)
    {
        for (SubsettedListPairListener listener : _listeners)
        {
            listener.subsettedListKeyEvent(keyCode);
        }
    }

    public void informSourceListKeyEvent(int keyCode)
    {
        for (SubsettedListPairListener listener : _listeners)
        {
            listener.subsetSourceListKeyEvent(keyCode);
        }
    }

    public void informSourceListChanged(boolean valueIsAdjusting)
    {
        for (SubsettedListPairListener listener : _listeners)
        {
            listener.subsetSourceListChanged(valueIsAdjusting);
        }
    }
}
