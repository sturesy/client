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
package sturesy.test.filter.filechooser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sturesy.core.backend.filter.filechooser.ZipFileFilter;

public class TestZipFileFilter
{

    private static final String FOLDER = "./folder";

    @Before
    public void setUp()
    {
        File folder = new File(FOLDER);
        folder.mkdir();
    }

    @Test
    public void testZipFileFilterWithZIPFilepath()
    {
        ZipFileFilter filter = new ZipFileFilter();
        File f = new File("anypath/name.zip");
        assertTrue(filter.accept(f));
    }

    @Test
    public void testZipFileFilterWithoutXML()
    {
        ZipFileFilter filter = new ZipFileFilter();
        File f = new File("anypath/name.xxx");
        assertFalse(filter.accept(f));
    }

    @Test
    public void testZipFileFilterWithDirectory()
    {
        ZipFileFilter filter = new ZipFileFilter();
        File f = new File(FOLDER);
        assertTrue(filter.accept(f));
    }

    @Test
    public void testDescription()
    {
        ZipFileFilter filter = new ZipFileFilter();
        assertEquals("*.zip", filter.getDescription());
    }

    @After
    public void tearDown()
    {
        File f = new File(FOLDER);
        f.delete();
    }
}
