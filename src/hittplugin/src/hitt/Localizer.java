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
package hitt;

import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * Provides Localization for H-itt
 * 
 * @author w.posdorfer
 * 
 */
public class Localizer
{

    private static Properties _properties;

    private static final String BASENAME = "i18n/hittlocale_";
    private static final String ENDNAME = ".properties";

    static
    {
        File f = new File(MainEntry.PATHOFPLUGIN + "/" + BASENAME + Locale.getDefault().getLanguage() + ENDNAME);

        if (!f.exists())
            f = new File(MainEntry.PATHOFPLUGIN + "/" + BASENAME + "en" + ENDNAME);

        Properties properties = new Properties();
        try
        {
            properties.load(new FileInputStream(f));
        }
        catch (Exception e)
        {
        }

        _properties = properties;

    }

    /**
     * returns a localized String if the files are present, or the propertyName
     * if not
     * 
     * @param propertyName
     * @return Localized String or PropertyName
     */
    public static String getString(String propertyName)
    {
        if (_properties == null)
            return propertyName;
        else
            return _properties.getProperty(propertyName);
    }
}
