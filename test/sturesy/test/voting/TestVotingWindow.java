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
package sturesy.test.voting;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import sturesy.items.LectureID;
import sturesy.items.QuestionModel;
import sturesy.items.QuestionSet;
import sturesy.items.SingleChoiceQuestion;
import sturesy.items.Vote;
import sturesy.items.VotingSet;
import sturesy.services.TechnicalVotingServiceImpl;
import sturesy.services.VotingTimeListener;
import sturesy.tools.LoadTestFilesService;
import sturesy.util.web.WebVotingHandler;
import sturesy.voting.VotingController;
import sturesy.voting.VotingEvaluationController;
import sturesy.voting.VotingPanel;
import sturesy.voting.gui.VotingUI;

@RunWith(MockitoJUnitRunner.class)
public class TestVotingWindow
{
    @Mock
    private VotingUI _ui;
    @Mock
    private VotingPanel _votingPanel;
    @Spy
    private TechnicalVotingServiceImpl _service;
    @Mock
    private VotingEvaluationController _evaluationPanel;
    @Spy
    private JButton _nextQButton;
    @Spy
    private JToggleButton _toggleShowCorrectAnswer;
    @Spy
    private JToggleButton _toggleQuestionChart;
    @Spy
    private JButton _prevQButton;
    @Spy
    private JButton _startStopButton;
    @Spy
    private JButton _showQRButton;
    @Spy
    private JButton _clearVotesButton;
    @Mock
    private VotingSet _votingSaver;
    @Mock
    private ActionEvent _actionEvent;

    @Before
    public void beforeEachTest()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructorCall()
    {
        configureUIMocks();
        createVotingWindowWithMocks();
        verify(_service, times(1)).registerTimeListener(Mockito.<VotingTimeListener> any());
        verify(_ui, times(1)).getNextQButton();
        verify(_ui, times(1)).getPrevQButton();
        verify(_ui, times(1)).getToggleShowCorrectAnswer();
        verify(_ui, times(1)).getToggleQuestionChart();
        verify(_ui, times(1)).getStartStopButton();
        verify(_ui, times(1)).getShowQRButton();
        verify(_ui, times(1)).getClearVotesButton();
    }

    private void configureUIMocks()
    {
        when(_ui.getNextQButton()).thenReturn(_nextQButton);
        when(_ui.getPrevQButton()).thenReturn(_prevQButton);
        when(_ui.getToggleShowCorrectAnswer()).thenReturn(_toggleShowCorrectAnswer);
        when(_ui.getToggleQuestionChart()).thenReturn(_toggleQuestionChart);
        when(_ui.getStartStopButton()).thenReturn(_startStopButton);
        when(_ui.getShowQRButton()).thenReturn(_showQRButton);
        when(_ui.getClearVotesButton()).thenReturn(_clearVotesButton);
    }

    @Test
    public void testVotingTimeListenerTimeChanged()
    {
        configureUIMocks();
        createVotingWindowWithMocks();
        _service.informListenerTimeChanged(1);
        verify(_ui, times(1)).setTimeLeftFieldText("1");
    }

    @Test
    public void testVotingTimeListenerVotingStarted()
    {
        configureUIMocks();
        createVotingWindowWithMocks();
        _service.informListenerStart();
        verify(_ui, times(1)).setPrevButtonEnabled(false);
        verify(_ui, times(1)).setNextButtonEnabled(false);
        verify(_startStopButton, times(1)).setIcon(Mockito.<ImageIcon> any());
        verify(_ui, times(1)).showMessageWindowSuccess(VotingController.MESSAGE_VOTING_STARTED);
    }

    @Test
    public void testVotingtimeListenerVotingStopped()
    {
        configureUIMocks();
        createVotingWindowWithMocks();
        _service.informListenerStop();
        verify(_ui, times(1)).setPrevButtonEnabled(true);
        verify(_ui, times(1)).setNextButtonEnabled(true);
        verify(_startStopButton, times(1)).setIcon(Mockito.<ImageIcon> any());
        verify(_ui, times(1)).showMessageWindowError(VotingController.MESSAGE_VOTING_STOPPED);

    }

