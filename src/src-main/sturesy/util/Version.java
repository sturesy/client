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
package sturesy.util;

/**
 * Contains the current program version
 * 
 * @author w.posdorfer
 * 
 */
public class Version
{

    public static final String CURRENTVERSION = "0.5.3";

    /**
     * Checks if provided version is higher than currentversion
     * 
     * @param version
     *            version to be compared to current version
     * @return <code>true</code> if version is higher than current version
     */
    public static boolean isHigherVersion(String version)
    {
        if (!version.matches("[0-9]*\\.[0-9]*\\.[0-9]*"))
        {
            return false;
        }

        String[] split = version.split("\\.");
        String[] current = CURRENTVERSION.split("\\.");

        for (int i = 0; i < current.length; i++)
        {
            int s = Integer.parseInt(split[i]);
            int c = Integer.parseInt(current[i]);

            if (s > c)
            {
                return true;
            }
            else if (s < c)
            {
                return false;
            }
            // else continue;
        }
        return false;
    }

}
