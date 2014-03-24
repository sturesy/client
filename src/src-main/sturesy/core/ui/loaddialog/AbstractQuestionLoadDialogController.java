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
package sturesy.core.ui.loaddialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.filechooser.FileFilter;

import sturesy.core.Localize;
import sturesy.core.backend.filter.file.FolderFileFilter;
import sturesy.core.backend.filter.file.NameXMLFileFilter;
import sturesy.core.backend.filter.filechooser.XMLFileFilter;
import sturesy.core.backend.services.crud.QuestionCRUDService;
import sturesy.core.error.ErrorController;
import sturesy.core.error.XMLException;
import sturesy.items.LectureID;
import sturesy.items.QuestionSet;

public class AbstractQuestionLoadDialogController
{
    private boolean _isExternal = false;
    private QuestionSet _loadedQuestionSet = null;
    private File _loadedFile = null;
    protected LoadDialog _loadDialog;
    private File _lecturesDirectory;

    public AbstractQuestionLoadDialogController(File lecturesDirectory)
    {
        this(new LoadDialog(lecturesDirectory.getAbsolutePath(), Localize.getString("label.load.question.set")), lecturesDirectory);
    }

    public AbstractQuestionLoadDialogController(FileFilter fileFilter, File lecturesDirectory)
    {
        this(new LoadDialog(lecturesDirectory.getAbsolutePath(), Localize.getString("label.load.question.set")), fileFilter,
                lecturesDirectory);
    }

    public AbstractQuestionLoadDialogController(LoadDialog loadDialog, File lecturesDirectory)
    {
        this(loadDialog, new XMLFileFilter(), lecturesDirectory);
    }

    public AbstractQuestionLoadDialogController(LoadDialog loadDialog, FileFilter fileFilter, File lecturesDirectory)
    {
        _loadDialog = loadDialog;
        _lecturesDirectory = lecturesDirectory;
        loadDialog.setFileFilter(fileFilter);
        createLectureList();
        File questionSetDirectory = getFirstLectureFolder();
        if (questionSetDirectory != null)
        {
            passNewQuestionSetToLoadDialog(questionSetDirectory);
        }
        addListener();
    }

    public void setExternalFileFlag(boolean isExternalFile)
    {
        _isExternal = isExternalFile;
    }

    /**
     * Return the First Lecture-Folder in the maindirectory
     * 
     * @return a Folder thats should hold questionsets
     */
    protected File getFirstLectureFolder()
    {
        String lectureDirectoryPath = _lecturesDirectory.getAbsolutePath();

        File[] questionSets = _lecturesDirectory.listFiles(new FolderFileFilter());
        if (questionSets != null && questionSets.length > 0)
        {
            return new File(lectureDirectoryPath + File.separator + questionSets[0].getName());
        }
        return null;
    }

    /**
     * Looks through the specified directory to find files matching
     * {@link #getCustomFileFilter()}, then adds them to the right list of the
     * JListPair
     * 
     * @param directory
     *            Directory to look for Files
     */
    public void passNewQuestionSetToLoadDialog(File directory)
    {
        if (directory.isDirectory())
        {
            File[] questionSetsInDirectory = directory.listFiles(getCustomFileFilter());
            if (questionSetsInDirectory.length > 0)
            {
                List<String> newQuestionListContent = new ArrayList<String>();
                for (File oneQuestionSet : questionSetsInDirectory)
                {
                    newQuestionListContent.add(oneQuestionSet.getName());
                }
                setNewQuestionSetNamesToLoadDialog(newQuestionListContent);
            }
            else
            {
                setNewQuestionSetNamesToLoadDialog(new ArrayList<String>());
            }
        }
    }

