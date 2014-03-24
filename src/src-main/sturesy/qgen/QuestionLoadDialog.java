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
package sturesy.qgen;

import java.io.File;

import sturesy.core.backend.filter.filechooser.QuestionSetFileFilter;
import sturesy.core.ui.loaddialog.AbstractQuestionLoadDialogController;
import sturesy.core.ui.loaddialog.LoadDialog;

/**
 * Class to handle loading of QuestionSets either from the specified
 * Maindirectory or from external source
 * 
 * @author w.posdorfer
 */
public class QuestionLoadDialog extends AbstractQuestionLoadDialogController
{

    private final File _lecturesDirectory;

    public QuestionLoadDialog(File lecturesDirectory)
    {
        super(new QuestionSetFileFilter(), lecturesDirectory);
        _lecturesDirectory = lecturesDirectory;
    }

    public QuestionLoadDialog(LoadDialog loadDialog, File lecturesDirectory)
    {
        super(loadDialog, lecturesDirectory);
        _lecturesDirectory = lecturesDirectory;
    }

    /**
     * shows up dialog
     */
    public void show()
    {
        showLoadDialog();
    }

    /**
     * Listener method when the Selection has changed
     */
    @Override
    public void lectureListValueChanged(File directory)
    {
        passNewQuestionSetToLoadDialog(directory);
    }

    @Override
    public void externalFileHasBeenLoaded(File file)
    {
        if (file != null)
        {
            super.externalFileHasBeenLoaded(file);
        }
        closeLoadDialog();
    }

    @Override
    public void internalFileHasBeenLoaded(File file)
    {
        super.internalFileHasBeenLoaded(file);
        closeLoadDialog();
    }

    /**
     * Returns the Title of the loaded QuestionSet, if it is an external File it
     * will be the absolute path of the File, if it is internal, the absolute
     * path will be stripped of the passed File resulting in a relative path
     * 
     * @return an absolute file path or a relative path
     */
    public String getTitle()
    {
        File loadedFile = getLoadedFile();
        if (loadedFile != null)
        {
            if (isExternal())
            {
                return loadedFile.getAbsolutePath();
            }
            else
            {
                return loadedFile.getAbsolutePath().replace(_lecturesDirectory.getAbsolutePath(), "");
            }
        }
        else
        {
            return "";
        }
    }
}
