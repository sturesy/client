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
package sturesy.core.ui.answerPanel;

import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import sturesy.core.Localize;
import sturesy.core.ui.TextSelectionFocusListener;

/**
 * An answer panel component is defined by 4 components: A radio button which
 * means if this answer is correct. A Label which shows the name of the answer.
 * A Textfield where an answer input is wanted. And a Button to toggle on/off
 * this answer. It switches the enabled state of editable components.
 * 
 * @author jens.dallmann, w.posdorfer
 */
public class SingleAnswerPanelController
{

    /**
     * the ui which layouts the 4 components
     */
    private SingleAnswerPanelUI _ui;

    /**
     * the answernumber of this Panel
     */
    private int _answerNumber;

    /** The Delegate to be asked for new toggle-states */
    private final ToggleDelegate _delegate;

    /**
     * Initialize with a number which means the number of the answer in the
     * current set of answers.
     * 
     * @param answerNumber
     *            the indexnumber representing this answer
     * @param delegate
     *            the delegate to ask for toggle-states
     * @param useRadioButtons
     */
    public SingleAnswerPanelController(int answerNumber, ToggleDelegate delegate, boolean useRadioButtons)
    {
        this(buildAnswerCharacter(answerNumber), answerNumber, delegate, useRadioButtons);

    }

    /**
     * Same as integer constructor except that the labeltext is passed
     * 
     * @param answerString
     *            the String to be shown next to the RadioButton
     * @param answerNumber
     *            the indexnumber representing this answer
     * @param delegate
     *            the delegate to ask for toggle-states
     * @param useRadioButtons 
     */
    public SingleAnswerPanelController(String answerString, int answerNumber, ToggleDelegate delegate, boolean useRadioButtons)
    {
        _answerNumber = answerNumber;
        _delegate = delegate;
        SingleAnswerPanelUI ui = new SingleAnswerPanelUI(answerString, useRadioButtons);
        init(ui);

        registerListeners();
    }

    /**
     * Injects the UI and the onOffButtonController. So the answer text is
     * already set in ui.
     * 
     * @param ui
     * @param toggleButton
     */
    public void init(SingleAnswerPanelUI ui)
    {
        _ui = ui;
        _ui.setTextFieldEnabled(false);
        _ui.setRadioButtonEnabled(false);
    }

    /**
     * Builds the answer string. It converts the number into an upper case
     * character and appends this to Localize("Answer") <br>
     * <br>
     * Example: <code>buildAnswerCharacter(0)</code> => Answer A
     * 
     * @param answerNumber
     *            Index of the letter in the alphabet
     * @return Answer + Letter
     */
    public static String buildAnswerCharacter(int answerNumber)
    {
        return Localize.getString("label.answer") + " " + (char) ('A' + answerNumber);
    }

    /**
     * Returns the panel which layouts the answer.
     * 
     * @return JPanel
     */
    public JPanel getPanel()
    {
        return _ui.getPanel();
    }

    /**
     * Returns the answer text
     * 
     * @return String
     */
    public String getAnswerText()
    {
        return _ui.getTextFieldText();
    }

    /**
     * Returns if this is the correct answer.
     * 
     * @return true if this one is selected as correct answer.
     */
    public boolean isSelectedAsCorrectAnswer()
    {
        return _ui.isRadioButtonSelected();
    }

    /**
     * Add the radio button to the button group to provide one only selection.
     * 
     * @param buttonGroup
     */
    public void addRadioButtonToButtonGroup(ButtonGroup buttonGroup)
    {
        buttonGroup.add(_ui.getIsCorrectAnswerButton());
    }

    /**
     * sets if the component has a textfield
     * 
     * @param hasTextField
     */
    public void setHasTextField(boolean hasTextField)
    {
        _ui.setTextFieldVisible(hasTextField);
    }

    public void setHasToggleButton(boolean bool)
    {
        _ui.setToggleButtonVisible(bool);
    }

    /**
     * Sets if the ToggleButton is clickable
     * 
     * @param bool
     *            <code>true</code> if enabled
     */
    public void setToggleButtonEnabled(boolean bool)
    {
        _ui.getToggleButton().setEnabled(bool);
    }

    public void setAnswerTextLabelEnabled(boolean bool)
    {
        _ui.setAnswerTextLabelEnabled(bool);
    }

    /**
     * Sets the togglebutton to a new selection-state
     * 
     * @param selected
     *            selection-state
     */
    public void setToggleButtonSelected(boolean selected)
    {
        _ui.getToggleButton().setSelected(selected);
    }

    /**
     * Is the ToggleButton selected
     * 
     * @return some boolean
     */
    public boolean isToggleButtonSelected()
    {
        return _ui.getToggleButton().isSelected();
    }

    /**
     * Set the Text of the Answertextfield
     * 
     * @param string
     *            text to set
     */
    public void setAnswerText(String string)
    {
        _ui.setTextFieldText(string);
    }

    /**
     * Set this answerpanel to hold the correct answer
     * 
     * @param isCorrectAnswer
     *            mark as correct answer
     */
    public void setIsCorrectAnswer(boolean isCorrectAnswer)
    {
        _ui.setIsCorrectAnswer(isCorrectAnswer);
    }

    /**
     * Sets the Enabled status of this Controllers UI elements
     * 
     * @param enabled
     */
    public void setEnabled(boolean enabled)
    {
        setPartialEnabled(enabled, enabled, enabled);
    }

    /**
     * Partially enables components of this controller
     * 
     * @param radiobutton
     *            should the radiobutton be enabled?
     * @param textfield
     *            should the textfield be enabled?
     * @param togglebutton
     *            should the togglebutton be enabled?
     */
    public void setPartialEnabled(boolean radiobutton, boolean textfield, boolean togglebutton)
    {
        _ui.setRadioButtonEnabled(radiobutton);
        _ui.setAnswerTextLabelEnabled(radiobutton);
        _ui.setTextFieldEnabled(textfield);
        _ui.getToggleButton().setEnabled(togglebutton);
    }

    /**
     * registers a FocusListener on the AnswerText-Field
     * 
     * @param listener
     *            the FocusListener to attach
     */
    public void registerFocusListener(FocusListener listener)
    {
        _ui.getAnswerTextField().addFocusListener(listener);
    }

    /**
     * ActionListener Method for ToggleButton
     * 
     * @param selected
     *            the new state of this button
     */
    private void toggleButtonStateChanged(boolean selected)
    {
        if (_delegate.amIAllowedToToggle(_answerNumber, selected))
        {
            setPartialEnabled(selected, selected, true);
            _ui.setAnswerTextLabelEnabled(selected);
        }
        else
        {
            setToggleButtonSelected(!selected);
        }
    }

    /**
     * register Listeners
     */
    private void registerListeners()
    {
        _ui.getToggleButton().addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                toggleButtonStateChanged(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        new TextSelectionFocusListener(_ui.getAnswerTextField());
    }
}
