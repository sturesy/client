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

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;

import sturesy.core.error.XMLException;
import sturesy.export.jaxb.UnmarshallEncoding;
import sturesy.items.QuestionSet;

/**
 * Writes a {@link QuestionSet} to a specified {@link File}
 * 
 * @author w.posdorfer
 * 
 */
public class QuestionCRUDService
{

    /**
     * Marshalls a QuestionSet into the given File
     * 
     * @param file
     *            destination
     * @param set
     *            source
     */
    public void createAndUpdateQuestionSet(File file, QuestionSet set) throws DataBindingException
    {
        JAXB.marshal(set, new WhiteSpaceStreamResult(file));
    }

    /**
     * Marshalls a QuestionSet into the given File
     * 
     * @param file
     *            destination
     * @param set
     *            source
     */
    public void createAndUpdateQuestionSet(String file, QuestionSet set)
    {
        createAndUpdateQuestionSet(new File(file), set);
    }

    /**
     * Unmarshall a QuestionSet
     * 
     * @param xmlFile
     *            XML File to use for unmarshalling
     * @return unmarshalled QuestionSet
     * @throws Exception
     */
    public QuestionSet readQuestionSet(File xmlFile) throws XMLException
    {
        QuestionSet set = null;

        try
        {
            set = JAXB.unmarshal(xmlFile, QuestionSet.class);
        }
        catch (Exception e)
        {
            try
            {
                set = UnmarshallEncoding.tryOtherEncodings(xmlFile, QuestionSet.class);
            }
            catch (Exception e1)
            {
                throw new XMLException(e);
            }
        }
        return set;
    }

}
