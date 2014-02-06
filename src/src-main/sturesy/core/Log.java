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

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import sturesy.Folder;

/**
 * Basic Logger for Sturesy-Eval, provides error, warning and debug
 * 
 * @author w.posdorfer
 * 
 */
public class Log
{

    private static Logger _errorlog;
    private static Logger _warninglog;
    private static File _logdir;
    private static String _lastErrorLogEntry;
    private static String _lastWarningLogEntry;

    static
    {
        _logdir = new File(Folder.getBaseFolder(), "logs" + File.separator);
        if (!_logdir.exists())
        {
            _logdir.mkdirs();
        }

        File ERROR_LOG_FILE = new File(_logdir, "errors.log");
        File WARNING_LOG_FILE = new File(_logdir, "warning.log");

        try
        {
            // Create an appending file handler
            boolean append = true;
            FileHandler errorHandler = new FileHandler(ERROR_LOG_FILE.getCanonicalPath(), append);
            errorHandler.setFormatter(new SimpleFormatter());

            FileHandler warnHandler = new FileHandler(WARNING_LOG_FILE.getCanonicalPath(), append);
            warnHandler.setFormatter(new SimpleFormatter());

            // Add to the desired logger
            _errorlog = Logger.getAnonymousLogger();
            _errorlog.addHandler(errorHandler);

            _warninglog = Logger.getAnonymousLogger();
            _warninglog.addHandler(warnHandler);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Log an error message
     * 
     * @param msg
     */
    public static void error(String msg)
    {
        _errorlog.log(Level.SEVERE, msg);
        _lastErrorLogEntry = msg;

    }

    /**
     * Log an error message containon a Throwable
     * 
     * @param msg
     */
    public static void error(Throwable thrown)
    {
        _errorlog.log(Level.SEVERE, "", thrown);
        _lastErrorLogEntry = thrown.getMessage();
    }

    /**
     * Log an error message with Throwable
     * 
     * @param msg
     * @param thrown
     */
    public static void error(String msg, Throwable thrown)
    {
        _errorlog.log(Level.SEVERE, msg, thrown);
        _lastErrorLogEntry = msg;
    }

    /**
     * Logs a warning message
     * 
     * @param msg
     */
    public static void warning(String msg)
    {
        _warninglog.log(Level.WARNING, msg);
        _lastWarningLogEntry = msg;
    }

    /**
     * Logs a warning throwable
     * 
     * @param thrown
     */
    public static void warning(Throwable thrown)
    {
        _warninglog.log(Level.WARNING, "", thrown);
        _lastWarningLogEntry = thrown.getMessage();
    }

    /**
     * Logs a warning Message with Throwable
     * 
     * @param msg
     * @param thrown
     */
    public static void warning(String msg, Throwable thrown)
    {
        _warninglog.log(Level.WARNING, msg, thrown);
        _lastWarningLogEntry = msg;
    }

    /**
     * Debug Log<br>
     * set VM-Paramater: <code>debug.enabled=true</code>
     * 
     * @param msg
     */
    public static void debug(String msg)
    {
        if (System.getProperty("debug.enabled") != null && System.getProperty("debug.enabled").equals("true"))
        {
            _warninglog.info(msg);
        }
    }

    public static String getLastErrorLogEntry()
    {
        return _lastErrorLogEntry;
    }

    public static String getLastWarningLogEntry()
    {
        return _lastWarningLogEntry;
    }

}
