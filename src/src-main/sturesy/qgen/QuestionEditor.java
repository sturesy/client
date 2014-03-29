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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sturesy.core.Controller;
import sturesy.core.Localize;
import sturesy.core.Log;
import sturesy.core.backend.services.crud.QuestionCRUDService;
import sturesy.core.ui.JMenuItem2;
import sturesy.core.ui.MessageWindow;
import sturesy.core.ui.SwapableListModel;
import sturesy.items.MultipleChoiceQuestion;
import sturesy.items.QuestionModel;
import sturesy.items.QuestionSet;
import sturesy.items.SingleChoiceQuestion;
import sturesy.items.TextQuestion;
import sturesy.qgen.gui.QuestionEditorUI;
import sturesy.qgen.gui.QuestionEditorUI.MenuItems;
import sturesy.qgen.gui.QuestionSaveWindowUI;
import sturesy.qgen.qimport.QTIImportController;
import sturesy.qgen.qimport.QuestionImportController;
import sturesy.util.Settings;
import sturesy.voting.VotingController;

/**
 * Class for Generating Questions and Questionsets<br>
 * Consists of a List of Questions and an Edit-Interface for a selected Question
 * 
 * @author w.posdorfer
 * 
 */
public class QuestionEditor implements Controller
{

    private SwapableListModel _listmodel;

    private EditQuestionController _editPanel;

    private QuestionSet _questionSet;

    boolean _isExternalFile = false;

    private QuestionEditorUI _gui;

    private final QuestionModel _emptyModel = new SingleChoiceQuestion("", new ArrayList<String>(),
            QuestionModel.NOCORRECTANSWER, QuestionModel.UNLIMITED);

    private File _lecturesDirectory;

    private Settings _settings;

    /**
     * Creates a new QuestionEditor
     */
    public QuestionEditor(File lecturesDirectory)
    {
        _settings = Settings.getInstance();
        _lecturesDirectory = lecturesDirectory;
        _editPanel = new EditQuestionController();
        _listmodel = new SwapableListModel();
        _questionSet = new QuestionSet();
        _gui = new QuestionEditorUI(_listmodel, _editPanel.getPanel(), getDividerLocation());

        addListeners();
    }

    /**
     * Constructor for injecting mock objects. Don't use to create a real
     * object.
     * 
     * @param questioneditorUi
     * @param editPanel
     * @param model
     * @deprecated
     */
    @Deprecated
    public QuestionEditor(File lecturesDirectory, QuestionEditorUI questioneditorUi, EditQuestionController editPanel,
            SwapableListModel model, QuestionSet set)
    {
        _settings = Settings.getInstance();
        _lecturesDirectory = lecturesDirectory;
        _editPanel = editPanel;
        _listmodel = model;
        _gui = questioneditorUi;
        _questionSet = set;
        addListeners();
    }

    /**
     * Reads the DividerLocation from the Settings or uses 200 as default
     * 
     * @return 200 or location
     */
    private int getDividerLocation()
    {

        int result = _settings.getInteger(Settings.QUESTIONEDITORDIVIDER);

        if (result == -1)
        {
            result = 200;
        }
        return result;
    }

    /**
     * {@link ListSelectionListener} for the QuestionList
     */
    private void reactToQuestionListSelection(boolean isValueAdjusting)
    {
        if (isValueAdjusting)
        {
            int selectedIndex = _gui.getQuestionJList().getSelectedIndex();
            if (selectedIndex != -1)
            {
                _editPanel.saveCurrentQuestionModel();
                _editPanel.updateQuestionModel(_questionSet.getIndex(selectedIndex));
            }
        }
    }

