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
package sturesy.core.backend.filter.file;

import java.io.File;
import java.io.FileFilter;

/**
 * Filters all files ending with <code>.xml</code> and not ending with
 * <code>_voting.xml</code>
 * 
 * @author w.posdorfer
 * 
 */
public class NameXMLFileFilter implements FileFilter
{
    @Override
    public boolean accept(File pathname)
    {
        return pathname.getAbsolutePath().endsWith(".xml") && !pathname.getAbsolutePath().endsWith("_voting.xml");
    }
}
