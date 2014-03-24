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
package sturesy.voting.chartcontroller;

import java.util.Set;

import javax.swing.JPanel;

import sturesy.items.QuestionModel;
import sturesy.items.Vote;

/**
 * Interface for providing evaluation Methods depending on which type of
 * question is present
 * 
 * @author w.posdorfer
 * 
 */
public interface IBarChartController
{
    /**
     * Applies given Votes to this BarChart<br>
     * Does not repaint the Barchart
     * 
     * @param question
     *            the question for which to evaluate
     * @param votes
     *            the votes
     */
    void applyVotesToChart(QuestionModel question, Set<Vote> votes);

    /**
     * Display an empty chart.
     */
    void displayEmptyChart();

    /**
     * Turns Bars with correct Answers green and with wrong answers red<br>
     * Does not repaint the Barchart
     * 
     * @param visible
     *            if <code>false</code> all bars in yellow, <code>true</code>
     *            all bars in red/green
     */
    void setAnswerVisible(boolean visible);

    /**
     * Shows percentage or absolute values<br>
     * Does not repaint the Barchart
     * 
     * @param showPercentage
     *            if <code>true</code> shows percentage, if <code>false</code>
     *            shows absolute values
     */
    void setShowPercentage(boolean showPercentage);

    /**
     * Updates the UI.<br>
     * <br>
     * This should be called everytime a modification to the barchart is desired
     */
    void updateUI();

    /**
     * @return the underlying Panel for this Chart
     */
    JPanel getPanel();

}
