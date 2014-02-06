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
package sturesy.test.core.ui.renderer;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Paint;

import org.junit.Test;

import sturesy.voting.gui.renderer.AnswersBarRenderer;

public class TestAnswersBarRenderer
{
    /**
     * Testcase: columns: 3, showAnswers: true, correctAnswer: 1
     */
    @Test
    public void showCorrectAnswers()
    {
        AnswersBarRenderer answersBarRenderer = new AnswersBarRenderer(true, 1);
        Paint itemPaint = answersBarRenderer.getItemPaint(0, 0);
        assertEquals(Color.RED, itemPaint);
        itemPaint = answersBarRenderer.getItemPaint(0, 1);
        assertEquals(Color.GREEN, itemPaint);
        itemPaint = answersBarRenderer.getItemPaint(0, 2);
        assertEquals(Color.RED, itemPaint);
    }

    @Test
    public void showNotCorrectAnswers()
    {
        AnswersBarRenderer answersBarRenderer = new AnswersBarRenderer(false, 1);
        Paint itemPaint = answersBarRenderer.getItemPaint(0, 0);
        assertEquals(Color.ORANGE, itemPaint);
        itemPaint = answersBarRenderer.getItemPaint(0, 1);
        assertEquals(Color.ORANGE, itemPaint);
        itemPaint = answersBarRenderer.getItemPaint(0, 2);
        assertEquals(Color.ORANGE, itemPaint);
    }

    /**
     * Open: Is this really wanted? Isn't it better to mark all wrong answers
     * red?
     */
    @Test
    public void showCorrectAnswersNoCorrectAnswer()
    {
        AnswersBarRenderer answersBarRenderer = new AnswersBarRenderer(true, -1);
        Paint itemPaint = answersBarRenderer.getItemPaint(0, 0);
        assertEquals(Color.ORANGE, itemPaint);
        itemPaint = answersBarRenderer.getItemPaint(0, 1);
        assertEquals(Color.ORANGE, itemPaint);
        itemPaint = answersBarRenderer.getItemPaint(0, 2);
        assertEquals(Color.ORANGE, itemPaint);
    }

    @Test
    public void doNotShowCorrectAnswersNoCorrectAnswer()
    {
        AnswersBarRenderer answersBarRenderer = new AnswersBarRenderer(false, -1);
        Paint itemPaint = answersBarRenderer.getItemPaint(0, 0);
        assertEquals(Color.ORANGE, itemPaint);
        itemPaint = answersBarRenderer.getItemPaint(0, 1);
        assertEquals(Color.ORANGE, itemPaint);
        itemPaint = answersBarRenderer.getItemPaint(0, 2);
        assertEquals(Color.ORANGE, itemPaint);
    }
}
