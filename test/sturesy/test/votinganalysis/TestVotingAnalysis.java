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
package sturesy.test.votinganalysis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import sturesy.MainScreenUI;
import sturesy.core.backend.services.crud.VotingAnalysisCRUDService;
import sturesy.core.ui.UneditableTableModel;
import sturesy.items.QuestionModel;
import sturesy.items.QuestionSet;
import sturesy.items.Vote;
import sturesy.tools.QuestionSetBuilder;
import sturesy.tools.VotesBuilder;
import sturesy.voting.VotingEvaluationController;
import sturesy.votinganalysis.TimeChart;
import sturesy.votinganalysis.VotingAnalysis;
import sturesy.votinganalysis.gui.VotingAnalysisUI;

@RunWith(MockitoJUnitRunner.class)
public class TestVotingAnalysis
{

    @Mock
    private VotingAnalysisUI _ui;
    @Mock
    private VotingAnalysisCRUDService _votingAnalysisCRUDService;
    @Mock
    private VotingEvaluationController _evalPanel;
    @Mock
    private TimeChart _timeChart;
    @Spy
    private JButton _previousButton;
    @Spy
    private JButton _nextButton;
    @Spy
    private JButton _exportCSVButton;

    @Before
    public void beforeEachTest()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor()
    {
        configureConstructorMocks();
        QuestionSet questionSet = createQuestionSet(3);
        Map<Integer, Set<Vote>> votes = createVotes(400, 3);
        new VotingAnalysis(questionSet, votes, _votingAnalysisCRUDService, _evalPanel, _ui, _timeChart);
        verifyActionListenerAdded();
        verify(_evalPanel, times(1)).setAnswerVisible(true);
        verify(_ui, times(1)).setLabelText(1, 3);
        int currentQuestionNumber = 1;
        QuestionModel currentQuestionModel = questionSet.getIndex(currentQuestionNumber - 1);
        Set<Vote> currentVotes = votes.get(currentQuestionNumber - 1);
        verifyUpdateGuiComponents(currentQuestionNumber, currentVotes, currentQuestionModel.getAnswers(), 0, 0);
    }

    private void verifyActionListenerAdded()
    {
        assertTrue(_nextButton.getActionListeners().length > 0);
        assertTrue(_previousButton.getActionListeners().length > 0);
        assertTrue(_exportCSVButton.getActionListeners().length > 0);
    }

    @Test
    public void testConstructorCallNoVotes()
    {
        configureConstructorMocks();
        QuestionSet questionSet = createQuestionSet(3);
        Map<Integer, Set<Vote>> votes = createVotes(0, 3);
        new VotingAnalysis(questionSet, votes, _votingAnalysisCRUDService, _evalPanel, _ui, _timeChart);
        verify(_evalPanel, times(1)).setAnswerVisible(true);
        verify(_ui, times(1)).setLabelText(1, 3);
        verifyActionListenerAdded();
        verify(_evalPanel, times(1)).setAnswerVisible(true);
        verify(_ui, times(1)).setLabelText(1, 3);
        int currentQuestionNumber = 1;
        QuestionModel currentQuestionModel = questionSet.getIndex(currentQuestionNumber - 1);
        Set<Vote> currentVotes = votes.get(currentQuestionNumber - 1);
        verifyUpdateGuiComponents(currentQuestionNumber, currentVotes, currentQuestionModel.getAnswers(), 0, 0);
    }

