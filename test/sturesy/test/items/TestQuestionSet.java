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
package sturesy.test.items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import sturesy.items.QuestionModel;
import sturesy.items.QuestionSet;
import sturesy.items.SingleChoiceQuestion;

@RunWith(MockitoJUnitRunner.class)
public class TestQuestionSet
{

    @Mock
    private QuestionModel _model1;
    @Mock
    private QuestionModel _model2;
    @Mock
    private QuestionModel _model3;
    @Mock
    private QuestionModel _model4;

    @Test
    public void testSetIsCreated()
    {
        QuestionSet questionSet = new QuestionSet();
        assertNotNull(questionSet.getQuestionModels());
    }

    @Test
    public void testQuestionSetWithExistingSet()
    {
        List<QuestionModel> questionsSetEntries = new ArrayList<QuestionModel>();
        QuestionSet questionSet = new QuestionSet(questionsSetEntries);
        assertSame(questionsSetEntries, questionSet.getQuestionModels());
    }

    @Test
    public void testQuestionSetWithExistingArray()
    {
        QuestionModel[] model = new QuestionModel[1];
        model[0] = _model1;
        QuestionSet set = new QuestionSet(model);
        assertEquals(1, set.size());
        assertSame(_model1, set.getIndex(0));
    }

    @Test
    public void testSwapTwoElementsSuccess()
    {
        QuestionSet questionSet = new QuestionSet();
        questionSet.addQuestionModel(_model1);
        questionSet.addQuestionModel(_model2);
        questionSet.addQuestionModel(_model3);
        questionSet.addQuestionModel(_model4);
        assertSame(_model1, questionSet.getIndex(0));
        assertSame(_model2, questionSet.getIndex(1));
        assertSame(_model3, questionSet.getIndex(2));
        assertSame(_model4, questionSet.getIndex(3));
        questionSet.swapElements(0, 3);
        assertSame(_model4, questionSet.getIndex(0));
        assertSame(_model2, questionSet.getIndex(1));
        assertSame(_model3, questionSet.getIndex(2));
        assertSame(_model1, questionSet.getIndex(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSwapElementsOutOfBounds()
    {
        QuestionSet questionSet = new QuestionSet();
        questionSet.swapElements(1, 2);
    }

    @Test
    public void testSize()
    {
        QuestionSet questionSet = new QuestionSet();
        assertEquals(0, questionSet.size());
        questionSet.addQuestionModel(_model1);
        assertEquals(1, questionSet.size());
        questionSet.removeQuestionModel(_model1);
        assertEquals(0, questionSet.size());
    }

    @Test
    public void testAnswerSize()
    {
        QuestionSet questionSet = new QuestionSet();
        questionSet.addQuestionModel(_model1);
        questionSet.addQuestionModel(_model2);
        Mockito.when(_model1.getAnswerSize()).thenReturn(3);
        Mockito.when(_model2.getAnswerSize()).thenReturn(2);

        assertEquals(3, questionSet.sizeOfAnswers(0));
        assertEquals(2, questionSet.sizeOfAnswers(1));
    }

    @Test
    public void testEqualsMethod()
    {
        SingleChoiceQuestion modellocal1 = new SingleChoiceQuestion();
        SingleChoiceQuestion modellocal2 = new SingleChoiceQuestion();
        modellocal1.addAnswer("Hallo");
        modellocal1.setQuestion("Was?");
        modellocal2.addAnswer("Hallo");
        modellocal2.setQuestion("Was?");
        QuestionSet questionSet = new QuestionSet();
        questionSet.addQuestionModel(modellocal1);

        QuestionSet questionSet2 = new QuestionSet();
        questionSet2.addQuestionModel(modellocal2);

        assertTrue(questionSet.equals(questionSet2));
        assertTrue(questionSet2.equals(questionSet));
    }

    @Test
    public void testNotEqualsOnWrongType()
    {
        QuestionSet questionSet = new QuestionSet();
        Object equalsObject = new Object();

        assertFalse(questionSet.equals(equalsObject));
    }

    @Test
    public void testEqualsWithNullArgument()
    {
        QuestionSet questionSet = new QuestionSet();
        assertFalse(questionSet.equals(null));
    }

    @Test
    public void testNotEqualsBecauseDifferentSize()
    {
        SingleChoiceQuestion modellocal1 = new SingleChoiceQuestion();
        modellocal1.addAnswer("Hallo");
        modellocal1.setQuestion("Was?");

        QuestionSet questionSet = new QuestionSet();
        questionSet.addQuestionModel(modellocal1);

        QuestionSet questionSet2 = new QuestionSet();

        assertFalse(questionSet.equals(questionSet2));
        assertFalse(questionSet2.equals(questionSet));
    }

    @Test
    public void testNotEqualsBecauseDifferentEntries()
    {
        SingleChoiceQuestion modellocal1 = new SingleChoiceQuestion();
        SingleChoiceQuestion modellocal2 = new SingleChoiceQuestion();
        modellocal1.addAnswer("Hallo");
        modellocal1.setQuestion("Was?");
        modellocal2.addAnswer("Andere Antort");
        modellocal2.setQuestion("Andere Frage");
        QuestionSet questionSet = new QuestionSet();
        questionSet.addQuestionModel(modellocal1);

        QuestionSet questionSet2 = new QuestionSet();
        questionSet2.addQuestionModel(modellocal2);

        assertFalse(questionSet.equals(questionSet2));
        assertFalse(questionSet2.equals(questionSet));
    }

    @Test
    public void testToString()
    {
        Mockito.doReturn("this is toString from questionmodel").when(_model1).toString();
        Mockito.doReturn("this is toString from questionmodel 2").when(_model2).toString();

        QuestionSet questionSet = new QuestionSet();
        questionSet.addQuestionModel(_model1);
        assertEquals("this is toString from questionmodel", questionSet.toString());
        questionSet.addQuestionModel(_model2);
        assertEquals("this is toString from questionmodelthis is toString from questionmodel 2", questionSet.toString());
    }
}
