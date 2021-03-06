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
package sturesy.test.filter.file;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import sturesy.core.backend.filter.file.NameXMLFileFilter;

public class TestNameXMLFileFilter
{
    @Test
    public void testNameXMLFileFilterACCEPTWithNameXML()
    {
        NameXMLFileFilter filter = new NameXMLFileFilter();
        File file = new File("/path/name.xml");
        assertTrue(filter.accept(file));
    }

    @Test
    public void testNameXMLFileIsVoting()
    {
        NameXMLFileFilter filter = new NameXMLFileFilter();
        File file = new File("/path/name_voting.xml");
        assertFalse(filter.accept(file));
    }

    @Test
    public void testNameXMLFileNotEndWithXML()
    {
        NameXMLFileFilter filter = new NameXMLFileFilter();
        File file = new File("/path/");
        assertFalse(filter.accept(file));
    }
}