    private void addListener()
    {
        _loadDialog.registerListener(new LoadDialogListener()
        {
            @Override
            public void internalFileLoaded(File file)
            {
                internalFileHasBeenLoaded(file);
            }

            @Override
            public void externalFileLoaded(File file)
            {
                externalFileHasBeenLoaded(file);
            }

            @Override
            public void subsetSourceListChanged(File newDirectory)
            {
                lectureListValueChanged(newDirectory);
            }
        });
    }

    public void lectureListValueChanged(File newDirectory)
    {
        passNewQuestionSetToLoadDialog(newDirectory);
    }

    public void externalFileHasBeenLoaded(File file)
    {
        if (file != null)
        {
            _loadedQuestionSet = parseQuestionFile(file);
            _loadedFile = file;
            _isExternal = true;
        }
    }

    public void internalFileHasBeenLoaded(File file)
    {
        _loadedQuestionSet = parseQuestionFile(file);
        _loadedFile = file;
        _isExternal = false;
    }

    /**
     * Parses the given XML-File and returns it as QuestionSet
     * 
     * @param file
     */
    protected QuestionSet parseQuestionFile(File file)
    {
        QuestionCRUDService qp = new QuestionCRUDService();

        QuestionSet result = null;
        try
        {
            result = qp.readQuestionSet(file);
        }
        catch (XMLException e)
        {
            ErrorController con = new ErrorController();
            con.insertError(e.getClass().getSimpleName(), e);
            con.show();
        }

        return result;
    }

    protected java.io.FileFilter getCustomFileFilter()
    {
        return new NameXMLFileFilter();
    }

    /**
     * Is this an External File?
     * 
     * @return true if the file is located outside of the lectures-directory
     */
    public boolean isExternal()
    {
        return _isExternal;
    }

    /**
     * Returns the loaded {@link QuestionSet}
     */
    public QuestionSet getLoadedQuestionSet()
    {
        return _loadedQuestionSet;
    }

    /**
     * Returns the {@link File} for the loaded QuestionSet
     */
    public File getLoadedFile()
    {
        return _loadedFile;
    }

    public void setLoadedFile(File file)
    {
        _loadedFile = file;
    }

    public void closeLoadDialog()
    {
        _loadDialog.closeDialog();
    }

    public void showErrorMessage(String resourceKey)
    {
        _loadDialog.showErrorMessage(resourceKey);
    }

    public void showLoadDialog()
    {
        _loadDialog.show();
    }

    /**
     * loads the LectureList into the JList
     */
    public void createLectureList()
    {
        File[] files = _lecturesDirectory.listFiles();

        List<String> newContent = new ArrayList<String>();
        for (File oneFile : files)
        {
            if (oneFile.isDirectory())
            {
                newContent.add(oneFile.getName());
            }
        }
        setLectureListInLoadDialog(newContent);
    }

    public void setLectureListInLoadDialog(List<String> newContent)
    {
        _loadDialog.setNewSourceListContent(newContent);
    }

    public void setNewQuestionSetNamesToLoadDialog(List<String> newQuestionListContent)
    {
        _loadDialog.setNewContentListContent(newQuestionListContent);
    }

    public void setLoadedDialogNonModal()
    {
        _loadDialog.setModal(false);
    }

    public void addExtraLoadDialogButton(JButton extraButton)
    {
        _loadDialog.addExtraButton(extraButton);
    }

    protected void addLabeledCombobox(LabeledCombobox<LectureID> labeledCombobox)
    {
        _loadDialog.replaceNorthernPanel(labeledCombobox.getPanel());
    }

    public void setLoadedQuestionSet(QuestionSet loadedQuestionSet)
    {
        _loadedQuestionSet = loadedQuestionSet;
    }

    public String getLoadedFileAbsolutePath()
    {
        return _loadedFile.getAbsolutePath();
    }

    /**
     * Has a File been loaded and a questionset been parsed
     * 
     * @return <code>true</code> if a file has been loaded and a questionset has
     *         been parsed
     */
    public boolean isFileLoaded()
    {
        return _loadedFile != null && _loadedQuestionSet != null;
    }
}
