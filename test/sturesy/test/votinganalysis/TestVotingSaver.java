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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import sturesy.items.Vote;
import sturesy.items.VotingSet;
import sturesy.items.vote.SingleVote;

public class TestVotingSaver
{

    @Test
    public void testClearVotesForExistingQuestionNumber()
    {
        VotingSet voteSaver = new VotingSet();
        voteSaver.addVote(0, new SingleVote("guiid", 0, 300));
        Set<Vote> votesFor = voteSaver.getVotesFor(0);
        assertTrue(votesFor.size() == 1);
        voteSaver.clearVotesFor(0);
        assertTrue(votesFor.size() == 0);
    }

    @Test
    public void testClearVotesForNonExistingQuestionNumber()
    {
        VotingSet voteSaver = new VotingSet();
        voteSaver.clearVotesFor(1);
        Set<Vote> votesFor = voteSaver.getVotesFor(1);
        assertTrue(votesFor.isEmpty());
    }

    @Test
    public void testAddVote()
    {
        VotingSet votingSaver = new VotingSet();
        Vote secondEntry = new SingleVote("guiid2", 0, 600);
        Vote firstEntry = new SingleVote("guiid", 0, 300);

        assertTrue(votingSaver.addVote(0, firstEntry));
        Set<Vote> votesForWithOneEntry = votingSaver.getVotesFor(0);
        assertTrue(1 == votesForWithOneEntry.size());
        assertTrue(votesForWithOneEntry.contains(firstEntry));

        assertTrue(votingSaver.addVote(0, secondEntry));
        Set<Vote> votesForWithTwoEntries = votingSaver.getVotesFor(0);
        assertTrue(2 == votesForWithOneEntry.size());
        assertTrue(votesForWithOneEntry.contains(firstEntry));
        assertTrue(votesForWithTwoEntries.contains(secondEntry));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetVotesForHasCorrectEntries()
    {
        VotingSet votingSaver = new VotingSet();
        Vote vote = new SingleVote("guiid", 0, 300);
        Vote vote1 = new SingleVote("guiid2", 0, 500);
        Vote vote2 = new SingleVote("guiid3", 0, 600);
        Vote vote3 = new SingleVote("guiid4", 0, 800);
        Vote vote4 = new SingleVote("guiid5", 0, 1000);
        Vote vote5 = new SingleVote("guiid6", 0, 1020);

        votingSaver.addVote(0, vote);
        votingSaver.addVote(1, vote1);
        votingSaver.addVote(1, vote2);
        votingSaver.addVote(2, vote3);
        votingSaver.addVote(2, vote4);
        votingSaver.addVote(2, vote5);

        Set<Vote> votesFor1 = votingSaver.getVotesFor(0);
        Set<Vote> votesFor2 = votingSaver.getVotesFor(1);
        Set<Vote> votesFor3 = votingSaver.getVotesFor(2);

        assertTrue(votesFor1.size() == 1);
        assertTrue(votesFor2.size() == 2);
        assertTrue(votesFor3.size() == 3);

        assertTrue(votesFor1.contains(vote));

        assertTrue(votesFor2.contains(vote1));
        assertTrue(votesFor2.contains(vote2));

        assertTrue(votesFor3.contains(vote3));
        assertTrue(votesFor3.contains(vote4));
        assertTrue(votesFor3.contains(vote5));

        votesFor1.add(vote5);
    }

    @Test
    public void testContainsVote()
    {
        VotingSet votingSaver = new VotingSet();
        assertFalse(votingSaver.containsVotes());
        votingSaver.addVote(0, new SingleVote("guiid", 0, 200));
        assertTrue(votingSaver.containsVotes());

        votingSaver.clearVotesFor(0);
        assertFalse(votingSaver.containsVotes());
    }
}
