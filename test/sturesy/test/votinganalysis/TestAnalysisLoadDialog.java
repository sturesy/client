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
package sturesy.test.votinganalysis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import sturesy.core.ui.loaddialog.LoadDialog;
import sturesy.items.QuestionSet;
import sturesy.items.Vote;
import sturesy.tools.LoadTestFilesService;
import sturesy.votinganalysis.AnalysisLoadDialog;

@RunWith(MockitoJUnitRunner.class)
public class TestAnalysisLoadDialog
{
    private LoadTestFilesService _testFileService;
    @Mock
    private LoadDialog _loadDialog;

    @Before
    public void beforeEachTest()
    {
        MockitoAnnotations.initMocks(this);
        _testFileService = new LoadTestFilesService();
    }

    @Test
    public void testInternalFileHasBeenLoaded()
    {
        File lectureDirectory = createlectureFileDirectory();

        File file = _testFileService.retrieveTestLectureFileVotingNearby();
        AnalysisLoadDialog dialog = new AnalysisLoadDialog(_loadDialog, lectureDirectory);
        dialog.internalFileHasBeenLoaded(file);

        verify(_loadDialog, times(1)).closeDialog();
        assertFalse(dialog.isExternal());
        QuestionSet loadedQuestionSet = dialog.getLoadedQuestionSet();
        Map<Integer, Set<Vote>> votes = dialog.getVotes();
        assertTrue(1 <= loadedQuestionSet.size());
        assertTrue(1 <= votes.size());
    }

    @Test
    public void testInternalFileHasBeenLoadedWithNoEntries()
    {
        File lectureFile = createlectureFileDirectory();
        File file = _testFileService.retrieveTestLectureNoEntriesInVotingFile();
        AnalysisLoadDialog dialog = new AnalysisLoadDialog(_loadDialog, lectureFile);
        dialog.internalFileHasBeenLoaded(file);

        verify(_loadDialog, times(1)).showErrorMessage("error.voting.file.no.result");
        assertFalse(dialog.isExternal());
        QuestionSet loadedQuestionSet = dialog.getLoadedQuestionSet();
        Map<Integer, Set<Vote>> votes = dialog.getVotes();
        assertNull(loadedQuestionSet);
        assertTrue(0 == votes.size());
    }

    @Test
    public void testExternalFileHasBeenLoadedWithNoEntries()
    {
        File file = _testFileService.retrieveTestLectureNoEntriesInVotingFile();
        File lectureFileDirectory = createlectureFileDirectory();
        AnalysisLoadDialog dialog = new AnalysisLoadDialog(_loadDialog, lectureFileDirectory);
        dialog.externalFileHasBeenLoaded(file);
        verify(_loadDialog, times(1)).showErrorMessage("error.voting.file.no.result");
        assertFalse(dialog.isExternal());
        QuestionSet loadedQuestionSet = dialog.getLoadedQuestionSet();
        Map<Integer, Set<Vote>> votes = dialog.getVotes();
        assertTrue(0 == votes.size());
        assertNull(loadedQuestionSet);
    }

    @Test
    public void testExternalFileHasBeenLoaded()
    {
        File lectureFileDirectory = createlectureFileDirectory();
        File file = _testFileService.retrieveTestLectureFileVotingNearby();
        AnalysisLoadDialog dialog = new AnalysisLoadDialog(_loadDialog, lectureFileDirectory);
        dialog.externalFileHasBeenLoaded(file);
        verify(_loadDialog, times(1)).closeDialog();
        assertTrue(dialog.isExternal());
        QuestionSet loadedQuestionSet = dialog.getLoadedQuestionSet();
        Map<Integer, Set<Vote>> votes = dialog.getVotes();
        assertTrue(1 <= loadedQuestionSet.size());
        assertTrue(1 <= votes.size());
    }

    @Test
    public void testExternalFileHasBeenLoadedWithFileNull()
    {
        File lectureFileDirectory = createlectureFileDirectory();

        AnalysisLoadDialog dialog = new AnalysisLoadDialog(_loadDialog, lectureFileDirectory);
        dialog.externalFileHasBeenLoaded(null);
        verify(_loadDialog, times(0)).closeDialog();
        assertFalse(dialog.isExternal());
        QuestionSet loadedQuestionSet = dialog.getLoadedQuestionSet();
        Map<Integer, Set<Vote>> votes = dialog.getVotes();
        assertNull(votes);
        assertNull(loadedQuestionSet);
    }

    private File createlectureFileDirectory()
    {
        File anyPathInMaindirectory = _testFileService.retrieveTestLectureFileVotingNearby();
        String extractTrimmedPath = extractTrimmedPath(anyPathInMaindirectory);
        return new File(extractTrimmedPath);
    }

    /**
     * replaces <code>lectures/SE1/Test.xml</code> with <code>nothing</code> to
     * get a fully qualified path to a usable maindirectory
     * 
     * @param maindirectory
     *            file to trim
     * @return trimmed path to maindirectory
     */
    private String extractTrimmedPath(File maindirectory)
    {
        String pathMaindirectory = maindirectory.getAbsolutePath();
        String trimmedMainDirectoryPath = pathMaindirectory.replace("lectures" + File.separator + "SE1"
                + File.separator + "Test.xml", "");
        return trimmedMainDirectoryPath;
    }
}
