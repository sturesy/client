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
package sturesy.core.plugin;

import java.util.ArrayList;

import sturesy.items.MultipleChoiceQuestion;
import sturesy.items.QuestionModel;
import sturesy.items.SingleChoiceQuestion;
import sturesy.items.TextQuestion;
import sturesy.items.Vote;
import sturesy.items.vote.MultipleVote;
import sturesy.items.vote.SingleVote;
import sturesy.items.vote.TextVote;

public final class QuestionVoteMatcher
{

    public static final Object[][] _matches = new Object[][] { { SingleChoiceQuestion.class, SingleVote.class },
            { MultipleChoiceQuestion.class, MultipleVote.class }, { TextQuestion.class, TextVote.class } };

    /**
     * Check if the given question matches the given vote<br>
     * e.g: SinleChoiceQuestion and SingleVote
     * 
     * @param question
     *            Question to check
     * @param vote
     *            Vote to check
     * @return true if question matches vote
     */
    public static boolean matches(QuestionModel question, Vote vote)
    {
        if (question == null || vote == null)
        {
            return false;
        }

        for (int i = 0; i < _matches.length; i++)
        {
            if (question.getClass() == _matches[i][0] && vote.getClass() == _matches[i][1])
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the Vote is a valid vote
     * 
     * @param vote
     *            the vote
     * @param upperbound
     *            the maximum index of an answer
     * @return
     */
    public static boolean isValidVote(Vote vote, int upperbound)
    {
        if (vote instanceof SingleVote)
        {
            SingleVote v = (SingleVote) vote;
            return v.getVote() >= 0 && v.getVote() < upperbound;
        }
        else if (vote instanceof MultipleVote)
        {
            MultipleVote v = (MultipleVote) vote;

            ArrayList<Short> l = v.asList();

            for (Short s : l)
            {
                if (s < 0 || s >= upperbound)
                    return false;
            }
            return true;
        }
        else if (vote instanceof TextVote)
        {
            return ((TextVote) vote).getAnswer() != null;
        }

        return false;
    }

    /**
     * Creates a new Instance of a Vote depending on the question given.<br>
     * use caution with the provided params
     * 
     * @param question
     * @param guid
     *            the guid used for the Vote
     * @param timediff
     *            timediff since beginning of vote in milliseconds
     * @param params
     *            a number of params as follows:
     *            <ul>
     *            <li>SingleVote requires 1x Integer</li>
     *            <li>MultipleVote requires 1x short[]</li>
     *            <li>TextVote requires 1x String</li>
     *            </ul>
     * @return
     */
    public static Vote instantiateVoteFor(QuestionModel question, String guid, long timediff, Object... params)
    {
        if (question instanceof SingleChoiceQuestion)
        {
            return new SingleVote(guid, timediff, (Integer) params[0]);
        }
        else if (question instanceof MultipleChoiceQuestion)
        {
            return new MultipleVote(guid, timediff, (short[]) params[0]);
        }
        else
        {
            return new TextVote(guid, timediff, (String) params[0]);
        }
    }

    /**
     * private constructor for util class
     */
    private QuestionVoteMatcher()
    {
    }
}
