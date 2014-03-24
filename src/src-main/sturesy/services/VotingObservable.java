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
 * A Type that observes a voting and informs its listeners
 * @author j.dallmann
 * 
 */
public interface VotingObservable
{

    /**
     * Informs all VotingTimeListeners about a started voting
     */
    public void informListenerStart();

    /**
     * Informs all VotingTimeListeners about a stopped voting
     */
    public void informListenerStop();

    /**
     * Register a VotingTimeListener with this VotingObservable, to be notified
     * on timeChanges or started or stopped votings
     * 
     * @param listener
     *            a VotingTimeListener to be registered
     */
    public void registerTimeListener(VotingTimeListener listener);

    /**
     * Removes a VotingTimeListenere, to receive no further notifications about
     * a voting
     * 
     * @param listener
     *            a VotingTimeListener to be removed from notifications
     */
    public void removeTimeListener(VotingTimeListener listener);

    /**
     * Informs all Listeneres about the changed time of the voting countdown
     * 
     * @param timeLeft
     *            the time left on the countdown in seconds
     */
    public void informListenerTimeChanged(int timeLeft);

}
