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
package sturesy.settings.websettings;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import sturesy.core.Localize;
import sturesy.core.ui.JGap;

/**
 * Manage and Create LectureID's and Passwords
 * 
 * @author w.posdorfer
 * 
 */
public class WebSettingsPasswordUI extends JPanel
{

    private static final long serialVersionUID = -7333317707308591102L;

    private JTable _table;
    private JScrollPane _scrollpane;
    private JCheckBox _passwordVisibleBox;
    private JButton _redeemToken;
    private JButton _manualAdd;

    public WebSettingsPasswordUI()
    {
        setLayout(new GridBagLayout());

        _passwordVisibleBox = new JCheckBox(Localize.getString("checkbox.show.password"));
        _passwordVisibleBox.setSelected(false);

        _table = new JTable();
        _table.getTableHeader().setVisible(true);
        _table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _scrollpane = new JScrollPane(_table);
        _scrollpane.setMinimumSize(new Dimension(50, 150));
        _scrollpane.setPreferredSize(new Dimension(50, 150));

        _redeemToken = new JButton(Localize.getString("button.redeem.token"));
        _manualAdd = new JButton(Localize.getString("button.add.new.lectureid"));
        _redeemToken.setToolTipText(Localize.getString("tool.settings.websettings.redeem.token"));
        _manualAdd.setToolTipText(Localize.getString("tool.settings.websettings.manual.add"));

        add(_passwordVisibleBox, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        add(new JGap(20), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        add(_scrollpane, new GridBagConstraints(0, 2, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        add(_redeemToken, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        add(_manualAdd, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        add(new JGap(10), new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    }

    /**
     * @return Manual Lecture-ID add button
     */
    public JButton getManualAdd()
    {
        return _manualAdd;
    }

    /**
     * @return TokenRedemtion Button
     */
    public JButton getRedeemToken()
    {
        return _redeemToken;
    }

    /**
     * @return Table holding information about LectureIDs
     */
    public JTable getTable()
    {
        return _table;
    }

    /**
     * @return Should passwords be visible Checkbox
     */
    public JCheckBox getPasswordVisibleBox()
    {
        return _passwordVisibleBox;
    }

}
