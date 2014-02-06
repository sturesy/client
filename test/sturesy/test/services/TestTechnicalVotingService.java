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
package sturesy.test.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import sturesy.items.LectureID;
import sturesy.items.QuestionModel;
import sturesy.items.QuestionSet;
import sturesy.services.PluginService;
import sturesy.services.TechnicalVotingServiceImpl;
import sturesy.services.TimeSource;
import sturesy.services.VotingTimeListener;

@RunWith(MockitoJUnitRunner.class)
public class TestTechnicalVotingService
{
    @Mock
    private PluginService _pluginService;

    @Mock
    private TimeSource _timeSource;

    @Mock
    private VotingTimeListener _listener;

    private TechnicalVotingServiceImpl _service;

    @Before
    public void beforeEachTest()
    {
        MockitoAnnotations.initMocks(this);
        _service = new TechnicalVotingServiceImpl(_timeSource, _pluginService);
        _service.registerTimeListener(_listener);
    }

    @Test
    public void testStartVoting()
    {
        when(_timeSource.getTimeLeft()).thenReturn(3, 2, 1, 0);
        _service.startVoting();
        verify(_pluginService, times(1)).startPolling();
        verify(_timeSource, times(1)).getTimeLeft();
        verify(_listener, times(1)).startedVoting();
    }

    @Test
    public void testStartVotingIsInfiniteTime()
    {
        when(_timeSource.getTimeLeft()).thenReturn(-1);
        _service.startVoting();
        verify(_pluginService, times(1)).startPolling();
        verify(_timeSource, times(1)).getTimeLeft();
        verify(_listener, times(1)).startedVoting();
    }

    @Test
    public void testStartVotingIsNoTimeLeft()
    {
        when(_timeSource.getTimeLeft()).thenReturn(0);
        _service.startVoting();
        verify(_pluginService, times(1)).startPolling();
        verify(_timeSource, times(1)).getTimeLeft();
        verify(_listener, times(1)).startedVoting();
        _service.stopVoting();
    }

    @Test
    public void testStartTwoVotings()
    {
        when(_timeSource.getTimeLeft()).thenReturn(3, 2, 1, 0);
        _service.startVoting();
        when(_timeSource.getTimeLeft()).thenReturn(3, 2, 1, 0);
        _service.startVoting();
        verify(_pluginService, times(2)).startPolling();
        verify(_timeSource, times(2)).getTimeLeft();
        verify(_listener, times(2)).startedVoting();
    }

    @Test
    public void testStopVoting()
    {
        when(_timeSource.getTimeLeft()).thenReturn(5, 4, 3, 2, 1, 0);
        _service.startVoting();
        _service.stopVoting();
        verify(_listener, times(1)).stoppedVoting();
        verify(_pluginService, times(1)).stopPolling();
    }

    @Test
    public void testStopVotingNoVotingRunning() throws InterruptedException
    {
        when(_timeSource.getTimeLeft()).thenReturn(0);
        _service.startVoting();
        Thread.sleep(1);
        _service.stopVoting();
        verify(_listener, times(1)).stoppedVoting();
        verify(_pluginService, times(1)).stopPolling();
    }

    @Test
    public void testStopVotingNoVotingStarted() throws InterruptedException
    {
        when(_timeSource.getTimeLeft()).thenReturn(0);
        _service.stopVoting();
        verify(_listener, times(1)).stoppedVoting();
        verify(_pluginService, times(1)).stopPolling();
    }

    @Test
    public void testTimeChangeListener()
    {
        _service.informListenerTimeChanged(2);
        verify(_listener, times(1)).votingTimeChanged(2);
    }

    @Test
    public void testTimeChangeListenerNoListenerRegistered()
    {
        TechnicalVotingServiceImpl service = new TechnicalVotingServiceImpl(_timeSource, _pluginService);
        service.informListenerTimeChanged(0);
        verify(_listener, times(0)).votingTimeChanged(0);
    }

    @Test
    public void testPrepareVote()
    {
        LectureID lectureID = mock(LectureID.class);
        QuestionModel questionModel = mock(QuestionModel.class);
        when(questionModel.getAnswerSize()).thenReturn(2);
        when(questionModel.getQuestion()).thenReturn("A Question");
        QuestionSet questionSet = new QuestionSet();
        questionSet.addQuestionModel(questionModel);
        _service.prepareVoting(lectureID, questionSet, 0);

        verify(_pluginService, times(1)).prepareVoting(lectureID, questionModel);
    }

    @After
    public void afterEachTest()
    {
        _service.removeTimeListener(_listener);
    }
}
