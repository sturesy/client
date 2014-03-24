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
package sturesy.qgen.editcontroller;

import java.awt.event.FocusListener;

import sturesy.core.ui.answerPanel.AnswerPanelController;
import sturesy.core.ui.answerPanel.SingleAnswerPanelController;
import sturesy.items.QuestionModel;
import sturesy.items.SingleChoiceQuestion;

/**
 * The Controller for handling singlechoicequestions
 * 
 * @author w.posdorfer
 */
public class SingleChoiceEditController extends AnswerPanelController implements IQuestionEditController
{
    /**
     * Creates the new Controller
     * 
     * @param numbersOfAnswers
     *            the maximum amount of answers supported (commonly 10)
     */
    public SingleChoiceEditController(int numbersOfAnswers)
    {
        super(numbersOfAnswers, true);
    }

    @Override
    public void registerFocusListener(FocusListener focuslistener)
    {
        for (SingleAnswerPanelController sapc : _singleAnswerPanels)
        {
            sapc.registerFocusListener(focuslistener);
        }
    }

    @Override
    public void setUp(QuestionModel question)
    {
        if (hasNoAnswers(question))
        {
            resetAnswerPanels();
        }
        else
        {
            if (question instanceof SingleChoiceQuestion)
            {
                SingleChoiceQuestion scq = (SingleChoiceQuestion) question;
                setAnswerTexts(scq.getAnswers(), scq.getCorrectAnswer());
            }
        }
    }

    /**
     * Does this Question have any answers
     * 
     * @param question
     * @return <code>false</code> if it has no answers
     */
    private boolean hasNoAnswers(QuestionModel question)
    {
        return 0 == question.getAnswerSize();
    }

    @Override
    public void saveQuestion(QuestionModel question)
    {
        if (question instanceof SingleChoiceQuestion)
        {
            SingleChoiceQuestion scq = (SingleChoiceQuestion) question;
            scq.setAnswers(getAnswers());
            scq.setCorrectAnswer(getCorrectAnswerNumber());
        }
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        setEditing(enabled);
    }

}
