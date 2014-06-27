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
package sturesy.core.backend.filter.file;

import java.io.File;
import java.io.FileFilter;

/**
 * The Lecture File Filter filters Files that end with .xml but don't end with
 * _voting.xml, and if it ends with .xml there has to be a file with the same
 * name located in the same folder that does end with _voting.xml<br>
 * <br>
 * For instance: <code>somefile.xml</code> will only be accepted by the
 * filefilter if there is a file called <code>somefile_voting.xml</code> located
 * in the same folder <br>
 * <br>
 * This Filter is primarily used to find questionssets which already have voting
 * data for analysis
 * 
 * @author w.posdorfer
 * 
 */
public class LectureFileFilter implements FileFilter
{
    private final String VOTING = "_voting.xml";

    @Override
    public boolean accept(File pathname)
    {

        boolean endsWithXML = pathname.getName().toLowerCase().endsWith(".xml");

        boolean notContainsVoting = !pathname.getName().toLowerCase().contains(VOTING);

        boolean votingFileNearbyExists = new File(pathname.getAbsolutePath().replace(".xml", VOTING)).exists();

        return endsWithXML && notContainsVoting && votingFileNearbyExists
                || (pathname.isDirectory() && pathname.listFiles(p -> p.isFile()).length < 1);
    }
}
