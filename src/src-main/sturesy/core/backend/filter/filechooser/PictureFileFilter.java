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
 * Filters Files that are of type GIF, PNG or JPG
 * 
 * @author w.posdorfer
 * 
 */
public class PictureFileFilter extends FileFilter
{

    @Override
    public boolean accept(File f)
    {
        String filetype = extractFileType(f);
        boolean isGif = filetype.equals("gif");
        boolean isPng = filetype.equals("png");
        boolean isJpg = filetype.equals("jpg") || filetype.equals("jpeg");
        boolean fileIsDirectory = f.isDirectory();
        return isGif || isPng || isJpg || fileIsDirectory;
    }

    /**
     * returns the filetype of the file
     * 
     * @param file
     *            the file to check
     * @return characters after the "."
     */
    private String extractFileType(File file)
    {
        int dot = file.getName().lastIndexOf(".");
        String type = file.getName().substring(dot + 1);
        return type.toLowerCase();
    }

    @Override
    public String getDescription()
    {
        return "*.JPG *.PNG *.GIF";
    }
}
