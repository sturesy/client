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
package sturesy.voting.gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import sturesy.core.ui.HTMLLabel;

/**
 * Panel that holds two labels<br>
 * the left label is a JLabel and the right label is an HTMLLabel
 * 
 * @author w.posdorfer
 */
public class DoubleLabel extends JPanel
{

    private static final long serialVersionUID = -5463560983491142155L;

    JLabel _left;
    JLabel _right;

    /**
     * Creates a new empty doublelabel
     */
    public DoubleLabel()
    {
        _left = new JLabel();
        _right = new HTMLLabel();
        setLayout(new BorderLayout());
        add(_left, BorderLayout.WEST);
        add(_right, BorderLayout.CENTER);
    }

    /**
     * Creates a new doublelabel using left and right text
     * 
     * @param left
     *            text for left side
     * @param right
     *            text for right side
     */
    public DoubleLabel(String left, String right)
    {
        this();
        _left.setText(left);
        _right.setText(right);
    }

    /**
     * Set the right label text
     * 
     * @param text
     *            text to display
     */
    public void setRightText(String text)
    {
        _right.setText(text);
    }

    /**
     * Set the left label text
     * 
     * @param text
     *            text to display
     */
    public void setLeftText(String text)
    {
        _left.setText(text);
    }

    /**
     * Returns the text on the right
     * 
     * @return String
     */
    public String getRightText()
    {
        return _right.getText();
    }

    /**
     * Returns the text on the left
     * 
     * @return String
     */
    public String getLeftText()
    {
        return _left.getText();
    }

    /**
     * Returns the Label on the left
     * 
     * @return JLabel
     */
    public JLabel getLeft()
    {
        return _left;
    }

    /**
     * Returns the Label on the right
     * 
     * @return HTMLLabel
     */
    public JLabel getRight()
    {
        return _right;
    }

    @Override
    public void setFont(Font font)
    {
        // This will be called by the super()-constructor, but _left and _right
        // have not been initialized, so we need to check
        if (_left != null && _right != null)
        {
            _left.setFont(font);
            _right.setFont(font);
        }
    }
}
