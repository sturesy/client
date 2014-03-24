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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableModel;

import sturesy.core.Localize;
import sturesy.items.LectureID;
import sturesy.util.SturesySwingHelper;

/**
 * Manage and Create LectureID's and Passwords
 * 
 * @author w.posdorfer
 * 
 */
public class WebSettingsPassword
{

    private final WebSettingsPasswordUI _ui;
    private final Collection<LectureID> _lectureIds;

    public WebSettingsPassword(Collection<LectureID> lectureIds)
    {
        _lectureIds = lectureIds;
        _ui = new WebSettingsPasswordUI();
        updateTable();
        addListeners();
    }

    /**
     * Show a dialog asking for an ID and Password
     */
    private void manualAdduttonAction(ActionEvent e)
    {
        NewLectureController frame = new NewLectureController(_lectureIds);
        frame.show((Component) e.getSource());
        updateTable();
    }

    /**
     * Shows the token redeption dialog
     */
    private void redeemTokenAction(ActionEvent e)
    {
        TokenRedemption tr = new TokenRedemption(_lectureIds);
        tr.showDialog((Component) e.getSource());
        if (tr.isSuccessful())
        {
            updateTable();
        }
    }

    /**
     * Creates a TableModel for the LectureID-Table
     * 
     * @return PasswordTableModel
     */
    private PasswordTableModel getTableModel()
    {
        String[] columnnames = { Localize.getString("label.lecture.id"), Localize.getString("label.password"),
                Localize.getString("label.server.adress") };

        PasswordTableModel model = new PasswordTableModel(getLecturesAndPasswords(), new Vector<String>(
                Arrays.asList(columnnames)), 1);
        model.setPasswordVisible(_ui.getPasswordVisibleBox().isSelected());
        return model;
    }

    /**
     * Gather all {@link LectureID#} into a Vector for a table
     */
    private Vector<Vector<String>> getLecturesAndPasswords()
    {
        Vector<Vector<String>> result = new Vector<Vector<String>>();

        for (LectureID id : _lectureIds)
        {
            Vector<String> vec = new Vector<String>();
            vec.add(id.getLectureID());
            vec.add(id.getPassword());
            vec.add(id.getHost().toString());
            result.add(vec);
        }

        // Sort the Vector by LectureIDs
        Collections.sort(result, new LectureIDComparator());
        return result;
    }

    /**
     * Shows a PopupMenu to ask for row deletion
     * 
     * @param row
     *            to delete
     * @param pointOnScreen
     *            Location where to show a PopupMenu
     */
    private void deleteLectureIdAtRow(final int row, MouseEvent e)
    {
        if (row >= 0)
        {
            final JPopupMenu menu = new JPopupMenu();
            JMenuItem deleteItem = new JMenuItem(Localize.getString("button.delete"));

            menu.add(deleteItem);
            deleteItem.addActionListener(new ActionListener()
            {
                @SuppressWarnings("unchecked")
                public void actionPerformed(ActionEvent e)
                {

                    String lid = (String) _ui.getTable().getValueAt(row, 0);
                    String pw = (String) ((Vector<String>) ((DefaultTableModel) _ui.getTable().getModel())
                            .getDataVector().elementAt(row)).elementAt(1);
                    String url = (String) _ui.getTable().getValueAt(row, 2);

                    _lectureIds.remove(new LectureID(lid, pw, url));
                    ((PasswordTableModel) _ui.getTable().getModel()).removeRow(row);
                    menu.setVisible(false);
                }
            });

            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    /**
     * Returns the Graphical component
     * 
     * @return JPanel
     */
    public JPanel getPanel()
    {
        return _ui;
    }

    /**
     * updates The Table
     */
    private void updateTable()
    {
        PasswordTableModel model = getTableModel();
        _ui.getTable().setModel(model);
        model.fireTableStructureChanged();
        model.fireTableDataChanged();
    }

    /**
     * Changes the visibility of passwords in the Table
     * 
     * @param visible
     *            should the password be visible
     */
    private void changePasswordVisible(boolean visible)
    {
        PasswordTableModel model = (PasswordTableModel) _ui.getTable().getModel();
        model.setPasswordVisible(visible);
        model.fireTableDataChanged();
    }

    /**
     * Adds Listeners to the Components
     */
    private void addListeners()
    {
        _ui.getRedeemToken().addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                redeemTokenAction(e);
            }
        });
        _ui.getManualAdd().addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                manualAdduttonAction(e);
            }
        });
        _ui.getPasswordVisibleBox().addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                boolean selected = e.getStateChange() == ItemEvent.SELECTED;
                changePasswordVisible(selected);
            }
        });
        _ui.getTable().addMouseListener(new MouseAdapter()
        {
            public void mouseReleased(MouseEvent e)
            {
                if (SturesySwingHelper.isRightClick(e))
                {
                    int row = _ui.getTable().rowAtPoint(e.getPoint());
                    _ui.getTable().getSelectionModel().setSelectionInterval(row, row);
                    deleteLectureIdAtRow(row, e);
                }
            }
        });
    }

    /**
     * Comparator to Sort a Vector of Strings by the 1st String
     */
    private final class LectureIDComparator implements Comparator<Vector<String>>
    {
        public int compare(Vector<String> o1, Vector<String> o2)
        {
            return o1.get(0).compareTo(o2.get(0));
        };
    }
}
