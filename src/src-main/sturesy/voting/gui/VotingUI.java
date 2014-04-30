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
package sturesy.voting.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;

import org.jfree.ui.IntegerDocument;

import sturesy.Macintosh;
import sturesy.core.Localize;
import sturesy.core.backend.Loader;
import sturesy.core.ui.MessageWindow;
import sturesy.util.Settings;

/**
 * The graphical Component belonging to the VotingWindow
 * 
 * @author w.posdorfer
 * 
 */
public class VotingUI extends JFrame
{

    public enum XButton
    {
        QRCODEBUTTON, STARTSTOPBUTTON, CLEARVOTESBUTTON, SHOWCORRECTANSWER, SHOWBARCHART, NEXTQUESTIONBUTTON, PREVIOUSQUESTIONBUTTON;
    }

    public static int CURRENTLAYOUT_MAXIMUM = 0;
    public static int CURRENTLAYOUT_MEDIUM = 1;
    public static int CURRENTLAYOUT_MINIMUM = 2;

    private static final long serialVersionUID = -1220783441728371137L;

    private Map<XButton, ActionListener> _buttonsToActionsMap = new HashMap<XButton, ActionListener>();

    public static final int ICONSIZE = 32;
    public static final String CARDLAYOUT_EVAL = "EVAL";
    public static final String CARDLAYOUT_VOTE = "VOTE";

    /** Displays Q: 5 / 20 */
    private JLabel _questionProgressLabel;
    private JTextField _timeLeftField;

    private JButton _startStopButton;
    private JButton _clearVotesButton;
    private JToggleButton _toggleShowCorrectAnswer;
    private JToggleButton _toggleQuestionChart;

    private JButton _nextQButton;
    private JButton _prevQButton;
    private JButton _showQRButton;
    public ImageIcon _playIcon;
    public ImageIcon _stopIcon;

    private JPanel _centerPanel;

    private CardLayout _cardlayout;
    private JLabel _bottomlabel;
    private JLabel _voteCountLabel;

    private List<JButton> _buttonsForMinimalLayout;
    private List<AbstractButton> _buttonsForMediumLayout;

    /** Panel holding the voting info */
    private JPanel _votingPanel;

    /** Panel holding the barchart */
    private JPanel _evaluationPanel;

    private int _currentLayout = CURRENTLAYOUT_MAXIMUM;

    /**
     * Constructor for VotingWindowUI
     * 
     * @param votingPanel
     *            panel to be used as votingpanel,
     * @param evaluationPanel
     *            panel to be used as evaluationpanel
     */
    public VotingUI(JPanel votingPanel, JPanel evaluationPanel)
    {
        _votingPanel = votingPanel;
        _evaluationPanel = evaluationPanel;

        Macintosh.enableFullScreen(this);

        layoutMainPanels();

        initToolbarForMaximumStyle();
        initToolbarForMinimumStyle();
        initToolbarForMediumStyle();

        addToolTips();

        setupWrapperActions();

        layoutToolbarMaximumStyle();
    }

    private void layoutMainPanels()
    {
        setIconImage(Loader.getImageIcon(Loader.IMAGE_STURESY).getImage());
        setLayout(new BorderLayout());

        _cardlayout = new CardLayout();
        _centerPanel = new JPanel(_cardlayout);
        _centerPanel.add(_votingPanel, CARDLAYOUT_VOTE);
        _centerPanel.add(_evaluationPanel, CARDLAYOUT_EVAL);
        _cardlayout.show(_centerPanel, CARDLAYOUT_VOTE);

        _bottomlabel = new JLabel(" ");
        _bottomlabel.setFont(_bottomlabel.getFont().deriveFont(20f));

        JPanel southpanel = new JPanel(new BorderLayout());
        southpanel.setBackground(Settings.getInstance().getColor("color.voting.main"));
        _voteCountLabel = new JLabel("0 " + Localize.getString("label.votes"));
        _voteCountLabel.setFont(_bottomlabel.getFont());

        southpanel.add(_bottomlabel, BorderLayout.CENTER);
        southpanel.add(_voteCountLabel, BorderLayout.EAST);

        add(_centerPanel, BorderLayout.CENTER);
        add(southpanel, BorderLayout.SOUTH);
    }

