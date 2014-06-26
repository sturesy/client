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
package sturesy.core.ui.editwindow;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import sturesy.core.Controller;
import sturesy.core.ui.SwapableListModel;
import sturesy.util.Settings;

/**
 * Class to handle common tasks of a "DetailViewController", it displays a List
 * on the left side and a detail view on the right side
 * 
 * @author w.posdorfer
 *
 */
public abstract class CommonEditWindowController implements Controller
{
    /** This views UI */
    protected CommonEditWindowUI _ui;
    /** The Listmodel used for the list on the left */
    protected SwapableListModel _listmodel;
    /** The DetailView on the right */
    protected JPanel _detailPanel;
    protected Settings _settings = Settings.getInstance();

    /**
     * No real constructor, call
     * {@link #setupUI(SwapableListModel, ListCellRenderer, int, JPanel)} when
     * done with configuration to setup UI
     */
    public CommonEditWindowController()
    {
    }

    /**
     * This sets-up the UI of the controller
     * 
     * @param listmodel
     *            listmodel to use on the left side
     * @param itemlistcellrenderer
     *            a specific cell-renderer for the list
     * @param dividerlocation
     *            the start-position of the splitview-divider
     * @param detailPanel
     *            the detailview on the right side
     * @param southpanelBorderTitle
     *            border title for the panel on the south, displaying the
     *            current "Set" of items
     * @param leftPanelBorderTitle
     *            border title for the left panel
     */
    protected void setupUI(SwapableListModel listmodel, ListCellRenderer itemlistcellrenderer, int dividerlocation,
            JPanel detailPanel, String southpanelBorderTitle, String leftPanelBorderTitle)
    {
        _ui = new CommonEditWindowUI(listmodel, itemlistcellrenderer, dividerlocation, detailPanel,
                southpanelBorderTitle, leftPanelBorderTitle);
        setuplisteners();
        setupTooltips();
    }

    private void moveDownButtonPressed()
    {
        int index = _ui.getItemList().getSelectedIndex();
        if (index + 1 < _listmodel.getSize())
        {
            swapItemsInList(index, index + 1);

            _listmodel.swap(index, index + 1);

            _ui.getItemList().setSelectedIndex(index + 1);
        }
    }

    private void moveUpButtonPressed()
    {
        int index = _ui.getItemList().getSelectedIndex();
        if (index > 0)
        {
            swapItemsInList(index - 1, index);

            _listmodel.swap(index - 1, index);

            _ui.getItemList().setSelectedIndex(index - 1);
        }
    }

    /**
     * Method is being called when a movement of items is triggered.<br>
     * the items in the listmodel have already been swapped
     * 
     * @param index1
     *            swap this index
     * @param index2
     *            with this index
     */
    public abstract void swapItemsInList(int index1, int index2);

    /**
     * The Plus button has been pressed, add a new item
     */
    public abstract void plusButtonPressed();

    /**
     * The Minus button has been pressed, remove the selected item
     */
    public abstract void minusButtonPressed();

    /**
     * The list selection has changed to the given index
     * 
     * @param index
     *            current index
     */
    public abstract void listSelectionChanged(int index);

    /**
     * The views window is closing, please save settings
     */
    public abstract void windowIsClosing();

    /**
     * The save-menu item was pressed, save the current item-set
     */
    public abstract void saveMenuItemPressed();

    /**
     * The load menu item was pressed, load something
     */
    public abstract void loadMenuItemPressed();

    /**
     * The new menu item was pressed, empty out the list and the detail view
     */
    public abstract void newMenuItemPressed();

    /**
     * Setup tooltips for buttons
     */
    public abstract void setupTooltips();

    /**
     * Presents this View
     * 
     * @param visible
     *            present or hide
     */
    public void setVisible(boolean visible)
    {
        _ui.getFrame().setVisible(visible);
    }

    /**
     * Returns this Controllers View
     * 
     * @return the view
     */
    public JFrame getFrame()
    {
        return _ui.getFrame();
    }

    /**
     * Registers the common listeners on the UI.
     */
    private void setuplisteners()
    {
        _ui.getPlusbutton().addActionListener(e -> plusButtonPressed());
        _ui.getMinusbutton().addActionListener(e -> minusButtonPressed());
        _ui.getItemList().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting())
            {
                listSelectionChanged(_ui.getItemList().getSelectedIndex());
            }
        });
        _ui.getItemList().addKeyListener(new KeyAdapter()
        {
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)
                {
                    listSelectionChanged(_ui.getItemList().getSelectedIndex());
                }
            }
        });
        _ui.getMoveItemDown().addActionListener(e -> moveDownButtonPressed());
        _ui.getMoveItemUp().addActionListener(e -> moveUpButtonPressed());
        _ui.getFrame().addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                windowIsClosing();
            }
        });
        _ui.getMenuItem(CommonEditWindowUI.MENUITEM_LOAD).addActionListener(e -> loadMenuItemPressed());
        _ui.getMenuItem(CommonEditWindowUI.MENUITEM_NEW).addActionListener(e -> newMenuItemPressed());
        _ui.getMenuItem(CommonEditWindowUI.MENUITEM_SAVE).addActionListener(e -> saveMenuItemPressed());
    }
}
