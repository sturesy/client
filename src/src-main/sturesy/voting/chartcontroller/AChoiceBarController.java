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

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import sturesy.core.ui.HTMLStripper;
import sturesy.items.Vote;
import sturesy.util.MathUtils;
import sturesy.util.Settings;
import sturesy.voting.gui.VotingEvaluationPanelUI;

/**
 * Abstract class to bundle some logic used by both the Single and
 * MultipleChoiceQuestionBarCharts
 * 
 * @author w.posdorfer
 * 
 */
public abstract class AChoiceBarController
{

    protected final Color _backgroundcolor = Settings.getInstance().getColor("color.analysis.main");
    protected VotingEvaluationPanelUI _gui;
    protected Set<Vote> _votes = new HashSet<Vote>();
    protected boolean _showPercent = true;
    protected boolean _showCorrectAnswer = false;

    public AChoiceBarController(ActionListener showpercentageListener)
    {
        _gui = new VotingEvaluationPanelUI(createDefaultDataset(), _backgroundcolor, false, new ArrayList<Integer>(),
                showpercentageListener);
    }

    public void setShowPercentage(boolean showPercentage)
    {
        _showPercent = showPercentage;
    }

    public void displayEmptyChart()
    {
        _gui.createNewChartPanel(createDefaultDataset(), "", _backgroundcolor, _showCorrectAnswer,
                new ArrayList<Integer>(), _showPercent);
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

        int all = 0;
        for (int i = 0; i < votes.length; i++)
        {
            all += votes[i];
        }

        for (int i = 0; i < votes.length; i++)
        {

            double value = votes[i] * 100 / all;

            value = MathUtils.roundToDecimals(value, 1);

            String answer = StringEscapeUtils.unescapeHtml4(HTMLStripper.stripHTML2(answers.get(i)));
            String columnKey = (char) ('A' + i) + ": " + answer;
            result.addValue(value, "SERIES", columnKey);
        }

        return result;
    }

    /**
     * Creates a dataset from given votes and Answers
     * 
     * @param votesarr
     *            the votes
     * @param answers
     *            the answers
     * @return CategoryDataset
     */
    protected CategoryDataset createDataSet(double[] votesarr, List<String> answers)
    {
        CategoryDataset dataset = null;
        if (_showPercent)
        {
            dataset = createDatasetPercent(votesarr, answers);
        }
        else
        {
            dataset = createDatasetAbsolute(votesarr, answers);
        }
        return dataset;
    }

    /**
     * Creates a Dataset containing absolute values
     * 
     * @param votes
     *            array of votes
     * @param answers
     *            List of answer strings
     * @return CategoryDataset containing absolute values
     */
    protected CategoryDataset createDatasetAbsolute(double[] votes, List<String> answers)
    {
        final DefaultCategoryDataset result = new DefaultCategoryDataset();

        for (int i = 0; i < votes.length; i++)
        {
            String answer = StringEscapeUtils.unescapeHtml4(HTMLStripper.stripHTML2(answers.get(i)));
            String columnKey = (char) ('A' + i) + ": " + answer;
            result.addValue(votes[i], "SERIES", columnKey);
        }
        return result;
    }

    /**
     * @return an empty default barchart
     */
    protected CategoryDataset createDefaultDataset()
    {
        return new DefaultCategoryDataset();
    }
}
