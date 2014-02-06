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
package sturesy.qgen.editcontroller;

import java.awt.event.FocusListener;

import sturesy.core.ui.answerPanel.AnswerPanelController;
import sturesy.core.ui.answerPanel.SingleAnswerPanelController;
import sturesy.items.MultipleChoiceQuestion;
import sturesy.items.QuestionModel;

/**
 * The Controller for handling multiplechoicequestions
 * 
 * @author w.posdorfer
 * 
 */
public class MultipleChoiceEditController extends AnswerPanelController implements IQuestionEditController
{

    public MultipleChoiceEditController(int numbersOfAnswers)
    {
        super(numbersOfAnswers, false);
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
        if (question.getAnswerSize() == 0)
        {
            resetAnswerPanels();
        }
        else
        {
            if (question instanceof MultipleChoiceQuestion)
            {
                MultipleChoiceQuestion multi = (MultipleChoiceQuestion) question;
                setAnswerTexts(multi.getAnswers(), multi.getCorrectAnswers());
            }
        }
    }

    @Override
    public void saveQuestion(QuestionModel question)
    {
        if (question instanceof MultipleChoiceQuestion)
        {
            MultipleChoiceQuestion mult = (MultipleChoiceQuestion) question;
            mult.setCorrectAnswers(getCorrectAnswerNumbers());
            mult.setAnswers(getAnswers());
        }
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        setEditing(enabled);
    }

}
