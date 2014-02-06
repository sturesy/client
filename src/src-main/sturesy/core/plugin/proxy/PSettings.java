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
package sturesy.core.plugin.proxy;

import java.awt.Dimension;
import java.util.Enumeration;

import sturesy.util.Settings;

/**
 * Proxy Settings for Plugins
 * 
 * @author w.posdorfer
 * 
 */
public class PSettings
{

    private static Settings _settings = Settings.getInstance();

    private PSettings()
    {
    }

    /**
     * @see Settings#getString(String)
     */
    public static String getString(String property)
    {
        return _settings.getString(property);
    }

    /**
     * @see Settings#getBoolean(String)
     */
    public static boolean getBoolean(String property)
    {
        return _settings.getBoolean(property);
    }

    /**
     * @see Settings#getInteger(String)
     */
    public static int getInteger(String property)
    {
        return _settings.getInteger(property);
    }

    /**
     * @see Settings#getDimension(String)
     */
    public static Dimension getDimension(String property)
    {
        return _settings.getDimension(property);
    }

    /**
     * @see Settings#setProperty(String, String)
     */
    public static synchronized void setProperty(String prop, String value)
    {
        _settings.setProperty(prop, value);
    }

    /**
     * @see Settings#setProperty(String, boolean)
     */
    public static synchronized void setProperty(String prop, boolean bool)
    {
        _settings.setProperty(prop, bool);
    }

    /**
     * @see Settings#setProperty(String, int)
     */
    public static synchronized void setProperty(String prop, int integer)
    {
        _settings.setProperty(prop, integer);
    }

    /**
     * @see Settings#setProperty(String, Dimension)
     */
    public static synchronized void setProperty(String prop, Dimension dim)
    {
        _settings.setProperty(prop, dim);
    }

    /**
     * @see Settings#removeProperty(String)
     */
    public static synchronized void removeProperty(String prop)
    {
        _settings.removeProperty(prop);
    }

    /**
     * @see Settings#getKeyEnumeration()
     */
    public static Enumeration<Object> getKeyEnumeration()
    {
        return _settings.getKeyEnumeration();
    }

    /**
     * @see Settings#save()
     */
    public static void save()
    {
        _settings.save();
    }

}
