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
package sturesy.test.core.backend;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import sturesy.core.Operatingsystem;

/**
 * Test Operatingsystem enum
 * 
 * @author w.posdorfer
 * 
 */
public class TestOperatingsystem
{

    @Test
    public void test()
    {
        System.setProperty("os.name", "Mac OS X");

        assertEquals(Operatingsystem.MAC, Operatingsystem.getOS());
        assertTrue(Operatingsystem.isMac());
        assertFalse(Operatingsystem.isWindows());
        assertFalse(Operatingsystem.isLinux());

        System.setProperty("os.name", "Microsoft Windows 7 32bit");

        assertEquals(Operatingsystem.WINDOWS, Operatingsystem.getOS());
        assertTrue(Operatingsystem.isWindows());
        assertFalse(Operatingsystem.isMac());
        assertFalse(Operatingsystem.isLinux());

        System.setProperty("os.name", "GNU Debian 6.0");

        assertEquals(Operatingsystem.LINUX, Operatingsystem.getOS());
        assertTrue(Operatingsystem.isLinux());
        assertFalse(Operatingsystem.isMac());
        assertFalse(Operatingsystem.isWindows());

        String arch = System.getProperty("sun.arch.data.model");

        assertEquals(arch, "" + Operatingsystem.getArchitecture());
    }

}
