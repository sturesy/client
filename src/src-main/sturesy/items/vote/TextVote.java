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
 * A vote containing a text answer
 * 
 * @author w.posdorfer
 * 
 */
public class TextVote extends Vote
{
    @XmlElement(name = "answer")
    private final String _answer;

    /**
     * Creates a new vote with text answer
     * 
     * @param id
     *            ID of Voter
     * @param timediffMilliseconds
     *            time difference since voting started
     * @param answer
     *            answer of voter
     */
    public TextVote(String id, long timediffMilliseconds, String answer)
    {
        super(id, timediffMilliseconds);
        _answer = answer;
    }

    public String getAnswer()
    {
        return _answer;
    }

    /**
     * <b>JAXB ONLY</b>
     */
    @SuppressWarnings("unused")
    private TextVote()
    {
        this("", 0, "");
    }

    @Override
    public String toString()
    {
        return "TextVote [_answer=" + _answer + ", guid=" + getGuid() + ", timediff=" + getTimeDiff() + "]";
    }

}
