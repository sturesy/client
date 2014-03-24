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
package sturesy.test.settings;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import sturesy.core.plugin.ISettingsScreen;
import sturesy.settings.SettingsController;
import sturesy.settings.SettingsUI;
import sturesy.settings.about.About;
import sturesy.settings.mainsettings.MainSettingsController;
import sturesy.util.Settings;

@RunWith(MockitoJUnitRunner.class)
public class TestSettingsController
{
    @Mock
    private SettingsUI _ui;
    @Mock
    private JButton _cancelButton;
    @Mock
    private JButton _saveAndCloseButton;
    @Mock
    private JButton _saveButton;
    @Mock
    private JFrame _frame;
    @Mock
    private JList _list;
    @Mock
    private Component _firstSelected;
    @Mock
    private Settings _propertyHandler;

    @Before
    public void beforeEachTest()
    {
        MockitoAnnotations.initMocks(this);
        configureUIForListener();
    }

    private void configureUIForListener()
    {
        when(_ui.getSaveButton()).thenReturn(_saveButton);
        when(_ui.getSaveAndCloseButton()).thenReturn(_saveAndCloseButton);
        when(_ui.getCancelButton()).thenReturn(_cancelButton);

        when(_ui.getFrame()).thenReturn(_frame);
        when(_ui.getIconList()).thenReturn(_list);
    }

    @Test
    public void testConstructor()
    {
        List<ISettingsScreen> settingsScreen = createListOfSettingsThree();
        Component component = mock(Component.class);
        when(_ui.getSelectedSettingsScreenValue()).thenReturn(settingsScreen.get(0));
        when(settingsScreen.get(0).getPanel()).thenReturn(component);
        new SettingsController(_ui, settingsScreen, _propertyHandler);
        verify(_ui, times(1)).setNewSettingsPanel(component);
        verify(_ui, times(1)).getSelectedSettingsScreenValue();
        verify(settingsScreen.get(0), times(1)).getPanel();
    }

    @Test
    public void testCancel()
    {
        List<ISettingsScreen> listOfSettings = createListOfSettingsThree();
        configureListSelectionChanged(listOfSettings);

        SettingsController controller = new SettingsController(_ui, listOfSettings, _propertyHandler);
        when(_ui.getFrameSize()).thenReturn(new Dimension(10, 10));
        controller.cancel();

        verify(_propertyHandler, times(1)).setProperty(eq(Settings.SETTINGSWINDOWSIZE), eq(new Dimension(10, 10)));
    }

    @Test
    public void testSaveSettingsWithSettingsScreens() throws Throwable
    {
        List<ISettingsScreen> listOfSettings = createListOfSettingsThree();
        configureListSelectionChanged(listOfSettings);

        SettingsController settingsController = new SettingsController(_ui, listOfSettings, _propertyHandler);
        settingsController.saveSettings();
        for (ISettingsScreen oneScreen : listOfSettings)
        {
            verify(oneScreen, times(1)).saveSettings();
        }
        verify(_propertyHandler, times(1)).save();
    }

    @Test
    public void testSaveSettingNoSettingsScreen()
    {
        List<ISettingsScreen> listOfSettings = new ArrayList<ISettingsScreen>();

        SettingsController controller = new SettingsController(_ui, listOfSettings, _propertyHandler);
        controller.saveSettings();
        verify(_propertyHandler, times(1)).save();
    }

    @Test
    public void testShow()
    {
        List<ISettingsScreen> listOfSettings = createListOfSettingsThree();
        configureListSelectionChanged(listOfSettings);
        SettingsController controller = new SettingsController(_ui, listOfSettings, _propertyHandler);
        WindowListener listener = mock(WindowListener.class);
        Dimension size = mock(Dimension.class);
        Component component = mock(Component.class);
        controller.displayController(component, listener);
        verify(_ui, times(1)).show(listener, size, component);
    }

    private void configureListSelectionChanged(List<ISettingsScreen> listOfSettings)
    {
        when(_ui.getSelectedSettingsScreenValue()).thenReturn(listOfSettings.get(0));
        when(listOfSettings.get(0).getPanel()).thenReturn(_firstSelected);
    }

    private List<ISettingsScreen> createListOfSettingsThree()
    {
        List<ISettingsScreen> settingsScreens = new ArrayList<ISettingsScreen>();
        settingsScreens.add(mock(MainSettingsController.class));
        settingsScreens.add(mock(ISettingsScreen.class));
        settingsScreens.add(mock(About.class));
        return settingsScreens;
    }
}
