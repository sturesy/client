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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import sturesy.Folder;
import sturesy.core.Log;

public class Settings
{

    public static final String MAINDIRECTORY = "main.directory";
    public static final String SERVERADDRESS = "server.adress";
    public static final String CLIENTADDRESS = "server.client.adress";
    public static final String POLL_FREQUENCY = "server.poll.frequency";
    public static final String WEB_PLUGIN_ENABLED = "web.plugin.enabled";
    public static final String SETTINGSWINDOWSIZE = "settings.window.size";
    public static final String VOTINGWINDOWSIZE = "voting.window.size";
    public static final String QUESTIONEDITORSIZE = "questiongenerator.window.size";
    public static final String QUESTIONEDITORDIVIDER = "questiongenerator.divider.location";
    public static final String EVALUATEWINDOWSIZE = "evaluate.window.size";
    
    public static final String FEEDBACKEDITORSIZE = "feedbackeditor.window.size";
    public static final String FEEDBACKEDITORDIVIDER = "feedbackeditor.divider.location";
    public static final String FEEDBACKVIEWERSIZE = "feedbackviewer.window.size";

    public static final String UPDATE_FREQUENCY = "update.frequency";
    public static final String UPDATE_LASTUPDATE = "update.last.update";

    private static final String DATEFORMAT = "yyyy-MM-d H:m:s";

    private static final String FILENAME = "sturesy.properties";
    private final Properties _properties;

    private static Settings _instance;

    /**
     * Returns the Instance of the Settings
     * 
     * @return Singleton-Instance
     */
    public static synchronized Settings getInstance()
    {
        if (_instance == null)
        {
            _instance = new Settings();
        }
        return _instance;
    }

    /**
     * Private Constructor
     */
    private Settings()
    {
        _properties = loadProperties();
    }

    /**
     * Returns a String from the properties, returns empty-string if
     * <code>null</code>
     */
    public String getString(String property)
    {
        Object result = _properties.get(property);
        if (result != null)
        {
            return (String) result;
        }
        else
        {
            return "";
        }
    }

    /**
     * Returns a boolean from the properties, if non-existant always returns
     * <code>false</code>
     */
    public boolean getBoolean(String property)
    {
        if (_properties.get(property) != null)
        {
            return Boolean.parseBoolean(_properties.getProperty(property));
        }

        return false;
    }

    /**
     * Returns an Integer from the properties, if non-existant always returns
     * <code>-1</code>
     */
    public int getInteger(String property)
    {

        String string = _properties.getProperty(property);
        if (string == null)
        {
            return SturesyConstants.UNDEFINEDINTEGER;
        }

        if (string.matches("[0-9]*"))
        {
            return Integer.parseInt(_properties.getProperty(property));
        }
        else
        {
            return SturesyConstants.UNDEFINEDINTEGER;
        }
    }

    /**
     * Returns a {@link Dimension} from the properties, also tries to check the
     * Defaults<br>
     * if non-existant returns {@link Settings#UNDEFINEDDIMENSION}
     */
    public Dimension getDimension(String property)
    {
        Dimension result = SturesyConstants.UNDEFINEDDIMENSION;
        String string = _properties.getProperty(property);

        if (string != null)
        {
            result = ParserUtils.parseDimension(string);
        }
        if (result == SturesyConstants.UNDEFINEDDIMENSION)
        {
            result = Defaults.getInstance().getDimension(property);
        }

        return result;
    }

    /**
     * Returns a Color from the Properties, also checks the Defaults<br>
     * Returns {@link Settings#UNDEFINEDCOLOR} if property was nonexistant
     * 
     * @param property
     *            property key
     * @return Color
     */
    public Color getColor(String property)
    {
        Color result = SturesyConstants.UNDEFINEDCOLOR;
        String string = _properties.getProperty(property);

        if (string != null)
        {
            result = ParserUtils.parseColor(string);
        }

        if (result == SturesyConstants.UNDEFINEDCOLOR)
        {
            result = Defaults.getInstance().getColor(property);
        }
        return result;
    }

    /**
     * Returns a {@link Date} from the properties, if non-existant or a parse
     * error occurs returns <code>null</code>
     */
    public Date getDate(String property)
    {
        try
        {
            String date = getString(property);
            if (date != null && !"".equals(date))
            {
                return new SimpleDateFormat(DATEFORMAT).parse(date);
            }
            else
            {
                return null;
            }
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * Save a property
     * 
     * @param prop
     *            property-key
     * @param value
     *            property-value
     */
    public synchronized void setProperty(String prop, String value)
    {
        _properties.setProperty(prop, value);
    }

    /**
     * Save a property
     * 
     * @param prop
     *            property-key
     * @param bool
     *            property-value
     */
    public synchronized void setProperty(String prop, boolean bool)
    {
        _properties.setProperty(prop, Boolean.toString(bool));
    }

    /**
     * Save a property
     * 
     * @param prop
     *            property-key
     * @param date
     *            property-value
     */
    public synchronized void setProperty(String prop, Date date)
    {
        setProperty(prop, new SimpleDateFormat(DATEFORMAT).format(date));
    }

    /**
     * Save a property
     * 
     * @param prop
     *            property-key
     * @param integer
     *            property-value
     */
    public synchronized void setProperty(String prop, int integer)
    {
        _properties.setProperty(prop, Integer.toString(integer));
    }

    /**
     * Save a property
     * 
     * @param prop
     *            property-key
     * @param dim
     *            {@link Dimension}
     */
    public synchronized void setProperty(String prop, Dimension dim)
    {
        _properties.setProperty(prop, dim.width + "," + dim.height);
    }

    /**
     * Save a property
     * 
     * @param prop
     *            property-key
     * @param color
     *            {@link Color}
     */
    public synchronized void setProperty(String prop, Color color)
    {
        _properties.setProperty(prop,
                color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "," + color.getAlpha());
    }

    /**
     * Removes a Property by name
     * 
     * @param prop
     *            to be removed
     */
    public synchronized void removeProperty(String prop)
    {
        _properties.remove(prop);
    }

    /**
     * Stores all current Properties in the Settings-File
     */
    public void save()
    {
        try
        {
            File f = new File(Folder.getBaseFolder(), FILENAME);
            _properties.store(new FileOutputStream(f), "Stored Properties");
        }
        catch (FileNotFoundException e)
        {
            Log.error("property save", e);
        }
        catch (IOException e)
        {
            Log.error("property save", e);
        }
    }

    /**
     * Returns an {@link Enumeration} of all the Keys of the underlying
     * Properties-file
     */
    public Enumeration<Object> getKeyEnumeration()
    {
        return _properties.keys();
    }

    /**
     * Loads the Properties-File, creating it if necessary
     */
    private Properties loadProperties()
    {

        Properties props = new Properties();

        File f = new File(Folder.getBaseFolder(), FILENAME);
        try
        {
            props.load(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            try
            {
                f.createNewFile();
                props.load(new FileInputStream(f));
            }
            catch (IOException e1)
            {
                Log.error("File cannot be loaded after creation?", e1);
            }

        }
        catch (IOException e)
        {
            Log.error("file cannot be loaded?", e);
        }

        return props;
    }
}
