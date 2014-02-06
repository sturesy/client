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
package sturesy.voting.gui.renderer;

import java.awt.Color;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jfree.chart.renderer.category.BarRenderer;

/**
 * Renders the Bars in a jFreeChart-BarChart in orange or red/green
 * 
 * @author w.posdorfer
 * 
 */
public class AnswersBarRenderer extends BarRenderer
{
    private static final long serialVersionUID = 6040964908042912532L;
    private boolean _showAnswers;
    private final List<Integer> _correctAnswer;

    /**
     * Creates a new AnswersBarRenderer
     * 
     * @param showAnswers
     *            <code>false</code> - orange color , <code>true</code>
     *            red/green color
     * @param correctAnswers
     *            the index of the correct answer
     */
    public AnswersBarRenderer(boolean showAnswers, int correctAnswer)
    {
        this(showAnswers, correctAnswer == -1 ? new ArrayList<Integer>() : Arrays.asList(correctAnswer));
    }

    public AnswersBarRenderer(boolean showAnswers, List<Integer> correctAnswers)
    {
        _showAnswers = showAnswers;
        _correctAnswer = correctAnswers;
    }

    @Override
    public Paint getItemPaint(final int row, final int column)
    {
        if (_showAnswers && !_correctAnswer.isEmpty())
        {
            if (_correctAnswer.contains(column))
            {
                return Color.green;
            }
            else
            {
                return Color.red;
            }
        }
        else
        {
            return Color.orange;
        }
    }
}
