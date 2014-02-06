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
package sturesy.qgen.editcontroller;

import java.awt.event.FocusListener;

import javax.swing.JPanel;

import sturesy.items.QuestionModel;

/**
 * Interface to bundle all common tasks of Controllers that are concerned with
 * editing Questions. These will all be specialized Controllers for handling a
 * special subtype of QuestionModel
 * 
 * @author w.posdorfer
 * 
 */
public interface IQuestionEditController
{
    /**
     * @return Graphical Interface of this Controller
     */
    JPanel getPanel();

    /**
     * Register a Focuslistener on all textcomponents that are capable of
     * accepting HTML-Code
     * 
     * @param focuslistener
     */
    void registerFocusListener(FocusListener focuslistener);

    /**
     * Populate UI-Elements with the information from the given question
     * 
     * @param question
     */
    void setUp(QuestionModel question);

    /**
     * Save the necessary Information into the given question
     * 
     * @param question
     */
    void saveQuestion(QuestionModel question);

    /**
     * Set the UI-Elements to be enabled/disabled
     * 
     * @param enabled
     */
    void setEnabled(boolean enabled);

}
