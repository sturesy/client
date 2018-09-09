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
package sturesy.util;

import org.apache.commons.collections4.Predicate;

import sturesy.core.plugin.QuestionVoteMatcher;
import sturesy.items.Vote;

/**
 * A Predicate used for filtering Votes, which do not match the questionmodels
 * answersize
 * 
 * @author w.posdorfer
 * 
 */
public class ValidVotePredicate implements Predicate<Vote>
{

    private final int _upperbound;

    /**
     * Creates a predicate using the upperbound as exclusive upperbound<br>
     * <br>
     * <code>v.getVote() &gt;= 0 && v.getVote() &lt; upperbound</code>
     * 
     * @param upperbound
     *            the exclusive upperbound
     */
    public ValidVotePredicate(int upperbound)
    {
        _upperbound = upperbound;
    }

    /**
     * Returns <b>true</b> if the Vote should stay in the Collection
     */
    public boolean evaluate(Vote vote)
    {
        return QuestionVoteMatcher.isValidVote(vote, _upperbound);
    }
}