    /**
     * {@link KeyListener} for the QuestionList
     */
    private void reactToQuestionListKeyEvent(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            reactToQuestionListSelection(true);
        }
    }

    /**
     * {@link ActionListener} for the Add-Button (+)
     */
    private void getPlusButtonAction()
    {
        JPopupMenu menu = new JPopupMenu();

        menu.add(new JMenuItem2(Localize.getString("label.single.choice"), new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                addSingleChoice();
            }
        }));
        menu.add(new JMenuItem2(Localize.getString("label.multiple.choice"), new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                addMultipleChoice();
            }
        }));
        menu.add(new JMenuItem2(Localize.getString("label.text.choice"), new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                addTextQuestion();
            }
        }));
        menu.add(new JSeparator());
        menu.add(new JMenuItem2(Localize.getString("label.yes.no.scale"), new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                addLabelYesNoScale();
            }
        }));
        menu.add(new JMenuItem2(Localize.getString("label.num.scale"), new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                addNumberScale();
            }
        }));
        menu.add(new JMenuItem2(Localize.getString("label.likert.scale"), new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                addLikertScale();
            }
        }));
        menu.show(_gui.getPlusbutton(), _gui.getPlusbutton().getX(), _gui.getPlusbutton().getY());
    }

    private void addLikertScale()
    {
        addQuestionToList(new SingleChoiceQuestion("", Arrays.asList(
                Localize.getString("label.likert.strong.disagree"), Localize.getString("label.likert.disagree"),
                Localize.getString("label.likert.medium"), Localize.getString("label.likert.agree"),
                Localize.getString("label.likert.strong.agree")), QuestionModel.NOCORRECTANSWER,
                QuestionModel.UNLIMITED));
    }

    private void addNumberScale()
    {
        addQuestionToList(new SingleChoiceQuestion("", Arrays.asList("1", "2", "3", "4", "5", "6"),
                QuestionModel.NOCORRECTANSWER, QuestionModel.UNLIMITED));
    }

    private void addTextQuestion()
    {
        addQuestionToList(new TextQuestion());
    }

    private void addMultipleChoice()
    {
        addQuestionToList(new MultipleChoiceQuestion());
    }

    private void addSingleChoice()
    {
        addQuestionToList(new SingleChoiceQuestion());
    }

    private void addLabelYesNoScale()
    {
        addQuestionToList(new SingleChoiceQuestion("", Arrays.asList(Localize.getString("label.yes"),
                Localize.getString("label.no")), QuestionModel.NOCORRECTANSWER, QuestionModel.UNLIMITED));
    }

    private void addQuestionToList(QuestionModel question)
    {
        _questionSet.addQuestionModel(question);
        _listmodel.addElement(question);
        _gui.getQuestionJList().setSelectedIndex(_listmodel.getSize() - 1);
        this.reactToQuestionListSelection(true);
    }

    /**
     * {@link ActionListener} for the Remove-Button (-)
     */
    private void getMinusButtonAction()
    {
        QuestionModel q = (QuestionModel) _gui.getQuestionJList().getSelectedValue();
        int index = _gui.getQuestionJList().getSelectedIndex();

        _questionSet.removeQuestionModel(q);
        _listmodel.removeElement(q);

        if (index > -1 && index < _listmodel.size())
        {
            _gui.getQuestionJList().setSelectedIndex(index);
            _editPanel.updateQuestionModel(_questionSet.getIndex(index));
            reactToQuestionListSelection(true);
        }
        else if (index >= _listmodel.size() && _listmodel.size() != 0)
        {
            _gui.getQuestionJList().setSelectedIndex(_listmodel.size() - 1);
            _editPanel.updateQuestionModel(_questionSet.getIndex(_listmodel.size() - 1));
            reactToQuestionListSelection(true);
        }
        else
        {
            _editPanel.updateQuestionModel(_emptyModel);
            _editPanel.setEnabled(false);
        }
    }

    /**
     * Called on click on the load button
     */
    private void getLoadButtonAction()
    {
        QuestionLoadDialog dialog = new QuestionLoadDialog(_lecturesDirectory);
        dialog.show();

        if (dialog.isFileLoaded()) // Check if Abort has been clicked
        {
            _questionSet = dialog.getLoadedQuestionSet();
            _gui.getQuestionTitle().setText(dialog.getTitle());
            _isExternalFile = dialog.isExternal();

            _listmodel.clear();
            if (_questionSet != null)
            {
                for (QuestionModel qm : _questionSet.getQuestionModels())
                {
                    _listmodel.addElement(qm);
                }

                _gui.getQuestionJList().setSelectedIndex(0);
                _editPanel.updateQuestionModel(_questionSet.getQuestionModels().get(0));

                _gui.getFrame().repaint();
            }
        }
    }

    /**
     * {@link ActionListener} for the question-save-button
     */
    private void getSaveButtonAction(ActionEvent e)
    {
        if (_listmodel.size() == 0)
        {
            MessageWindow.showMessageWindowError(Localize.getString("error.missing.questions"), 1500);
            return;
        }

        _editPanel.saveCurrentQuestionModel();

        if (_gui.getQuestionTitle().getText().length() > 0 && !_isExternalFile)
        {
            // SIMPLE SAVE
            String title = _gui.getQuestionTitle().getText();
            String filepath = _lecturesDirectory + "/" + title;
            writeFile(filepath, _questionSet);
        }
        else if (_gui.getQuestionTitle().getText().length() > 0 && _isExternalFile)
        {
            // SIMPLE SAVE TO EXTERNAL
            String filepath = _gui.getQuestionTitle().getText();

            writeFile(filepath, _questionSet);
        }
        else
        {
            // use QuestionSaveWindow
            QuestionSaveWindowUI qsw = new QuestionSaveWindowUI(_questionSet, _lecturesDirectory);
            qsw.setLocationRelativeTo((Component) e.getSource());
            qsw.setVisible(true);

            File f = qsw.getFile();

            _gui.getQuestionTitle().setText(f.getAbsolutePath().replace(_lecturesDirectory.getAbsolutePath(), ""));
        }
    }

    private void writeFile(String filepath, QuestionSet set)
    {
        try
        {
            QuestionCRUDService questionWriter = new QuestionCRUDService();
            questionWriter.createAndUpdateQuestionSet(filepath, set);
            MessageWindow.showMessageWindowSuccess(Localize.getString("message.saved"), 1000);
        }
        catch (Exception e)
        {
            MessageWindow.showMessageWindowError(Localize.getString("error.save.questionset"), 2000);
            Log.error("error saving questionset", e);
        }
    }

    /**
     * {@link ActionListener} for the new-question-button
     */
    private void getNewButtonAction()
    {
        _gui.getQuestionTitle().setText("");
        _questionSet = new QuestionSet();
        _listmodel.clear();
        _editPanel.updateQuestionModel(_emptyModel);

        _editPanel.setEnabled(false);
        _isExternalFile = false;
    }

    /**
     * Moves a Question one position up in the List
     */
    private void getMoveQuestionUpAction()
    {
        int index = _gui.getQuestionJList().getSelectedIndex();
        if (index > 0)
        {
            _questionSet.swapElements(index - 1, index);

            _listmodel.swap(index - 1, index);

            _gui.getQuestionJList().setSelectedIndex(index - 1);
        }
    }

    /**
     * Moves a Question one position down in the List
     */
    private void getMoveQuestionDownAction()
    {
        int index = _gui.getQuestionJList().getSelectedIndex();
        if (canMoveDown(index))
        {
            _questionSet.swapElements(index, index + 1);

            _listmodel.swap(index, index + 1);

            _gui.getQuestionJList().setSelectedIndex(index + 1);
        }
    }

    private void importQuestionSetAction()
    {
        QuestionLoadDialog dialog = new QuestionLoadDialog(_lecturesDirectory);
        dialog.show();

        if (dialog.isFileLoaded()) // Check if Abort has been clicked
        {
            addQuestions(dialog.getLoadedQuestionSet());
        }
    }

    private void importQuestionAction()
    {
        QuestionLoadDialog dialog = new QuestionLoadDialog(_lecturesDirectory);
        dialog.show();

        if (dialog.isFileLoaded())
        {
            QuestionImportController controller = new QuestionImportController(dialog.getLoadedQuestionSet());
            controller.displayController(_gui.getFrame(), null);

            if (controller.isLoadButtonPressed())
            {
                addQuestions(controller.getSelectedQuestions());
            }
        }
    }

    private void importQTIAction()
    {
        QTIImportController controller = new QTIImportController();
        QuestionSet set = controller.getQuestions();
        if (set != null)
        {
            addQuestions(set);
        }
    }

    /**
     * Adds all questions inside the Set to the current QuestionSet
     * 
     * @param set
     *            containing questionmodels to be added
     */
    private void addQuestions(QuestionSet set)
    {
        for (QuestionModel model : set)
        {
            _questionSet.addQuestionModel(model);
            _listmodel.addElement(model);
        }
    }

    private void previewQuestionAction()
    {
        _editPanel.saveCurrentQuestionModel();
        VotingController votingpreview;
        votingpreview = new VotingController(null);
        votingpreview.setQuestionSet(_questionSet, null, null, null);
        votingpreview.displayController(_gui.getFrame(), null);
    }

    private boolean canMoveDown(int index)
    {
        return index + 1 < _listmodel.getSize();
    }

    /**
     * Called when this Window is Closing, to save the size
     */
    private void windowIsClosing()
    {
        Settings settings = _settings;
        settings.setProperty(Settings.QUESTIONEDITORSIZE, _gui.getFrame().getSize());
        settings.setProperty(Settings.QUESTIONEDITORDIVIDER, _gui.getSplitPaneDividerLocation());
        settings.save();
    }

    public void setVisible(boolean b)
    {
        _gui.getFrame().setVisible(b);
    }

    public JFrame getFrame()
    {
        return _gui.getFrame();
    }

    @Override
    public void displayController(Component relativeTo, WindowListener listener)
    {

        _gui.getFrame().setSize(_settings.getDimension(Settings.QUESTIONEDITORSIZE));
        _gui.getFrame().setLocationRelativeTo(relativeTo);
        setVisible(true);
        _gui.getFrame().addWindowListener(listener);
    }

    /**
     * Adds listeners to all the GUI-Elements
     */
    private void addListeners()
    {
        _gui.getPlusbutton().addActionListener(e -> getPlusButtonAction());
        _gui.getMinusbutton().addActionListener(e -> getMinusButtonAction());
        _gui.getQuestionJList().addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                reactToQuestionListSelection(e.getValueIsAdjusting());
            }
        });
        _gui.getQuestionJList().addKeyListener(new KeyAdapter()
        {
            public void keyReleased(KeyEvent e)
            {
                reactToQuestionListKeyEvent(e);
            }
        });
        _gui.getMoveQuestionDown().addActionListener(e -> getMoveQuestionDownAction());
        _gui.getMoveQuestionUp().addActionListener(e -> getMoveQuestionUpAction());
        _gui.getFrame().addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                windowIsClosing();
            }
        });
        _gui.getMenuItem(MenuItems.LOADQUESTIONSET).addActionListener(e -> getLoadButtonAction());
        _gui.getMenuItem(MenuItems.SAVEQUESTIONSET).addActionListener(e -> getSaveButtonAction(e));
        _gui.getMenuItem(MenuItems.NEWQUESTIONSET).addActionListener(e -> getNewButtonAction());
        _gui.getMenuItem(MenuItems.IMPORTQTI).addActionListener(e -> importQTIAction());
        _gui.getMenuItem(MenuItems.IMPORTQUESTION).addActionListener(e -> importQuestionAction());
        _gui.getMenuItem(MenuItems.IMPORTQUESTIONSET).addActionListener(e -> importQuestionSetAction());
        _gui.getMenuItem(MenuItems.PREVIEWQUESTION).addActionListener(e -> previewQuestionAction());
    }

}
