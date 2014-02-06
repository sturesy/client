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
package sturesy.items;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class representing a Single-Choice-Question, containing the question,
 * answers, the correct answer
 * 
 * @author w.posdorfer
 * 
 */
@XmlRootElement(name = "questionmodel")
@XmlAccessorType(XmlAccessType.NONE)
public class SingleChoiceQuestion extends QuestionModel
{

    @XmlElement(name = "answer")
    private List<String> _answers;

    @XmlElement(name = "correct")
    private int _correctAnswer;

    /**
     * Creates a default empty Question
     */
    public SingleChoiceQuestion()
    {
        this("", new ArrayList<String>(), NOCORRECTANSWER, UNLIMITED);
    }

    /**
     * Creates a question with given
     * 
     * @param question
     *            the Question Text
     * @param answers
     *            the Answers
     * @param correct
     *            index of the correct answer or {@link #NOCORRECTANSWER}
     * @param duration
     *            duration of this question or {@link #UNLIMITED}
     */
    public SingleChoiceQuestion(String question, List<String> answers, int correct, int duration)
    {
        super(question, duration);
        _answers = answers;
        _correctAnswer = correct;
    }
    
    @Override
    public String getType()
    {
        return SINGLECHOICE;
    }

    /**
     * @return the answers
     */
    public List<String> getAnswers()
    {
        return _answers;
    }

    /**
     * @return amount of possible answers
     */
    public int getAnswerSize()
    {
        return _answers.size();
    }

    /**
     * Does this Question have a correct answer
     * 
     * @return <code>true</code> if this Question has a correct answer,
     *         <code>false</code> otherwise
     */
    public boolean hasCorrectAnswer()
    {
        return _correctAnswer != NOCORRECTANSWER;
    }

    /**
     * <b>Only for SingleChoice</b>
     * 
     * @return the correctAnswer
     */
    public int getCorrectAnswer()
    {
        return _correctAnswer;
    }

    /**
     * @param answers
     *            the answers to set
     */
    public void setAnswers(List<String> answers)
    {
        _answers = answers;
    }

    /**
     * Adds an answer to the List
     * 
     * @answer the answer to add
     */
    public void addAnswer(String answer)
    {
        _answers.add(answer);
    }

    /**
     * The correct answer is identified by the index in the answers list<br>
     * <br>
     * 
     * <b>Only for SingleChoice</b>
     * 
     * @param correctAnswer
     *            the correctAnswer index in answer list
     */
    public void setCorrectAnswer(int correctAnswer)
    {
        _correctAnswer = correctAnswer;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof SingleChoiceQuestion)
        {
            SingleChoiceQuestion other = (SingleChoiceQuestion) obj;

            boolean superequals = super.equals(obj);
            boolean equalCorrectAnswer = other._correctAnswer == _correctAnswer;
            boolean equalAnswers = other._answers.equals(_answers);

            return superequals && equalCorrectAnswer && equalAnswers;
        }
        return false;
    }
}