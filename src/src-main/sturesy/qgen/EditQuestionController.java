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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import sturesy.core.ui.FocusTransferalKeyListener;
import sturesy.core.ui.TextSelectionFocusListener;
import sturesy.items.MultipleChoiceQuestion;
import sturesy.items.QuestionModel;
import sturesy.items.SingleChoiceQuestion;
import sturesy.items.TextQuestion;
import sturesy.qgen.editcontroller.IQuestionEditController;
import sturesy.qgen.editcontroller.MultipleChoiceEditController;
import sturesy.qgen.editcontroller.SingleChoiceEditController;
import sturesy.qgen.editcontroller.TextQuestionEditController;
import sturesy.qgen.gui.EditQuestionUI;
import sturesy.util.TranslationUtils;

/**
 * A Panel which displays Options to Edit a QuestionModel
 * 
 * @author w.posdorfer
 * 
 */
public class EditQuestionController
{
    private QuestionModel _currentQuestionModel;

    private final EditQuestionUI _gui;
    private RememberingFocusListener _focusListener;
    private ShefHTML _shefhtml;

    private Map<Class<? extends QuestionModel>, IQuestionEditController> _controllers = new HashMap<Class<? extends QuestionModel>, IQuestionEditController>();

    /** The maximum amount of answers possible */
    private static final short MAXIMUMAMOUNTOFANSWERS = 10;

    public EditQuestionController()
    {
        _controllers.put(SingleChoiceQuestion.class, new SingleChoiceEditController(MAXIMUMAMOUNTOFANSWERS));
        _controllers.put(MultipleChoiceQuestion.class, new MultipleChoiceEditController(MAXIMUMAMOUNTOFANSWERS));
        _controllers.put(TextQuestion.class, new TextQuestionEditController());

        IQuestionEditController scec = _controllers.get(SingleChoiceQuestion.class);

        JPanel answerPanelUI = scec.getPanel();
        _gui = new EditQuestionUI(answerPanelUI);

        _focusListener = new RememberingFocusListener();
        scec.registerFocusListener(_focusListener);

        init();
    }

    private void init()
    {
        addListeners();
        _gui.setEnabled(false);
    }

    /**
     * Updates all TextFields with the provided information from the given
     * {@link QuestionModel}
     * 
     * @param newQuestion
     */
    public void updateQuestionModel(QuestionModel newQuestion)
    {
        if (!_gui.isEnabled())
        {
            _gui.setEnabled(true);
        }

        IQuestionEditController cont = _controllers.get(newQuestion.getClass());
        cont.setUp(newQuestion);
        if (!classEquals(_currentQuestionModel, newQuestion))
        {
            // only replace Answerpanels when the new question
            // differs from the old question
            _gui.replaceAnswerPanel(cont.getPanel());
            _gui.setBorderTitle(TranslationUtils.getLocalizedName(newQuestion));
        }

        _currentQuestionModel = newQuestion;

        _gui.getQuestionField().setText(newQuestion.getQuestion());

        int duration = newQuestion.getDuration();
        boolean isInfiniteDuration = duration == -1;
        String dur = isInfiniteDuration ? "" : "" + duration;
        _gui.getDurationField().setText(dur);

    }

    private boolean classEquals(Object ob1, Object ob2)
    {
        if (ob1 == null || ob2 == null)
        {
            return false;
        }
        else
        {
            return ob1.getClass().equals(ob2.getClass());
        }
    }

    /**
     * Returns the current active {@link QuestionModel}
     * 
     * @return
     */
    public QuestionModel getCurrentQuestionModel()
    {
        return _currentQuestionModel;
    }

    /**
     * Saves the QuestionModel that has currently the focus
     */
    public void saveCurrentQuestionModel()
    {
        if (_currentQuestionModel != null)
        {
            saveQuestionModel();
        }
    }

    private void saveQuestionModel()
    {
        _currentQuestionModel.setQuestion(_gui.getQuestionField().getText());
        _controllers.get(_currentQuestionModel.getClass()).saveQuestion(_currentQuestionModel);

        String durationFieldText = _gui.getDurationFieldText();
        int durationLength = durationFieldText.length();
        boolean hasNoSpecifiedLength = durationLength == 0;
        int dur = hasNoSpecifiedLength ? -1 : Integer.parseInt(durationFieldText);
        _currentQuestionModel.setDuration(dur);
    }

    /**
     * Returns the graphical Component
     * 
     * @return {@link JPanel}
     */
    public JPanel getPanel()
    {
        return _gui.getJPanel();
    }

    private void resetButtonAction(ActionEvent e)
    {
        updateQuestionModel(_currentQuestionModel);
    }

    public void setEnabled(boolean enabled)
    {
        _controllers.get(_currentQuestionModel.getClass()).setEnabled(enabled);
        _gui.setEnabled(enabled);
    }

    private void showSHEFEditorAction(Component c)
    {
        if (c instanceof JTextComponent)
        {
            showShef((JTextComponent) c);
        }
        else if (c == null)
        {
            showShef(_gui.getQuestionField());
        }
    }

    private void showShef(JTextComponent textComp)
    {
        if (_shefhtml == null)
        {
            _shefhtml = new ShefHTML(textComp, new WindowAdapter()
            {
                public void windowClosing(WindowEvent e)
                {
                    _shefhtml = null;
                }
            });
        }
    }

    /**
     * Adds Listeners to all the Buttons and Labels
     */
    private void addListeners()
    {
        _gui.getResetButton().addActionListener(e -> resetButtonAction(e));
        new TextSelectionFocusListener(_gui.getQuestionField());
        new TextSelectionFocusListener(_gui.getDurationField());
        new FocusTransferalKeyListener(_gui.getQuestionField());
        _gui.getQuestionField().addFocusListener(_focusListener);
        _gui.getHTMLButton().addActionListener(e -> showSHEFEditorAction(_focusListener.getComponent()));
    }
}
