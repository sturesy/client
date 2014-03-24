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
package sturesy.test.qgen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import sturesy.SturesyManager;
import sturesy.core.ui.loaddialog.LoadDialog;
import sturesy.items.QuestionSet;
import sturesy.qgen.QuestionLoadDialog;
import sturesy.tools.LoadTestFilesService;

@RunWith(MockitoJUnitRunner.class)
public class TestQuestionLoadDialog
{
    @Mock
    private File _loadedFile;

    private final String _expectedLoadedFileAbsolutePath = File.separator + "test" + File.separator + "text.txt";

    @Mock
    private LoadDialog _loadDialog;

    private LoadTestFilesService _testFileService;

    @Before
    public void beforeEachTest()
    {
        MockitoAnnotations.initMocks(this);
        _testFileService = new LoadTestFilesService();
        File maindirectory = _testFileService.retrieveTestLectureFileVotingNearby();

        String trimmedMainDirectoryPath = extractTrimmedPath(maindirectory);
        SturesyManager.setMainDirectory(trimmedMainDirectoryPath);
    }

    @Test
    public void testTitleLoadedFileIsNull()
    {
        QuestionLoadDialog questionLoadDialog = createQuestionLoadDialog();

        String title = questionLoadDialog.getTitle();
        assertEquals("", title);
    }

    @Test
    public void testTitleNotExternal()
    {
        when(_loadedFile.getAbsolutePath()).thenReturn(_expectedLoadedFileAbsolutePath);
        QuestionLoadDialog questionLoadDialog = createQuestionLoadDialog();
        questionLoadDialog.setLoadedFile(_loadedFile);
        String title = questionLoadDialog.getTitle();
        assertEquals(_expectedLoadedFileAbsolutePath, title);

    }

    @Test
    public void testTitleIsExternal()
    {
        when(_loadedFile.getAbsolutePath()).thenReturn(_expectedLoadedFileAbsolutePath);
        QuestionLoadDialog questionLoadDialog = createQuestionLoadDialog();
        questionLoadDialog.setLoadedFile(_loadedFile);
        String title = questionLoadDialog.getTitle();
        assertEquals(_expectedLoadedFileAbsolutePath, title);
    }

    @Test
    public void testInternalFileHasBeenLoaded()
    {
        File maindirectory = _testFileService.retrieveTestLectureFileVotingNearby();
        String trimmedMainDirectoryPath = extractTrimmedPath(maindirectory);
        SturesyManager.setMainDirectory(trimmedMainDirectoryPath);
        QuestionLoadDialog questionLoadDialog = createQuestionLoadDialog();
        questionLoadDialog.internalFileHasBeenLoaded(maindirectory);
        verifyInternalFileHasBeenLoaded(maindirectory, questionLoadDialog);
    }

    @Test
    public void testExternalFileHasBeenLoadedWithFile()
    {
        File testFile = _testFileService.retrieveTestLectureFileVotingNearby();
        QuestionLoadDialog questionLoadDialog = createQuestionLoadDialog();
        questionLoadDialog.externalFileHasBeenLoaded(testFile);
        verifyExternalFileHasBeenLoaded(questionLoadDialog, testFile);
    }

    @Test
    public void testExternalFileHasBeenLoadedWithNull()
    {
        QuestionLoadDialog questionLoadDialog = createQuestionLoadDialog();
        verifyExternalFileHasBeenLoaded(questionLoadDialog, null);

    }

    @Test
    public void testShowDialog()
    {
        QuestionLoadDialog questionLoadDialog = createQuestionLoadDialog();
        questionLoadDialog.show();
        verify(_loadDialog, times(1)).show();

    }

    private QuestionLoadDialog createQuestionLoadDialog()
    {
        return new QuestionLoadDialog(_loadDialog,
                SturesyManager.getLecturesDirectory());
    }

    private void verifyExternalFileHasBeenLoaded(QuestionLoadDialog questionLoadDialog, File testFile)
    {
        if (testFile != null)
        {
            QuestionSet loadedQuestionSet = questionLoadDialog.getLoadedQuestionSet();
            assertTrue(questionLoadDialog.isExternal());
            assertTrue(loadedQuestionSet.size() == 1);
            assertSame(testFile, questionLoadDialog.getLoadedFile());
        }
        else
        {
            assertFalse(questionLoadDialog.isExternal());
        }
    }

    private void verifyInternalFileHasBeenLoaded(File maindirectory, QuestionLoadDialog questionLoadDialog)
    {
        assertEquals(new File(maindirectory.getAbsolutePath()), questionLoadDialog.getLoadedFile());
        QuestionSet loadedQuestionSet = questionLoadDialog.getLoadedQuestionSet();
        assertFalse(questionLoadDialog.isExternal());
        assertTrue(1 == loadedQuestionSet.size());
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
