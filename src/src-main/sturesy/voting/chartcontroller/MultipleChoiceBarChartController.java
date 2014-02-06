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
package sturesy.voting.chartcontroller;

import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;

import sturesy.items.QuestionModel;
import sturesy.items.Vote;

/**
 * Provides functionality to evaluate MultipleChoiceQuestions
 * 
 * @author w.posdorfer
 * 
 */
public class MultipleChoiceBarChartController implements IBarChartController
{
    // TODO Implement MultipleChoiceBarChartController
    @Override
    public void applyVotesToChart(QuestionModel question, Set<Vote> votes)
    {
    }

    @Override
    public void setAnswerVisible(boolean visible)
    {
    }

    @Override
    public JPanel getPanel()
    {
        JPanel p = new JPanel();
        p.add(new JLabel("HERE COMES THE BARCHART"));
        return p;
    }

    @Override
    public void updateUI()
    {
    }

    @Override
    public void setShowPercentage(boolean showPercentage)
    {
    }

    @Override
    public void displayEmptyChart()
    {
    }

}
