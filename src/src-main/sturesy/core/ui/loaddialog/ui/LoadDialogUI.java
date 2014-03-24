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
package sturesy.core.ui.loaddialog.ui;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;

import sturesy.core.Localize;
import sturesy.core.backend.Loader;
import sturesy.core.ui.MessageWindow;

/**
 * UI for the load dialog. It handles the layouting of the components in the
 * dialog. The UI contains three possible components. One in the north, one in
 * the center and one in the south.
 * 
 * @author jens.dallmann
 */
public class LoadDialogUI
{
    /**
     * the dialog
     */
    private JDialog _dialog;
    /**
     * the north panel which is free on default
     */
    private JPanel _northpanel;

    /**
     * Initialize the ui with three panels. if the panels are not null they will
     * be added to the dialog.
     * 
     * @param String
     *            the title of the dialog
     * @param JPanel
     *            the panel in the north
     * @param JPanel
     *            the panel in the center
     * @param JPanel
     *            the panel in the south
     */
    public LoadDialogUI(String title, JPanel northpanel, JPanel centerpanel, JPanel southpanel)
    {
        _northpanel = northpanel;
        _dialog = new JDialog();
        _dialog.setIconImage(Loader.getImageIcon(Loader.IMAGE_STURESY).getImage());
        _dialog.setTitle(title);
        _dialog.setLayout(new BorderLayout());
        if (northpanel != null)
        {
            _dialog.add(northpanel, BorderLayout.NORTH);
        }
        if (centerpanel != null)
        {
            _dialog.add(centerpanel, BorderLayout.CENTER);
        }
        if (southpanel != null)
        {
            _dialog.add(southpanel, BorderLayout.SOUTH);
        }
    }

    /**
     * removes the old panel and adds the new panel to the dialog
     * 
     * @param newNorthpanel
     */
    public void replaceNorthPanel(JPanel newNorthpanel)
    {
        if (_northpanel != null)
        {
            _dialog.remove(_northpanel);
        }
        _northpanel = newNorthpanel;
        _dialog.add(newNorthpanel, BorderLayout.NORTH);
    }

    /**
     * Closes the Dialog
     */
    public void closeDialog()
    {
        _dialog.dispose();
    }

    /**
     * shows the dialog
     */
    public void showDialog()
    {
        _dialog.setLocationRelativeTo(null);
        _dialog.pack();
        _dialog.setModal(true);
        _dialog.setVisible(true);
    }

    /**
     * Shows an error message on the screen
     * 
     * @param the
     *            ressource key for the error message.
     */
    public void showErrorMessage(String resourceKey)
    {
        MessageWindow.showMessageWindowError(Localize.getString(resourceKey), 2000);
    }

    /**
     * sets the screen modal or not.
     * 
     * @param isModal
     */
    public void setModal(boolean isModal)
    {
        _dialog.setModal(isModal);
    }
}
