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
package sturesy.test.export;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

import sturesy.core.backend.services.crud.VotingCRUDService;
import sturesy.core.error.XMLException;
import sturesy.items.Vote;
import sturesy.items.VotingSet;
import sturesy.items.vote.SingleVote;

public class TestVotingParser
{

    private VotingCRUDService _parser;
    private String _votingfile;
    private String _analysisfile;

    private Map<Integer, Set<Vote>> _votes;

    @Before
    public void setUp()
    {
        _parser = new VotingCRUDService();
        _votingfile = "sample.xml";
        _analysisfile = "sample_voting.xml";
        _votes = new HashMap<Integer, Set<Vote>>();
        VotingSet votingsaver = new VotingSet();

        Random r = new Random();

        for (int qnumb = 0; qnumb < 10; qnumb++)
        {
            _votes.put(qnumb, new HashSet<Vote>());

            for (int i = 0; i < 100; i++)
            {

                String guid = "" + (r.nextInt(800000) + 1000000);
                Vote v = new SingleVote(guid, r.nextInt(10), r.nextInt(6000) + 1000);
                votingsaver.addVote(qnumb, v);
                _votes.get(qnumb).add(v);
            }
        }
        VotingCRUDService votingWriter = new VotingCRUDService();

        votingWriter.createAndUpdateVoting(votingsaver, _votingfile);

    }

    @Test
    public void test() throws IOException, XmlPullParserException
    {
        try
        {
            Map<Integer, Set<Vote>> votes = _parser.readVoting(new File(_analysisfile));
            
            // Testing if the parsed HashMap of Votings
            // matches the previously saved ones
            assertEquals(_votes, votes);
        }
        catch (XMLException e)
        {
        }
    }

    @After
    public void tearDown()
    {
        File f = new File(_analysisfile);
        if (f.exists())
            f.delete();
    }

}
