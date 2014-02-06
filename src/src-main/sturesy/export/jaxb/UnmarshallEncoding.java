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
package sturesy.export.jaxb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.bind.JAXB;

/**
 * Sometimes XML-marshalled files are not encoded in UTF-8, which is the
 * default, This class tries to decode such a file using other encodings
 * 
 * @author w.posdorfer
 * 
 */
public class UnmarshallEncoding
{

    /**
     * try to unmarshall a File with different encodings
     * 
     * @param xmlFile
     *            which File
     * @param clazz
     *            the class of the Object to be unmarshalled
     * @throws Exception 
     */
    public static <E> E tryOtherEncodings(File xmlFile, Class<E> clazz) throws Exception
    {
        E result = null;

        String[] encodings = new String[] { System.getProperty("sun.jnu.encoding"), "ISO-8859-1", "MacRoman", "CP1252",
                "US-ASCII" };

        for (String s : encodings)
        {
            result = tryUnmarshallWithEncoding(xmlFile, clazz, s);
            if (result != null)
            {
                return result;
            }
        }

        return result;
    }

    private static <E> E tryUnmarshallWithEncoding(File xmlFile, Class<E> clazz, String encoding) throws Exception
    {
        
        E set = null;
        FileInputStream fileinput = null;
        InputStreamReader reader = null;
        try
        {
            fileinput = new FileInputStream(xmlFile);
            reader = new InputStreamReader(fileinput, encoding);
            set = JAXB.unmarshal(reader, clazz);
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            try
            {
                if (reader != null)
                    reader.close();
                if (fileinput != null)
                    fileinput.close();
            }
            catch (IOException e)
            {
            }
        }
        return set;

    }

}
