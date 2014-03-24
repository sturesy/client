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
package sturesy.core;

import java.text.MessageFormat;
import java.util.PropertyResourceBundle;

/**
 * Provides localization
 * 
 * @author w.posdorfer
 * 
 */
public class Localize
{

    public static String SETTINGS = "settings";
    public static String MAINSCREEN = "main.screen";
    public static String QUESTIONEDITOR = "question.editor";
    public static String VOTING = "voting";
    public static String VOTINGANALYSIS = "voting.analysis";

    public static String MAINSETTINGS = "main.settings";
    public static String WEBSETTINGS = "web.settings";

    private static PropertyResourceBundle _propresbundle;

    static
    {
        _propresbundle = (PropertyResourceBundle) PropertyResourceBundle.getBundle("i18n/locale");
    }

    /**
     * returns a localized String
     * 
     * @param propertyName
     * 
     * @see PropertyResourceBundle#getString(String)
     */
    public static String getString(String propertyName)
    {
        return _propresbundle.getString(propertyName);
    }

    /**
     * Returns a localized String and substitutes keywords with given elements
     * 
     * @param propertyName
     * @param obj
     */
    public static String getString(String propertyName, Object... obj)
    {
        String str = _propresbundle.getString(propertyName);
        if (str == null)
        {
            return propertyName;
        }

        return MessageFormat.format(str, obj);
    }

    /**
     * Returns this PropertyResourceBundle
     */
    public static PropertyResourceBundle getBundle()
    {
        return _propresbundle;
    }

}