    @Test
    public void testConstructorCallWithInvalidVote()
    {
        configureConstructorMocks();
        QuestionSet questionSet = createQuestionSet(3);
        Map<Integer, Set<Vote>> votes = createVotes(1, 5);
        new VotingAnalysis(questionSet, votes, _votingAnalysisCRUDService, _evalPanel, _ui, _timeChart);
        verify(_evalPanel, times(1)).setAnswerVisible(true);
        verify(_ui, times(1)).setLabelText(1, 3);
        verifyActionListenerAdded();
        verify(_evalPanel, times(1)).setAnswerVisible(true);
        verify(_ui, times(1)).setLabelText(1, 3);
        int currentQuestionNumber = 1;
        QuestionModel currentQuestionModel = questionSet.getIndex(currentQuestionNumber - 1);
        Set<Vote> currentVotes = votes.get(currentQuestionNumber - 1);
        verifyUpdateGuiComponents(currentQuestionNumber, currentVotes, currentQuestionModel.getAnswers(), 0, 0);
    }

    @Test
    public void testNextButtonAction()
    {
        configureConstructorMocks();
        QuestionSet questionSet = createQuestionSet(3);
        Map<Integer, Set<Vote>> votes = createVotes(20, 3);
        new VotingAnalysis(questionSet, votes, _votingAnalysisCRUDService, _evalPanel, _ui, _timeChart);

        triggerActionListener(_nextButton);
        verify(_ui, times(1)).setLabelText(2, 3);
        triggerActionListener(_nextButton);
        triggerActionListener(_nextButton);
        verify(_ui, times(2)).setLabelText(3, 3);

    }

    @Test
    public void testPreviousButtonAction()
    {
        configureConstructorMocks();
        QuestionSet questionSet = createQuestionSet(3);
        Map<Integer, Set<Vote>> votes = createVotes(20, 3);
        new VotingAnalysis(questionSet, votes, _votingAnalysisCRUDService, _evalPanel, _ui, _timeChart);

        triggerActionListener(_previousButton);
        triggerActionListener(_nextButton);
        triggerActionListener(_previousButton);
        verify(_ui, times(1)).setLabelText(2, 3);
        // one more because it depends on constructor call and this method is
        // called in constructor call
        verify(_ui, times(3)).setLabelText(1, 3);
    }

    @Test
    public void testExportButtonWithNoVotes()
    {
        configureConstructorMocks();
        File file = mock(File.class);
        when(_ui.acceptSaveFromUser()).thenReturn(file);
        QuestionSet questionSet = createQuestionSet(3);
        Map<Integer, Set<Vote>> allVotes = createVotes(0, 3);
        Set<Vote> votesForQuestionSet = allVotes.get(0);
        when(_votingAnalysisCRUDService.saveVotingResult(votesForQuestionSet, file)).thenReturn(null);
        new VotingAnalysis(questionSet, allVotes, _votingAnalysisCRUDService, _evalPanel, _ui, _timeChart);
        triggerActionListener(_exportCSVButton);
        verify(_votingAnalysisCRUDService, times(1)).saveVotingResult(votesForQuestionSet, file);
        verify(_ui, times(0)).showMessageWindowError(Mockito.<String> any());
    }

    @Test
    public void testExportButtonErrorOccured()
    {
        configureConstructorMocks();
        QuestionSet questionSet = createQuestionSet(3);
        Map<Integer, Set<Vote>> votes = createVotes(20, 3);
        new VotingAnalysis(questionSet, votes, _votingAnalysisCRUDService, _evalPanel, _ui, _timeChart);
        File file = mock(File.class);
        when(_ui.acceptSaveFromUser()).thenReturn(file);
        Set<Vote> votesForQuestionSet = votes.get(0);
        when(_votingAnalysisCRUDService.saveVotingResult(votesForQuestionSet, file)).thenReturn(
                "Error exporting to CSV");
        triggerActionListener(_exportCSVButton);
        verify(_votingAnalysisCRUDService, times(1)).saveVotingResult(votesForQuestionSet, file);
        verify(_ui, times(1)).showMessageWindowError("Error exporting to CSV");

    }

