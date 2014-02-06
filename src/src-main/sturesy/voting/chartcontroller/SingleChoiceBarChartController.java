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

import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Set;

import javax.swing.JPanel;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jfree.data.category.CategoryDataset;

import sturesy.core.ui.HTMLStripper;
import sturesy.items.QuestionModel;
import sturesy.items.SingleChoiceQuestion;
import sturesy.items.Vote;

/**
 * Provides functionality to evaluate SingleChoiceQuestions
 * 
 * @author w.posdorfer
 */
public class SingleChoiceBarChartController extends AChoiceBarController implements IBarChartController
{

    private SingleChoiceQuestion _question;

    public SingleChoiceBarChartController(ActionListener showpercentageListener)
    {
        super(showpercentageListener);
    }

    @Override
    public void applyVotesToChart(QuestionModel question, Set<Vote> votes)
    {
        _question = (SingleChoiceQuestion) question;
        _votes = votes;
    }

    @Override
    public void setAnswerVisible(boolean visible)
    {
        if (visible && _question != null && _question.hasCorrectAnswer())
        {
            _showCorrectAnswer = true;
        }
        else
        {
            _showCorrectAnswer = false;
        }
    }

    @Override
    public JPanel getPanel()
    {
        return _gui;
    }

    @Override
    public void updateUI()
    {
        double[] votesarr = new double[_question.getAnswerSize()];
        Arrays.fill(votesarr, 0);

        for (Vote vote : _votes)
        {
            if (vote.getVote() < votesarr.length && vote.getVote() >= 0)
            {
                votesarr[vote.getVote()]++;
            }
        }

        CategoryDataset dataset = null;
        if (_showPercent)
        {
            dataset = createDatasetPercent(votesarr, _question.getAnswers());
        }
        else
        {
            dataset = createDatasetAbsolute(votesarr, _question.getAnswers());
        }

        _gui.createNewChartPanel(dataset,
                StringEscapeUtils.unescapeHtml4(HTMLStripper.stripHTML2(_question.getQuestion())), _backgroundcolor,
                _showCorrectAnswer, Arrays.asList(_question.getCorrectAnswer()), _showPercent);
    }

}
