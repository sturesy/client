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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXB;

import org.junit.Test;

import sturesy.export.jaxb.JAXBLectureIDList;
import sturesy.items.LectureID;
import sturesy.util.Crypt;

/**
 * Test marshalling and unmarshalling of LectureID-Lists
 * 
 * @author w.posdorfer
 * 
 */
public class TestLectureIdListJaxb
{

    @Test
    public void testmarshalling() throws IOException
    {

        JAXBLectureIDList list = new JAXBLectureIDList();

        for (int i = 0; i < 25; i++)
        {
            list.add(new LectureID("id" + i, Crypt.encrypt("passwort" + i), "http://localhost/" + i, true));
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        JAXB.marshal(list, baos);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        JAXBLectureIDList list2 = JAXB.unmarshal(bais, JAXBLectureIDList.class);

        assertEquals(list, list2);

        bais.close();
        baos.close();

    }

    @Test
    public void testMethods()
    {

        List<LectureID> liste = new ArrayList<LectureID>();
        for (int i = 0; i < 25; i++)
        {
            liste.add(new LectureID("id" + i, Crypt.encrypt("passwort" + i), "http://localhost/" + i, true));
        }

        JAXBLectureIDList jaxblist = new JAXBLectureIDList(liste);

        assertEquals(liste, jaxblist.getList());

        Collection<LectureID> collection = new ArrayList<LectureID>(liste);

        JAXBLectureIDList jaxblist2 = new JAXBLectureIDList(collection);

        assertTrue(jaxblist.equals(jaxblist2));

        assertFalse(jaxblist.equals(new String()));

    }

}
