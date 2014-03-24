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

import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import sturesy.core.ui.HTMLStripper;
import sturesy.items.MultipleChoiceQuestion;
import sturesy.items.QuestionModel;
import sturesy.items.Vote;
import sturesy.items.vote.MultipleVote;
import sturesy.util.MathUtils;

/**
 * Provides functionality to evaluate MultipleChoiceQuestions
 * 
 * @author w.posdorfer
 * 
 */
public class MultipleChoiceBarChartController extends AChoiceBarController implements IBarChartController
{

    private MultipleChoiceQuestion _question;

    public MultipleChoiceBarChartController(ActionListener showpercentageListener)
    {
        super(showpercentageListener);
    }

    @Override
    public void applyVotesToChart(QuestionModel question, Set<Vote> votes)
    {
        _question = (MultipleChoiceQuestion) question;
        _votes = votes;
    }

    @Override
    public void setAnswerVisible(boolean visible)
    {
        _showCorrectAnswer = visible && _question != null && _question.hasCorrectAnswer();
    }

    @Override
    public JPanel getPanel()
    {
        return _gui;
    }

    @Override
    public void updateUI()
    {
        double[] votesarr = prepareVotesForDataSet(_question.getAnswerSize(), _votes);

        CategoryDataset dataset = createDataSet(votesarr, _question.getAnswers());

        final String unescapeHtml4 = StringEscapeUtils.unescapeHtml4(HTMLStripper.stripHTML2(_question.getQuestion()));

        _gui.createNewChartPanel(dataset, unescapeHtml4, _backgroundcolor, _showCorrectAnswer,
                _question.getCorrectAnswers(), _showPercent);
    }

    /**
     * Creates an array of the given votes
     * 
     * @param answerSize
     * @param votes
     * @return votesArray
     */
    private double[] prepareVotesForDataSet(int answerSize, Collection<Vote> votes)
    {
        double[] votesarr = new double[answerSize];
        Arrays.fill(votesarr, 0);

        for (Vote mVote : votes)
        {
            if (mVote instanceof MultipleVote)
            {
                for (short vote : ((MultipleVote) mVote).getVotes())
                {
                    if (vote < votesarr.length && vote >= 0)
                    {
                        votesarr[vote]++;
                    }
                }
            }
        }
        return votesarr;
    }

    /**
     * Creates a Dataset containing percent values
     * 
     * @param votes
     *            array of votes
     * @param answers
     *            List of answer strings
     * @return CategoryDataset containing percent values
     */
    protected CategoryDataset createDatasetPercent(double[] votes, List<String> answers)
    {
        final DefaultCategoryDataset result = new DefaultCategoryDataset();

        for (int i = 0; i < votes.length; i++)
        {
            double value = votes[i] * 100 / _votes.size();
            value = MathUtils.roundToDecimals(value, 1);
            String answer = StringEscapeUtils.unescapeHtml4(HTMLStripper.stripHTML2(answers.get(i)));
            String columnKey = (char) ('A' + i) + ": " + answer;
            result.addValue(value, "SERIES", columnKey);
        }

        return result;
    }

}
