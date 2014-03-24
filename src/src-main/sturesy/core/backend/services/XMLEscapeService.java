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
package sturesy.core.backend.services;

public class XMLEscapeService
{

    /**
     * Escapes a String for use within XML
     */
    public static String escapeString(String string)
    {
        String result = string.replace("<", "&lt;");
        result = result.replace(">", "&gt;");

        return result;
    }

    /**
     * Unescapes a String from XML usage
     */
    public static String unescapeString(String string)
    {
        String result = string.replace("&lt;", "<");

        result = result.replace("&gt;", ">");

        return result;
    }
}
