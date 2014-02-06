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
package sturesy.test.filter.file;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import sturesy.core.backend.filter.file.LectureFileFilter;
import sturesy.tools.LoadTestFilesService;

public class TestLectureFileFilter
{

    @Test
    public void testDoNotAcceptNoXMLAtEnd()
    {
        File file = new File("");
        LectureFileFilter filter = new LectureFileFilter();
        boolean isAccepted = filter.accept(file);
        assertFalse(isAccepted);
    }

    @Test
    public void testDoNotAcceptContainsVoting()
    {
        LoadTestFilesService loader = new LoadTestFilesService();
        File testFile = loader.retrieveTestLectureVotingFile();
        LectureFileFilter filter = new LectureFileFilter();
        boolean isAccepted = filter.accept(testFile);
        assertFalse(isAccepted);
    }

    @Test
    public void testDoNotAcceptVotingFileNotExist()
    {
        LoadTestFilesService loader = new LoadTestFilesService();
        File testFile = loader.retrieveTestFileWithoutVotingNearby();
        LectureFileFilter filter = new LectureFileFilter();
        boolean isAccepted = filter.accept(testFile);
        assertFalse(isAccepted);
    }

    @Test
    public void testDoAcceptIsDirectory()
    {
        LoadTestFilesService loader = new LoadTestFilesService();
        File testfile = loader.retrieveTestDirectoryFile();
        LectureFileFilter filter = new LectureFileFilter();
        boolean isAccepted = filter.accept(testfile);
        assertTrue(isAccepted);
    }

    @Test
    public void testDoAcceptTestFile()
    {
        LoadTestFilesService loader = new LoadTestFilesService();
        File testfile = loader.retrieveTestLectureFileVotingNearby();
        LectureFileFilter filter = new LectureFileFilter();
        boolean isAccepted = filter.accept(testfile);
        assertTrue(isAccepted);
    }
}
