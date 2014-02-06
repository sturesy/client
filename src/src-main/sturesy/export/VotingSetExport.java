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
package sturesy.export;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXB;

import sturesy.core.backend.services.crud.WhiteSpaceStreamResult;
import sturesy.core.error.XMLException;
import sturesy.export.jaxb.JAXBListVotingSet;
import sturesy.export.jaxb.JAXBVotingSet;
import sturesy.export.jaxb.UnmarshallEncoding;
import sturesy.items.Vote;
import sturesy.items.VotingSet;

/**
 * Marshall and Unmarshall VotingSets
 * 
 * @author w.posdorfer
 * 
 */
public class VotingSetExport
{

    /**
     * Marshalls a VotingSet
     * 
     * @param votingset
     *            the VotingSet to be marshalled
     * @param file
     *            where the Saver should be saved
     */
    public static void marshall(VotingSet votingset, String file)
    {
        marshall(votingset, new File(file));
    }

    /**
     * Marshalls a VotingSet
     * 
     * @param votingset
     *            the VotingSet to be marshalled
     * @param file
     *            where the Saver should be saved
     */
    public static void marshall(VotingSet votingset, File file)
    {
        JAXB.marshal(prepareForMarshalling(votingset), new WhiteSpaceStreamResult(file));
    }

    /**
     * Constructs marshall-able objects
     * 
     * @return JAXBListVotingSet which can be marshalled
     */
    private static JAXBListVotingSet prepareForMarshalling(VotingSet votingset)
    {
        int questionsize = votingset.getQuestionSize();
        ArrayList<JAXBVotingSet> list = new ArrayList<JAXBVotingSet>(questionsize);

        for (int i = 0; i < questionsize; i++)
        {
            JAXBVotingSet jaxbVotingSet = new JAXBVotingSet(votingset.getVotesFor(i), i);
            list.add(jaxbVotingSet);
        }
        return new JAXBListVotingSet(list);
    }

    /**
     * Unmarshall a VotingSet
     * 
     * @param file
     *            from where
     * @return freshly marshalled VotingSet
     * @throws XMLException
     */
    public static VotingSet unmarshall(String file) throws XMLException
    {
        return unmarshall(new File(file));
    }

    /**
     * Unmarshall a VotingSet
     * 
     * @param file
     *            from where
     * @return freshly marshalled VotingSet
     * @throws XMLException
     */
    public static VotingSet unmarshall(File file) throws XMLException
    {
        JAXBListVotingSet unmarshalledList = unmarshallFile(file);

        VotingSet saver = new VotingSet();

        for (JAXBVotingSet votset : unmarshalledList.getList())
        {
            for (Vote vote : votset.getVotes())
            {
                saver.addVote(votset.getNumber(), vote);
            }
        }
        return saver;
    }

    /**
     * Unmarshall a VotingSet into a Map for analysis
     * 
     * @param file
     *            from where
     * @return Map of Integer to Set of Votes
     * @throws XMLException
     */
    public static Map<Integer, Set<Vote>> unmarshallToMap(File file) throws XMLException
    {
        Map<Integer, Set<Vote>> result = new HashMap<Integer, Set<Vote>>();

        JAXBListVotingSet unmarshalledList = unmarshallFile(file);

        for (JAXBVotingSet votset : unmarshalledList.getList())
        {
            result.put(votset.getNumber(), votset.getVotes());
        }

        return result;
    }

    /**
     * Uses normal unmarshalling techniques, if that fails, tries to marshall
     * with different encodings
     * 
     * @throws XMLException
     */
    private static JAXBListVotingSet unmarshallFile(File file) throws XMLException
    {
        JAXBListVotingSet unmarshalledList = null;
        try
        {
            unmarshalledList = JAXB.unmarshal(file, JAXBListVotingSet.class);
        }
        catch (Exception e)
        {
            try
            {
                unmarshalledList = UnmarshallEncoding.tryOtherEncodings(file, JAXBListVotingSet.class);
            }
            catch (Exception e1)
            {
                throw new XMLException(e);
            }
        }
        return unmarshalledList;
    }

    /**
     * Unmarshall a VotingSet into a Map for analysis
     * 
     * @param file
     *            from where
     * @return Map of Integer to Set of Votes
     * @throws XMLException
     */
    public static Map<Integer, Set<Vote>> unmarshallToMap(String file) throws XMLException
    {
        return unmarshallToMap(new File(file));
    }
}
