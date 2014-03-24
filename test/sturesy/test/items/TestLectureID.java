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
package sturesy.test.items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXB;

import org.junit.Test;

import sturesy.items.LectureID;
import sturesy.util.Crypt;

public class TestLectureID
{

    @Test
    public void testEquals() throws Exception
    {

        LectureID id1 = new LectureID("test", "test", "http://www.someurl.com");
        LectureID id1b = new LectureID("test", "test", "http://www.someurl.com");
        LectureID id2 = new LectureID("test", "test", "http://www.otherurl.com");
        LectureID id3 = new LectureID("othertest", "otherpassword", "http://www.otherurl.com");

        assertFalse(id1.equals(id2));
        assertFalse(id1.equals(id3));
        assertFalse(id2.equals(id3));
        assertTrue(id1.equals(id1b));

        LectureID idcrypt = new LectureID("test", Crypt.encrypt("test"), "http://www.someurl.com", true);

        assertEquals(id1, idcrypt);
    }

    @Test
    public void testEncryptingPassword() throws Exception
    {
        LectureID idcrypt = new LectureID("test", Crypt.encrypt("test"), "http://www.someurl.com", true);

        assertEquals(idcrypt.getPassword(), "test");

    }

    @Test
    public void testJAXB() throws Exception
    {
        LectureID id = new LectureID("test", "test", "http://www.someurl.com");
        LectureID idcrypt = new LectureID("test", Crypt.encrypt("cryptedpassword"), "http://www.someurl.com", true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        JAXB.marshal(id, baos);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        LectureID id2 = JAXB.unmarshal(bais, LectureID.class);

        baos.close();
        baos = new ByteArrayOutputStream();

        JAXB.marshal(idcrypt, baos);

        bais.close();
        bais = new ByteArrayInputStream(baos.toByteArray());
        LectureID idcrypt2 = JAXB.unmarshal(bais, LectureID.class);

        assertEquals(id, id2);
        assertEquals(idcrypt, idcrypt2);

        bais.close();
        baos.close();
    }

    @Test
    public void testMalformedHost()
    {
        LectureID lectureid = new LectureID("id", "", "anyurlwhichisnotmalformed");
        assertNull(lectureid.getHost());
    }

    @Test
    public void testLectureId()
    {
        LectureID lectureid = new LectureID("id", "", "anyurl");
        assertEquals("id", lectureid.getLectureID());
    }

    @Test
    public void testHashCode()
    {
        LectureID lectureid = new LectureID("id", "", "anyurl");
        assertEquals("id".hashCode(), lectureid.hashCode());
    }

    @Test
    public void testEqualsWithNull()
    {
        LectureID lectureid = new LectureID();
        assertFalse(lectureid.equals(null));
    }

    @Test
    public void testEqualsWithOtherObject()
    {
        LectureID lectureid = new LectureID();
        assertFalse(lectureid.equals(new Object()));
    }

    @Test
    public void testEqualsWithDifferentLectureID()
    {
        LectureID lectureid = new LectureID("id", "pw", "http://localhost");
        LectureID lectureid2 = new LectureID("id2", "pw", "http://localhost");
        assertFalse(lectureid.equals(lectureid2));
        assertFalse(lectureid2.equals(lectureid));
    }

    @Test
    public void testEqualsWithDifferentPassword()
    {
        LectureID lectureid = new LectureID("id", "pw", "http://localhost");
        LectureID lectureid2 = new LectureID("id", "pw1", "http://localhost");
        assertFalse(lectureid.equals(lectureid2));
        assertFalse(lectureid2.equals(lectureid));
    }

    @Test
    public void testEqualsWithDifferentHost()
    {
        LectureID lectureid = new LectureID("id", "pw", "http://localhost:8080");
        LectureID lectureid2 = new LectureID("id", "pw", "http://localhost");
        assertFalse(lectureid.equals(lectureid2));
        assertFalse(lectureid2.equals(lectureid));
    }

    @Test
    public void testIsEqual()
    {
        LectureID lectureid = new LectureID("id", "pw", "http://localhost:8080");
        assertEquals(lectureid, lectureid);
    }
    
    @Test
    public void testToString()
    {
        LectureID lectureid = new LectureID("id", "pw", "http://localhost:8080");
        assertEquals("LectureID [_lectureID=id, _password=pw, _host=http://localhost:8080]", lectureid.toString());
    }
}
