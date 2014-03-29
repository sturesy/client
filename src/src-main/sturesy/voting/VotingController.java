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
package sturesy.voting;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import sturesy.core.Controller;
import sturesy.core.backend.Loader;
import sturesy.core.backend.services.crud.VotingCRUDService;
import sturesy.core.plugin.IPollPlugin;
import sturesy.core.plugin.Injectable;
import sturesy.core.plugin.QuestionVoteMatcher;
import sturesy.items.LectureID;
import sturesy.items.QuestionModel;
import sturesy.items.QuestionSet;
import sturesy.items.Vote;
import sturesy.items.VotingSet;
import sturesy.services.TechnicalVotingService;
import sturesy.services.TechnicalVotingServiceImpl;
import sturesy.services.TimeSource;
import sturesy.services.VotingTimeListener;
import sturesy.util.Settings;
import sturesy.util.web.WebCommands2;
import sturesy.util.web.WebVotingHandler;
import sturesy.voting.gui.QRWindowUI;
import sturesy.voting.gui.VotingUI;

/**
 * Displays the Voting Window containing a Toolbar at the top, and a CardLayout
 * at the Center containing a visualisation of the currently selected Question
 * and the incoming votes on the right side, as well as a BarChart-diagram of
 * the already placed votes
 * 
 * @author w.posdorfer
 * 
 */
public class VotingController implements Injectable, TimeSource, VotingTimeListener, Controller
{

    public static final String MESSAGE_VOTING_STOPPED = "message.voting.stopped";
    public static final String MESSAGE_VOTING_STARTED = "message.voting.started";
    private ImageIcon _playIcon;
    private ImageIcon _stopIcon;

    private ImageIcon _yellowBarIcon;
    private ImageIcon _greenredBarIcon;

    private QuestionSet _currentQuestionSet;
    private int _currentQuestionModel;

    private LectureID _lectureID;

    private String _lecturefile;

    private VotingSet _votingSaver;

    private VotingPanel _votingPanel;
    private VotingEvaluationController _evaluationPanel;
    private TechnicalVotingService _votingService;
    private VotingUI _gui;
    private Settings _settings = Settings.getInstance();

    private boolean _isVotingRunning = false;

    private boolean _windowIsClosing = false;

    public VotingController(Set<IPollPlugin> pollPlugins)
    {
        _votingService = new TechnicalVotingServiceImpl(this, this, pollPlugins);
        _votingPanel = new VotingPanel();
        _evaluationPanel = new VotingEvaluationController();
        _gui = new VotingUI(_votingPanel, _evaluationPanel.getPanel());

        initIcons();

        addListener();
    }

    /**
     * A Constructor for injecting mocks Don't use to create new Objects in
     * sturesy
     * 
     * @deprecated
     */
    @Deprecated
    public VotingController(VotingUI gui, TechnicalVotingService votingService, VotingPanel votingPanel,
            VotingEvaluationController evaluationPanel)
    {
        _gui = gui;
        _votingService = votingService;
        _votingPanel = votingPanel;
        _evaluationPanel = evaluationPanel;

        initIcons();

        addListener();
    }

    private void initIcons()
    {
        _playIcon = Loader.getImageIconResized(Loader.IMAGE_PLAY, VotingUI.ICONSIZE, VotingUI.ICONSIZE,
                Image.SCALE_SMOOTH);
        _stopIcon = Loader.getImageIconResized(Loader.IMAGE_STOP, VotingUI.ICONSIZE, VotingUI.ICONSIZE,
                Image.SCALE_SMOOTH);

        _yellowBarIcon = Loader.getImageIconResized(Loader.IMAGE_YELLOW_BAR, VotingUI.ICONSIZE, VotingUI.ICONSIZE,
                Image.SCALE_SMOOTH);
        _greenredBarIcon = Loader.getImageIconResized(Loader.IMAGE_GREEN_RED_BAR, VotingUI.ICONSIZE, VotingUI.ICONSIZE,
                Image.SCALE_SMOOTH);
    }

    /**
     * Applies the given QuestionSet and lecturename to this VotingWindow.<br>
     * <br>
     * If all parameters are <code>null</code> except for the qset, the preview
     * mode will be started
     * 
     * @param qset
     * @param lectureID
     * @param lecturefile
     * @param votingSaver
     *            the votingsaver to use
     */
    public void setQuestionSet(QuestionSet qset, LectureID lectureID, String lecturefile, VotingSet votingSaver)
    {
        if (lectureID == null && lecturefile == null && votingSaver == null)
        {
            setUpForPreviewMode();
        }
        else if (lectureID == WebVotingHandler.NOLECTUREID)
        {
            _gui.setTitle("StuReSy");
            _gui.getShowQRButton().setVisible(false);
        }
        else
        {
            _gui.setTitle("StuReSy - " + _settings.getString(Settings.CLIENTADDRESS) + "?lecture="
                    + WebCommands2.encode(lectureID.getLectureID()));
        }
        _lectureID = lectureID;
        _lecturefile = lecturefile;
        _votingSaver = votingSaver;
        _currentQuestionSet = qset;
        setCurrentQuestionModel(0);
    }