    @Test
    public void testSetQuestionSet()
    {
        configureUIMocks();
        String lecturefile = createLectureFile();

        QuestionSet questionSet = createQuestionSet();
        LectureID id = createLectureID();
        doNothing().when(_service).prepareVoting(Mockito.<LectureID> any(), Mockito.<QuestionSet> any(),
                Mockito.anyInt());
        VotingController votingWindow = createVotingWindowWithMocks();
        votingWindow.setQuestionSet(questionSet, id, lecturefile, new VotingSet());

        verify(_ui, times(1)).setBottomLabelText("");
        verify(_ui, times(1)).setQuestionProgressLabelText("1 / 1");
        verify(_ui, times(1)).setTimeLeftFieldText("5");
        verify(_ui, times(1)).setVoteCountLabelText(Mockito.anyInt());

        verify(_votingPanel, times(1)).setCurrentQuestionModel(questionSet, 0, lecturefile);
        verify(_service, times(1)).prepareVoting(id, questionSet, 0);
    }

    @Test
    public void testSetQuestionSetWithNOLECTUREID()
    {
        configureUIMocks();
        String lecturefile = createLectureFile();

        QuestionSet questionSet = createQuestionSet();
        questionSet.getIndex(0).setDuration(QuestionModel.UNLIMITED);
        LectureID id = WebVotingHandler.NOLECTUREID;
        doNothing().when(_service).prepareVoting(Mockito.<LectureID> any(), Mockito.<QuestionSet> any(),
                Mockito.anyInt());
        VotingController votingWindow = createVotingWindowWithMocks();
        votingWindow.setQuestionSet(questionSet, id, lecturefile, new VotingSet());

        verify(_ui, times(0)).setBottomLabelText("");
        verify(_ui, times(1)).setQuestionProgressLabelText("1 / 1");
        verify(_ui, times(1)).setTimeLeftFieldText("");
        verify(_ui, times(1)).setVoteCountLabelText(Mockito.anyInt());

        verify(_votingPanel, times(1)).setCurrentQuestionModel(questionSet, 0, lecturefile);
        verify(_service, times(1)).prepareVoting(id, questionSet, 0);
    }

    @Test
    public void testGetTimeLeftUnlimited()
    {
        configureUIMocks();
        when(_ui.getTimeLeftFieldText()).thenReturn("");
        VotingController votingWindow = createVotingWindowWithMocks();
        int timeLeft = votingWindow.getTimeLeft();
        assertTrue(-1 == timeLeft);
    }

    @Test
    public void testGetTimeLeftFixTime()
    {
        configureUIMocks();
        when(_ui.getTimeLeftFieldText()).thenReturn("5");
        VotingController votingWindow = createVotingWindowWithMocks();
        int timeLeft = votingWindow.getTimeLeft();
        assertTrue(5 == timeLeft);
    }

    @Test
    public void testInjectVoteAnswerOutOfRange()
    {
        configureUIMocks();
        LectureID lectureID = createLectureID();
        QuestionSet questionSet = createQuestionSetForInjecting();
        String lecturefile = createLectureFile();
        doNothing().when(_service).prepareVoting(Mockito.<LectureID> any(), Mockito.<QuestionSet> any(),
                Mockito.anyInt());
        VotingController votingWindow = createVotingWindowWithMocks();
        votingWindow.setQuestionSet(questionSet, lectureID, lecturefile, _votingSaver);
        Vote vote = mock(Vote.class);
        votingWindow.injectVote(vote);
        verify(_votingSaver, times(0)).addVote(0, vote);
    }

    @Test
    public void testInjectVoteAnswerInRangeSuccessfulAddVote()
    {
        configureUIMocks();
        LectureID lectureID = createLectureID();
        QuestionSet questionSet = createQuestionSet();
        String lecturefile = createLectureFile();
        VotingSet votingSaver = mock(VotingSet.class);
        Vote vote = mock(Vote.class);
        doNothing().when(_service).prepareVoting(Mockito.<LectureID> any(), Mockito.<QuestionSet> any(),
                Mockito.anyInt());
        when(votingSaver.addVote(0, vote)).thenReturn(true);
        VotingController votingWindow = createVotingWindowWithMocks();
        votingWindow.setQuestionSet(questionSet, lectureID, lecturefile, votingSaver);
        votingWindow.injectVote(vote);
        verify(votingSaver, times(1)).addVote(0, vote);
        verify(_ui, times(2)).setVoteCountLabelText(0);
    }

