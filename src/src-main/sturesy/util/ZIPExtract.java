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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZIPExtract
{
    /**
     * See {@link #extractFolder(String, String)}
     * 
     * @param sourceZipFile
     *            sourceZip
     * @param toFolder
     *            destination folder
     * @throws IOException
     * @see #extractFolder(String, String)
     */
    public static void extractFolder(File sourceZipFile, File toFolder) throws IOException
    {
        extractFolder(sourceZipFile.getAbsolutePath(), toFolder.getAbsolutePath());
    }

    /**
     * Extracts a ZIP-File to a destination Folder
     * 
     * @param sourceZipFile
     *            the zip file to extract
     * @param toFolder
     *            the destination Folder, where the contents should be located
     * @throws IOException
     */
    public static void extractFolder(String sourceZipFile, String toFolder) throws IOException
    {
        ZipFile zip = new ZipFile(sourceZipFile);

        Enumeration<? extends ZipEntry> zipFileEntries = zip.entries();

        while (zipFileEntries.hasMoreElements())
        {
            ZipEntry entry = zipFileEntries.nextElement();

            File destFile = new File(toFolder, entry.getName());

            destFile.getParentFile().mkdirs();

            if (!entry.isDirectory())
            {
                copyFile(zip.getInputStream(entry), destFile);
            }
        }
        zip.close();
    }

    /**
     * Copies a File given an {@link InputStream} to a destination File
     * 
     * @param inStream
     *            InputStream of a File
     * @param destFile
     *            Destination File
     * @throws IOException
     */
    private static void copyFile(InputStream inStream, File destFile) throws IOException
    {
        final int BUFFER = 2048;

        BufferedInputStream is = new BufferedInputStream(inStream);
        int currentByte;
        byte data[] = new byte[BUFFER];

        FileOutputStream fos = new FileOutputStream(destFile);
        BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

        while ((currentByte = is.read(data, 0, BUFFER)) != -1)
        {
            dest.write(data, 0, currentByte);
        }
        dest.flush();
        dest.close();
        is.close();

    }

}
