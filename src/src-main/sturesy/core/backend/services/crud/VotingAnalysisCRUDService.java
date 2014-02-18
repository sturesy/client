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

import javax.swing.table.TableModel;

import sturesy.core.backend.services.FileIOService;

/**
 * A Create Read update delete service for voting analysis content. Not every
 * method is supported by default maybe it has to be extended for new
 * functionality.
 * 
 * @author jens.dallmann, w.posdorfer
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
     * @param model
     *            the votes for which the result file should be build, coming
     *            directly from the TableModel
     * @param file
     *            the file where it has to be stored
     * @return String if an error occurs the result is an error message, if no
     *         error occurs it will return <code>null</code>
     */
    public String saveVotingResult(TableModel model, File file)
    {
        if (model.getRowCount() == 0)
        {
            return NO_VOTES_TO_EXPORT;
        }

        StringBuilder builder = new StringBuilder();

        for (int column = 0; column < model.getColumnCount(); column++)
        {
            builder.append(model.getColumnName(column));

            if (column != model.getColumnCount() - 1)
            {
                builder.append(",");
            }
            else
            {
                builder.append("\n");
            }
        }
        for (int row = 0; row < model.getRowCount(); row++)
        {
            for (int column = 0; column < model.getColumnCount(); column++)
            {
                builder.append(model.getValueAt(row, column)).append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
            builder.append("\n");
        }

        return saveVotingResult(builder.toString(), file);
    }

    /**
     * Writes votingresults to a file
     * 
     * @param content
     *            already commaseperated values
     * @param file
     *            destination file
     * @return if an error occurs the result is an error message, if no error
     *         occurs it will return <code>null</code>
     */
    private String saveVotingResult(String content, File file)
    {
        String result = null;
        try
        {
            if (content.isEmpty())
            {
                result = NO_VOTES_TO_EXPORT;
            }
            else if (file != null)
            {
                file = _fileService.createCSVFileIfNotExist(file);
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
        return result;
    }

}
