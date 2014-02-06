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
package sturesy.tools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sturesy.items.Vote;
import sturesy.items.vote.SingleVote;

public class VotesBuilder
{
    public Map<Integer, Set<Vote>> buildVotes(int voteSize, int questionSize, int voteRange)
    {
        Map<Integer, Set<Vote>> votesToQuestion = new HashMap<Integer, Set<Vote>>();

        for (int questionNumber = 0; questionNumber < questionSize; questionNumber++)
        {
            Set<Vote> votes = createVotes(voteSize, questionNumber, voteRange);
            votesToQuestion.put(questionNumber, votes);
        }
        return votesToQuestion;
    }

    private Set<Vote> createVotes(int voteSize, int questionNumber, int voteRange)
    {
        Set<Vote> votes = null;
        if (voteSize > 0)
        {
            votes = new HashSet<Vote>();
            for (int i = 0; i < voteSize; i++)
            {
                long timeDiff = (long) Math.random() * 1000;
                Vote vote = new SingleVote("" + i, timeDiff, (short) (voteRange - 1));
                votes.add(vote);
            }
        }
        return votes;
    }
}
