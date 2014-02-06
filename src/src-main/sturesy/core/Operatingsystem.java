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
package sturesy.core;

/**
 * OperatingSystem Enum
 * 
 * @author w.posdorfer
 * 
 */
public enum Operatingsystem
{
    WINDOWS, LINUX, MAC;

    /**
     * Returns the current Operatingsystem
     */
    public static Operatingsystem getOS()
    {
        String osname = System.getProperty("os.name").toLowerCase();
        if (osname.contains("mac os x"))
        {
            return Operatingsystem.MAC;
        }
        else if (osname.contains("windows"))
        {
            return Operatingsystem.WINDOWS;
        }
        else
        {
            return Operatingsystem.LINUX;
        }
    }

    /**
     * True if this machine is running Macintosh OS X
     */
    public static boolean isMac()
    {
        return getOS() == MAC;
    }

    /**
     * True if this machine is running MS Windows
     */
    public static boolean isWindows()
    {
        return getOS() == WINDOWS;
    }

    /**
     * True if this machine is running any form of Linux
     */
    public static boolean isLinux()
    {
        return getOS() == LINUX;
    }

    /**
     * Returns <code>32</code> or <code>64</code>
     */
    public static int getArchitecture()
    {
        return Integer.parseInt(System.getProperty("sun.arch.data.model"));
    }

}
