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

import java.util.Timer;


/**
 * A VotingRunningTimerTask extends a RunningTimerTask with the features of
 * notifying a VotingService about timechanges
 * 
 * @author j.dallmann
 * 
 */
public class VotingRunningTimerTask extends RunningTimerTask
{
    private final TimeSource _timeSource;
    private final TechnicalVotingService _service;
    private Timer _timer;

    /**
     * Creates a new TimerTask which will notify the given VotingService about
     * changes and using the TimeSource as a countdown-time
     * 
     * @param service
     *            the VotingService to notify about changes
     * @param source
     *            the TimeSource to use as countdown-reference
     */
    public VotingRunningTimerTask(TechnicalVotingService service, TimeSource source)
    {
        _service = service;
        _timeSource = source;
    }

    /**
     * Starts the VotingTimer
     */
    public void startVotingTimer()
    {
        setRunning(true);
        _timer = new Timer();
        setTimer(_timer);
        _timer.scheduleAtFixedRate(this, 1000, 1000);
    }

    @Override
    public void run()
    {
        if (isRunning())
        {
            int duration = _timeSource.getTimeLeft();
            if (duration == 0)
            {
                _service.stopVoting();
            }
            else
            {
                _service.informListenerTimeChanged(duration - 1);
            }
        }
    }

    /**
     * Stops the VotingTimer
     */
    public void stopVotingTimer()
    {
        setRunning(false);
        cancel();
        getTimer().purge();
        getTimer().cancel();
    }

}