    private void initToolbarForMaximumStyle()
    {
        _playIcon = Loader.getImageIconResized(Loader.IMAGE_PLAY, ICONSIZE, ICONSIZE, Image.SCALE_SMOOTH);
        _stopIcon = Loader.getImageIconResized(Loader.IMAGE_STOP, ICONSIZE, ICONSIZE, Image.SCALE_SMOOTH);

        _questionProgressLabel = new JLabel(Localize.getString("label.question").substring(0, 1) + ": 0 / 0");
        _timeLeftField = new JTextField("0", 3);
        _timeLeftField.setHorizontalAlignment(JTextField.RIGHT);
        _timeLeftField.setDocument(new IntegerDocument());
        _timeLeftField.setFont(_timeLeftField.getFont().deriveFont(20.0f));

        _startStopButton = new JButton(_playIcon);
        _clearVotesButton = new JButton(Loader.getImageIconResized(Loader.IMAGE_UNDO, ICONSIZE, ICONSIZE,
                Image.SCALE_SMOOTH));

        _toggleShowCorrectAnswer = new JToggleButton(Loader.getImageIconResized(Loader.IMAGE_YELLOW_BAR, ICONSIZE,
                ICONSIZE, Image.SCALE_SMOOTH));
        _toggleQuestionChart = new JToggleButton(Loader.getImageIconResized(Loader.IMAGE_STURESY, ICONSIZE, ICONSIZE,
                Image.SCALE_SMOOTH), false);

        _nextQButton = new JButton(
                Loader.getImageIconResized(Loader.IMAGE_NEXT, ICONSIZE, ICONSIZE, Image.SCALE_SMOOTH));
        _prevQButton = new JButton(Loader.getImageIconResized(Loader.IMAGE_PREVIOUS, ICONSIZE, ICONSIZE,
                Image.SCALE_SMOOTH));
        _showQRButton = new JButton(Loader.getImageIconResized(Loader.IMAGE_BARCODE, ICONSIZE, ICONSIZE,
                Image.SCALE_SMOOTH));
    }

    private void layoutToolbarMaximumStyle()
    {
        Color toolbarColor = Settings.getInstance().getColor("color.voting.toolbar");
        JPanel toolbarpanelleft = new JPanel();
        toolbarpanelleft.add(new JLabel(Localize.getString("label.time")));
        toolbarpanelleft.add(_timeLeftField);
        toolbarpanelleft.add(_showQRButton);

        toolbarpanelleft.setBackground(toolbarColor);

        JPanel toolbarpanelcenter = new JPanel();
        toolbarpanelcenter.add(_startStopButton);
        toolbarpanelcenter.add(_clearVotesButton);

        toolbarpanelcenter.add(_toggleShowCorrectAnswer);
        toolbarpanelcenter.add(_toggleQuestionChart);
        toolbarpanelcenter.setBackground(toolbarColor);

        JPanel toolbarpanelright = new JPanel();
        toolbarpanelright.add(_prevQButton);
        toolbarpanelright.add(_questionProgressLabel);
        toolbarpanelright.add(_nextQButton);
        toolbarpanelright.setBackground(toolbarColor);

        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.add(toolbarpanelleft, BorderLayout.WEST);
        toolbar.add(toolbarpanelcenter, BorderLayout.CENTER);
        toolbar.add(toolbarpanelright, BorderLayout.EAST);
        toolbar.setBorder(new BevelBorder(BevelBorder.RAISED));

        add(toolbar, BorderLayout.NORTH);
    }

    private void initToolbarForMinimumStyle()
    {
        _buttonsForMinimalLayout = new ArrayList<JButton>();

        JButton timeAndBarCode = new JButton(Loader.getImageIconResized(Loader.IMAGE_CLOCK_AND_BARCODE, ICONSIZE,
                ICONSIZE, Image.SCALE_SMOOTH));
        JButton otherButtons = new JButton(Loader.getImageIconResized(Loader.IMAGE_MORE, ICONSIZE, ICONSIZE,
                Image.SCALE_SMOOTH));

        _buttonsForMinimalLayout.add(timeAndBarCode);
        _buttonsForMinimalLayout.add(_startStopButton);
        _buttonsForMinimalLayout.add(otherButtons);

        otherButtons.addActionListener(e -> otherButtonPopup(e, true));
        timeAndBarCode.addActionListener(e -> timeAndBarCodeActionPopup(e));
    }