    private void setUpForPreviewMode()
    {
        _gui.setTitle("StuReSy - Preview");
        _gui.getStartStopButton().setEnabled(false);
        _gui.getClearVotesButton().setEnabled(false);
        _gui.getShowQRButton().setEnabled(false);
        _gui.getToggleQuestionChart().setEnabled(false);
        _gui.getToggleShowCorrectAnswer().setEnabled(false);
    }

    /**
     * Sets the current {@link QuestionModel} to the given index, and applies
     * necessary changes to the graphical and logical components
     * 
     * @param index
     *            index of questionmodel
     */
    private void setCurrentQuestionModel(int index)
    {
        _currentQuestionModel = index;

        _evaluationPanel.setCurrentQuestion(_currentQuestionSet.getIndex(index));

        _votingPanel.setCurrentQuestionModel(_currentQuestionSet, _currentQuestionModel, _lecturefile);

        if (_lectureID != WebVotingHandler.NOLECTUREID && _lectureID != null)
        {
            _gui.setBottomLabelText(_lectureID.getLectureID());
        }
        _gui.setQuestionProgressLabelText((index + 1) + " / " + _currentQuestionSet.size());
        int duration = _currentQuestionSet.getIndex(index).getDuration();
        _gui.setTimeLeftFieldText("" + (duration == QuestionModel.UNLIMITED ? "" : duration));

        if (_votingSaver != null)
        {
            _gui.setVoteCountLabelText(_votingSaver.getVotesFor(_currentQuestionModel).size());
        }
        _votingService.prepareVoting(_lectureID, _currentQuestionSet, index);
    }

    /**
     * Action perfomed on the Start/Stop Button
     */
    private void votingStartStopAction()
    {
        if (_isVotingRunning)
        {
            _votingService.stopVoting();
        }
        else
        {
            _votingService.startVoting();
        }
    }

    /**
     * Action for the Next Question Button
     */
    private void getNextAction()
    {

        if (_currentQuestionSet.size() != _currentQuestionModel + 1)
        {
            setCurrentQuestionModel(_currentQuestionModel + 1);
        }

        prepareForNewQuestionModel();
    }

    /**
     * Action for the Previous Question Button
     */
    private void getPreviousAction()
    {
        if (_currentQuestionModel - 1 >= 0)
        {
            setCurrentQuestionModel(_currentQuestionModel - 1);
        }
        prepareForNewQuestionModel();
    }

    /**
     * Sets the ShowCorrectAnswerFlag to false and repaints the Barchart for new
     * data, this is performed when the Next or Previousbuttons are pressed
     */
    private void prepareForNewQuestionModel()
    {
        // this is necessary for repainting the barchart, with different data
        showQuestionChartAction();

        // always start with yellow-only barchart
        _gui.setToggleShowCorrectAnswerState(false);
        toggleShowCorrectAnswer(); // also changes the button icon
    }

    @Override
    public void injectVote(Vote vote)
    {

        int size = _currentQuestionSet.getIndex(_currentQuestionModel).getAnswerSize();

        if (QuestionVoteMatcher.isValidVote(vote, size))
        {
            // Save Votes for Evaluation
            if (_votingSaver.addVote(_currentQuestionModel, vote))
            {
                _gui.setVoteCountLabelText(_votingSaver.getVotesFor(_currentQuestionModel).size());
            }
            if (_gui.isShowQuestionChart())
            {
                applyVotesToChart();
            }
        }
    }

    /**
     * Returns the graphical Component
     */
    public JFrame getFrame()
    {
        return _gui;
    }

    @Override
    public int getTimeLeft()
    {
        String textFieldText = _gui.getTimeLeftFieldText();
        if ("".equals(textFieldText))
        {
            return -1;
        }
        else
        {
            return Integer.parseInt(_gui.getTimeLeftFieldText());
        }
    }

    /**
     * Action perfomed when the Togglebutton ShowCorrectAnswer is pressed
     */
    private void toggleShowCorrectAnswer()
    {

        boolean showingAnswer = _gui.getToggleShowCorrectAnswerState();
        _evaluationPanel.setAnswerVisible(showingAnswer);

        if (showingAnswer)
        {
            _gui.getToggleShowCorrectAnswer().setIcon(_greenredBarIcon);
        }
        else
        {
            _gui.getToggleShowCorrectAnswer().setIcon(_yellowBarIcon);
        }
    }

