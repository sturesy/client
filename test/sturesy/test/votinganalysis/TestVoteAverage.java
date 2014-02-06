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
package sturesy.test.votinganalysis;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import sturesy.items.Vote;
import sturesy.items.vote.SingleVote;
import sturesy.votinganalysis.VoteAverage;

public class TestVoteAverage
{

    public void testSetIsNull() throws Exception
    {
        VoteAverage voteAverage = new VoteAverage(null);
        assertTrue(0 == voteAverage.getTimeArithmeticMean());
        assertTrue(0 == voteAverage.getTimeMedian());
    }

    public void testAverageNoEntries() throws Exception
    {
        Set<Vote> votingSet = new HashSet<Vote>();
        VoteAverage voteAverage = new VoteAverage(votingSet);
        assertTrue(0 == voteAverage.getTimeArithmeticMean());
        assertTrue(0 == voteAverage.getTimeMedian());
    }

    @Test
    public void testAverageOddSmallNumbers() throws Exception
    {
        Set<Vote> votingSet = new HashSet<Vote>();
        votingSet.add(new SingleVote("guiid", 2, 0));
        votingSet.add(new SingleVote("guiid1", 2, 1));
        votingSet.add(new SingleVote("guiid2", 2, 2));
        VoteAverage voteAverage = new VoteAverage(votingSet);
        assertTrue(0.0 == voteAverage.getTimeMedian());
        assertTrue(0.0 == voteAverage.getTimeArithmeticMean());
    }

    @Test
    public void testAverageOdd() throws Exception
    {
        Set<Vote> votingSet = new HashSet<Vote>();
        votingSet.add(new SingleVote("guiid", 2, 200));
        votingSet.add(new SingleVote("guiid2", 2, 400));
        votingSet.add(new SingleVote("guiid3", 2, 600));
        VoteAverage voteAverage = new VoteAverage(votingSet);
        assertTrue(0.4 == voteAverage.getTimeArithmeticMean());
        assertTrue(0.4 == voteAverage.getTimeMedian());
    }

    @Test
    public void testAverageEven() throws Exception
    {
        Set<Vote> votingSet = new HashSet<Vote>();
        votingSet.add(new SingleVote("guiid", 2, 200));
        votingSet.add(new SingleVote("guiid2", 2, 400));
        votingSet.add(new SingleVote("guiid3", 2, 600));
        votingSet.add(new SingleVote("guiid4", 2, 1200));
        VoteAverage voteAverage = new VoteAverage(votingSet);
        assertTrue(0.6 == voteAverage.getTimeArithmeticMean());
        assertTrue(0.5 == voteAverage.getTimeMedian());
    }

    @Test
    public void testAverageNull() throws Exception
    {
        VoteAverage voteAverage = new VoteAverage(null);
        assertTrue(0 == voteAverage.getTimeArithmeticMean());
        assertTrue(0 == voteAverage.getTimeMedian());
    }
}
