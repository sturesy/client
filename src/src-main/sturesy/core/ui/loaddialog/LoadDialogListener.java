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
 * Listener for the LoadButtonBar to inform the clients on ui interactions.
 * 
 * @author jens dallmann
 * 
 */
public interface LoadDialogListener
{

    /**
     * Informs and pass the file loaded by the Load Button which loads internal
     * files subsetted by the passed lists
     * 
     * @param file
     *            the loaded file
     */
    public void internalFileLoaded(File file);

    /**
     * Informs and pass the file loaded by the External Load Button which is
     * selected from a file chooser
     * 
     * @param file
     *            the loaded file
     */
    public void externalFileLoaded(File file);

    /**
     * The SubsetSource List changed so the subsetted list may have to be
     * refilled.
     * 
     * @param newDirectory
     *            the new directory
     */
    public void subsetSourceListChanged(File newDirectory);

}
