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
package sturesy.test.items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import org.junit.Before;
import org.junit.Test;

import sturesy.items.QuestionModel;
import sturesy.items.SingleChoiceQuestion;

public class TestQuestionModel
{

    private SingleChoiceQuestion _model;
    private List<String> _answers;

    @Before
    public void setUp()
    {
        _answers = new ArrayList<String>();
        _answers.add("answer a");
        _answers.add("answer b");

        _model = new SingleChoiceQuestion("Question", _answers, QuestionModel.NOCORRECTANSWER, 120);

    }

    @Test
    public void testQuestionModelConstructorAndGetter()
    {
        assertEquals(_answers, _model.getAnswers());
        assertEquals("Question", _model.getQuestion());
        assertEquals(QuestionModel.NOCORRECTANSWER, _model.getCorrectAnswer());
        assertEquals(120, _model.getDuration());
    }

    @Test
    public void testQuestionModelIsEqual()
    {
        SingleChoiceQuestion model = new SingleChoiceQuestion("Question", _answers, QuestionModel.NOCORRECTANSWER, 120);
        assertEquals(model, _model);

    }

    @Test
    public void testHashCode()
    {
        int questionModelHashCode = _model.hashCode();
        assertEquals("Question".hashCode(), questionModelHashCode);
    }

    @Test
    public void testQuestionModelNotEqualsWrongQuestion()
    {
        SingleChoiceQuestion model = new SingleChoiceQuestion("Wrong Question", _answers, QuestionModel.NOCORRECTANSWER, 120);
        assertFalse(model.equals(_model));
    }

    @Test
    public void testQuestionModelNotEqualsDifferentAnswers()
    {
        List<String> answers = new ArrayList<String>();
        SingleChoiceQuestion model = new SingleChoiceQuestion("Question", answers, QuestionModel.NOCORRECTANSWER, 120);
        assertFalse(model.equals(_model));

    }

    @Test
    public void testQuestionModelNotEqualsWrongCorrectAnswer()
    {
        QuestionModel model = new SingleChoiceQuestion("Question", _answers, 0, 120);
        assertFalse(model.equals(_model));
    }

    @Test
    public void testQuestionModelEqualsOtherObject()
    {
        assertFalse(_model.equals(new Object()));
    }

    @Test
    public void testQuestionModelEqualsNull()
    {
        assertFalse(_model.equals(null));
    }

    @Test
    public void testAddAnswer()
    {
        ArrayList<String> newlist = new ArrayList<String>(_answers);

        _model.addAnswer("answer c");

        assertTrue(_model.getAnswerSize() == 3);

        newlist.add("answer c");

        assertEquals(newlist, _model.getAnswers());
    }

    @Test
    public void testFont()
    {
        Font font_a = new JLabel().getFont().deriveFont(18f);
        Font font_q = font_a.deriveFont(20f);

        QuestionModel m = new SingleChoiceQuestion();

        assertEquals(font_a, m.getAnswerFont());
        assertEquals(font_q, m.getQuestionFont());

        m.setAnswerFont(10);
        assertEquals(10, m.getAnswerFont(), 0);

    }

}
