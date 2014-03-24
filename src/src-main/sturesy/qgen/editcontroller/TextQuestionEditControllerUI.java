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

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import sturesy.core.ui.answerPanel.GrayLabel;
import sturesy.qgen.gui.RealIntegerDocument;

/**
 * TextQuestionEditControllerUI
 * 
 * @author w.posdorfer
 * 
 */
class TextQuestionEditControllerUI
{

    private JPanel _mainpanel;
    private JTextField _answerField;
    private JCheckBox _ignoreCaseBox;
    private JCheckBox _ignoreWhiteSpaceBox;
    private JTextField _toleranceField;

    /**
     * Creates UI
     */
    public TextQuestionEditControllerUI()
    {
        _mainpanel = new JPanel(new MigLayout("flowx, align left, wrap 2", "[][grow]"));

        _answerField = new JTextField(15);
        _ignoreCaseBox = new JCheckBox("Ignore Case");
        _ignoreWhiteSpaceBox = new JCheckBox("Ignore Whitespace");

        _toleranceField = new JTextField(5);
        _toleranceField.setDocument(new RealIntegerDocument());

        _mainpanel.add(new GrayLabel("Answer:"));
        _mainpanel.add(_answerField);

        _mainpanel.add(new GrayLabel("Tolerance:"));
        _mainpanel.add(_toleranceField, "wrap");

        _mainpanel.add(_ignoreCaseBox, "span 2");
        _mainpanel.add(_ignoreWhiteSpaceBox, "span 2");

    }

    /**
     * @return main JPanel
     */
    public JPanel getPanel()
    {
        return _mainpanel;
    }

    /**
     * @return Field with the correct answer
     */
    public JTextField getAnswerField()
    {
        return _answerField;
    }

    /**
     * @return checkbox determines wether to ignore case
     */
    public JCheckBox getIgnoreCaseBox()
    {
        return _ignoreCaseBox;
    }

    /**
     * @return checkbox determines wether to ignore whitespaces
     */
    public JCheckBox getIgnoreWhiteSpaceBox()
    {
        return _ignoreWhiteSpaceBox;
    }

    /**
     * @return field determines the error tolerance
     */
    public JTextField getToleranceField()
    {
        return _toleranceField;
    }

    /**
     * Sets all components inside the mainpanel to be enabled or disabled
     * 
     * @param enabled
     */
    public void setEnabled(boolean enabled)
    {
        for (Component c : _mainpanel.getComponents())
        {
            c.setEnabled(enabled);
        }
    }
}
