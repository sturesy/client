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
package net.sourceforge.sturesy.update;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * Entry Class for Updater
 * 
 * @author w.posdorfer
 */
public class Startup
{

    private static final String STURESY_UPDATE_JAR = "sturesy-update.jar";
    private static final String STURESY_JAR = "sturesy.jar";

    /**
     * The old sturesy.jar which will be replaced
     */
    private static File oldSturesyFile;
    /**
     * The new sturesy.jar which will be relocated
     */
    private static File newSturesyFile;

    /**
     * Logging the update process
     */
    private EasyFileWriter _writer;

    public static void main(String[] args)
    {
        // Waiting for sturesy to shutdown
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        new Startup();
    }

    public Startup()
    {
        _writer = new EasyFileWriter();
        _writer.initFileWriter(new File("../update.log"), true);

        _writer.writeln("Starting Update on " + Calendar.getInstance().getTime());
        _writer.writeln("Current Folder" + new File(".").getAbsolutePath() + "\n");

        try
        {
            _writer.writeln("Finding Files");
            findAllFiles();

            if (oldSturesyFile != null && newSturesyFile != null)
            {
                _writer.writeln("Replacing Jars");
                replaceJars();

                restart();
            }
            else
            {
                _writer.writeln("Can't find files");
            }
        }
        catch (IOException e)
        {
            _writer.write(e.getMessage());
            e.printStackTrace();
        }
        exit();
    }

    private void findAllFiles() throws IOException
    {
        File currentdirCanonical = new File(".").getCanonicalFile();

        for (File f : currentdirCanonical.listFiles())
        {

            _writer.writeln(" > " + f);

            if (f.getCanonicalPath().contains("update" + File.separator + STURESY_JAR))
            {
                newSturesyFile = f.getCanonicalFile();
            }
            else if (f.getCanonicalPath().contains(STURESY_JAR)
                    && !new File(f.getCanonicalPath().replace(STURESY_JAR, STURESY_UPDATE_JAR)).exists())
            {
                oldSturesyFile = f.getCanonicalFile();
            }
            else if (f.getCanonicalPath().endsWith("update") && f.isDirectory())
            {
                for (File updatefolderfile : f.listFiles())
                {
                    if (updatefolderfile.getCanonicalPath().contains("update" + File.separator + STURESY_JAR))
                    {
                        newSturesyFile = updatefolderfile.getCanonicalFile();
                    }
                }
            }

        }

        if (oldSturesyFile == null && isMac() && isInAppBundle())
        {
            File jarInAppBundle = new File("./../Java/" + STURESY_JAR);
            if (jarInAppBundle.exists())
            {
                oldSturesyFile = jarInAppBundle;
            }
        }

        if (oldSturesyFile == null)
        {
            File possible = new File(currentdirCanonical.getParentFile(), STURESY_JAR);

            if (possible.exists() && !possible.getCanonicalPath().contains("update/" + STURESY_JAR))
            {
                oldSturesyFile = possible;
            }
        }

    }

    private boolean replaceJars() throws IOException
    {
        if (oldSturesyFile.exists() && newSturesyFile.exists())
        {
            oldSturesyFile.delete();
        }

        _writer.writeln("Replacing files:");
        _writer.writeln(" this:" + oldSturesyFile.getAbsolutePath());
        _writer.writeln(" with:" + newSturesyFile.getAbsolutePath());

        return newSturesyFile.renameTo(oldSturesyFile);
    }

    private void restart() throws IOException
    {
        File toRestart = oldSturesyFile;

        if (isMac() && (isInAppBundle() || comesWithLauncher()))
        {
            toRestart = restartMac();
        }

        if (isWindows() && comesWithExe())
        {
            toRestart = restartWindowsExe();
        }

        _writer.writeln("Restarting Sturesy using:");
        _writer.writeln(" -> " + toRestart.getAbsolutePath());

        Desktop.getDesktop().open(toRestart);

        exit();
    }

    private static File restartWindowsExe()
    {
        File[] currentDir = oldSturesyFile.getParentFile().listFiles();

        for (File f : currentDir)
        {
            if (f.getName().toLowerCase().equals("sturesy.exe"))
            {
                return f;
            }
        }

        return oldSturesyFile;
    }

    private static boolean comesWithExe()
    {
        return restartWindowsExe() != oldSturesyFile;
    }

    private static boolean comesWithLauncher()
    {
        File dir = new File("./../StuReSy.app");
        return dir.exists();
    }

    private static boolean isMac()
    {
        return System.getProperty("os.name").toLowerCase().contains("mac os x");
    }

    private static boolean isWindows()
    {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    private static boolean isInAppBundle()
    {
        File currentdir = new File(".").getAbsoluteFile();
        String absPath = currentdir.getAbsolutePath();

        int indexOfAPP = absPath.indexOf(".app/Contents/Resources");
        return indexOfAPP != -1;
    }

    private static File restartMac() throws IOException
    {
        File currentdir = new File(".").getAbsoluteFile();
        String absPath = currentdir.getAbsolutePath();

        int indexOfAPP = absPath.indexOf(".app/Contents/Resources");
        if (indexOfAPP != -1)
        {
            return new File(absPath.substring(0, indexOfAPP + 4)).getCanonicalFile();
        }
        else if (comesWithLauncher())
        {
            return new File("./../StuReSy.app").getCanonicalFile();
        }
        else
        {
            return oldSturesyFile;
        }
    }

    public void exit()
    {
        _writer.writeln("\n\n\n");
        _writer.closeFileWriter();

        System.exit(0);
    }
}
