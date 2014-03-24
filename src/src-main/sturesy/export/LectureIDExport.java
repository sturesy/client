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
package sturesy.export;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.JAXB;

import sturesy.Folder;
import sturesy.core.Log;
import sturesy.core.backend.services.crud.WhiteSpaceStreamResult;
import sturesy.core.error.XMLException;
import sturesy.export.jaxb.JAXBLectureIDList;
import sturesy.export.jaxb.UnmarshallEncoding;
import sturesy.items.LectureID;

/**
 * Export and Import of LectureIDs from XML file
 * 
 * @author w.posdorfer
 * 
 */
public class LectureIDExport
{

    public static final File FILENAME = new File(Folder.getBaseFolder(), "lid.xml");
    public static final String ERROR_MSG_MARSHALLING = "Error marshalling Lecture-IDs";
    public static final String ERROR_MSG_LOADING_LECTURE_IDS = "Error loading Lecture-IDs";

    public LectureIDExport()
    {
    }

    /**
     * Marshall LectureIDs using JAXB
     * 
     * @param ids
     *            Collection of LectureIDs
     * @param filename
     */
    public static void marshallLectureIDs(Collection<LectureID> ids, String filename)
    {
        marshallLectureIDs(ids, new File(filename));
    }

    public static void marshallLectureIDs(Collection<LectureID> ids, File xml)
    {
        try
        {
            if (!xml.exists())
            {
                xml.createNewFile();
            }
            JAXB.marshal(new JAXBLectureIDList(ids), new WhiteSpaceStreamResult(xml));
        }
        catch (IOException e)
        {
            Log.error(ERROR_MSG_MARSHALLING, e);
        }
    }

    /**
     * Unmarshall LectureIDs using JAXB
     * 
     * @param filename
     * @return
     * @throws XMLException 
     */
    public static Collection<LectureID> unmarshallLectureIDs(String filename) throws XMLException
    {
        File lectureIdFile = new File(filename);
        return unmarshallLectureIDs(lectureIdFile);
    }

    /**
     * Unmarshall LectureIDs using JAXB
     * 
     * @param filename
     * @return
     * @throws XMLException
     */
    public static Collection<LectureID> unmarshallLectureIDs(File filename) throws XMLException
    {
        Collection<LectureID> result = new ArrayList<LectureID>();
        if (filename.exists())
        {
            JAXBLectureIDList list = unmarshallFile(filename);
            result.addAll(list.getList());
        }

        return result;
    }

    private static JAXBLectureIDList unmarshallFile(File filename) throws XMLException
    {
        JAXBLectureIDList list = null;
        try
        {
            list = JAXB.unmarshal(filename, JAXBLectureIDList.class);
        }
        catch (Exception e)
        {
            try
            {
                list = UnmarshallEncoding.tryOtherEncodings(filename, JAXBLectureIDList.class);
            }
            catch (Exception e1)
            {
                throw new XMLException(e);
            }
        }
        return list;
    }

}