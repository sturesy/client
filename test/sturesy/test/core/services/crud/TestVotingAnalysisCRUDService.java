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
package sturesy.test.core.services.crud;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import sturesy.core.backend.services.FileIOService;
import sturesy.core.backend.services.crud.VotingAnalysisCRUDService;
import sturesy.items.Vote;
import sturesy.items.vote.SingleVote;


///XXX FIX TEST

@RunWith(MockitoJUnitRunner.class)
public class TestVotingAnalysisCRUDService
{

    private VotingAnalysisCRUDService _crudService;

    @Mock
    private FileIOService _fileService;

    @Test
    public void testSaveVotingResultVotesAreNull() throws IOException
    {
        createCRUDService();
        String result = _crudService.saveVotingResult(null, null);

        assertEquals(VotingAnalysisCRUDService.NO_VOTES_TO_EXPORT, result);
        verify(_fileService, times(0)).createCSVFileIfNotExist(Mockito.<File> any());
        verify(_fileService, times(0)).writeToFile(Mockito.<File> any(), anyString());
    }

    @Test
    public void testSaveVotingResultVotesExistFileNull() throws IOException
    {
        // createCRUDService();
        // Set<Vote> votes = createFilledVoteSet();
        // String result = _crudService.saveVotingResult(votes, null);
        //
        // assertEquals(VotingAnalysisCRUDService.NO_FILE_TO_EXPORT_TO, result);
        // verify(_fileService, times(0)).createCSVFileIfNotExist(Mockito.<File>
        // any());
        // verify(_fileService, times(0)).writeToFile(Mockito.<File> any(),
        // anyString());
    }

    @Test
    public void testSaveVotingResultVotesExistFileExist() throws IOException
    {
        // createCRUDService();
        // Set<Vote> votes = createFilledVoteSet();
        // File file = mock(File.class);
        // String result = _crudService.saveVotingResult(votes, file);
        //
        // assertNull(result);
        // verify(_fileService, times(1)).createCSVFileIfNotExist(Mockito.<File>
        // any());
        // verify(_fileService, times(1)).writeToFile(file,
        // "2,B,1000\n1,B,1000\n");
    }

    @Test
    public void testSaveVotingResultFileServiceThrowsException() throws IOException
    {
        // createCRUDService();
        // File file = mock(File.class);
        // when(_fileService.createCSVFileIfNotExist(file)).thenThrow(new
        // IOException());
        // Set<Vote> votes = createFilledVoteSet();
        // String result = _crudService.saveVotingResult(votes, file);
        //
        // assertEquals(VotingAnalysisCRUDService.ERROR_EXPORTING_TO_CSV,
        // result);
        // verify(_fileService, times(1)).createCSVFileIfNotExist(file);
        // verify(_fileService, times(0)).writeToFile(Mockito.<File> any(),
        // anyString());
    }

    @Test
    public void testSaveVotingResultVotesAreEmpty() throws IOException
    {
        // createCRUDService();
        // File file = mock(File.class);
        // Set<Vote> votes = new HashSet<Vote>();
        //
        // String result = _crudService.saveVotingResult(votes, file);
        //
        // assertEquals(VotingAnalysisCRUDService.NO_VOTES_TO_EXPORT, result);
        // verify(_fileService, times(0)).createCSVFileIfNotExist(Mockito.<File>
        // any());
        // verify(_fileService, times(0)).writeToFile(Mockito.<File> any(),
        // anyString());
    }

    private Set<Vote> createFilledVoteSet()
    {
        Set<Vote> votes = new HashSet<Vote>();
        votes.add(new SingleVote("1", 1, 1000));
        votes.add(new SingleVote("2", 1, 1000));
        return votes;
    }

    private void createCRUDService()
    {
        _crudService = new VotingAnalysisCRUDService(_fileService);
    }
}
