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

import java.awt.Color;

import javax.swing.JLabel;

/**
 * A JLabel that turns its text to gray when disabled
 * 
 * @author w.posdorfer
 * 
 */
public class GrayLabel extends JLabel
{
    private static final long serialVersionUID = 5307818913014145767L;

    private final Color normalColor;
    private static final Color grayColor = Color.gray;

    /**
     * Creates a JLabel that turns its text to gray when disabled
     * 
     * @param text
     *            The text to be displayed by the label.
     */
    public GrayLabel(String text)
    {
        super(text);
        normalColor = super.getForeground();
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        if (enabled)
        {
            setForeground(normalColor);
        }
        else
        {
            setForeground(grayColor);
        }
    }
}
