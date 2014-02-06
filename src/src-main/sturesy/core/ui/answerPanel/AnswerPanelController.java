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
package sturesy.core.ui.answerPanel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import sturesy.core.Localize;

/**
 * The Controller class for the answer panel. It manages the different answers.
 * It defines the size of answers which is passed in constructor + one for no
 * correct answer.
 * 
 * @author wolf.posdorfer, jens.dallmann
 */
public class AnswerPanelController implements ToggleDelegate
{
    /** The List of answer panels */
    protected List<SingleAnswerPanelController> _singleAnswerPanels;
    /** the ui class for the controller */
    protected AnswerPanelUI _ui;
    /** the number of answers to display, this is usually 10 */
    private final int _numberOfAnswers;
    /** The buttongroup for the radio button. */
    protected MyButtonGroup _buttonGroup = new MyButtonGroup();
    /** Does this controller use Radiobuttons, or CheckBoxes */
    private boolean _useRadioButtons;

    /**
     * Creates a panel with the passed size of answers.
     * 
     * @param numbersOfAnswers
     *            numberOfAnswers
     */
    public AnswerPanelController(int numbersOfAnswers, boolean useRadioButtons)
    {
        this(numbersOfAnswers, new AnswerPanelUI(), useRadioButtons);
    }

    /**
     * Creates a panel with the passed size of answers using the passed UI
     * 
     * @param numbersOfAnswers
     * @param ui
     * @param useRadioButtons
     */
    public AnswerPanelController(int numbersOfAnswers, AnswerPanelUI ui, boolean useRadioButtons)
    {
        _ui = ui;
        _numberOfAnswers = numbersOfAnswers;
        _useRadioButtons = useRadioButtons;
        _singleAnswerPanels = new ArrayList<SingleAnswerPanelController>(numbersOfAnswers);

        setUpAnswerPanels();
        if (_useRadioButtons)
        {
            setUpNoCorrectAnswerButton();
        }
    }

    /**
     * Does the initial setup of the first ten panels
     */
    private void setUpAnswerPanels()
    {
        for (int i = 0; i < _numberOfAnswers; i++)
        {
            SingleAnswerPanelController cont = new SingleAnswerPanelController(i, this, _useRadioButtons);
            cont.setToggleButtonSelected(false);
            cont.setEnabled(false);
            cont.setAnswerTextLabelEnabled(false);

            if (_useRadioButtons)
            {
                cont.addRadioButtonToButtonGroup(_buttonGroup);
            }

            _singleAnswerPanels.add(cont);
            _ui.addDefineAnswerPanel(cont.getPanel());
            cont.setHasToggleButton(i >= 2);
            cont.setToggleButtonSelected(i < 2);
        }
    }

    /**
     * Setup an additional "no correct answer" panel
     */
    private void setUpNoCorrectAnswerButton()
    {
        SingleAnswerPanelController controller = new SingleAnswerPanelController(
                Localize.getString("label.no.answer.correct"), _numberOfAnswers, null, _useRadioButtons);
        controller.setHasToggleButton(false);
        controller.setHasTextField(false);

        if (_useRadioButtons)
        {
            controller.addRadioButtonToButtonGroup(_buttonGroup);
        }

        _singleAnswerPanels.add(controller);

        _ui.addDefineAnswerPanel(controller.getPanel());
    }

    /**
     * Resets all Panels<br>
     * Clears the text, enables panels 0 and 1, disabled panels 2 through 9,
     * selects panel 10
     */
    public void resetAnswerPanels()
    {
        for (int i = 0; i < 2; i++)
        {
            SingleAnswerPanelController cont = _singleAnswerPanels.get(i);
            cont.setAnswerText("");
            cont.setEnabled(true);
            cont.setToggleButtonSelected(true);
            cont.setAnswerTextLabelEnabled(true);
        }

        for (int i = 2; i < _numberOfAnswers; i++)
        {
            SingleAnswerPanelController cont = _singleAnswerPanels.get(i);
            cont.setAnswerText("");
            cont.setPartialEnabled(false, false, true);
            cont.setToggleButtonSelected(false);
            cont.setAnswerTextLabelEnabled(false);
        }
        if (_useRadioButtons) // only applicable if "nocorrectanswer" is present
        {
            _singleAnswerPanels.get(_numberOfAnswers).setEnabled(true);
            _singleAnswerPanels.get(_numberOfAnswers).setIsCorrectAnswer(true);
        }
    }

