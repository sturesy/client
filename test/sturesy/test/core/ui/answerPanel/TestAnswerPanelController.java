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
package sturesy.test.core.ui.answerPanel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import sturesy.core.ui.answerPanel.AnswerPanelController;
import sturesy.core.ui.answerPanel.AnswerPanelUI;

@RunWith(MockitoJUnitRunner.class)
public class TestAnswerPanelController
{
    @Mock
    private AnswerPanelUI _ui;

    @Before
    public void beforeEachTest()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructorPassOne()
    {
        AnswerPanelController answerPanelController = new AnswerPanelController(1, _ui, true);
        verify(_ui, times(2)).addDefineAnswerPanel(Mockito.<JPanel> any());
        assertEquals(1, answerPanelController.getAnswers().size());
    }

    @Test
    public void testConstructorPassTwo()
    {
        AnswerPanelController answerPanelController = new AnswerPanelController(2, _ui, true);
        verify(_ui, times(3)).addDefineAnswerPanel(Mockito.<JPanel> any());

        assertEquals(2, answerPanelController.getAnswers().size());
    }

    @Test
    public void testConstructorPassThree()
    {
        AnswerPanelController answerPanelController = new AnswerPanelController(3, _ui, true);

        verify(_ui, times(4)).addDefineAnswerPanel(Mockito.<JPanel> any());

        assertEquals(2, answerPanelController.getAnswers().size());

    }

    @Test
    public void testConstructorWithVaryingAmount()
    {

        for (int i = 1; i <= 10; i++)
        {
            AnswerPanelController controller = new AnswerPanelController(i, true);

            assertEquals(i < 2 ? 1 : 2, controller.getAnswers().size());
        }

    }

    @Test
    public void testGetPanel()
    {
        AnswerPanelController answerPanelController = new AnswerPanelController(3, _ui, true);
        answerPanelController.getPanel();
        verify(_ui, times(1)).getPanel();
    }

}
