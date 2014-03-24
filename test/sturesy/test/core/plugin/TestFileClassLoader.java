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
package sturesy.test.core.plugin;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import sturesy.core.plugin.FileClassLoader;

public class TestFileClassLoader
{

    @Test(expected = IllegalArgumentException.class)
    public void testNullDir()
    {
        new FileClassLoader(null);
    }

    @Test
    public void testFileLoad() throws ClassNotFoundException
    {
        String rootdir = "test" + File.separator + "core" + File.separator + "plugin" + File.separator;
        FileClassLoader loader = new FileClassLoader(rootdir);
        Class<?> resultAsClass = loader.loadClass("sturesy.test.core.plugin.FileClassLoaderTestClass", Boolean.TRUE);
        assertEquals("sturesy.test.core.plugin.FileClassLoaderTestClass", resultAsClass.getName());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testFileLoadWithInvalidClassName() throws ClassNotFoundException
    {
        String rootdir = "test" + File.separator + "core" + File.separator + "plugin" + File.separator;
        FileClassLoader loader = new FileClassLoader(rootdir);
        loader.loadClass("FileClassLoaderTestClass", Boolean.TRUE);
    }
}
