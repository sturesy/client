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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import sturesy.export.jaxb.adapter.FontAdapter;

/**
 * Abstract Class representing a Question, containing the question and duration
 * 
 * @author w.posdorfer
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class QuestionModel
{
    public static final String SINGLECHOICE = "singlechoice";
    public static final String MULTIPLECHOICE = "multiplechoice";
    public static final String TEXTCHOICE = "textchoice";

    /** NOCORRECTANSWER = -1 */
    public static final int NOCORRECTANSWER = -1;
    /** UNLIMITED = -1 */
    public static final int UNLIMITED = -1;

    private static final float DEFAULTANSWERFONTSIZE = 18f;
    private static final float DEFAULTQUESTIONFONTSIZE = 20f;

    @XmlID
    @XmlElement(name = "question")
    private String _question;

    @XmlElement(name = "afont")
    @XmlJavaTypeAdapter(FontAdapter.class)
    private Float _answerFont;

    @XmlElement(name = "qfont")
    @XmlJavaTypeAdapter(FontAdapter.class)
    private Float _questionFont;

    @XmlElement(name = "duration")
    private int _duration;

    /**
     * Creates a default empty Question
     */
    public QuestionModel()
    {
        this("", UNLIMITED);
    }

    /**
     * Creates a question with given
     * 
     * @param question
     *            the Question Text
     * @param duration
     *            duration of this question
     */
    public QuestionModel(String question, int duration)
    {
        _question = question;
        _duration = duration;
        _answerFont = DEFAULTANSWERFONTSIZE;
        _questionFont = DEFAULTQUESTIONFONTSIZE;
    }

    /**
     * Returns the Type of this Question as a String<br>
     * {@link #TEXTCHOICE} or {@link #SINGLECHOICE} or {@link #MULTIPLECHOICE}
     * 
     * @return String
     */
    public abstract String getType();

    /**
     * @return the question
     */
    public String getQuestion()
    {
        return _question;
    }

    /**
     * @param question
     *            the question to set
     */
    public void setQuestion(String question)
    {
        _question = question;
    }

    /**
     * Sets the Answer Font
     * 
     * @param font
     *            Font
     */
    public void setAnswerFont(float font)
    {
        _answerFont = font;
    }

    /**
     * Sets the Question Font
     * 
     * @param font
     *            Font
     */
    public void setQuestionFont(float font)
    {
        _questionFont = font;
    }

    /**
     * Font to be used for Answers
     * 
     * @return AnswerFont
     */
    public float getAnswerFont()
    {
        return _answerFont;
    }

    public abstract int getAnswerSize();

    public abstract boolean hasCorrectAnswer();

    public abstract List<String> getAnswers();

    /**
     * Font to be used for the Question
     * 
     * @return QuestionFont
     */
    public float getQuestionFont()
    {
        return _questionFont;
    }

    /**
     * Duration to be used for a voting with this question
     * 
     * @return -1 for unlimited or positive integer
     */
    public int getDuration()
    {
        return _duration;
    }

    /**
     * Set the duration to be used for voting
     * 
     * @param d
     *            -1 for unlimited or positive integer
     */
    public void setDuration(int d)
    {
        _duration = d;
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "[Question:" + _question + "]";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof QuestionModel)
        {
            QuestionModel other = (QuestionModel) obj;

            boolean equalQuestions = other._question.equals(_question);
            boolean isEqual = equalQuestions;
            return isEqual;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return _question.hashCode();
    }
}