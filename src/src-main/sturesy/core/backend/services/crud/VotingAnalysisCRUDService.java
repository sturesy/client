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
package sturesy.core.backend.services.crud;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import sturesy.core.backend.services.FileIOService;
import sturesy.items.Vote;

/**
 * A Create Read update delete service for voting analysis content. Not every
 * method is supported by default maybe it has to be extended for new
 * functionality.
 * 
 * @author jens.dallmann
 */
public class VotingAnalysisCRUDService
{
    /**
     * Error message which occurs if null is passed for the votes
     */
    public static final String NO_VOTES_TO_EXPORT = "No Votes to export";
    /**
     * Error message which occurs if the something went wrong on exporting
     */
    public static final String ERROR_EXPORTING_TO_CSV = "Error exporting to CSV";
    /**
     * Error message which occurs if null is passed for the file
     */
    public static final String NO_FILE_TO_EXPORT_TO = "No File to export to";

    /**
     * File IO Service to create and write files
     */
    private FileIOService _fileService;

    public VotingAnalysisCRUDService()
    {
        this(new FileIOService());
    }

    public VotingAnalysisCRUDService(FileIOService fileService)
    {
        _fileService = fileService;
    }

    /**
     * Creates a csv file with the result of a voting
     * 
     * @param votes
     *            the votes for which the result file should be build
     * @param file
     *            the file where it has to be stored
     * @return String if an error occurs the result is an error message, if no
     *         error occurs it will return null
     */
    public String saveVotingResult(Set<Vote> votes, File file)
    {
        String result = null;
        if (CollectionUtils.isNotEmpty(votes))
        {
            String content = createCSVContent(votes);
            try
            {
                if (file != null)
                {
                    _fileService.createCSVFileIfNotExist(file);
                    _fileService.writeToFile(file, content);
                }
                else
                {
                    result = NO_FILE_TO_EXPORT_TO;
                }
            }
            catch (IOException e1)
            {
                result = ERROR_EXPORTING_TO_CSV;
            }
        }
        else
        {
            result = NO_VOTES_TO_EXPORT;
        }
        return result;
    }

    /**
     * places the votes in a comma separated string
     * 
     * @param votes
     *            the votes to be parse
     * @return comma separated string
     */
    private String createCSVContent(Set<Vote> votes)
    {
        StringBuffer content = new StringBuffer();
        for (Vote v : votes)
        {
            String guid = v.getGuid();
            char voteAsUpperCase = (char) ('A' + v.getVote());
            long timediff = v.getTimeDiff();
            content.append(guid + "," + voteAsUpperCase + "," + timediff + "\n");
        }
        return content.toString();
    }
}
