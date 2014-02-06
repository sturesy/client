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
package sturesy.items.vote;

import javax.xml.bind.annotation.XmlElement;

import sturesy.items.Vote;

/**
 * Class representing a vote with a single answer
 * 
 * @author w.posdorfer
 */
public class SingleVote extends Vote
{

    @XmlElement(name = "voting")
    private final short _vote;

    /**
     * Creates a new Vote with one selected answer
     * 
     * @param id
     *            id of voter
     * @param timediffMilliseconds
     *            time difference since this vote started
     * @param vote
     *            the actual vote
     */
    public SingleVote(String id, long timediffMilliseconds, int vote)
    {
        super(id, timediffMilliseconds);
        _vote = (short) vote;
    }

    @Override
    public int getVote()
    {
        return _vote;
    }

    /**
     * <b>JAXB ONLY</b>
     */
    @SuppressWarnings("unused")
    private SingleVote()
    {
        this("", 0, NOSINGLEVOTE);
    }

    @Override
    public String toString()
    {
        return "SingleVote [_vote=" + _vote + ", guid=" + getGuid() + ", timediff=" + getTimeDiff() + "]";
    }

}
