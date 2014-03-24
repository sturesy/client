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
package sturesy.test.voting;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import sturesy.core.backend.services.crud.QuestionCRUDService;
import sturesy.items.QuestionModel;
import sturesy.items.QuestionSet;
import sturesy.tools.LoadTestFilesService;
import sturesy.tools.QuestionSetBuilder;
import sturesy.voting.VisualQuestionPanel;
import sturesy.voting.gui.VisualQuestionPanelUI;

@RunWith(MockitoJUnitRunner.class)
public class TestVisualQuestionPanel
{
    @Mock
    private VisualQuestionPanelUI _ui;
    @Mock
    private QuestionCRUDService _questionWriter;

    @Test
    public void testConstructor()
    {
        QuestionSet questionSet = retrieveQuestionSet();
        QuestionModel questionModel = questionSet.getIndex(0);
        String lectureFilePath = retrieveLectureFilePath();
        VisualQuestionPanel panel = new VisualQuestionPanel(_ui, _questionWriter, questionModel, lectureFilePath,
                questionSet);
        String questionText = questionModel.getQuestion();
        List<String> answers = questionModel.getAnswers();
        float questionFont = questionModel.getQuestionFont();
        float answersFont = questionModel.getAnswerFont();
        verifyConstructorCall(panel, questionText, answers, questionFont, answersFont);
    }

    private void verifyConstructorCall(VisualQuestionPanel panel, String question, List<String> answers,
            float questionFont, float answerFont)
    {
        verify(_ui, times(1)).clearPanel();
        verify(_ui, times(1)).addQuestionLabel(question, questionFont, null);
        verify(_ui, times(1)).addAnswers(answers, answerFont, null);
    }

    @Test
    public void testIncreaseQuestionSize()
    {
        QuestionSet questionSet = retrieveQuestionSet();
        QuestionModel questionModel = questionSet.getIndex(0);
        String lectureFilePath = retrieveLectureFilePath();
        VisualQuestionPanel panel = new VisualQuestionPanel(_ui, _questionWriter, questionModel, lectureFilePath,
                questionSet);
        panel.increaseQuestionSize();
        float newFontSize = questionModel.getQuestionFont();
        assertTrue(25 == newFontSize);
    }

    @Test
    public void testDecreaseQuestionSize()
    {
        QuestionSet questionSet = retrieveQuestionSet();
        QuestionModel questionModel = questionSet.getIndex(0);
        String lectureFilePath = retrieveLectureFilePath();
        VisualQuestionPanel panel = new VisualQuestionPanel(_ui, _questionWriter, questionModel, lectureFilePath,
                questionSet);
        panel.decreaseQuestionSize();
        float newFontSize = questionModel.getQuestionFont();
        assertTrue(15 == newFontSize);
    }

    @Test
    public void testIncreaseAnswerSize()
    {
        QuestionSet questionSet = retrieveQuestionSet();
        QuestionModel questionModel = questionSet.getIndex(0);
        String lectureFilePath = retrieveLectureFilePath();
        VisualQuestionPanel panel = new VisualQuestionPanel(_ui, _questionWriter, questionModel, lectureFilePath,
                questionSet);
        panel.increaseAnswerSize();
        float newFontSize = questionModel.getAnswerFont();
        assertTrue(23 == newFontSize);
    }

    @Test
    public void testDecreaseAnswerSize()
    {
        QuestionSet questionSet = retrieveQuestionSet();
        QuestionModel questionModel = questionSet.getIndex(0);
        String lectureFilePath = retrieveLectureFilePath();
        VisualQuestionPanel panel = new VisualQuestionPanel(_ui, _questionWriter, questionModel, lectureFilePath,
                questionSet);
        panel.decreaseAnswerSize();
        float newFontSize = questionModel.getAnswerFont();
        assertTrue(13 == newFontSize);
    }

    @Test
    public void testRefreshData()
    {
        QuestionSet questionSet = retrieveQuestionSet();
        QuestionModel questionModel = questionSet.getIndex(0);
        String question = questionModel.getQuestion();
        List<String> answers = questionModel.getAnswers();
        float answerFont = questionModel.getAnswerFont();
        float questionFont = questionModel.getQuestionFont();
        String lectureFilePath = retrieveLectureFilePath();
        VisualQuestionPanel panel = new VisualQuestionPanel(_ui, _questionWriter, questionModel, lectureFilePath,
                questionSet);
        panel.refreshData();
        verify(_ui, times(2)).clearPanel();
        verify(_ui, times(2)).addQuestionLabel(question, questionFont, null);
        verify(_ui, times(2)).addAnswers(answers, answerFont, null);
    }

    @Test
    public void testSaveAction() throws IOException
    {
        QuestionSet questionSet = retrieveQuestionSet();
        QuestionModel questionModel = questionSet.getIndex(0);
        String lectureFilePath = retrieveLectureFilePath();
        VisualQuestionPanel panel = new VisualQuestionPanel(_ui, _questionWriter, questionModel, lectureFilePath,
                questionSet);
        panel.save();
        verify(_questionWriter, times(1)).createAndUpdateQuestionSet(lectureFilePath, questionSet);
    }

    @Test
    public void testSaveActionException() throws IOException
    {
        QuestionSet questionSet = retrieveQuestionSet();
        QuestionModel questionModel = questionSet.getIndex(0);
        String lectureFilePath = retrieveLectureFilePath();

        VisualQuestionPanel panel = new VisualQuestionPanel(_ui, _questionWriter, questionModel, lectureFilePath,
                questionSet);
        panel.save();
        verify(_questionWriter, times(1)).createAndUpdateQuestionSet(lectureFilePath, questionSet);
    }

    private QuestionSet retrieveQuestionSet()
    {
        QuestionSetBuilder builder = new QuestionSetBuilder();
        QuestionSet questionSet = builder.buildQuestionSet(3);
        return questionSet;
    }

    private String retrieveLectureFilePath()
    {
        LoadTestFilesService service = new LoadTestFilesService();
        File lectureFile = service.retrieveVisualQuestionPanelLecture();
        String lectureFilePath = lectureFile.getPath();
        return lectureFilePath;
    }
}
