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
package sturesy.tools;

import java.io.File;

import sturesy.core.backend.Loader;

public class LoadTestFilesService
{
    public File retrieveTestLectureFileVotingNearby()
    {

        String path = buildQuestionSetPath("SE1", "Test.xml");
        File maindirectory = Loader.getFile(path);
        return maindirectory;
    }

    public File retrieveTestLectureVotingFile()
    {

        String path = buildQuestionSetPath("SE1", "Test_voting.xml");

        File maindirectory = Loader.getFile(path);
        return maindirectory;
    }

    public File retrieveVisualQuestionPanelLecture()
    {

        String path = buildQuestionSetPath("SE1", "VisualQuestionPanelLecture.xml");
        File maindirectory = Loader.getFile(path);
        return maindirectory;
    }

    public File retrieveTestLectureNoEntriesInVotingFile()
    {

        String path = buildQuestionSetPath("SE1", "TestNoVotingEntries.xml");
        File maindirectory = Loader.getFile(path);
        return maindirectory;
    }

    public File retrieveTestFileWithoutVotingNearby()
    {

        String path = buildQuestionSetPath("SE1", "Test1.xml");
        File maindirectory = Loader.getFile(path);
        return maindirectory;
    }

    public File retrieveTestLidXML()
    {

        File lidxml = Loader.getFile("test/lid-test.xml");
        return lidxml;
    }

    public File retrieveOldTestLidXML()
    {

        File lidxml = Loader.getFile("test/lid-old-test.xml");
        return lidxml;
    }

    public File retrieveTestDirectoryFile()
    {

        File maindirectory = Loader.getFile("test/");
        return maindirectory;
    }

    public File retrieveCSVFile()
    {

        File pathToCSVFile = Loader.getFile("test/content_free_csv.csv");
        return pathToCSVFile;
    }

    public File getLectureIdFolder()
    {
        return new File("test" + File.separator + "lectures");
    }

    public File retrieveNotACSVFile()
    {

        File pathToNotACSVFile = Loader.getFile("test/not-a-csv.txt");
        return pathToNotACSVFile;
    }

    private String buildQuestionSetPath(String lecture, String questionSetFileName)
    {
        return buildLecturePath(lecture) + File.separator + questionSetFileName;
    }

    private String buildLecturePath(String lecture)
    {
        return "test" + File.separator + "lectures" + File.separator + lecture;
    }
}
