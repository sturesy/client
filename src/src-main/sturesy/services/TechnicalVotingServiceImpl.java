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
package sturesy.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import sturesy.core.plugin.IPollPlugin;
import sturesy.core.plugin.Injectable;
import sturesy.items.LectureID;
import sturesy.items.QuestionSet;

public class TechnicalVotingServiceImpl implements TechnicalVotingService
{

    private List<VotingTimeListener> _votingTimeListener;
    private TimeSource _timeSource;
    private VotingRunningTimerTask _timertask;
    private PluginService _pluginService;

    public TechnicalVotingServiceImpl()
    {
        _votingTimeListener = new ArrayList<VotingTimeListener>();
        _timeSource = null;
    }

    public TechnicalVotingServiceImpl(TimeSource source, Injectable injectable, Set<IPollPlugin> pollingPlugins)
    {
        this(source, new PluginService(injectable, pollingPlugins));
    }

    public TechnicalVotingServiceImpl(TimeSource timeSource, PluginService pluginService)
    {
        _timeSource = timeSource;
        _votingTimeListener = new ArrayList<VotingTimeListener>();
        _pluginService = pluginService;
        _pluginService.setInjectorInPlugins();
    }

    @Override
    public void registerTimeListener(VotingTimeListener listener)
    {
        _votingTimeListener.add(listener);
    }

    @Override
    public void removeTimeListener(VotingTimeListener listener)
    {
        _votingTimeListener.remove(listener);
    }

    @Override
    public void informListenerTimeChanged(int timeLeft)
    {
        for (VotingTimeListener listener : _votingTimeListener)
        {
            listener.votingTimeChanged(timeLeft);
        }
    }

    private boolean isTimeLeft(int duration)
    {
        return !(0 == duration);
    }

    private boolean isInfiniteTime(int duration)
    {
        return -1 == duration;
    }

    @Override
    public void startVoting()
    {
        int duration = _timeSource.getTimeLeft();
        if (!isInfiniteTime(duration) && isTimeLeft(duration))
        {
            if (_timertask != null)
            {
                _timertask.stopVotingTimer();
            }
            _timertask = startTimer();

        }
        informListenerStart();
        _pluginService.startPolling();
    }

    /**
     * Starts a Timer to countdown the Seconds in the TimeLeftField, once it
     * reaches 0 it cancels the currentvote
     * 
     * @return the started RunningTimerTask
     */
    private VotingRunningTimerTask startTimer()
    {
        final VotingRunningTimerTask timertask = new VotingRunningTimerTask(this, _timeSource);
        timertask.startVotingTimer();
        return timertask;
    }

    @Override
    public void stopVoting()
    {
        if (_timertask != null && _timertask.isRunning())
        {
            _timertask.stopVotingTimer();
        }
        _timertask = null;
        _pluginService.stopPolling();
        informListenerStop();
    }

    @Override
    public void prepareVoting(LectureID lectureID, QuestionSet currentQuestionSet, int index)
    {
        _pluginService.prepareVoting(lectureID, currentQuestionSet.getIndex(index));
    }

    @Override
    public void informListenerStart()
    {
        for (VotingTimeListener listener : _votingTimeListener)
        {
            listener.startedVoting();
        }
    }

    @Override
    public void informListenerStop()
    {
        for (VotingTimeListener listener : _votingTimeListener)
        {
            listener.stoppedVoting();
        }
    }
}
