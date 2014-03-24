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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class for Managing Votes, so they can be easily saved and accessed
 * 
 * @author w.posdorfer
 */
@XmlRootElement(name = "results")
public class VotingSet
{
    private Map<Integer, Set<Vote>> _votings;

    private int _highestIndex = -1;

    public VotingSet()
    {
        this(new HashMap<Integer, Set<Vote>>());
    }

    public VotingSet(Map<Integer, Set<Vote>> votes)
    {
        _votings = votes;
    }

    /**
     * Returns an <b>Unmodifiable</b> Set of the saved Votes for the given
     * Questionnumber
     */
    public Set<Vote> getVotesFor(int index)
    {

        if (_votings.get(index) != null)
        {
            return Collections.unmodifiableSet(_votings.get(index));
        }
        else
        {
            return Collections.emptySet();
        }
    }

    /**
     * Deletes all votes for the given question index
     */
    public void clearVotesFor(int index)
    {
        if (_votings.get(index) != null)
        {
            _votings.get(index).clear();
        }
    }

    /**
     * Saves a Vote to the current Question
     * 
     * @param questionNumber
     * @param vote
     * @return <b>false</b> if duplicate
     */
    public boolean addVote(int questionNumber, Vote vote)
    {
        Set<Vote> votes = _votings.get(questionNumber);

        if (votes == null)
        {
            votes = new HashSet<Vote>();
            _votings.put(questionNumber, votes);
            if (_highestIndex < questionNumber)
            {
                _highestIndex = questionNumber;
            }
        }

        return votes.add(vote);
    }

    /**
     * Returns <b>false</b> if no votes have been applied yet
     */
    public boolean containsVotes()
    {
        for (Set<Vote> votes : _votings.values())
        {
            if (votes.size() > 0)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Applies the give map to this VotingSet, replacing the old state
     * 
     * @param votes
     *            new Map, mapping QuestionNumbers to a Set of Votes
     */
    public void setMap(Map<Integer, Set<Vote>> votes)
    {
        _votings = votes;
    }

    /**
     * Returns the number of Questions
     * 
     * @return number of Questions
     */
    public int getQuestionSize()
    {
        return _highestIndex + 1;
    }
}