    private void otherButtonPopup(ActionEvent e, boolean isForMinimal)
    {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem clearVotesItem = new JMenuItem(Loader.getImageIconResized(Loader.IMAGE_UNDO, ICONSIZE, ICONSIZE,
                Image.SCALE_SMOOTH));
        clearVotesItem.addActionListener(event -> performAction(XButton.CLEARVOTESBUTTON, event));

        JMenuItem previousButton = new JMenuItem(Loader.getImageIconResized(Loader.IMAGE_PREVIOUS, ICONSIZE, ICONSIZE,
                Image.SCALE_SMOOTH));
        previousButton.addActionListener(event -> performAction(XButton.PREVIOUSQUESTIONBUTTON, event));
        previousButton.setEnabled(_prevQButton.isEnabled());

        JMenuItem progressItem = new JMenuItem(_questionProgressLabel.getText());
        menu.add(clearVotesItem);

        if (isForMinimal)
        {
            JMenuItem showBarChartItem = new JMenuItem(_toggleQuestionChart.getIcon());
            showBarChartItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent event)
                {
                    _toggleQuestionChart.setSelected(!_toggleQuestionChart.isSelected());
                    performAction(XButton.SHOWBARCHART, event);
                }
            });

            JMenuItem showCorrectAnswer = new JMenuItem(_toggleShowCorrectAnswer.getIcon());
            showCorrectAnswer.addActionListener(event -> {
                _toggleShowCorrectAnswer.setSelected(!_toggleShowCorrectAnswer.isSelected());
                performAction(XButton.SHOWCORRECTANSWER, event);
            });
            menu.add(showCorrectAnswer);
            menu.add(showBarChartItem);

        }

        menu.add(previousButton);
        menu.add(progressItem);

        if (isForMinimal)
        {
            JMenuItem nextButton = new JMenuItem(Loader.getImageIconResized(Loader.IMAGE_NEXT, ICONSIZE, ICONSIZE,
                    Image.SCALE_SMOOTH));
            nextButton.addActionListener(event -> performAction(XButton.NEXTQUESTIONBUTTON, event));
            nextButton.setEnabled(_nextQButton.isEnabled());
            menu.add(nextButton);
        }

        menu.show((JComponent) e.getSource(), 20, 20);
    }

    private void timeAndBarCodeActionPopup(ActionEvent event)
    {
        JMenuItem barcode = new JMenuItem(Loader.getImageIconResized(Loader.IMAGE_BARCODE, ICONSIZE, ICONSIZE,
                Image.SCALE_SMOOTH));
        barcode.addActionListener(ex -> performAction(XButton.QRCODEBUTTON, ex));

        JPopupMenu menu = new JPopupMenu();

        menu.add(barcode);
        menu.add(_timeLeftField);
        menu.show((JComponent) event.getSource(), 20, 20);

    }

    private void layoutToolbarMinimumStyle()
    {
        Color toolbarColor = Settings.getInstance().getColor("color.voting.toolbar");
        JPanel toolbarPanel = new JPanel();
        toolbarPanel.setBackground(toolbarColor);
        for (JButton button : _buttonsForMinimalLayout)
        {
            toolbarPanel.add(button);
        }
        toolbarPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
        add(toolbarPanel, BorderLayout.NORTH);
    }

    private void initToolbarForMediumStyle()
    {
        _buttonsForMediumLayout = new ArrayList<AbstractButton>();

        JButton timeAndBarCode = new JButton(Loader.getImageIconResized(Loader.IMAGE_CLOCK_AND_BARCODE, ICONSIZE,
                ICONSIZE, Image.SCALE_SMOOTH));
        JButton otherButtons = new JButton(Loader.getImageIconResized(Loader.IMAGE_MORE, ICONSIZE, ICONSIZE,
                Image.SCALE_SMOOTH));

        _buttonsForMediumLayout.add(timeAndBarCode);
        _buttonsForMediumLayout.add(_startStopButton);
        _buttonsForMediumLayout.add(_toggleShowCorrectAnswer);
        _buttonsForMediumLayout.add(_toggleQuestionChart);
        _buttonsForMediumLayout.add(otherButtons);
        _buttonsForMediumLayout.add(_nextQButton);

        timeAndBarCode.addActionListener(e -> timeAndBarCodeActionPopup(e));
        otherButtons.addActionListener(e -> otherButtonPopup(e, false));
    }

    private void layoutToolbarMediumStyle()
    {
        Color toolbarColor = Settings.getInstance().getColor("color.voting.toolbar");
        JPanel toolbarPanel = new JPanel();
        toolbarPanel.setBackground(toolbarColor);

        for (AbstractButton button : _buttonsForMediumLayout)
        {
            toolbarPanel.add(button);
        }

        toolbarPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
        add(toolbarPanel, BorderLayout.NORTH);

    }

    private void setupWrapperActions()
    {
        _startStopButton.addActionListener(e -> performAction(XButton.STARTSTOPBUTTON, e));
        _clearVotesButton.addActionListener(e -> performAction(XButton.CLEARVOTESBUTTON, e));
        _toggleShowCorrectAnswer.addActionListener(e -> performAction(XButton.SHOWCORRECTANSWER, e));
        _toggleQuestionChart.addActionListener(e -> performAction(XButton.SHOWBARCHART, e));
        _nextQButton.addActionListener(e -> performAction(XButton.NEXTQUESTIONBUTTON, e));
        _prevQButton.addActionListener(e -> performAction(XButton.PREVIOUSQUESTIONBUTTON, e));
        _showQRButton.addActionListener(e -> performAction(XButton.QRCODEBUTTON, e));
    }

    /**
     * Converts the current layout to a minimalistic style, only containing 3
     * buttons
     */
    public void relayoutToolbarForMinimum()
    {
        remove(((BorderLayout) getContentPane().getLayout()).getLayoutComponent(BorderLayout.NORTH));
        _currentLayout = CURRENTLAYOUT_MINIMUM;
        layoutToolbarMinimumStyle();
    }

    /**
     * Converts the current layout to a medium style, containing only important
     * buttons
     */
    public void relayoutToolbarForMedium()
    {
        remove(((BorderLayout) getContentPane().getLayout()).getLayoutComponent(BorderLayout.NORTH));
        _currentLayout = CURRENTLAYOUT_MEDIUM;
        layoutToolbarMediumStyle();
    }

    /**
     * Converts the current layout to the maximum style, containing all the
     * elemts
     */
    public void relayoutToolbarForMaximum()
    {
        remove(((BorderLayout) getContentPane().getLayout()).getLayoutComponent(BorderLayout.NORTH));
        _currentLayout = CURRENTLAYOUT_MAXIMUM;
        layoutToolbarMaximumStyle();
    }

    /**
     * Add Tooltips to components
     */
    private void addToolTips()
    {
        _startStopButton.setToolTipText(Localize.getString("tool.vote.start.stop"));
        _clearVotesButton.setToolTipText(Localize.getString("tool.vote.clear.votings"));
        _showQRButton.setToolTipText(Localize.getString("tool.vote.show.qr"));
        _toggleQuestionChart.setToolTipText(Localize.getString("tool.vote.question.chart"));
        _toggleShowCorrectAnswer.setToolTipText(Localize.getString("tool.vote.show.correct.answer"));
    }

    /**
     * @return TextField displaying the current time left on the counter
     */
    public JTextField getTimeLeftField()
    {
        return _timeLeftField;
    }

    /**
     * @param TextField
     *            displaying the current time left on the counter
     */
    public void setTimeLeftFieldText(String text)
    {
        getTimeLeftField().setText(text);
    }

    /**
     * @return Button for Starting and Stopping of Votings
     */
    public JButton getStartStopButton()
    {
        return _startStopButton;
    }

    /**
     * @return Button for Clearing the current Votings
     */
    public JButton getClearVotesButton()
    {
        return _clearVotesButton;
    }

    /**
     * @return ToggleButton to display or hide the correct answer
     */
    public JToggleButton getToggleShowCorrectAnswer()
    {
        return _toggleShowCorrectAnswer;
    }

    /**
     * @return ToggleButton to display or hide the Chartdiagram
     */
    public JToggleButton getToggleQuestionChart()
    {
        return _toggleQuestionChart;
    }

    /**
     * @return Button to advance to the next questionmodel
     */
    public JButton getNextQButton()
    {
        return _nextQButton;
    }

    /**
     * @return Button to regress to the previous questionmodel
     */
    public JButton getPrevQButton()
    {
        return _prevQButton;
    }

    /**
     * @return Button to display the QRCode
     */
    public JButton getShowQRButton()
    {
        return _showQRButton;
    }

    /**
     * @return Label displaying the current QuestionModel of total Questions
     */
    public JLabel getQuestionProgressLabel()
    {
        return _questionProgressLabel;
    }

    /**
     * @return Panel containing the Questions and the ChartDiagram
     */
    public JPanel getCenterPanel()
    {
        return _centerPanel;
    }

    /**
     * Returns the {@link CardLayout} of the CenterPanel
     */
    public CardLayout getCardLayout()
    {
        return _cardlayout;
    }

    /**
     * Returns the Label displaying information about current lecture-id
     * 
     * @return JLabel
     */
    public JLabel getBottomLabel()
    {
        return _bottomlabel;
    }

    /**
     * Returns the Label displaying the current vote count
     * 
     * @return JLabel
     */
    public JLabel getVoteCountLabel()
    {
        return _voteCountLabel;
    }

    /**
     * Sets amount of votes to the VoteCountLabel, a localized String will be
     * appended at the end
     * 
     * @param votes
     *            amount of votes
     */
    public void setVoteCountLabelText(int votes)
    {
        getVoteCountLabel().setText(votes + " " + Localize.getString("label.votes"));
    }

    /**
     * Returns the Text from the TextField displaying the time left
     * 
     * @return a String containing only integers or empty String
     */
    public String getTimeLeftFieldText()
    {
        return getTimeLeftField().getText();
    }

    /**
     * Shows a green message window for 2000ms, using the String as a
     * resourcekey from the localization files
     * 
     * @param resourceKey
     *            a resourceKey from the localized files
     * 
     * @see Localize#getString(String)
     * 
     */
    public void showMessageWindowSuccess(String resourceKey)
    {
        MessageWindow.showMessageWindowSuccess(Localize.getString(resourceKey), 2000);
    }

    /**
     * Shows a red message window for 2000ms, using the String as a resourcekey
     * from the localization files
     * 
     * @param resourceKey
     *            a resourceKey from the localized files
     * 
     * @see Localize#getString(String)
     */
    public void showMessageWindowError(String resourceKey)
    {
        MessageWindow.showMessageWindowError(Localize.getString(resourceKey), 2000);
    }

    /**
     * Shows a ConfirmationDialog asking for Votes to be deleted
     * 
     * @return {@link JOptionPane#YES_OPTION} or {@link JOptionPane#NO_OPTION}
     */
    public int acceptDeleteVote()
    {
        return JOptionPane.showConfirmDialog(this, Localize.getString("label.confirm.delete.vote"), "",
                JOptionPane.YES_NO_OPTION);
    }

    /**
     * Sets the text to be displayed in the bottomLabel, a localized string will
     * be prepended
     * 
     * @param lectureID
     *            Lecture-ID to be appended to the localized String
     */
    public void setBottomLabelText(String lectureID)
    {
        getBottomLabel().setText(Localize.getString("label.lecture.id.to.vote") + ": " + lectureID);
    }

    /**
     * Sets text to be displayed in the QuestionProgressLabel
     * 
     * @see {@link #getQuestionProgressLabel()}
     * 
     * @param text
     *            Text to be displayed
     */
    public void setQuestionProgressLabelText(String text)
    {
        getQuestionProgressLabel().setText(text);
    }

    /**
     * Displays the card layout in center
     * 
     * @param card
     *            layout VotingWindowUI.CARDLAYOUT_EVAL = "EVAL";
     *            VotingWindowUI.CARDLAYOUT_VOTE = "VOTE";
     */
    public void showCardLayout(String cardlayoutEval)
    {
        getCardLayout().show(getCenterPanel(), cardlayoutEval);
        refreshContents();
    }

    /**
     * returns if the question chart is shown or not
     * 
     * @return true if questionChart is shown
     */
    public boolean isShowQuestionChart()
    {
        return getToggleQuestionChart().isSelected();
    }

    /**
     * Sets if the correct answer is marked
     */
    public void setToggleShowCorrectAnswerState(boolean newState)
    {
        getToggleShowCorrectAnswer().setSelected(newState);
    }

    /**
     * return if the correct answer is marked
     * 
     * @return boolean true if the correct answer is marked
     */
    public boolean getToggleShowCorrectAnswerState()
    {
        return getToggleShowCorrectAnswer().isSelected();
    }

    /**
     * Set the next question button enabled or disabled
     * 
     * @param boolean the enable state -true if the button is enabled, else
     *        false
     */
    public void setNextButtonEnabled(boolean b)
    {
        getNextQButton().setEnabled(b);
    }

    /**
     * Set the previous question button enabled or disabled
     * 
     * @param boolean the enable state -true if the button is enabled, else
     *        false
     */
    public void setPrevButtonEnabled(boolean b)
    {
        getPrevQButton().setEnabled(b);
    }

    private void performAction(XButton button, ActionEvent event)
    {
        ActionListener action = _buttonsToActionsMap.get(button);
        if (action != null)
        {
            action.actionPerformed(event);
        }
    }

    public void setActionForButton(ActionListener action, XButton button)
    {
        _buttonsToActionsMap.put(button, action);
    }

    public int getCurrentLayout()
    {
        return _currentLayout;
    }

    public void refreshContents()
    {
        this.revalidate();
        this.repaint();
    }
}
