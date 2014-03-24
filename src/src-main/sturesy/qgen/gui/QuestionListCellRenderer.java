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
package sturesy.qgen.gui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import sturesy.core.Localize;
import sturesy.core.ui.HTMLStripper;
import sturesy.items.QuestionModel;

/**
 * A CellListRenderer which displays consecutive numbers and questiontext ,<br>
 * i.e. Q1 (What is a....)
 * 
 * @author w.posdorfer
 * 
 */
public class QuestionListCellRenderer extends DefaultListCellRenderer
{

    private static final long serialVersionUID = -2205581038524260860L;
    private final int _textlength;

    /**
     * Creates a QuestionListCellRenderer with a default fixed
     * text-length-abbreviation of 20
     */
    public QuestionListCellRenderer()
    {
        this(20);
    }

    /**
     * Creates a QuestionListCellRenderer with a fixed text-length-abbreviation
     * 
     * @param textlength
     *            textlength to be displayed as preview
     */
    public QuestionListCellRenderer(int textlength)
    {
        _textlength = textlength;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus)
    {
        Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        String question = "";
        QuestionModel model = ((QuestionModel) value);

        String questiontext = HTMLStripper.stripHTML2(model.getQuestion()).trim();

        if (questiontext.length() > _textlength)
        {
            question = " (" + questiontext.substring(0, _textlength) + "...)";
        }
        else if (model.getQuestion().length() > 0)
        {
            question = " (" + questiontext + ")";
        }

        setText(Localize.getString("label.question").substring(0, 1) + (index + 1) + question);

        return component;
    }
}
