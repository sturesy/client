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

/**
 * A Type that would like to be notified when a countdown timer has changed its
 * value, or is notified when a voting has started or stopped<br>
 * Usually a VotingService will notify its Listeners about these changes.
 * 
 * @author j.dallmann
 * 
 */
public interface VotingTimeListener
{
    /**
     * A Voting has been started
     */
    public void startedVoting();

    /**
     * A Voting has been stopped
     */
    public void stoppedVoting();

    /**
     * The Voting Countdown has changed to the remaining time in seconds
     * 
     * @param timeLeft
     *            the current time left on the voting countdown in seconds
     */
    public void votingTimeChanged(int timeLeft);
}
