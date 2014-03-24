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
package sturesy.items;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class representing a Multiple-Choice-Question, containing the question,
 * answers, the correct answers
 * 
 * @author w.posdorfer
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class MultipleChoiceQuestion extends QuestionModel
{

    @XmlElement(name = "answers")
    private List<String> _answers;

    @XmlElement(name = "correctAnswers")
    private List<Integer> _correctAnswers;

    public MultipleChoiceQuestion()
    {
        this("", UNLIMITED, new ArrayList<String>(), new ArrayList<Integer>());
    }

    /**
     * Constructs a MultipleChoiceQuestion
     * 
     * @param question
     *            Questionstring
     * @param duration
     *            duration in Seconds
     * @param answers
     *            AnswerStringList
     * @param correctAnswers
     *            List of correct answers, starting with 0, pass empty list for
     *            no-correct answer
     */
    public MultipleChoiceQuestion(String question, int duration, List<String> answers, List<Integer> correctAnswers)
    {

        super(question, duration);
        _answers = answers;
        _correctAnswers = correctAnswers;
    }

    @Override
    public String getType()
    {
        return MULTIPLECHOICE;
    }

    public List<String> getAnswers()
    {
        return _answers;
    }

    /**
     * @return List of correct answers or an emtpy list if no correct answer was
     *         provided.<br>
     *         Returns original List
     */
    public List<Integer> getCorrectAnswers()
    {
        return _correctAnswers;
    }

    public void setAnswers(List<String> answers)
    {
        _answers = answers;
    }

    /**
     * Set the correct answers to be the given list, use empty list to specify
     * no correct answer
     * 
     * @param correctAnswers
     *            list of correct answers
     */
    public void setCorrectAnswers(List<Integer> correctAnswers)
    {
        _correctAnswers = correctAnswers;
    }

    /**
     * Does this Question have a correct answer
     * 
     * @return <code>true</code> if this Question has a correct answer,
     *         <code>false</code> otherwise
     */
    public boolean hasCorrectAnswer()
    {
        return !_correctAnswers.isEmpty();
    }

    @Override
    public int getAnswerSize()
    {
        return _answers.size();
    }

}