    public void setEditing(boolean enabled)
    {
        for (int i = 0; i < _singleAnswerPanels.size(); i++)
        {
            SingleAnswerPanelController single = _singleAnswerPanels.get(i);
            single.setAnswerText("");
            single.setToggleButtonSelected(enabled);
            single.setIsCorrectAnswer(enabled);
            single.setEnabled(enabled);
            single.setAnswerTextLabelEnabled(enabled);
        }
    }

    /**
     * Set text to the answer fields, and selects the correct answer
     * 
     * @param texts
     *            List of Texts to display
     * @param correctAnswer
     *            selected answer
     */
    public void setAnswerTexts(List<String> texts, int correctAnswer)
    {
        resetAnswerPanels();
        if (correctAnswer == -1)
        {
            correctAnswer = _numberOfAnswers;
        }

        for (int i = 0; i < _numberOfAnswers; i++)
        {
            SingleAnswerPanelController contr = _singleAnswerPanels.get(i);

            boolean valid = i < texts.size();

            boolean smallertwo = i < 2;

            contr.setPartialEnabled(valid || smallertwo, valid || smallertwo, true);
            contr.setAnswerTextLabelEnabled(valid || smallertwo);

            if (valid)
            {
                contr.setAnswerText(texts.get(i));
                contr.setToggleButtonSelected(true);
            }
        }
        if (correctAnswer < _singleAnswerPanels.size())
        {
            _singleAnswerPanels.get(correctAnswer).setIsCorrectAnswer(true);
        }

    }

    public void setAnswerTexts(List<String> texts, List<Integer> correctAnswers)
    {
        resetAnswerPanels();

        // using logic from above method to set all the texts and stuff
        setAnswerTexts(texts, 0);

        // now mark the correct answers
        for (int i = 0; i < _singleAnswerPanels.size(); i++)
        {
            SingleAnswerPanelController con = _singleAnswerPanels.get(i);
            con.setIsCorrectAnswer(correctAnswers.contains(i));
        }
    }

    /**
     * Returns the selected answernumber or -1 if no correct answer has been
     * selected
     * 
     * @return -1 or 0 through 9
     */
    public int getCorrectAnswerNumber()
    {
        int index = _buttonGroup.getSelectedIndex();
        if (index == _numberOfAnswers)
        {
            // if no correct return -1, else return index
            return -1;
        }
        else
        {
            return index;
        }
    }

    public List<Integer> getCorrectAnswerNumbers()
    {
        ArrayList<Integer> list = new ArrayList<Integer>();

        for (int i = 0; i < _numberOfAnswers; i++)
        {
            SingleAnswerPanelController c = _singleAnswerPanels.get(i);

            if (c.isSelectedAsCorrectAnswer())
            {
                list.add(i);
            }
        }

        return list;
    }

    /**
     * Returns the Content of the enabled answerpanels as List
     * 
     * @return List of Answers
     */
    public List<String> getAnswers()
    {
        ArrayList<String> strings = new ArrayList<String>();

        for (int i = 0; i < _numberOfAnswers; i++)
        {
            SingleAnswerPanelController controller = _singleAnswerPanels.get(i);
            if (controller.isToggleButtonSelected())
            {
                strings.add(controller.getAnswerText());
            }
            else
            {
                break;
            }
        }

        return strings;
    }

    /**
     * returns the ui for the answer panel
     * 
     * @return JPanel
     */
    public JPanel getPanel()
    {
        return _ui.getPanel();
    }

    @Override
    public boolean amIAllowedToToggle(int number, boolean newStatus)
    {
        if (newStatus)
        {
            SingleAnswerPanelController toggler = _singleAnswerPanels.get(number - 1);
            return toggler.isToggleButtonSelected();
        }
        else
        {
            SingleAnswerPanelController toggler = _singleAnswerPanels.get(number + 1);
            return !toggler.isToggleButtonSelected();
        }
    }

}
