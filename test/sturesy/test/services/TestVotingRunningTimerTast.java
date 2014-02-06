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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import sturesy.services.TechnicalVotingService;
import sturesy.services.TimeSource;
import sturesy.services.VotingRunningTimerTask;

@RunWith(MockitoJUnitRunner.class)
public class TestVotingRunningTimerTast
{

    @Mock
    private TechnicalVotingService _service;
    @Mock
    private TimeSource _source;

    @Before
    public void beforeEachTest()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRunNotRunning()
    {
        VotingRunningTimerTask votingTimerTask = new VotingRunningTimerTask(_service, _source);
        votingTimerTask.setRunning(false);
        votingTimerTask.run();
        verify(_source, times(0)).getTimeLeft();
    }

    @Test
    public void testRunIsRunning()
    {
        VotingRunningTimerTask votingTimerTask = new VotingRunningTimerTask(_service, _source);
        votingTimerTask.setRunning(true);
        when(_source.getTimeLeft()).thenReturn(2, 1, 0);
        votingTimerTask.run();
        verify(_service, times(1)).informListenerTimeChanged(1);

        votingTimerTask.run();
        verify(_service, times(1)).informListenerTimeChanged(0);

        votingTimerTask.run();
        verify(_service, times(0)).informListenerTimeChanged(-1);

        verify(_source, times(3)).getTimeLeft();
        verify(_service, times(1)).stopVoting();
    }

    @Test
    public void testRunStartVoting()
    {
        VotingRunningTimerTask votingRunningTimerTask = new VotingRunningTimerTask(_service, _source);
        votingRunningTimerTask.startVotingTimer();
        assertTrue(votingRunningTimerTask.isRunning());
    }

    @Test
    public void testRunStoppVoting()
    {
        VotingRunningTimerTask votingRunningTimerTask = new VotingRunningTimerTask(_service, _source);
        votingRunningTimerTask.startVotingTimer();
        votingRunningTimerTask.stopVotingTimer();

        assertFalse(votingRunningTimerTask.isRunning());
    }
}
