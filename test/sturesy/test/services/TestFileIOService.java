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
package sturesy.test.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.mockito.Mockito;

import sturesy.core.backend.services.FileIOService;
import sturesy.tools.LoadTestFilesService;

public class TestFileIOService
{

    @Test
    public void testCreateIfFileNotExistFileExist() throws IOException
    {
        FileIOService service = new FileIOService();
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        service.createFileIfNotExist(file);
        verify(file, times(0)).createNewFile();
    }

    @Test
    public void testCreateIfFileNotExist() throws IOException
    {
        FileIOService service = new FileIOService();
        File file = mock(File.class);
        when(file.exists()).thenReturn(false);
        service.createFileIfNotExist(file);
        verify(file, times(1)).createNewFile();
    }

    @Test
    public void testCreateIfFileNotExistException() throws IOException
    {
        FileIOService service = new FileIOService();
        File file = mock(File.class);
        when(file.exists()).thenReturn(false);
        when(file.createNewFile()).thenThrow(new IOException());
        service.createFileIfNotExist(file);
        verify(file, times(1)).createNewFile();
    }

    @Test
    public void testCreateIfFileNotExistString() throws IOException
    {
        String filepath = "path/to/nowhere.txt";

        String expectedFilepath = filepath.replace("/", "" + File.separatorChar);

        FileIOService service = new FileIOService();
        FileIOService serviceSpy = spy(service);
        Mockito.doReturn(new File("path/to/nowhere.txt")).when(serviceSpy).createFileIfNotExist(Mockito.<File> any());
        File newFile = serviceSpy.createFileIfNotExist(filepath);
        assertEquals(expectedFilepath, newFile.getPath());
        verify(serviceSpy, times(1)).createFileIfNotExist(Mockito.<File> any());
    }

    @Test
    public void testWriteToFileCreateIfNotExistWithFile() throws IOException
    {
        FileIOService service = new FileIOService();
        FileIOService spy = spy(service);
        File file = mock(File.class);
        Mockito.doReturn(file).when(spy).createFileIfNotExist(file);
        Mockito.doReturn(true).when(spy).writeToFile(file, "");
        when(file.exists()).thenReturn(true);
        spy.writeToFileCreateIfNotExist(file, "");
        verify(spy, times(1)).createFileIfNotExist(file);
        verify(spy, times(1)).writeToFile(file, "");
    }

    @Test
    public void testWriteToFileCreateIfNotExistWithString() throws IOException
    {
        FileIOService service = new FileIOService();
        FileIOService spy = spy(service);
        File file = mock(File.class);
        Mockito.doReturn(true).when(spy).writeToFile(Mockito.<File> any(), Mockito.eq(""));
        Mockito.doReturn(file).when(spy).createFileIfNotExist("filepath");
        spy.writeToFileCreateIfNotExist("filepath", "");
        verify(spy, times(1)).createFileIfNotExist("filepath");
        verify(spy, times(1)).writeToFile(Mockito.<File> any(), Mockito.eq(""));
    }

    @Test
    public void testCreateCSVFileIfNotExistFileEndsWithCSV() throws IOException
    {
        LoadTestFilesService testFileService = new LoadTestFilesService();
        File file = testFileService.retrieveCSVFile();
        FileIOService serviceUnderTest = new FileIOService();
        FileIOService spyUnderTest = spy(serviceUnderTest);
        Mockito.doReturn(file).when(spyUnderTest).createFileIfNotExist(Mockito.<File> any());
        File createdFile = spyUnderTest.createCSVFileIfNotExist(file);
        assertSame(createdFile, file);
    }

    @Test
    public void testCreateCSVFileIfNotExistFileNotEndsWithCSV() throws IOException
    {
        LoadTestFilesService testFileService = new LoadTestFilesService();
        File file = testFileService.retrieveNotACSVFile();
        FileIOService serviceUnderTest = new FileIOService();
        FileIOService spyUnderTest = spy(serviceUnderTest);
        Mockito.doReturn(file).when(spyUnderTest).createFileIfNotExist(Mockito.<File> any());
        File createdFile = spyUnderTest.createCSVFileIfNotExist(file);
        assertNotSame(createdFile, file);
        assertTrue(createdFile.getName().endsWith(".csv"));
    }
}
