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
package sturesy.core.backend.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import sturesy.core.Log;

public class FileIOService
{

    public File createFileIfNotExist(File file)
    {
        try
        {
            if (!file.exists())
            {
                file.createNewFile();
            }
        }
        catch (IOException e)
        {
            Log.error("error creating file:" + file, e);
        }
        return file;
    }

    public File createFileIfNotExist(String filepath)
    {
        File file = new File(filepath);
        return createFileIfNotExist(file);
    }

    public boolean writeToFile(File file, String content) throws IOException
    {
        FileWriter fileWriter = new FileWriter(file, false);
        return writeToFile(fileWriter, content);
    }

    public boolean writeToFile(FileWriter fileWriter, String content) throws IOException
    {
        try
        {
            fileWriter.write(content);
        }
        catch (IOException ioex)
        {
            Log.error("error saving questionset", ioex);
            throw ioex;
        }
        finally
        {
            if (fileWriter != null)
            {
                try
                {
                    fileWriter.close();
                }
                catch (IOException ioex2)
                {
                }
            }
        }
        return true;

    }

    public boolean writeToFile(String filepath, String content) throws IOException
    {
        File file = new File(filepath);
        return writeToFile(file, content);
    }

    public boolean writeToFileCreateIfNotExist(File file, String content) throws IOException
    {
        createFileIfNotExist(file);
        return writeToFile(file, content);
    }

    public boolean writeToFileCreateIfNotExist(String filepath, String content) throws IOException
    {
        createFileIfNotExist(filepath);
        return writeToFile(filepath, content);
    }

    public File createCSVFileIfNotExist(File csvFile) throws IOException
    {
        File newFile = csvFile;
        if (!csvFile.getName().endsWith(".csv"))
        {
            newFile = new File(csvFile + ".csv");
        }
        createFileIfNotExist(newFile);
        return newFile;
    }
}
