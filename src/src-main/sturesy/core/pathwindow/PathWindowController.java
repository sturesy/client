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
package sturesy.core.pathwindow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFileChooser;

import sturesy.core.Localize;

/**
 * The PathWindowController will be displayed the first time sturesy is started.
 * it will ask for the main user directory in where all the stuff will be placed
 * 
 * @author w.posdorfer
 * 
 */
public class PathWindowController
{

    private PathWindowUI _ui;

    private File _selectedFolder;

    /**
     * creates a new Controller
     */
    public PathWindowController()
    {

        _ui = new PathWindowUI();

        registerListeners();

        _ui.showDialog(null);
    }

    /**
     * @return the folder which was selected during the process
     */
    public File getSelectedFolder()
    {
        return _selectedFolder;
    }

    private void cancelButtonAction()
    {
        System.exit(0);
    }

    private void okButtonAction()
    {
        if (_ui.getTextField().getText().length() > 0 && new File(_ui.getTextField().getText()).exists())
        {
            _ui.hideDialog();
        }
        else
        {
            _ui.getErrorLabel().setText(Localize.getString("label.invalid.file"));
        }
    }

    private void browseButtonAction()
    {
        JFileChooser fc = new JFileChooser(System.getProperty("user.home"));
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = fc.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION)
        {
            _selectedFolder = fc.getSelectedFile();
        }

        _ui.getTextField().setText(_selectedFolder != null ? _selectedFolder.getAbsolutePath() : "");
    }

    private void registerListeners()
    {
        _ui.getCancelButton().addActionListener(e -> cancelButtonAction());
        _ui.getOkButton().addActionListener(e -> okButtonAction());
        _ui.getChooseButton().addActionListener(e -> browseButtonAction());
        _ui.getDialog().addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                cancelButtonAction();
            }
        });
    }

}
