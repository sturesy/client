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
import java.util.TimerTask;

/**
 * A {@link TimerTask} that knows if its running, and knows the {@link Timer} it
 * is scheduled in
 * 
 * @author w.posdorfer
 */
public abstract class RunningTimerTask extends TimerTask
{

    private boolean _running = false;
    private Timer _timer;

    public boolean isRunning()
    {
        return _running;
    }

    public Timer getTimer()
    {
        return _timer;
    }

    public void setTimer(Timer timer)
    {
        _timer = timer;
    }

    public void setRunning(boolean running)
    {
        _running = running;
    }

    @Override
    abstract public void run();

}
