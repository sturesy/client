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
package sturesy.settings.mainsettings;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;

import org.jfree.util.Log;

import sturesy.core.Localize;
import sturesy.core.backend.Loader;
import sturesy.core.backend.filter.filechooser.ZipFileFilter;
import sturesy.core.plugin.ISettingsScreen;
import sturesy.core.ui.MessageWindow;
import sturesy.update.UpdateChecker;
import sturesy.util.Settings;
import sturesy.util.ZIPExtract;

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

    /**
     * Registers all Listeners
     */
    private void registerListeners()
    {
        _gui.getMainDirButton();
        _gui.getMainDirButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                selectMaindir();
            }
        });
        _gui.getImportPlugin().addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                importPlugin();
            }
        });
        _gui.getCheckForUpdates().addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                checkForUpdates();
            }
        });
    }
}
