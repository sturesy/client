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
package sturesy.votinganalysis;

import java.io.File;
import java.io.FileFilter;
import java.util.Map;
import java.util.Set;

import sturesy.core.backend.filter.file.LectureFileFilter;
import sturesy.core.backend.filter.filechooser.QuestionSetFileFilter;
import sturesy.core.backend.services.crud.VotingCRUDService;
import sturesy.core.error.ErrorController;
import sturesy.core.error.XMLException;
import sturesy.core.ui.loaddialog.AbstractQuestionLoadDialogController;
import sturesy.core.ui.loaddialog.LoadDialog;
import sturesy.items.Vote;

public class AnalysisLoadDialog extends AbstractQuestionLoadDialogController
{
    private final String VOTING = "_voting.xml";

    private Map<Integer, Set<Vote>> _votes;

    public AnalysisLoadDialog(File lectureDirectory)
    {
        super(new QuestionSetFileFilter(), lectureDirectory);
    }

    public AnalysisLoadDialog(LoadDialog loadDialog, File lectureDirectory)
    {
        super(loadDialog, lectureDirectory);
    }

    @Override
    public void internalFileHasBeenLoaded(File questionFile)
    {
        File votingResultFile = new File(questionFile.getAbsolutePath().replace(".xml", VOTING));

        if (readFile(votingResultFile))
        {
            super.internalFileHasBeenLoaded(questionFile);
            closeLoadDialog();
        }
        else
        {
            showErrorMessage("error.voting.file.no.result");
        }
    }

    @Override
    public void externalFileHasBeenLoaded(File questionFile)
    {
        if (questionFile != null)
        {
            File votingResultFile = new File(questionFile.getAbsolutePath().replace(".xml", VOTING));
            if (readFile(votingResultFile))
            {
                super.externalFileHasBeenLoaded(questionFile);
                closeLoadDialog();
            }
            else
            {
                showErrorMessage("error.voting.file.no.result");
            }
        }
    }

    public Map<Integer, Set<Vote>> getVotes()
    {
        return _votes;
    }

    private boolean readFile(File file)
    {
        VotingCRUDService service = new VotingCRUDService();
        try
        {
            _votes = service.readVoting(file);
        }
        catch (XMLException e)
        {
            ErrorController con = new ErrorController();
            con.insertError(e.getClass().getSimpleName(), e);
            con.show();
        }
        return _votes.keySet().size() != 0 && _votes.entrySet().size() != 0;
    }

    @Override
    protected FileFilter getCustomFileFilter()
    {
        return new LectureFileFilter();
    }

    public void show()
    {
        showLoadDialog();
    }
}
