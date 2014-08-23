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
package sturesy.settings.mainsettings;

import org.jfree.util.Log;
import sturesy.core.Localize;
import sturesy.core.backend.Loader;
import sturesy.core.backend.filter.filechooser.ZipFileFilter;
import sturesy.core.plugin.ISettingsScreen;
import sturesy.core.ui.MessageWindow;
import sturesy.core.ui.NotificationService;
import sturesy.core.ui.NotificationWindow;
import sturesy.update.UpdateChecker;
import sturesy.util.Settings;
import sturesy.util.ZIPExtract;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Contains the Mainsettings like Maindirectory etc...
 * 
 * @author w.posdorfer
 * 
 */
public class MainSettingsController extends ObservableMainSettings implements ISettingsScreen
{

    private String _name;
    private ImageIcon _icon;

    private JTabbedPane _componentToDisplay;
    private MainSettingsUI _gui;
    private final File _pluginsDirectory;
    private final String _maindirectoryAbsolutePath;

    public MainSettingsController(String maindirectoryAbsolutePath, File pluginsDirectory)
    {
        _maindirectoryAbsolutePath = maindirectoryAbsolutePath;
        _pluginsDirectory = pluginsDirectory;
        _name = Localize.getString(Localize.MAINSETTINGS);
        _icon = Loader.getImageIconResized(Loader.IMAGE_SETTINGS, 64, 64, Image.SCALE_SMOOTH);
        _componentToDisplay = new JTabbedPane();
        _gui = new MainSettingsUI(maindirectoryAbsolutePath);
        _gui.getUpdatefrequency().setSelectedIndex(Settings.getInstance().getInteger(Settings.UPDATE_FREQUENCY));

        _gui.getScreens().setSelectedIndex(Settings.getInstance().getInteger(Settings.NOTIFICATION_SCREEN));
        _gui.getPosition().setSelectedItem(Settings.getInstance().getString(Settings.NOTIFICATION_POSITION));

        _componentToDisplay.add(getName(), _gui);
        registerListeners();
    }

    @Override
    public String getName()
    {
        return _name;
    }

    @Override
    public ImageIcon getIcon()
    {
        return _icon;
    }

    @Override
    public Component getPanel()
    {
        return _componentToDisplay;
    }

    @Override
    public void saveSettings()
    {
        Settings settings = Settings.getInstance();
        settings.setProperty(Settings.UPDATE_FREQUENCY, _gui.getUpdatefrequency().getSelectedIndex());
        settings.setProperty(Settings.MAINDIRECTORY, _gui.getMainDir());
        settings.setProperty(Settings.NOTIFICATION_SCREEN, _gui.getScreens().getSelectedIndex());
        settings.setProperty(Settings.NOTIFICATION_POSITION, (String)_gui.getPosition().getSelectedItem());
        informMaindirectoryChanged(_gui.getMainDir());
    }

    /**
     * Action performed on Import button click
     */
    private void importPlugin()
    {
        JFileChooser filecho = new JFileChooser();
        filecho.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        filecho.setFileFilter(new ZipFileFilter());
        int chooserresult = filecho.showOpenDialog(null);

        File file = filecho.getSelectedFile();
        if (file != null && chooserresult == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                ZIPExtract.extractFolder(file, _pluginsDirectory);
                _gui.showMessageDialogInformation("label.plugin.restart");
            }
            catch (IOException e)
            {
                Log.error("Error Unzipping Plugin", e);
                MessageWindow.showMessageWindowError(Localize.getString("error.unzip.plugin"), 3500);
            }
        }
    }

    /**
     * Opens a FileChoose to pick a new Main-Directory
     */
    private void selectMaindir()
    {
        JFileChooser filecho = new JFileChooser(_maindirectoryAbsolutePath);
        filecho.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = filecho.showOpenDialog(null);

        File file = filecho.getSelectedFile();

        if (file != null && result == JFileChooser.APPROVE_OPTION)
        {
            _gui.setMainDirTextField(file.getAbsolutePath());
        }
    }

    private void checkForUpdates()
    {
        UpdateChecker checker = new UpdateChecker();
        checker.checkForUpdate(false);
    }

    private void testNotifications() {
        NotificationService service = NotificationService.getInstance();

        // retain saved settings
        NotificationWindow.Position oldPos = service.getPosition();
        GraphicsDevice oldScreen = service.getScreen();

        // apply current changes
        service.setScreen(_gui.getScreens().getSelectedIndex());
        service.setPosition((String) _gui.getPosition().getSelectedItem());

        // if position/screen has changed, reset service
        if (!oldPos.equals(service.getPosition()) || !oldScreen.equals(service.getScreen())) {
            service.reset();
        }

        // display test notification
        NotificationService.getInstance().addNotification("Test", "Test notification", 3);
    }

    /**
     * Registers all Listeners
     */
    private void registerListeners()
    {
        _gui.getMainDirButton();
        _gui.getMainDirButton().addActionListener(e -> selectMaindir());
        _gui.getImportPlugin().addActionListener(e -> importPlugin());
        _gui.getCheckForUpdates().addActionListener(e -> checkForUpdates());
        _gui.getNotificationTestButton().addActionListener(e -> testNotifications());
    }
}
