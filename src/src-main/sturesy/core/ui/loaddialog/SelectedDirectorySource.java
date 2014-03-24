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

/**
 * Source for a directory path and file
 * 
 * @author jdallman
 * 
 */
public interface SelectedDirectorySource
{
    /**
     * returns the directory absolute path.
     * 
     * @return String directory path
     */
    public String getDirectoryAbsolutePath();

    /**
     * Returns the name of a file.
     * 
     * @return String the filename
     */
    public String getFileName();
}
