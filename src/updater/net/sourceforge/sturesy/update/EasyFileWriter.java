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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Wraps a FileWriter
 * 
 * @author w.posdorfer
 * 
 */
public class EasyFileWriter
{

    private FileWriter _writer;

    /**
     * initialize<br>
     * does not append
     * 
     * @param file
     */
    public void initFileWriter(File file)
    {
        System.out.println("Starting with:" + file);
        initFileWriter(file, false);
    }

    /**
     * initialize
     * 
     * @param file
     *            destination
     * @param append
     *            should append text?
     */
    public void initFileWriter(File file, boolean append)
    {
        if (_writer != null)
        {
            try
            {
                _writer.close();
            }
            catch (IOException e)
            {
            }
        }
        try
        {
            _writer = new FileWriter(file, append);
        }
        catch (IOException e)
        {
            System.err.println("Error creating FileWriter");
        }

    }

    /**
     * Closes the writer
     */
    public void closeFileWriter()
    {
        try
        {
            _writer.close();
        }
        catch (Exception e)
        {
            System.err.println("Error closing FileWriter");
        }
        _writer = null;
    }

    /**
     * Writes a String to the file
     * 
     * @param s
     *            String to write
     */
    public void write(String s)
    {
        try
        {
            if (_writer != null)
            {
                System.out.print(s);
                _writer.write(s);
            }
        }
        catch (IOException e)
        {
            System.err.println("Error writing FileWriter");
        }
    }

    /**
     * Writes a string to the file and ends the line
     * 
     * @param s
     *            String to write
     */
    public void writeln(String s)
    {
        write(s + '\n');
    }

}
