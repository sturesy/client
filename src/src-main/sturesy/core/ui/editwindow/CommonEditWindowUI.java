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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import sturesy.Macintosh;
import sturesy.core.Localize;
import sturesy.core.backend.Loader;
import sturesy.core.ui.JGap;
import sturesy.core.ui.SFrame;
import sturesy.core.ui.SwapableListModel;
import sturesy.core.ui.VerticalLayout;

public final class CommonEditWindowUI
{
    public static final String MENUITEM_SAVE = "SAVE";
    public static final String MENUITEM_LOAD = "LOAD";
    public static final String MENUITEM_NEW = "NEW";
    private JFrame _frame;
    private JButton _plusbutton;
    private JButton _minusbutton;
    private JButton _moveItemUp;
    private JButton _moveItemDown;
    private JSplitPane _splitpane;

    private JLabel _itemTitle;
    private JList _itemList;

    private Map<String, JMenuItem> _menuitems = new HashMap<String, JMenuItem>();
    private String _southpanelBorderTitle;
    private String _leftPanelBorderTitle;

    public CommonEditWindowUI(SwapableListModel listmodel, ListCellRenderer itemlistcellrenderer, int dividerlocation,
            JPanel detailPanel, String southpanelBorderTitle, String leftPanelBorderTitle)
    {
        _southpanelBorderTitle = southpanelBorderTitle;
        _leftPanelBorderTitle = leftPanelBorderTitle;
        setupUI(listmodel, itemlistcellrenderer, dividerlocation, detailPanel);
    }

    private void setupUI(SwapableListModel listmodel, ListCellRenderer itemlistcellrenderer, int dividerlocation,
            JPanel detailPanel)
    {
        _frame = new SFrame();

        Macintosh.enableFullScreen(_frame);

        JPanel leftpanel = setupLeftPanel(listmodel, itemlistcellrenderer);

        JPanel southpanel = setupSouthPanel();

        _splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftpanel, detailPanel);
        _splitpane.setDividerLocation(dividerlocation);

        _frame.setLayout(new BorderLayout());

        _frame.add(_splitpane, BorderLayout.CENTER);
        _frame.add(southpanel, BorderLayout.SOUTH);

        setUpMenuBar();
    }

    /**
     * Sets up the left panel, consisting of a JList and several Buttons
     * 
     * @param listmodel
     *            listmodel to use for the JList
     * @return JPanel
     */
    private JPanel setupLeftPanel(SwapableListModel listmodel, ListCellRenderer itemlistcellrenderer)
    {
        JPanel leftpanel = new JPanel(new BorderLayout());
        leftpanel.setBorder(new TitledBorder(_leftPanelBorderTitle));

        _itemList = new JList(listmodel);
        _itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _itemList.setCellRenderer(itemlistcellrenderer);
        JScrollPane scrollpane = new JScrollPane(_itemList);
        scrollpane.setBorder(new EtchedBorder());
        leftpanel.add(scrollpane, BorderLayout.CENTER);

        JPanel plusminuspanel = new JPanel();
        _plusbutton = new JButton("+");
        _minusbutton = new JButton("-");
        plusminuspanel.add(_plusbutton);
        plusminuspanel.add(_minusbutton);
        leftpanel.add(plusminuspanel, BorderLayout.NORTH);

        JPanel updownpanel = new JPanel();
        updownpanel.setLayout(new VerticalLayout(5, VerticalLayout.CENTER, 0));
        _moveItemUp = new JButton(Loader.getImageIconResized(Loader.IMAGE_ARROW_UP, 16, 16, 2));
        _moveItemDown = new JButton(Loader.getImageIconResized(Loader.IMAGE_ARROW_DOWN, 16, 16, 2));

        updownpanel.add(_moveItemUp);
        updownpanel.add(_moveItemDown);

        leftpanel.add(updownpanel, BorderLayout.WEST);

        return leftpanel;
    }

    private void setUpMenuBar()
    {
        // FILE MENU
        JMenu fileMenu = new JMenu(Localize.getString("menu.file"));
        addMenuItem(Localize.getString("button.new"), null, fileMenu, MENUITEM_NEW);
        addMenuItem(Localize.getString("button.load") + "...", null, fileMenu, MENUITEM_LOAD);
        addMenuItem(Localize.getString("button.save"), null, fileMenu, MENUITEM_SAVE);

        // ADD TO MENUBAR
        JMenuBar menubar = new JMenuBar();
        menubar.add(fileMenu);
        _frame.setJMenuBar(menubar);
    }

    public void addMenuToBar(JMenu someMenu)
    {
        _frame.getJMenuBar().add(someMenu);
    }

    /**
     * Adds a JMenuItem with text to a JMenu and places it in the MenuItemsMap
     * 
     * @param text
     *            the text of the JMenuItem
     * @param tooltipText
     *            ToolTip for the JMenuItem, if <code>null</code> ToolTip is
     *            deactivated
     * @param menu
     *            the JMenu where to be added to
     * @param identifier
     *            for Map
     */
    public void addMenuItem(String text, String tooltipText, JMenu menu, String identifier)
    {
        JMenuItem menuItem = menu.add(new JMenuItem(text));
        menuItem.setToolTipText(tooltipText);
        _menuitems.put(identifier, menuItem);
    }

    /**
     * Setup the southpanel holding control buttons
     * 
     * @return JPanel
     */
    public JPanel setupSouthPanel()
    {
        JPanel southpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        _itemTitle = new JLabel("");
        southpanel.add(new JGap(9));
        southpanel.add(_itemTitle);
        southpanel.setBorder(new TitledBorder(_southpanelBorderTitle));
        return southpanel;
    }

    /**
     * Returns plusbutton
     * 
     * @return JButton
     */
    public JButton getPlusbutton()
    {
        return _plusbutton;
    }

    /**
     * Returns minusbutton
     * 
     * @return JButton
     */
    public JButton getMinusbutton()
    {
        return _minusbutton;
    }

    /**
     * Returns this item list
     * 
     * @return JList
     */
    public JList getItemList()
    {
        return _itemList;
    }

    public JLabel getItemTitle()
    {
        return _itemTitle;
    }

    /**
     * Returns move down button
     * 
     * @return JButton
     */
    public JButton getMoveItemDown()
    {
        return _moveItemDown;
    }

    /**
     * Returns move up button
     * 
     * @return JButton
     */
    public JButton getMoveItemUp()
    {
        return _moveItemUp;
    }

    /**
     * Returns this frame
     * 
     * @return JFrame
     */
    public JFrame getFrame()
    {
        return _frame;
    }

    /**
     * Returns the Location of the SplitPane-Divider for saving
     * 
     * @return some integer
     */
    public int getSplitPaneDividerLocation()
    {
        return _splitpane.getDividerLocation();
    }

    /**
     * Returns the JMenuItem declared by given identifier, might return
     * <code>null</code>
     * 
     * @param identifier
     *            identifier of JMenuItem
     * @return JMenuItem
     */
    public JMenuItem getMenuItem(String identifier)
    {
        return _menuitems.get(identifier);
    }

    public void setFrameTitleAndIcon(String title, Image imageicon)
    {
        _frame.setIconImage(imageicon);
        _frame.setTitle(title);
    }

}
