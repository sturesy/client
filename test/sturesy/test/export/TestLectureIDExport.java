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
package sturesy.test.export;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.After;
import org.junit.Test;

import sturesy.core.error.XMLException;
import sturesy.export.LectureIDExport;
import sturesy.items.LectureID;
import sturesy.tools.LoadTestFilesService;
import sturesy.util.Crypt;

public class TestLectureIDExport
{

    /** {@value #LID_TEST_XML} */
    private static final String LID_TEST_XML = System.getProperty("user.home") + "/lid-test.xml";

    @Test
    public void testMarshallingUnmarshalling()
    {
        LoadTestFilesService testFileService = new LoadTestFilesService();
        File lidxml = testFileService.retrieveTestLidXML();
        doMarshalUnmarshalRoundtripAndVerify(lidxml.getAbsolutePath());
    }

    @Test
    public void testMarshallingUnmarshallingWithRealLectureIdFile()
    {
        doMarshalUnmarshalRoundtripAndVerify(LectureIDExport.FILENAME.getAbsolutePath());
    }

    private void doMarshalUnmarshalRoundtripAndVerify(String datei)
    {
        Collection<LectureID> collection = createFilledCollection();

        LectureIDExport.marshallLectureIDs(collection, datei);

        try
        {
            Collection<LectureID> result;
            result = LectureIDExport.unmarshallLectureIDs(datei);
            assertEquals(collection, result);
        }
        catch (XMLException e)
        {
        }

    }

    private Collection<LectureID> createFilledCollection()
    {
        Collection<LectureID> collection = new ArrayList<LectureID>();
        for (int i = 0; i < 25; i++)
        {
            collection.add(new LectureID("id" + i, Crypt.encrypt("pw" + i), "http://localhost/" + i, true));
        }
        return collection;
    }

    @After
    public void teardown()
    {
        File f = new File(LID_TEST_XML);
        if (f.exists())
            f.delete();
    }

}
