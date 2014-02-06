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

import javax.swing.filechooser.FileFilter;

import org.junit.Test;

import sturesy.core.backend.Loader;
import sturesy.core.backend.filter.filechooser.PictureFileFilter;

public class TestPictureFileFilter
{
    @Test
    public void testDescription()
    {
        FileFilter filter = new PictureFileFilter();
        assertEquals("*.JPG *.PNG *.GIF", filter.getDescription());
    }

    @Test
    public void testAcceptGif()
    {
        assertFileType(".gif");
        assertFileType(".GIF");
    }

    @Test
    public void testAcceptPng()
    {
        assertFileType(".png");
        assertFileType(".PNG");
    }

    @Test
    public void testAcceptJpg()
    {
        assertFileType(".jpg");
        assertFileType(".JPG");
    }

    @Test
    public void testNotAcceptBmp()
    {
        FileFilter filter = new PictureFileFilter();
        File f = new File("path/to/picture.bmp");
        assertFalse(filter.accept(f));
    }

    @Test
    public void testAcceptDirectory()
    {
        File f = Loader.getFile("image");
        FileFilter filter = new PictureFileFilter();
        assertTrue(filter.accept(f));
    }

    private void assertFileType(String filetype)
    {
        FileFilter filter = new PictureFileFilter();
        File f = new File("path/to/picture" + filetype);
        assertTrue(filter.accept(f));
    }
}
