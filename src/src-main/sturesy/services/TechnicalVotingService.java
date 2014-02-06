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
package sturesy.services;

import sturesy.items.LectureID;
import sturesy.items.QuestionSet;

/**
 * A VotingService handles currently running votes, manages Timers if needed and
 * notifies interested Listeners about the current status of the voting
 * 
 * @author j.dallmann
 * 
 */
public interface TechnicalVotingService extends VotingObservable
{

    /**
     * Stops the Voting, all currently running timers will be stopped, all
     * listerens will be informed, all plugins will be stopped
     */
    public void stopVoting();

    /**
     * Calls prepareVoting on all registered IPollPlugins
     * 
     * @see PluginService#prepareVoting(LectureID, int, String)
     * @param lectureID
     *            LectureID to use
     * @param currentQuestionSet
     *            Questionset to use
     * @param index
     *            answeramount
     */
    public void prepareVoting(LectureID lectureID, QuestionSet currentQuestionSet, int index);

    /**
     * Starts the Voting, all cnecessary timers will be started, all listerens
     * will be informed, all plugins will be started
     */
    public void startVoting();

}
