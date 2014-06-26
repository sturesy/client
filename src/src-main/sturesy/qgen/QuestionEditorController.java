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
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import sturesy.core.Localize;
import sturesy.core.Log;
import sturesy.core.backend.Loader;
import sturesy.core.backend.services.crud.QuestionCRUDService;
import sturesy.core.ui.JMenuItem2;
import sturesy.core.ui.MessageWindow;
import sturesy.core.ui.SwapableListModel;
import sturesy.core.ui.editwindow.CommonEditWindowController;
import sturesy.core.ui.editwindow.CommonEditWindowUI;
import sturesy.items.MultipleChoiceQuestion;
import sturesy.items.QuestionModel;
import sturesy.items.QuestionSet;
import sturesy.items.SingleChoiceQuestion;
import sturesy.items.TextQuestion;
import sturesy.qgen.gui.QuestionListCellRenderer;
import sturesy.qgen.gui.QuestionSaveWindowUI;
import sturesy.qgen.qimport.QTIImportController;
import sturesy.qgen.qimport.QuestionImportController;
import sturesy.util.Settings;
import sturesy.voting.VotingController;

/**
 * Class to handle editing of Questionsets
 * 
 * @author w.posdorfer
 *
 */
public class QuestionEditorController extends CommonEditWindowController
{

    private static final String MENUITEM_PREVIEWQUESTIONSET = "PREVIEWQUESTIONSET";
    private static final String MENUITEM_IMPORTQTI = "IMPORTQTI";
    private static final String MENUITEM_IMPORTQUESTIONSET = "IMPORTQUESTIONSET";
    private static final String MENUITEM_IMPORTQUESTION = "IMPORTQUESTION";

    private EditQuestionController _editPanel;

    private QuestionSet _questionSet;

    boolean _isExternalFile = false;

    private final QuestionModel _emptyModel = new SingleChoiceQuestion("", new ArrayList<String>(),
            QuestionModel.NOCORRECTANSWER, QuestionModel.UNLIMITED);

    private File _lecturesDirectory;

