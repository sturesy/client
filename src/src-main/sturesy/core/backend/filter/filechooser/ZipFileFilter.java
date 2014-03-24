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
package sturesy.core.backend.filter.filechooser;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * ZIP-File FileFilter
 * 
 * @author w.posdorfer
 */
public class ZipFileFilter extends FileFilter
{

    @Override
    public boolean accept(File f)
    {
        return f.getAbsolutePath().toLowerCase().endsWith(".zip") || f.isDirectory();
    }

    @Override
    public String getDescription()
    {
        return "*.zip";
    }

}
