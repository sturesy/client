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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class to represent a Vote, consisting of a guid, the vote and the date this
 * vote has been placed
 * 
 * @author w.posdorfer
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class Vote
{
    public static final short NOSINGLEVOTE = -1;

    @XmlElement(name = "guid")
    private final String _guid;

    @XmlElement(name = "tdiff")
    private final long _timediffMilliSeconds;

    public Vote(String guid, long timediffMilliSeconds)
    {
        _guid = guid;
        _timediffMilliSeconds = timediffMilliSeconds;
    }

    /**
     * @return Returns the ID of this voting
     */
    public String getGuid()
    {
        return _guid;
    }

    /**
     * @return either {@link #NOSINGLEVOTE} if this is not a single-vote, or the
     *         actual vote
     */
    public int getVote()
    {
        return NOSINGLEVOTE;
    }

    /**
     * @return the time difference of this voting compared to the beginning
     */
    public long getTimeDiff()
    {
        return _timediffMilliSeconds;
    }

    @Override
    public String toString()
    {
        return "Vote[guid=" + _guid + ", t-diff=" + _timediffMilliSeconds + "]";
    }

    @Override
    public int hashCode()
    {
        return _guid.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Vote)
        {
            Vote other = (Vote) obj;
            return this._guid.equals(other._guid);
        }
        return false;
    }

    /**
     * <b>JAXB ONLY</b>
     */
    @SuppressWarnings("unused")
    private Vote()
    {
        _guid = null;
        _timediffMilliSeconds = 0;
    }

}
