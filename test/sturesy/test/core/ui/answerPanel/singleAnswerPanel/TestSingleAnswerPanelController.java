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
package sturesy.test.core.ui.answerPanel.singleAnswerPanel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Image;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import sturesy.core.Localize;
import sturesy.core.backend.Loader;
import sturesy.core.ui.answerPanel.SingleAnswerPanelController;
import sturesy.core.ui.answerPanel.SingleAnswerPanelUI;
import sturesy.core.ui.answerPanel.ToggleButton;
import sturesy.core.ui.answerPanel.ToggleDelegate;

@RunWith(MockitoJUnitRunner.class)
public class TestSingleAnswerPanelController
{
    /**
     * The width of the images shown on the on/off button
     */
    public static final int IMAGEWIDTH = 16;
    /**
     * The scaling type of the images shown on the on/off button
     */
    public static final int SCALESMOOTH = Image.SCALE_SMOOTH;

    @Mock
    private SingleAnswerPanelUI _ui;

    @Mock
    private ToggleButton _toggleButton;

    private SingleAnswerPanelController _controllerUnderTest;

    @Before
    public void beforeEachTest()
    {
        MockitoAnnotations.initMocks(this);
        ImageIcon selected = Loader.getImageIconResized(Loader.IMAGE_GREEN, IMAGEWIDTH, IMAGEWIDTH, SCALESMOOTH);
        ImageIcon unselected = Loader.getImageIconResized(Loader.IMAGE_RED, IMAGEWIDTH, IMAGEWIDTH, SCALESMOOTH);

        ToggleButton toggleButton = new ToggleButton(unselected, selected);
        _toggleButton = spy(toggleButton);
        when(_ui.getToggleButton()).thenReturn(_toggleButton);
        createController();
    }

    @Test
    public void testSetHasIsEnabledButtonTrue()
    {
        SingleAnswerPanelController controllerSpy = spy(_controllerUnderTest);
        when(_toggleButton.isSelected()).thenReturn(false);
        doNothing().when(controllerSpy).setToggleButtonSelected(false);
        controllerSpy.setHasToggleButton(true);// setHasIsEnabledButton(true);

        verify(_ui, times(1)).setToggleButtonVisible(true);
        verify(_toggleButton, times(1)).isSelected();
        verify(controllerSpy, times(1)).setToggleButtonSelected(false);
    }

    @Test
    public void testSetHasIsEnabledButtonFalse()
    {
        SingleAnswerPanelController controllerSpy = spy(_controllerUnderTest);
        controllerSpy.setHasToggleButton(false);

        verify(_ui, times(1)).setToggleButtonVisible(false);
        verify(controllerSpy, times(1)).setToggleButtonSelected(true);

    }

    @Test
    public void testBuildAnswer()
    {
        String firstCharacter = SingleAnswerPanelController.buildAnswerCharacter(0);
        assertEquals(Localize.getString("label.answer") + " A", firstCharacter);

        String lastCharacter = SingleAnswerPanelController.buildAnswerCharacter(25);
        assertEquals(Localize.getString("label.answer") + " Z", lastCharacter);
    }

    @Test
    public void testIsEnabledStateChangedTrue()
    {
        _controllerUnderTest.setToggleButtonSelected(true);
        verify(_ui, times(1)).setRadioButtonEnabled(true);
        verify(_ui, times(1)).setTextFieldEnabled(true);
    }

    @Test
    public void testIsEnabledStateChangedFalse()
    {
        _controllerUnderTest.setToggleButtonSelected(false);
        // two times because in init both are called one time
        verify(_ui, times(2)).setRadioButtonEnabled(false);
        verify(_ui, times(2)).setTextFieldEnabled(false);
    }

    @Test
    public void testGetPanel()
    {
        _controllerUnderTest.getPanel();
        verify(_ui, times(1)).getPanel();
    }

    @Test
    public void testGetAnswerText()
    {
        _controllerUnderTest.getAnswerText();
        verify(_ui, times(1)).getTextFieldText();
    }

    @Test
    public void testIsCorrectAnswer()
    {
        _controllerUnderTest.isSelectedAsCorrectAnswer();
        verify(_ui, times(1)).isRadioButtonSelected();
    }

    @Test
    public void testAddRadioButtonToButtonGroup()
    {
        ButtonGroup buttonGroup = mock(ButtonGroup.class);
        _controllerUnderTest.addRadioButtonToButtonGroup(buttonGroup);
        verify(_ui, times(1)).getIsCorrectAnswerButton();
        verify(buttonGroup, times(1)).add(null);
    }

    @Test
    public void testSetHasTextFieldTrue()
    {
        _controllerUnderTest.setHasTextField(true);
        verify(_ui, times(1)).setTextFieldVisible(true);
    }

    @Test
    public void testSetHasTextFieldFalse()
    {
        _controllerUnderTest.setHasTextField(false);
        verify(_ui, times(1)).setTextFieldVisible(false);
    }

    public void createController()
    {
        _controllerUnderTest = new SingleAnswerPanelController("", 1, mock(ToggleDelegate.class), true);
        _controllerUnderTest.init(_ui);
        _toggleButton = _ui.getToggleButton();

        verify(_ui, times(1)).setTextFieldEnabled(false);
        verify(_ui, times(1)).setRadioButtonEnabled(false);
        verify(_toggleButton, times(1)).addItemListener(Mockito.<ItemListener> any());
    }
}
