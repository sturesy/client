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
package sturesy.core.backend.services.crud;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;

import sturesy.core.Log;
import sturesy.core.error.XMLException;
import sturesy.export.VotingSetExport;
import sturesy.items.Vote;
import sturesy.items.VotingSet;
import sturesy.util.ValidVotePredicate;

public class VotingCRUDService
{
    /**
     * Saves this whole VotingSet in XML-Format to a file
     */
    public void createAndUpdateVoting(VotingSet votingSet, String filename)
    {
        // lecture/question.xml => lecture/question_voting.xml
        filename = filename.replace(".xml", "_voting.xml");
        try
        {
            VotingSetExport.marshall(votingSet, filename);
        }
        catch (Exception e)
        {
            Log.error("error saving voting result", e);
        }
    }

    /**
     * Reads an XML-File into a Map of Integer to Set of Votes
     * 
     * @return Map&lt;Integer, Set&lt;Vote&gt;&gt;
     * @throws XMLException
     */
    public Map<Integer, Set<Vote>> readVoting(File file) throws XMLException
    {
        Map<Integer, Set<Vote>> votes = null;
        try
        {
            votes = VotingSetExport.unmarshallToMap(file);
            for (Set<Vote> voteset : votes.values())
            {
                filterFalseVotes(voteset);
            }
        }
        catch (Exception ex)
        {
            Log.error("error parsing votes", ex);
        }
        return votes;
    }

    /**
     * Filters a collection of Votes for invalid votes
     * 
     * @param votes
     */
    private void filterFalseVotes(Collection<Vote> votes)
    {
        CollectionUtils.filter(votes, new ValidVotePredicate(10));
    }
}