    /**
     * Creates a new QuestionEditor
     * 
     * @param lecturesDirectory
     *            the default directory where lectures should be saved to and
     *            loaded from
     */
    public QuestionEditorController(File lecturesDirectory)
    {
        _lecturesDirectory = lecturesDirectory;
        _editPanel = new EditQuestionController();
        _listmodel = new SwapableListModel();
        _questionSet = new QuestionSet();

        String southPanelBorderTitle = Localize.getString("label.questionset");
        String leftPanelBorderTitle = Localize.getString("label.question");
        setupUI(_listmodel, new QuestionListCellRenderer(), getDividerLocation(), _editPanel.getPanel(),
                southPanelBorderTitle, leftPanelBorderTitle);

        addAdditionalMenuItems();
        registerAdditionaListeners();

        _ui.setFrameTitleAndIcon(Localize.getString(Localize.QUESTIONEDITOR), Loader.getImageIcon(Loader.IMAGE_STURESY)
                .getImage());

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

    @Override
    public void swapItemsInList(int index1, int index2)
    {
        _questionSet.swapElements(index1, index2);
    }

    @Override
    public void saveMenuItemPressed()
    {
        if (_listmodel.size() == 0)
        {
            MessageWindow.showMessageWindowError(Localize.getString("error.missing.questions"), 1500);
            return;
        }

        _editPanel.saveCurrentQuestionModel();

        if (_ui.getItemTitle().getText().length() > 0 && !_isExternalFile)
        {
            // SIMPLE SAVE
            String title = _ui.getItemTitle().getText();
            String filepath = _lecturesDirectory + "/" + title;
            writeFile(filepath, _questionSet);
        }
        else if (_ui.getItemTitle().getText().length() > 0 && _isExternalFile)
        {
            // SIMPLE SAVE TO EXTERNAL
            String filepath = _ui.getItemTitle().getText();

            writeFile(filepath, _questionSet);
        }
        else
        {
            // use QuestionSaveWindow
            QuestionSaveWindowUI qsw = new QuestionSaveWindowUI(_questionSet, _lecturesDirectory);
            qsw.setLocationRelativeTo(null);
            qsw.setVisible(true);

            File f = qsw.getFile();

            _ui.getItemTitle().setText(f.getAbsolutePath().replace(_lecturesDirectory.getAbsolutePath(), ""));
        }
    }

    /**
     * Writes a questionset to a file
     * 
     * @param filepath
     *            the filepath to save this questionset to
     * @param set
     *            the questionset to save
     */
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

    @Override
    public void loadMenuItemPressed()
    {
        QuestionLoadDialog dialog = new QuestionLoadDialog(_lecturesDirectory);
        dialog.show();

        if (dialog.isFileLoaded()) // Check if Abort has been clicked
        {
            _questionSet = dialog.getLoadedQuestionSet();
            _ui.getItemTitle().setText(dialog.getTitle());
            _isExternalFile = dialog.isExternal();

            _listmodel.clear();
            if (_questionSet != null)
            {
                for (QuestionModel qm : _questionSet.getQuestionModels())
                {
                    _listmodel.addElement(qm);
                }

                _ui.getItemList().setSelectedIndex(0);
                _editPanel.updateQuestionModel(_questionSet.getQuestionModels().get(0));

                _ui.getFrame().repaint();
            }
        }
    }

    @Override
    public void newMenuItemPressed()
    {
        _ui.getItemTitle().setText("");
        _questionSet = new QuestionSet();
        _listmodel.clear();
        _editPanel.updateQuestionModel(_emptyModel);

        _editPanel.setEnabled(false);
        _isExternalFile = false;
    }

    @Override
    public void plusButtonPressed()
    {
        JPopupMenu menu = new JPopupMenu();

        menu.add(new JMenuItem2(Localize.getString("label.single.choice"), e -> addSingleChoice()));
        menu.add(new JMenuItem2(Localize.getString("label.multiple.choice"), e -> addMultipleChoice()));
        menu.add(new JMenuItem2(Localize.getString("label.text.choice"), e -> addTextQuestion()));
        menu.add(new JSeparator());
        menu.add(new JMenuItem2(Localize.getString("label.yes.no.scale"), e -> addLabelYesNoScale()));
        menu.add(new JMenuItem2(Localize.getString("label.num.scale"), e -> addNumberScale()));
        menu.add(new JMenuItem2(Localize.getString("label.likert.scale"), e -> addLikertScale()));
        menu.show(_ui.getPlusbutton(), _ui.getPlusbutton().getX(), _ui.getPlusbutton().getY());
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
        _ui.getItemList().setSelectedIndex(_listmodel.getSize() - 1);
        listSelectionChanged(_ui.getItemList().getSelectedIndex());
    }

    @Override
    public void minusButtonPressed()
    {
        QuestionModel q = (QuestionModel) _ui.getItemList().getSelectedValue();
        int index = _ui.getItemList().getSelectedIndex();

        _questionSet.removeQuestionModel(q);
        _listmodel.removeElement(q);

        if (index > -1 && index < _listmodel.size())
        {
            _ui.getItemList().setSelectedIndex(index);
            _editPanel.updateQuestionModel(_questionSet.getIndex(index));
            listSelectionChanged(_ui.getItemList().getSelectedIndex());
        }
        else if (index >= _listmodel.size() && _listmodel.size() != 0)
        {
            _ui.getItemList().setSelectedIndex(_listmodel.size() - 1);
            _editPanel.updateQuestionModel(_questionSet.getIndex(_listmodel.size() - 1));
            listSelectionChanged(_ui.getItemList().getSelectedIndex());
        }
        else
        {
            _editPanel.updateQuestionModel(_emptyModel);
            _editPanel.setEnabled(false);
        }
    }

    @Override
    public void listSelectionChanged(int index)
    {
        if (index != -1)
        {
            _editPanel.saveCurrentQuestionModel();
            _editPanel.updateQuestionModel(_questionSet.getIndex(index));
        }
    }

    @Override
    public void windowIsClosing()
    {
        _settings.setProperty(Settings.QUESTIONEDITORSIZE, _ui.getFrame().getSize());
        _settings.setProperty(Settings.QUESTIONEDITORDIVIDER, _ui.getSplitPaneDividerLocation());
        _settings.save();
    }

    @Override
    public void displayController(Component relativeTo, WindowListener listener)
    {
        _ui.getFrame().setSize(_settings.getDimension(Settings.QUESTIONEDITORSIZE));
        _ui.getFrame().setLocationRelativeTo(relativeTo);
        _ui.getFrame().addWindowListener(listener);
        setVisible(true);
    }

    private void addAdditionalMenuItems()
    {
        JMenu importMenu = new JMenu(Localize.getString("menu.import"));
        _ui.addMenuItem(Localize.getString("button.import.questions"), null, importMenu, MENUITEM_IMPORTQUESTION);
        _ui.addMenuItem(Localize.getString("button.import.questionset"), null, importMenu, MENUITEM_IMPORTQUESTIONSET);
        _ui.addMenuItem(Localize.getString("button.import.qti"), null, importMenu, MENUITEM_IMPORTQTI);

        JMenu previewMenu = new JMenu(Localize.getString("menu.preview"));
        _ui.addMenuItem(Localize.getString("button.preview.questionset"), null, previewMenu,
                MENUITEM_PREVIEWQUESTIONSET);

        _ui.addMenuToBar(importMenu);
        _ui.addMenuToBar(previewMenu);
    }

    private void importQuestionMenuItemPressed()
    {
        QuestionLoadDialog dialog = new QuestionLoadDialog(_lecturesDirectory);
        dialog.show();

        if (dialog.isFileLoaded())
        {
            QuestionImportController controller = new QuestionImportController(dialog.getLoadedQuestionSet());
            controller.displayController(_ui.getFrame(), null);

            if (controller.isLoadButtonPressed())
            {
                addQuestions(controller.getSelectedQuestions());
            }
        }
    }

    private void importQuestionSetMenuItemPressed()
    {
        QuestionLoadDialog dialog = new QuestionLoadDialog(_lecturesDirectory);
        dialog.show();

        if (dialog.isFileLoaded()) // Check if Abort has been clicked
        {
            addQuestions(dialog.getLoadedQuestionSet());
        }
    }

    private void importQTIMenuItemPressed()
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

    private void previewQuestionMenuItemPressed()
    {
        _editPanel.saveCurrentQuestionModel();
        VotingController votingpreview;
        votingpreview = new VotingController(null);
        votingpreview.setQuestionSet(_questionSet, null, null, null);
        votingpreview.displayController(_ui.getFrame(), null);
    }

    private void registerAdditionaListeners()
    {
        _ui.getMenuItem(MENUITEM_IMPORTQUESTION).addActionListener(e -> importQuestionMenuItemPressed());
        _ui.getMenuItem(MENUITEM_IMPORTQUESTIONSET).addActionListener(e -> importQuestionSetMenuItemPressed());
        _ui.getMenuItem(MENUITEM_IMPORTQTI).addActionListener(e -> importQTIMenuItemPressed());
        _ui.getMenuItem(MENUITEM_PREVIEWQUESTIONSET).addActionListener(e -> previewQuestionMenuItemPressed());
    }

    @Override
    public void setupTooltips()
    {
        _ui.getPlusbutton().setToolTipText(Localize.getString("tool.questiongen.add.questionmodel"));
        _ui.getMinusbutton().setToolTipText(Localize.getString("tool.questiongen.remove.questionmodel"));
        _ui.getMoveItemUp().setToolTipText(Localize.getString("tool.questiongen.move.up"));
        _ui.getMoveItemDown().setToolTipText(Localize.getString("tool.questiongen.move.down"));

        /*
         * Setting tooltips for menuitems previously created, to be more
         * specific to the question editor
         */
        _ui.getMenuItem(CommonEditWindowUI.MENUITEM_NEW).setToolTipText(Localize.getString("tool.questiongen.new"));
        _ui.getMenuItem(CommonEditWindowUI.MENUITEM_LOAD).setToolTipText(Localize.getString("tool.questiongen.load"));
        _ui.getMenuItem(CommonEditWindowUI.MENUITEM_SAVE).setToolTipText(Localize.getString("tool.questiongen.save"));
    }

}
