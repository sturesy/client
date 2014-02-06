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
package sturesy.qgen.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
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
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import sturesy.Macintosh;
import sturesy.core.Localize;
import sturesy.core.backend.Loader;
import sturesy.core.ui.JGap;
import sturesy.core.ui.VerticalLayout;

/**
 * Graphical component for QuestionEditor
 * 
 * @author w.posdorfer
 * 
 */
public class QuestionEditorUI extends JFrame
{

    private static final long serialVersionUID = -1532068724096551094L;
    private JLabel _questionTitle;
    private JList _questionJList;
    private JButton _plusbutton;
    private JButton _minusbutton;
    private JButton _moveQuestionUp;
    private JButton _moveQuestionDown;

    private JSplitPane _splitpane;

    private Map<MenuItems, JMenuItem> _menuitems;

    /**
     * Creates the UI for QuestionEditor, passing a listmodel and the UI of the
     * EditPanel
     * 
     * @param listmodel
     *            a listmodel holding the Questions
     * @param editpanel
     *            a JPanel to be displayed on the right side, should be the UI
     *            of <code>EditPanel</code>
     * @param dividerlocation
     *            the Location of the Divider in the Splitpane
     */
    public QuestionEditorUI(DefaultListModel listmodel, JPanel editpanel, int dividerlocation)
    {
        Macintosh.enableFullScreen(this);
        setIconImage(Loader.getImageIcon(Loader.IMAGE_STURESY).getImage());
        setTitle(Localize.getString(Localize.QUESTIONEDITOR));

        JPanel leftpanel = setupLeftPanel(listmodel);

        JPanel southpanel = setupSouthPanel();

        _menuitems = new HashMap<MenuItems, JMenuItem>();

        _splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftpanel, editpanel);
        _splitpane.setDividerLocation(dividerlocation);

        setLayout(new BorderLayout());

        add(_splitpane, BorderLayout.CENTER);
        add(southpanel, BorderLayout.SOUTH);

