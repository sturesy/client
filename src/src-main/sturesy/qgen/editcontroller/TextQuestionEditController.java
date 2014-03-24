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
package sturesy.qgen.editcontroller;

import java.awt.event.FocusListener;

import javax.swing.JPanel;

import sturesy.items.QuestionModel;
import sturesy.items.TextQuestion;

/**
 * This Controller handles editing of TextQuestions
 * 
 * @author w.posdorfer
 * 
 */
public class TextQuestionEditController implements IQuestionEditController
{
    private final TextQuestionEditControllerUI _ui;

    /**
     * Creates the new Controller
     */
    public TextQuestionEditController()
    {
        _ui = new TextQuestionEditControllerUI();
    }

    @Override
    public JPanel getPanel()
    {
        return _ui.getPanel();
    }

    @Override
    public void registerFocusListener(FocusListener focuslistener)
    {
        // no capable HTML Textareas
    }

    @Override
    public void setUp(QuestionModel question)
    {
        if (question instanceof TextQuestion)
        {
            TextQuestion txt = (TextQuestion) question;

            _ui.getAnswerField().setText(txt.getAnswer());
            _ui.getToleranceField().setText("" + txt.getTolerance());
            _ui.getIgnoreCaseBox().setSelected(txt.isIgnoreCase());
            _ui.getIgnoreWhiteSpaceBox().setSelected(txt.isIgnoreSpaces());
        }
    }

    @Override
    public void saveQuestion(QuestionModel question)
    {
        if (question instanceof TextQuestion)
        {
            TextQuestion txt = (TextQuestion) question;
            txt.setAnswer(_ui.getAnswerField().getText());
            txt.setIgnoreCase(_ui.getIgnoreCaseBox().isSelected());
            txt.setIgnoreSpaces(_ui.getIgnoreWhiteSpaceBox().isSelected());

            String tolerance = _ui.getToleranceField().getText();
            if (tolerance.isEmpty() || !tolerance.matches("[0-9]*"))
            {
                tolerance = "0";
            }
            txt.setTolerance(Integer.parseInt(tolerance));
        }
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        _ui.setEnabled(enabled);
    }
}