    /**
     * Displays an undecorated JFrame containing a QRCode-Image with the maximum
     * size fitting on the screen
     */
    private void showQRAction()
    {
        ImageIcon icon = QRCodeGenerator.getQRImageForSavedAdress(_lectureID.getLectureID(), 800);
        QRWindowUI qrui = new QRWindowUI(icon);
        qrui.showQRWindow();
    }

    /**
     * Toggles between EvaluationWindow and QuestionWindow
     */
    private void showQuestionChartAction()
    {
        if (_gui.isShowQuestionChart())
        {
            applyVotesToChart();
            _gui.showCardLayout(VotingUI.CARDLAYOUT_EVAL);
        }
        else
        {
            _gui.showCardLayout(VotingUI.CARDLAYOUT_VOTE);
        }
    }

    private void applyVotesToChart()
    {
        Set<Vote> votes = _votingSaver.getVotesFor(_currentQuestionModel);
        _evaluationPanel.applyVotesToChart(votes, _currentQuestionSet.getIndex(_currentQuestionModel));
    }

    /**
     * Action performed, when Clear Votings button was pressed.<br>
     * Displays PopupWindow asking for confirmation and deletes votes for the
     * current question
     */
    private void clearVotingsAction()
    {
        int res = _gui.acceptDeleteVote();

        if (res == JOptionPane.YES_OPTION)
        {
            _votingSaver.clearVotesFor(_currentQuestionModel);
            _gui.setVoteCountLabelText(_votingSaver.getVotesFor(_currentQuestionModel).size());
            // re-prepare the Voting
            // for instance: WebPlugin cleans the serverdatabase
            _votingService.prepareVoting(_lectureID, _currentQuestionSet, _currentQuestionModel);
        }
    }

    /**
     * Action performed when the Votingwindow is closing<br>
     * Save current votes and windowsize
     */
    private void votingWindowClosing()
    {
        _windowIsClosing = true;
        _votingService.stopVoting();

        if (_votingSaver != null && _votingSaver.containsVotes())
        {
            VotingCRUDService votingService = new VotingCRUDService();
            votingService.createAndUpdateVoting(_votingSaver, _lecturefile);
        }

        _settings.setProperty(Settings.VOTINGWINDOWSIZE, _gui.getSize());
        _settings.save();
    }

    @Override
    public void votingTimeChanged(int timeLeft)
    {
        _gui.setTimeLeftFieldText("" + timeLeft);
    }

    @Override
    public void startedVoting()
    {
        _isVotingRunning = true;
        JButton startstop = _gui.getStartStopButton();
        startstop.setIcon(_stopIcon);
        _gui.showMessageWindowSuccess(MESSAGE_VOTING_STARTED);
        _gui.setNextButtonEnabled(false);
        _gui.setPrevButtonEnabled(false);
    }

    @Override
    public void stoppedVoting()
    {
        _isVotingRunning = false;
        if (!_windowIsClosing)
        {
            // Only do stuff when window is not closing
            JButton startstop = _gui.getStartStopButton();
            startstop.setIcon(_playIcon);
            _gui.showMessageWindowError(MESSAGE_VOTING_STOPPED);
            _gui.setNextButtonEnabled(true);
            _gui.setPrevButtonEnabled(true);
        }

    }

    @Override
    public void displayController(Component relativeTo, WindowListener listener)
    {
        Dimension d = _settings.getDimension(Settings.VOTINGWINDOWSIZE);
        _gui.addWindowListener(listener);
        _gui.setSize(d);
        _gui.setLocationRelativeTo(relativeTo);
        _gui.setVisible(true);
    }

    /**
     * Adds listeneres to all the graphical components
     */
    private void addListener()
    {
        _gui.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                votingWindowClosing();
            }
        });
        _gui.getToggleShowCorrectAnswer().addActionListener(e -> toggleShowCorrectAnswer());
        _gui.getToggleQuestionChart().addActionListener(e -> showQuestionChartAction());
        _gui.getNextQButton().addActionListener(e -> getNextAction());
        _gui.getPrevQButton().addActionListener(e -> getPreviousAction());
        _gui.getStartStopButton().addActionListener(e -> votingStartStopAction());
        _gui.getShowQRButton().addActionListener(e -> showQRAction());
        _gui.getClearVotesButton().addActionListener(e -> clearVotingsAction());
        _votingService.registerTimeListener(this);
    }
}
