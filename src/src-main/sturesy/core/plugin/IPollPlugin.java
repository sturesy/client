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
package sturesy.core.plugin;

import sturesy.items.LectureID;
import sturesy.items.QuestionModel;

/**
 * Interface which models the votingprocess
 * 
 * @author w.posdorfer
 * 
 */
public interface IPollPlugin
{

    /**
     * Called to tell the instance of IPollPlugin where to inject votes on
     * polling
     * 
     * @param injectable
     */
    public void setInjectable(Injectable injectable);

    /**
     * Called when a Voting is about to commence, the polling should start with
     * preparations here, i.e. the WebPlugin sets up a voting table and matches
     * the current question in the database to this one<br>
     * <br>
     * <b>Note:</b> Updates that take longer are expected to be executed in a
     * seperate thread, so they won't block the GUI (use BackgroundWorker,
     * SwingWorker, TimerTasks etc...)
     * 
     * @param lectureid
     *            the LectureID that is beeing used
     * @param question
     *            the current questionmodel
     */
    public void prepareVoting(LectureID lectureid, final QuestionModel question);

    /**
     * Called when the voting starts and the PollPlugin should start receiving
     * and injecting votes
     */
    public void startPolling();

    /**
     * Called when the voting ends and polling needs to be stopped
     */
    public void stopPolling();
}
