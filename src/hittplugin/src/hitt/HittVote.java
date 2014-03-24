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
package hitt;

/**
 * Models a Hitt-Vote, containing errorcode, device-id and button-id
 * 
 * @author w.posdorfer
 */
public class HittVote
{

    public int error;
    public int guid;
    public int vote;

    /**
     * Create a Vote
     */
    public HittVote(int err, int guid, int vote)
    {
        error = err;
        this.guid = guid;
        this.vote = vote;
    }

    @Override
    public String toString()
    {
        return "HittVote [error=" + error + ", guid=" + guid + ", vote=" + vote + "]";
    }

}
