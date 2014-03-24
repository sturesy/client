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

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class representing a Fill-in-the-blank-Question, containing the question and
 * the possible answer
 * 
 * @author w.posdorfer
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class TextQuestion extends QuestionModel
{
    @XmlElement(name = "answer")
    private String _answer;

    @XmlElement(name = "tolerance")
    private int _tolerance;

    @XmlElement(name = "ignorecase")
    private boolean _ignoreCase;

    @XmlElement(name = "ignorespaces")
    private boolean _ignoreSpaces;

    /**
     * Creates a new TextQuestion
     */
    public TextQuestion()
    {
        this("", "", 0, false, false, UNLIMITED);
    }

    /**
     * Creates a new TextQuestion
     * 
     * @param question
     *            the Question
     * @param answer
     *            the answer
     * @param tolerance
     *            tolerance strings have to match, 0 means strings have to be
     *            identical
     * @param ignoreCase
     *            should the case be ignored while matching
     * @param ignoreWhiteSpace
     *            should whitespaces be ignored while matching
     * @param duration
     *            duration of question
     */
    public TextQuestion(String question, String answer, int tolerance, boolean ignoreCase, boolean ignoreWhiteSpace,
            int duration)
    {
        super(question, duration);
        _answer = answer;
        _ignoreSpaces = ignoreWhiteSpace;
        _ignoreCase = ignoreCase;
        _tolerance = tolerance;
    }

    @Override
    public String getType()
    {
        return TEXTCHOICE;
    }

    @Override
    public int getAnswerSize()
    {
        return 1;
    }

    /**
     * Always returns <code>true</code> for TextQuestions
     */
    @Override
    public boolean hasCorrectAnswer()
    {
        return true;
    }

    @Override
    public List<String> getAnswers()
    {
        return Collections.emptyList();
    }

    /**
     * @return the correct String for the blank space
     */
    public String getAnswer()
    {
        return _answer;
    }

    /**
     * Sets the answer
     * 
     * @param answer
     *            answer
     */
    public void setAnswer(String answer)
    {
        _answer = answer;
    }

    /**
     * @return Tolerance amount strings have to match
     */
    public int getTolerance()
    {
        return _tolerance;
    }

    /**
     * Set the tolerance, in variance of characters
     * 
     * @param tolerance
     */
    public void setTolerance(int tolerance)
    {
        _tolerance = tolerance;
    }

    /**
     * @return Should the case be ignored during matching
     */
    public boolean isIgnoreCase()
    {
        return _ignoreCase;
    }

    /**
     * Set if the case should be ignored
     * 
     * @param ignoreCase
     */
    public void setIgnoreCase(boolean ignoreCase)
    {
        _ignoreCase = ignoreCase;
    }

    /**
     * @return should whitespaces be ignored during matching
     */
    public boolean isIgnoreSpaces()
    {
        return _ignoreSpaces;
    }

    /**
     * Set if whitespaces should be ignored
     * 
     * @param ignoreSpaces
     */
    public void setIgnoreSpaces(boolean ignoreSpaces)
    {
        _ignoreSpaces = ignoreSpaces;
    }

    /**
     * Does the given String match the Answer withing the percentage of this
     * TextQuestion
     * 
     * @param stringToMatch
     *            the string to match
     * @return <code>true</code> if s matches the set percentage,
     *         <code>false</code> otherwise
     */
    public boolean matchesPercentage(String stringToMatch)
    {
        String original = _answer;

        if (_ignoreCase)
        {
            original = original.toLowerCase();
            stringToMatch = stringToMatch.toLowerCase();
        }
        if (_ignoreSpaces)
        {
            original = original.replace(" ", "");
            stringToMatch = stringToMatch.replace(" ", "");
        }

        if (original.length() + _tolerance < stringToMatch.length())
        {
            // impossible to match the tolerance, because the stringToMatch is
            // much longer, so we save computation time
            return false;
        }
        else
        {
            int distance = calculateLevenshteinDistance(original, stringToMatch);
            return distance <= _tolerance;
        }
    }

    /**
     * Calculates the Levenshtein Distance of two Strings
     * 
     * @param a
     *            String a
     * @param b
     *            String b
     * @return levenshtein distance
     */
    public static int calculateLevenshteinDistance(String a, String b)
    {
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++)
        {
            costs[j] = j;
        }
        for (int i = 1; i <= a.length(); i++)
        {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++)
            {
                int min1 = 1 + Math.min(costs[j], costs[j - 1]);

                int min2 = a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1;

                int cj = Math.min(min1, min2);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

}