    @Test
    public void testSetNewWindowSize()
    {
        configureConstructorMocks();
        QuestionSet questionSet = createQuestionSet(3);
        Map<Integer, Set<Vote>> votes = createVotes(20, 3);
        VotingAnalysis votingAnalysis = new VotingAnalysis(questionSet, votes, _votingAnalysisCRUDService, _evalPanel,
                _ui, _timeChart);
        Dimension dimension = new Dimension(2, 1);
        votingAnalysis.getFrame().setSize(dimension);// setNewWindowSize(dimension);
        verify(_ui, times(1)).setSize(dimension);
    }

    @Test
    public void testSetLocationRelativeTo()
    {
        configureConstructorMocks();
        QuestionSet questionSet = createQuestionSet(3);
        Map<Integer, Set<Vote>> votes = createVotes(20, 3);
        VotingAnalysis votingAnalysis = new VotingAnalysis(questionSet, votes, _votingAnalysisCRUDService, _evalPanel,
                _ui, _timeChart);
        MainScreenUI mainscreenUIMock = mock(MainScreenUI.class);
        votingAnalysis.getFrame().setLocationRelativeTo(mainscreenUIMock);
        verify(_ui, times(1)).setLocationRelativeTo(mainscreenUIMock);
    }

    @Test
    public void testSetWindowVisibility()
    {
        configureConstructorMocks();
        QuestionSet questionSet = createQuestionSet(3);
        Map<Integer, Set<Vote>> votes = createVotes(20, 3);
        VotingAnalysis votingAnalysis = new VotingAnalysis(questionSet, votes, _votingAnalysisCRUDService, _evalPanel,
                _ui, _timeChart);
        votingAnalysis.getFrame().setVisible(true);
        verify(_ui, times(1)).setVisible(true);
    }

    @Test
    public void testIsUI()
    {
        configureConstructorMocks();
        QuestionSet questionSet = createQuestionSet(3);
        Map<Integer, Set<Vote>> votes = createVotes(20, 3);
        VotingAnalysis votingAnalysis = new VotingAnalysis(questionSet, votes, _votingAnalysisCRUDService, _evalPanel,
                _ui, _timeChart);

        boolean isUI = votingAnalysis.getFrame() == _ui;
        assertTrue(isUI);
        isUI = votingAnalysis.getFrame() == new Object();
        assertFalse(isUI);
    }

    private void triggerActionListener(JButton button)
    {
        ActionListener[] actionListeners = button.getActionListeners();
        for (ActionListener actionListener : actionListeners)
        {
            actionListener.actionPerformed(mock(ActionEvent.class));
        }
    }

    private void verifyUpdateGuiComponents(int currentQuestion, Set<Vote> currentVote, List<String> answers,
            int expectedMedian, int expectedArithmeticMean)
    {
        if (currentVote == null)
        {
            currentVote = new HashSet<Vote>();
        }
        verify(_ui, times(1)).setLabelText(currentQuestion, 3);
        verify(_timeChart, times(1)).applyVotesToChart(currentVote);
        verify(_ui, times(1)).setArithmeticMeanText("Mittelwert: " + expectedArithmeticMean);
        verify(_ui, times(1)).setMedianText("Median: " + expectedMedian);
        verify(_ui, times(1)).setTableModel(Mockito.<UneditableTableModel> any(),
                Mockito.<TableRowSorter<TableModel>> any());
    }

    private void configureConstructorMocks()
    {
        when(_ui.getNextButton()).thenReturn(_nextButton);
        when(_ui.getPreviousButton()).thenReturn(_previousButton);
        when(_ui.getExportCSV()).thenReturn(_exportCSVButton);
    }

    private Map<Integer, Set<Vote>> createVotes(int voteSize, int voteRange)
    {
        VotesBuilder builder = new VotesBuilder();
        Map<Integer, Set<Vote>> votingMap = builder.buildVotes(voteSize, 3, voteRange);
        return votingMap;
    }

    private QuestionSet createQuestionSet(int questionSetSize)
    {
        QuestionSetBuilder builder = new QuestionSetBuilder();
        return builder.buildQuestionSet(questionSetSize);
    }
}