        setUpMenuBar();
    }

    private void setUpMenuBar()
    {

        // FILE MENU
        JMenu fileMenu = new JMenu(Localize.getString("menu.file"));
        addMenuItem(Localize.getString("button.new"), Localize.getString("tool.questiongen.new"), fileMenu,
                MenuItems.NEWQUESTIONSET);
        addMenuItem(Localize.getString("button.load") + "...", Localize.getString("tool.questiongen.load"), fileMenu,
                MenuItems.LOADQUESTIONSET);
        addMenuItem(Localize.getString("button.save"), Localize.getString("tool.questiongen.save"), fileMenu,
                MenuItems.SAVEQUESTIONSET);

        // IMPORT MENU
        JMenu importMenu = new JMenu(Localize.getString("menu.import"));
        addMenuItem(Localize.getString("button.import.questions"), null, importMenu, MenuItems.IMPORTQUESTION);
        addMenuItem(Localize.getString("button.import.questionset"), null, importMenu, MenuItems.IMPORTQUESTIONSET);
        addMenuItem(Localize.getString("button.import.qti"), null, importMenu, MenuItems.IMPORTQTI);

        // PREVIEW MENU
        JMenu previewMenu = new JMenu(Localize.getString("menu.preview"));
        addMenuItem(Localize.getString("button.preview.questionset"), null, previewMenu, MenuItems.PREVIEWQUESTION);

        // ADD TO MENUBAR
        JMenuBar menubar = new JMenuBar();
        menubar.add(fileMenu);
        menubar.add(importMenu);
        menubar.add(previewMenu);
        setJMenuBar(menubar);
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
    private void addMenuItem(String text, String tooltipText, JMenu menu, MenuItems identifier)
    {
        JMenuItem menuItem = menu.add(new JMenuItem(text));
        menuItem.setToolTipText(tooltipText);
        _menuitems.put(identifier, menuItem);
    }

    /**
     * Sets up the left panel, consisting of a JList and several Buttons
     * 
     * @param listmodel
     *            listmodel to use for the JList
     * @return JPanel
     */
    private JPanel setupLeftPanel(DefaultListModel listmodel)
    {
        JPanel leftpanel = new JPanel(new BorderLayout());
        leftpanel.setBorder(new TitledBorder(Localize.getString("label.question")));

        _questionJList = new JList(listmodel);
        _questionJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _questionJList.setCellRenderer(new QuestionListCellRenderer());
        JScrollPane scrollpane = new JScrollPane(_questionJList);
        scrollpane.setBorder(new EtchedBorder());
        leftpanel.add(scrollpane, BorderLayout.CENTER);

        JPanel plusminuspanel = new JPanel();
        _plusbutton = new JButton("+");
        _plusbutton.setToolTipText(Localize.getString("tool.questiongen.add.questionmodel"));
        _minusbutton = new JButton("-");
        _minusbutton.setToolTipText(Localize.getString("tool.questiongen.remove.questionmodel"));
        plusminuspanel.add(_plusbutton);
        plusminuspanel.add(_minusbutton);
        leftpanel.add(plusminuspanel, BorderLayout.NORTH);

        JPanel updownpanel = new JPanel();
        updownpanel.setLayout(new VerticalLayout(5, VerticalLayout.CENTER, 0));
        _moveQuestionUp = new JButton(Loader.getImageIconResized(Loader.IMAGE_ARROW_UP, 16, 16, 2));
        _moveQuestionDown = new JButton(Loader.getImageIconResized(Loader.IMAGE_ARROW_DOWN, 16, 16, 2));
        _moveQuestionUp.setToolTipText(Localize.getString("tool.questiongen.move.up"));
        _moveQuestionDown.setToolTipText(Localize.getString("tool.questiongen.move.down"));
        updownpanel.add(_moveQuestionUp);
        updownpanel.add(_moveQuestionDown);

        leftpanel.add(updownpanel, BorderLayout.WEST);

        return leftpanel;
    }

    /**
     * Setup the southpanel holding control buttons
     * 
     * @return JPanel
     */
    public JPanel setupSouthPanel()
    {
        JPanel southpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        _questionTitle = new JLabel("");
        southpanel.add(new JGap(9));
        southpanel.add(_questionTitle);
        southpanel.setBorder(new TitledBorder(Localize.getString("label.questionset")));
        return southpanel;
    }

    /**
     * Returns the JMenuItem declared by given identifier
     * 
     * @param identifier
     *            identifier of JMenuItem
     * @return JMenuItem
     */
    public JMenuItem getMenuItem(MenuItems identifier)
    {
        return _menuitems.get(identifier);
    }

    /**
     * Returns the Plus Question Button
     * 
     * @return JButton
     */
    public JButton getPlusbutton()
    {
        return _plusbutton;
    }

    /**
     * Returns the Minus Question Button
     * 
     * @return JButton
     */
    public JButton getMinusbutton()
    {
        return _minusbutton;
    }

    /**
     * Returns the List holding the Questions
     * 
     * @return JList of QuestionModel
     */
    public JList getQuestionJList()
    {
        return _questionJList;
    }

    /**
     * Return the QuestionSetTitle
     * 
     * @return JLabel
     */
    public JLabel getQuestionTitle()
    {
        return _questionTitle;
    }

    /**
     * Returns the Move Question Down Button
     * 
     * @return JButton
     */
    public JButton getMoveQuestionDown()
    {
        return _moveQuestionDown;
    }

    /**
     * Returns the Move Question Up Button
     * 
     * @return JButton
     */
    public JButton getMoveQuestionUp()
    {
        return _moveQuestionUp;
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

    public enum MenuItems
    {
        SAVEQUESTIONSET, LOADQUESTIONSET, NEWQUESTIONSET, IMPORTQUESTIONSET, IMPORTQUESTION, IMPORTQTI, PREVIEWQUESTION;
    }
}