    @Test
    public void testInjectVoteAnswerInRangeUnsuccessfulAddVote()
    {
        configureUIMocks();
        LectureID lectureID = createLectureID();
        QuestionSet questionSet = createQuestionSet();
        String lecturefile = createLectureFile();
        Vote vote = mock(Vote.class);
        doNothing().when(_service).prepareVoting(Mockito.<LectureID> any(), Mockito.<QuestionSet> any(),
                Mockito.anyInt());
        when(_votingSaver.addVote(0, vote)).thenReturn(false);
        VotingController votingWindow = createVotingWindowWithMocks();
        votingWindow.setQuestionSet(questionSet, lectureID, lecturefile, _votingSaver);
        votingWindow.injectVote(vote);
        verify(_votingSaver, times(1)).addVote(0, vote);
        verify(_ui, times(1)).setVoteCountLabelText(0);
    }

    @Test
    public void testClearVotingsUserConfirmed()
    {
        configureUIMocks();
        doNothing().when(_service).prepareVoting(Mockito.<LectureID> any(), Mockito.<QuestionSet> any(),
                Mockito.anyInt());
        when(_ui.acceptDeleteVote()).thenReturn(JOptionPane.YES_OPTION);
        LectureID lectureID = createLectureID();
        QuestionSet questionSet = createQuestionSet();
        String lecturefile = createLectureFile();
        VotingController votingWindow = createVotingWindowWithMocks();
        votingWindow.setQuestionSet(questionSet, lectureID, lecturefile, _votingSaver);
        triggerActionListener(_clearVotesButton);
        verify(_votingSaver, times(1)).clearVotesFor(0);
        verify(_ui, times(2)).setVoteCountLabelText(0);
    }

    @Test
    public void testClearVotingsUserDoNotWantToDeleteVotes()
    {
        configureUIMocks();
        doNothing().when(_service).prepareVoting(Mockito.<LectureID> any(), Mockito.<QuestionSet> any(),
                Mockito.anyInt());
        when(_ui.acceptDeleteVote()).thenReturn(JOptionPane.NO_OPTION);
        LectureID lectureID = createLectureID();
        QuestionSet questionSet = createQuestionSet();
        String lecturefile = createLectureFile();
        VotingController votingWindow = createVotingWindowWithMocks();
        votingWindow.setQuestionSet(questionSet, lectureID, lecturefile, _votingSaver);
        triggerActionListener(_clearVotesButton);
        verify(_votingSaver, times(0)).clearVotesFor(0);
        verify(_ui, times(1)).setVoteCountLabelText(0);
    }

    @Test
    public void testToggleQuestionChartOn()
    {
        configureUIMocks();
        doNothing().when(_service).prepareVoting(Mockito.<LectureID> any(), Mockito.<QuestionSet> any(),
                Mockito.anyInt());
        when(_ui.isShowQuestionChart()).thenReturn(true);
        LectureID lectureID = createLectureID();
        QuestionSet questionSet = createQuestionSet();
        String lecturefile = createLectureFile();
        VotingController votingWindow = createVotingWindowWithMocks();
        votingWindow.setQuestionSet(questionSet, lectureID, lecturefile, _votingSaver);
        triggerActionListener(_toggleQuestionChart);
        verify(_votingSaver, times(2)).getVotesFor(0);
        verify(_ui, times(1)).showCardLayout(VotingUI.CARDLAYOUT_EVAL);
    }

    @Test
    public void testToggleQuestionChartOff()
    {
        configureUIMocks();
        doNothing().when(_service).prepareVoting(Mockito.<LectureID> any(), Mockito.<QuestionSet> any(),
                Mockito.anyInt());
        when(_ui.isShowQuestionChart()).thenReturn(false);
        LectureID lectureID = createLectureID();
        QuestionSet questionSet = createQuestionSet();
        String lecturefile = createLectureFile();
        VotingController votingWindow = createVotingWindowWithMocks();
        votingWindow.setQuestionSet(questionSet, lectureID, lecturefile, _votingSaver);
        triggerActionListener(_toggleQuestionChart);
        verify(_votingSaver, times(1)).getVotesFor(0);
        verify(_ui, times(1)).showCardLayout(VotingUI.CARDLAYOUT_VOTE);
    }

    @Test
    public void testNextButton()
    {
        configureUIMocks();
        doNothing().when(_service).prepareVoting(Mockito.<LectureID> any(), Mockito.<QuestionSet> any(),
                Mockito.anyInt());
        LectureID lectureID = createLectureID();
        QuestionSet questionSet = createQuestionSetTwoEntries();
        String lecturefile = createLectureFile();

        VotingController votingWindow = createVotingWindowWithMocks();
        votingWindow.setQuestionSet(questionSet, lectureID, lecturefile, _votingSaver);
        triggerActionListener(_nextQButton);
        triggerActionListener(_nextQButton);
        verify(_ui, times(2)).setToggleShowCorrectAnswerState(false);
        verify(_ui, times(2)).isShowQuestionChart();
        verify(_ui, times(2)).getToggleShowCorrectAnswerState();
    }

