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

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Properties;

import sturesy.core.Log;
import sturesy.core.backend.Loader;

public class Defaults
{

    private static Defaults _instance;

    private static final String FILENAME = "defaults/defaults.properties";

    private final Properties _defaults;

    /**
     * Returns the Instance of the Defaults
     * 
     * @return Singleton-Instance
     */
    public static synchronized Defaults getInstance()
    {
        if (_instance == null)
        {
            _instance = new Defaults();
        }
        return _instance;
    }

    public Color getColor(String prop)
    {
        String string = _defaults.getProperty(prop);

        if (string != null)
        {
            return ParserUtils.parseColor(string);
        }
        else
        {
            return SturesyConstants.UNDEFINEDCOLOR;
        }
    }

    public Dimension getDimension(String prop)
    {
        String string = _defaults.getProperty(prop);

        if (string != null)
        {
            return ParserUtils.parseDimension(string);
        }
        else
        {
            return SturesyConstants.UNDEFINEDDIMENSION;
        }
    }

    /**
     * Private Constructor
     */
    private Defaults()
    {
        _defaults = loadProperties();
    }

    /**
     * Loads the Properties-File, creating it if necessary
     */
    private Properties loadProperties()
    {

        Properties props = new Properties();
        try
        {
            props.load(Loader.getResourceAsStream(FILENAME));
        }
        catch (IOException e)
        {
            Log.error("file cannot be loaded?", e);
        }

        return props;
    }

}
