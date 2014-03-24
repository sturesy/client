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
package sturesy.core.ui;

/**
 * HTML Stripping utility class
 * 
 * @author w.posdorfer
 * 
 */
public class HTMLStripper
{

    /**
     * Tries to strip a string of all HTML-Content
     * 
     * @param string
     *            String to be stripped
     * @return HTML-Free String
     */
    public static String stripHTML(String string)
    {

        String noHTMLString = string.replace("<br>", " ");
        noHTMLString = noHTMLString.replace("</br>", " ");
        noHTMLString = noHTMLString.replace("<br/>", " ");
        noHTMLString = noHTMLString.replace("&nbsp;", "");

        noHTMLString = noHTMLString.replaceAll("\\<.*?\\>", "");
        return noHTMLString;
    }

    /**
     * Same as {@link #stripHTML(String)} but preserves unsurrounded &lt; and
     * &gt; and &lt;= and &gt;=<br>
     * example:<br>
     * 
     * <code>a <= b >= c</code> will still be <code>a <= b >= c</code> <br>
     * <b>but</b> <code>a<=b>=c<code/> will become <code>a=c</code>
     * 
     * @param string
     *            String to be stripped
     * @return HTML-Free String
     */
    public static String stripHTML2(String string)
    {
        String noHTMLString = string;

        if (string.startsWith("<html>") && string.endsWith("</html>"))
        {
            noHTMLString = noHTMLString.replace("\n", "");

            noHTMLString = noHTMLString.replace(" < ", " &lt; ");
            noHTMLString = noHTMLString.replace(" > ", " &gt; ");

            noHTMLString = noHTMLString.replace(" <= ", " &lt;= ");
            noHTMLString = noHTMLString.replace(" >= ", " &gt;= ");

            noHTMLString = stripHTML(noHTMLString);

            noHTMLString = noHTMLString.replace("&gt;", ">");
            noHTMLString = noHTMLString.replace("&lt;", "<");
            noHTMLString = noHTMLString.trim();
        }
        
        if(string.equals(noHTMLString))
        {
            noHTMLString = stripHTML(string);
        }
        return noHTMLString;
    }

    /** Util class, so no constructor */
    private HTMLStripper()
    {
    }
}
