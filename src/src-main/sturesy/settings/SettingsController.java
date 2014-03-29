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
package sturesy.settings;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;

import org.apache.commons.collections.CollectionUtils;

import sturesy.core.Controller;
import sturesy.core.Localize;
import sturesy.core.error.ErrorController;
import sturesy.core.plugin.ISettingsScreen;
import sturesy.items.LectureID;
import sturesy.settings.about.About;
import sturesy.settings.colors.ColorSettingController;
import sturesy.settings.mainsettings.MainSettingsController;
import sturesy.settings.mainsettings.MainSettingsListener;
import sturesy.settings.websettings.WebSettings;
import sturesy.util.Settings;

public class SettingsController implements Controller
{
    private SettingsUI _ui;
    private List<ISettingsScreen> _listOfSettings;
    private Settings _settings;

    public SettingsController(Set<ISettingsScreen> settingsScreens, MainSettingsListener maindirectoryListener,
            String maindirectoryAbsolutePath, File pluginDirectory, Collection<LectureID> lectureId)
    {
        _settings = Settings.getInstance();
        _listOfSettings = new ArrayList<ISettingsScreen>();
        MainSettingsController mainSettings = new MainSettingsController(maindirectoryAbsolutePath, pluginDirectory);
        mainSettings.registerListener(maindirectoryListener);

        _listOfSettings.add(mainSettings);
        _listOfSettings.add(new ColorSettingController());
        _listOfSettings.add(new WebSettings(lectureId));
        _listOfSettings.addAll(settingsScreens);

        _listOfSettings.add(new About(_settings.getDimension("settings.window.size")));
        _ui = new SettingsUI(_listOfSettings.toArray());

        registerListener();
        triggerListSelectionChanged();
    }

    public SettingsController(SettingsUI ui, List<ISettingsScreen> settingScreens, Settings propertyHandler)
    {
        _settings = propertyHandler;
        _listOfSettings = settingScreens;
        _ui = ui;
        registerListener();
        triggerListSelectionChanged();
    }

    private void triggerListSelectionChanged()
    {
        if (CollectionUtils.isNotEmpty(_listOfSettings))
        {
            listSelectionChanged();
        }

    }

    public void cancel()
    {
        saveWindowState();
        WindowEvent wev = new WindowEvent(_ui.getFrame(), WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }

    /**
     * Saves the current windowposition when this window is closing
     */
    private void saveWindowState()
    {
        _settings.setProperty(Settings.SETTINGSWINDOWSIZE, _ui.getFrameSize());
    }

    public void saveSettings()
    {
        ErrorController errorController = new ErrorController();

        for (ISettingsScreen iss : _listOfSettings)
        {
            try
            {
                iss.saveSettings();
            }
            catch (Throwable e)
            {
                errorController.insertError(Localize.getString(Localize.SETTINGS) + ": " + iss.getName(), e);
            }
        }
        _settings.save();

        if (errorController.errorsOccured())
        {
            errorController.show();
        }
    }

    private void saveAndClose()
    {
        saveSettings(); // SAVE
        cancel(); // CLOSE
    }

    public void listSelectionChanged()
    {
        ISettingsScreen selectedSettingsScreenValue = (ISettingsScreen) _ui.getSelectedSettingsScreenValue();
        Component p = selectedSettingsScreenValue.getPanel();
        _ui.setNewSettingsPanel(p);
    }

    public JFrame getFrame()
    {
        return _ui.getFrame();
    }

    @Override
    public void displayController(Component relativeTo, WindowListener listener)
    {
        Dimension d = _settings.getDimension(Settings.SETTINGSWINDOWSIZE);
        _ui.show(listener, d, relativeTo);
    }

    /**
     * the action performed on a list selection change
     * 
     * @param e
     *            some event
     */
    private void listSelectionChangedAction(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting() && _ui.getSelectedSettingsScreenValue() != null)
        {
            listSelectionChanged();
        }
    }

    /**
     * Registers Listeners
     */
    private void registerListener()
    {
        _ui.getSaveButton().addActionListener(e -> saveSettings());
        _ui.getSaveAndCloseButton().addActionListener(e -> saveAndClose());
        _ui.getCancelButton().addActionListener(e -> cancel());
        _ui.getFrame().addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                saveWindowState();
                super.windowClosing(e);
            }
        });
        _ui.getIconList().addListSelectionListener(e -> listSelectionChangedAction(e));
    }

}
