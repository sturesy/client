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

import java.io.File;

import sturesy.core.AbstractObservable;

/**
 * provides listener handling for button bar to inform about the accepted file
 * 
 * @author jens dallmann
 */
public class LoadButtonBarObservable extends AbstractObservable<LoadButtonBarListener>
{

    /**
     * informs the listener in the list that an internal file has been loaded
     * 
     * @param File
     *            the internal file
     */
    public void informLoadListenerLoadInternalFile(File file)
    {
        for (LoadButtonBarListener listener : _listeners)
        {
            listener.loadedInternalFile(file);
        }
    }

    /**
     * informs the listener in the list that an external file has been loaded
     * 
     * @param File
     *            the external file
     */
    public void informLoadListenerLoadExternalFile(File file)
    {
        for (LoadButtonBarListener listener : _listeners)
        {
            listener.loadedExternalFile(file);
        }
    }
}
