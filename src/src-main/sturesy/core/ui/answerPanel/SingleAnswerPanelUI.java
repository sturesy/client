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

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import sturesy.core.backend.Loader;

/**
 * Graphical Component of SingleAnswerPanel
 * 
 * @author j.dallmann
 */
public class SingleAnswerPanelUI
{

    /**
     * The width of the images shown on the on/off button
     */
    public static final int IMAGEWIDTH = 16;
    /**
     * The scaling type of the images shown on the on/off button
     */
    public static final int SCALESMOOTH = Image.SCALE_SMOOTH;

    private JPanel _answerPanel;
    private JToggleButton _isCorrectAnswerButton;
    private JTextField _answerTextField;
    private ToggleButton _toggleButton;
    private JLabel _answerTextLabel;

    public SingleAnswerPanelUI(String answerText, boolean useRadioButtons)
    {
        ImageIcon selected = Loader.getImageIconResized(Loader.IMAGE_GREEN, IMAGEWIDTH, IMAGEWIDTH, SCALESMOOTH);
        ImageIcon unselected = Loader.getImageIconResized(Loader.IMAGE_RED, IMAGEWIDTH, IMAGEWIDTH, SCALESMOOTH);
        _toggleButton = new ToggleButton(unselected, selected);

        if (useRadioButtons)
        {
            _isCorrectAnswerButton = new JRadioButton();
        }
        else
        {
            _isCorrectAnswerButton = new JCheckBox();
        }

        _answerTextField = new JTextField(20);

        MigLayout migLayout = new MigLayout("flowx,align left, insets 0", "[][fill][grow,fill][]");

        _answerTextLabel = new GrayLabel(answerText);

        _answerPanel = new JPanel(migLayout);
        _answerPanel.add(_isCorrectAnswerButton);
        _answerPanel.add(_answerTextLabel, new CC().width("70px"));
        _answerPanel.add(_answerTextField, new CC().growY());
        _answerPanel.add(_toggleButton);
    }

    /**
     * Returns the toggle button
     * 
     * @return ToggleButton
     */
    public ToggleButton getToggleButton()
    {
        return _toggleButton;
    }

    /**
     * Returns the AnswerTextfield
     * 
     * @return JTextField
     */
    public JTextField getAnswerTextField()
    {
        return _answerTextField;
    }

    /**
     * Is the RadioButton selected?
     * 
     * @return true or false
     */
    public boolean isRadioButtonSelected()
    {
        return _isCorrectAnswerButton.isSelected();
    }

    /**
     * Returns the correct answer button
     * 
     * @return JToggleButton
     */
    public JToggleButton getIsCorrectAnswerButton()
    {
        return _isCorrectAnswerButton;
    }

    /**
     * Sets the textfield to be enabled/disabled
     * 
     * @param enabled
     *            the new state of the textfield
     */
    public void setTextFieldEnabled(boolean enabled)
    {
        _answerTextField.setEnabled(enabled);
    }

    /**
     * Returns the Text of the TextField
     * 
     * @return String, never <code>null</code>
     */
    public String getTextFieldText()
    {
        return _answerTextField.getText();
    }

    /**
     * Sets the visibility of the toggle button
     * 
     * @param visible
     *            should the togglebutton be visible
     */
    public void setToggleButtonVisible(boolean visible)
    {
        _toggleButton.setVisible(visible);
    }

    /**
     * Sets the radio buttons new enabled-state
     * 
     * @param isEnabled
     *            should the radiobutton be enabled
     */
    public void setRadioButtonEnabled(boolean isEnabled)
    {
        _isCorrectAnswerButton.setEnabled(isEnabled);
    }

    /**
     * Sets the labels new enabled-state
     * 
     * @param isEnabled
     *            should the label be enabled
     */
    public void setAnswerTextLabelEnabled(boolean isEnabled)
    {
        _answerTextLabel.setEnabled(isEnabled);
    }

    /**
     * Sets the visibility of the Textfield
     * 
     * @param visible
     *            should the Textfield be visible
     */
    public void setTextFieldVisible(boolean visible)
    {
        _answerTextField.setVisible(visible);
    }

    /**
     * Sets the text of the Textfield
     * 
     * @param string
     *            the text
     */
    public void setTextFieldText(String string)
    {
        _answerTextField.setText(string);

    }

    /**
     * Selects or unselects the CorrectAnswer-Radiobutton
     * 
     * @param isCorrectAnswer
     *            the selectionstatus
     */
    public void setIsCorrectAnswer(boolean isCorrectAnswer)
    {
        _isCorrectAnswerButton.setSelected(isCorrectAnswer);
    }

    /**
     * Returns the graphical component
     * 
     * @return JPanel
     */
    public JPanel getPanel()
    {
        return _answerPanel;
    }

}
