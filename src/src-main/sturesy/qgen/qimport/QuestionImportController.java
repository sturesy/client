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
package sturesy.qgen.qimport;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import sturesy.core.Controller;
import sturesy.items.QuestionModel;
import sturesy.items.QuestionSet;

/**
 * A Dialog where multiple Questions can be selected
 * 
 * @author w.posdorfer
 * 
 */
public class QuestionImportController implements Controller
{
    private QuestionImportUI _gui;
    private boolean _questionsSelected = false;

    /**
     * Creates a new QuestionImportController
     * 
     * @param set
     *            QuestionSet to be displayed
     */
    public QuestionImportController(QuestionSet set)
    {
        _gui = new QuestionImportUI();

        for (QuestionModel model : set)
        {
            _gui.getListModel().addElement(model);
        }

        registerListeners();
    }

    /**
     * Has the OK button been pressed
     * 
     * @return <code>true</code> if the OK button was pressed,
     *         <code>false</code> otherwise
     */
    public boolean isLoadButtonPressed()
    {
        return _questionsSelected;
    }

    /**
     * Returns the selected Questions in a QuestionSet
     * 
     * @return QuestionSet containing the selected questions
     */
    public QuestionSet getSelectedQuestions()
    {
        QuestionSet result = new QuestionSet();

        for (Object o : _gui.getList().getSelectedValues())
        {
            result.addQuestionModel((QuestionModel) o);
        }

        return result;
    }

    /**
     * Action triggered by Cancel and Loadbutton
     * 
     * @param isLoadButton
     *            is this the LoadButton
     */
    private void buttonAction(boolean isLoadButton)
    {
        _questionsSelected = isLoadButton;
        _gui.getDialog().dispose();
    }

    @Override
    public void displayController(Component relativeTo, WindowListener listener)
    {
        _gui.show(relativeTo, true);
    }

    private void registerListeners()
    {
        _gui.getCancelButton().addActionListener(e -> buttonAction(false));
        _gui.getLoadButton().addActionListener(e -> buttonAction(true));
        _gui.getDialog().addWindowListener(new WindowAdapter()
        {
            public void windowClosed(WindowEvent e)
            {
                buttonAction(false);
            }
        });
    }

}
