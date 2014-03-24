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

import java.io.File;

/**
 * Listener for the LoadButtonBar. The LoadButtonBar informs if a file has been
 * loaded.
 * 
 * @author jdallman
 * 
 */
public interface LoadButtonBarListener
{
    /**
     * Informs that a File is loaded and pass the file. The File is loaded by
     * the load button for internal files
     * 
     * @param file
     *            the loaded File
     */
    public void loadedInternalFile(File file);

    /**
     * Informs that a File is loaded and pass the file. The File loaded by the
     * external load button. The File was selected from any directory.
     * 
     * @param file
     */
    public void loadedExternalFile(File file);
}
