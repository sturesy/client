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
package sturesy.test.core.ui.filter.filechooser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Test;

import sturesy.core.backend.filter.filechooser.QuestionSetFileFilter;

public class TestQuestionSetFileFilter
{
    @Test
    public void testFileIsDirectory()
    {
        QuestionSetFileFilter filter = new QuestionSetFileFilter();

        File passedFile = mock(File.class);
        when(passedFile.isDirectory()).thenReturn(true);
        boolean isAccepted = filter.accept(passedFile);
        assertTrue(isAccepted);
    }

    @Test
    public void testFileIsNoDirectoryEndsWithXMLNotWithVotingXML()
    {
        QuestionSetFileFilter filter = new QuestionSetFileFilter();
        File passedFile = mock(File.class);
        when(passedFile.isDirectory()).thenReturn(false);
        when(passedFile.getAbsolutePath()).thenReturn("test.xml");
        boolean isAccepted = filter.accept(passedFile);
        assertTrue(isAccepted);
    }

    @Test
    public void testFileIsNoDirectoryEndsWithVotingXML()
    {
        QuestionSetFileFilter filter = new QuestionSetFileFilter();
        File passedFile = mock(File.class);
        when(passedFile.isDirectory()).thenReturn(false);
        when(passedFile.getAbsolutePath()).thenReturn("test_voting.xml");
        boolean isAccepted = filter.accept(passedFile);
        assertFalse(isAccepted);
    }

    @Test
    public void testGetDescription()
    {
        QuestionSetFileFilter filter = new QuestionSetFileFilter();
        assertEquals("*.xml", filter.getDescription());
    }
}
