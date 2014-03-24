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
package sturesy.util;

import java.awt.Color;
import java.awt.Dimension;

public class ParserUtils
{

    public static Color parseColor(String string)
    {
        Color result = null;
        string = string.replace(" ", "");
        if (string.matches("[0-9]{1,3},[0-9]{1,3},[0-9]{1,3},[0-9]{1,3}"))
        {
            String[] arr = string.split(",");

            result = new Color(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]),
                    Integer.parseInt(arr[3]));
        }
        return result;
    }

    public static Dimension parseDimension(String string)
    {
        string = string.replace(" ", "");
        if (string.matches("[0-9]*,[0-9]*"))
        {
            return new Dimension(Integer.parseInt(string.split(",")[0]), Integer.parseInt(string.split(",")[1]));
        }
        else
        {
            return SturesyConstants.UNDEFINEDDIMENSION;
        }
    }
}