    @Test
    public void testVotingStartStopStart()
    {
        configureUIMocks();
        doNothing().when(_service).startVoting();
        doNothing().when(_service).stopVoting();
        doNothing().when(_startStopButton).setIcon(Mockito.<ImageIcon> any());
        VotingController votingWindow = createVotingWindowWithMocks();
        triggerActionListener(_startStopButton);
        verify(_service, times(1)).startVoting();
        votingWindow.startedVoting();
        triggerActionListener(_startStopButton);
        verify(_service, times(1)).stopVoting();
    }

    @Test
    public void testToggleCorrectAnswer()
    {
        configureUIMocks();
        createVotingWindowWithMocks();
        when(_ui.getToggleShowCorrectAnswerState()).thenReturn(true);
        triggerActionListener(_toggleShowCorrectAnswer);
        verify(_evaluationPanel, times(1)).setAnswerVisible(true);
    }

    @Test
    public void testPreviousQuestionActionHasPrevious()
    {
        configureUIMocks();
        doNothing().when(_service).prepareVoting(Mockito.<LectureID> any(), Mockito.<QuestionSet> any(),
                Mockito.anyInt());
        VotingController votingWindow = createVotingWindowWithMocks();
        LectureID lectureID = createLectureID();
        QuestionSet questionSet = createQuestionSetTwoEntries();
        String lecturefile = createLectureFile();
        VotingSet votingSaver = mock(VotingSet.class);
        votingWindow.setQuestionSet(questionSet, lectureID, lecturefile, votingSaver);
        when(_ui.isShowQuestionChart()).thenReturn(false);
        when(_ui.getToggleShowCorrectAnswerState()).thenReturn(false);
        triggerActionListener(_prevQButton);
        triggerActionListener(_nextQButton);
        triggerActionListener(_prevQButton);
        verify(_ui, times(3)).showCardLayout(VotingUI.CARDLAYOUT_VOTE);
        verify(_ui, times(3)).setToggleShowCorrectAnswerState(false);
        verify(_evaluationPanel, times(3)).setAnswerVisible(false);
        verify(_service, times(3)).prepareVoting(Mockito.<LectureID> any(), Mockito.<QuestionSet> any(),
                Mockito.anyInt());
    }

    private void triggerActionListener(AbstractButton button)
    {
        ActionListener[] actionListeners = button.getActionListeners();
        for (ActionListener actionListener : actionListeners)
        {
            actionListener.actionPerformed(_actionEvent);
        }
    }

    private String createLectureFile()
    {
        LoadTestFilesService testFileService = new LoadTestFilesService();
        File retrieveTestLectureVotingFile = testFileService.retrieveTestLectureVotingFile();
        return retrieveTestLectureVotingFile.getAbsolutePath();
    }

    private QuestionSet createQuestionSetForInjecting()
    {
        QuestionSet questionSet = new QuestionSet();
        questionSet.addQuestionModel(createSecondQuestionModel());
        return questionSet;
    }

    private QuestionSet createQuestionSetTwoEntries()
    {
        QuestionSet questionSet = new QuestionSet();
        questionSet.addQuestionModel(createFirstQuestionModel());
        questionSet.addQuestionModel(createSecondQuestionModel());
        return questionSet;
    }

    private QuestionSet createQuestionSet()
    {
        QuestionSet questionSet = new QuestionSet();
        questionSet.addQuestionModel(createFirstQuestionModel());
        return questionSet;
    }

    private QuestionModel createSecondQuestionModel()
    {
        List<String> answers = new ArrayList<String>();
        SingleChoiceQuestion model = new SingleChoiceQuestion("", answers, 1, 5);
        model.setAnswers(answers);
        return model;
    }

    private QuestionModel createFirstQuestionModel()
    {
        List<String> answers = new ArrayList<String>();
        answers.add("q1firstAnswer");
        answers.add("q2secondAnswer");
        SingleChoiceQuestion model = new SingleChoiceQuestion("", answers, 1, 5);
        model.setAnswers(answers);
        return model;
    }

    private LectureID createLectureID()
    {
        LectureID lectureID = new LectureID();
        return lectureID;
    }

    @SuppressWarnings("deprecation")
    private VotingController createVotingWindowWithMocks()
    {
        return new VotingController(_ui, _service, _votingPanel, _evaluationPanel);
    }
}
