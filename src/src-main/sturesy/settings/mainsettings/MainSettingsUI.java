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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sturesy.core.Localize;
import sturesy.core.plugin.ISettingsScreen;
import sturesy.core.ui.JGap;
import sturesy.update.UpdateFrequency;
import sturesy.update.UpdateFrequencyCellRenderer;

/**
 * The Panel which gets displayed on {@link ISettingsScreen#getPanel()}
 * 
 * @author w.posdorfer
 * 
 */
public class MainSettingsUI extends JPanel
{

    private static final long serialVersionUID = -6308968376964709535L;
    private final Insets INSETS_ZERO = new Insets(0, 0, 0, 0);
    private JTextField _maindirtextfield;
    private JButton _maindirchoosenew;
    private JButton _importPlugin;
    private JButton _checkForUpdates;
    private JComboBox _updatefrequency;

    public MainSettingsUI(String maindirectoryAbsolutPath)
    {
        setLayout(new GridBagLayout());

        JLabel maindirlabel = new JLabel(Localize.getString("label.maindir"));
        _maindirtextfield = new JTextField(30);
        _maindirtextfield.setText(maindirectoryAbsolutPath == null ? "" : maindirectoryAbsolutPath);
        // _maindirtextfield.setPreferredSize(new Dimension(350,
        // _maindirtextfield.getSize().height));
        _maindirchoosenew = new JButton(Localize.getString("button.select"));
        _checkForUpdates = new JButton(Localize.getString("button.check.update"));

        _importPlugin = new JButton(Localize.getString("button.import.plugin"));

        JLabel updatelabel = new JLabel(Localize.getString("update.frequency"));
        _updatefrequency = new JComboBox(UpdateFrequency.values());
        _updatefrequency.setRenderer(new UpdateFrequencyCellRenderer());

        
        /**  MAINDIRECTORY **/
        add(maindirlabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, INSETS_ZERO, 0, 0));
        add(_maindirtextfield, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, INSETS_ZERO, 0, 0));
        add(_maindirchoosenew, new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, INSETS_ZERO, 0, 0));

        add(new JGap(10), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.1, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, INSETS_ZERO, 0, 0));

        /**  SELFUPDATE **/
        add(updatelabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, INSETS_ZERO, 0, 0));
        add(_updatefrequency, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, INSETS_ZERO, 0, 0));
        add(_checkForUpdates, new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, INSETS_ZERO, 0, 0));

        add(new JGap(10), new GridBagConstraints(0, 4, 1, 1, 1.0, 0.1, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, INSETS_ZERO, 0, 0));

        /**  PLUGINIMPORT **/
        add(new JLabel(Localize.getString("label.plugins.optional")), new GridBagConstraints(0, 5, 4, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, INSETS_ZERO, 0, 0));
        add(_importPlugin, new GridBagConstraints(0, 6, 4, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, INSETS_ZERO, 0, 0));

        add(new JGap(10), new GridBagConstraints(0, 7, 4, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, INSETS_ZERO, 0, 0));

    }

    /**
     * Returns the currently selected Maindirectory
     */
    public String getMainDir()
    {
        return _maindirtextfield.getText();
    }

    public JButton getMainDirButton()
    {
        return _maindirchoosenew;
    }

    public JButton getImportPlugin()
    {
        return _importPlugin;
    }

    public JButton getCheckForUpdates()
    {
        return _checkForUpdates;
    }

    public void setMainDirTextField(String absolutePath)
    {
        _maindirtextfield.setText(absolutePath);
    }

    public JComboBox getUpdatefrequency()
    {
        return _updatefrequency;
    }

    public void showMessageDialogInformation(String ressourceKey)
    {
        JOptionPane.showMessageDialog(this, Localize.getString(ressourceKey), "", JOptionPane.INFORMATION_MESSAGE);
    }

}
