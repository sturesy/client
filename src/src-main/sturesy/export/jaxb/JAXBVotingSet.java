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
package sturesy.export.jaxb;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import sturesy.items.Vote;
import sturesy.items.vote.MultipleVote;
import sturesy.items.vote.SingleVote;
import sturesy.items.vote.TextVote;

/**
 * 
 * Class to save the keyValues into a normal Set <br>
 * JAXB-Class
 * 
 * @author w.posdorfer
 * 
 */
@XmlRootElement
public class JAXBVotingSet
{
    @XmlElement(name = "number")
    private int _number;

    @XmlElements({ @XmlElement(name = "vote", type = SingleVote.class),
            @XmlElement(name = "multiplevote", type = MultipleVote.class),
            @XmlElement(name = "textvote", type = TextVote.class) })
    private Set<Vote> _votes;

    /**
     * JAXB ONLY
     */
    @SuppressWarnings("unused")
    private JAXBVotingSet()
    {
        this(new HashSet<Vote>(), 0);
    }

    /**
     * Creates the Set
     * 
     * @param votes
     *            the set to use
     */
    public JAXBVotingSet(Set<Vote> votes, int number)
    {
        _votes = votes;
        _number = number;
    }

    /**
     * Return the Set of Votes
     * 
     * @return Set of Votes
     */
    public Set<Vote> getVotes()
    {
        return _votes;
    }

    /**
     * return the QuestionNumber
     * 
     * @return some Integer
     */
    public int getNumber()
    {
        return _number;
    }

}
