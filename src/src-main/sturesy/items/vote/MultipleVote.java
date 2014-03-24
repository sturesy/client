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
package sturesy.items.vote;

import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.ArrayUtils;

import sturesy.items.Vote;

/**
 * A Vote containing multiple selected Answers
 * 
 * @author w.posdorfer
 */
public class MultipleVote extends Vote
{

    @XmlElement(name = "votes")
    private final short[] _votes;

    /**
     * Creates a new Vote with multiple answers
     * 
     * @param id
     *            id of Voter
     * @param timediffMilliseconds
     *            time difference from beginning
     * @param votes
     *            array of votes
     */
    public MultipleVote(String id, long timediffMilliseconds, short[] votes)
    {
        super(id, timediffMilliseconds);
        _votes = votes;
    }

    /**
     * Creates a new Vote with multiple answers
     * 
     * @param id
     *            id of Voter
     * @param timediffMilliseconds
     *            time difference from beginning
     * @param votes
     *            arraylist of votes
     */
    public MultipleVote(String id, long timediffMilliseconds, ArrayList<Short> votes)
    {
        super(id, timediffMilliseconds);
        _votes = ArrayUtils.toPrimitive(votes.toArray(new Short[0]));
    }

    public short[] getVotes()
    {
        return _votes;
    }

    /**
     * <b>JAXB ONLY</b>
     */
    @SuppressWarnings("unused")
    private MultipleVote()
    {
        this("", 0, (short[]) null);
    }

    /**
     * @return the votes as List
     */
    public ArrayList<Short> asList()
    {
        ArrayList<Short> list = new ArrayList<Short>();
        for (int i = 0; i < _votes.length; i++)
        {
            list.add(_votes[i]);
        }
        return list;
    }

    @Override
    public String toString()
    {
        return "MultipleVote [_votes=" + Arrays.toString(_votes) + ", guid=" + getGuid() + ", timediff="
                + getTimeDiff() + "]";
    }

}
