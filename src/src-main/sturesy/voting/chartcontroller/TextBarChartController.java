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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jfree.chart.ChartPanel;
import org.jfree.data.category.CategoryDataset;

import sturesy.core.Localize;
import sturesy.core.ui.HTMLLabel;
import sturesy.core.ui.HTMLStripper;
import sturesy.items.QuestionModel;
import sturesy.items.TextQuestion;
import sturesy.items.Vote;
import sturesy.items.vote.TextVote;
import sturesy.voting.gui.VotingEvaluationPanelUI;

/**
 * Provides functionality to evaluate TextQuestions
 * 
 * @author w.posdorfer
 * 
 */
public class TextBarChartController extends AChoiceBarController implements IBarChartController
{

    private TextQuestion _question;

    private boolean _showAnserText = false;

    public TextBarChartController(ActionListener showpercentageListener)
    {
        super(showpercentageListener);
        _gui = new TextBarPanel(createDefaultDataset(), Color.GRAY, false, new ArrayList<Integer>(),
                showpercentageListener);
    }

    @Override
    public void applyVotesToChart(QuestionModel question, Set<Vote> votes)
    {
        _question = (TextQuestion) question;
        _votes = votes;
    }

    @Override
    public void setAnswerVisible(boolean visible)
    {
        _showCorrectAnswer = true;
        _showAnserText = visible && _question != null;
    }

    @Override
    public JPanel getPanel()
    {
        return _gui;
    }

    @Override
    public void updateUI()
    {
        double[] votesarr = prepareVotesForDataset();

        final String correctA = Localize.getString("label.textquestion.correctanswers");
        final String wrongA = Localize.getString("label.textquestion.wronganswers");
        CategoryDataset dataset = createDataSet(votesarr, Arrays.asList(correctA, wrongA));

        final String unescapeHtml4 = StringEscapeUtils.unescapeHtml4(HTMLStripper.stripHTML2(_question.getQuestion()));

        _gui.createNewChartPanel(dataset, unescapeHtml4, _backgroundcolor, _showCorrectAnswer, Arrays.asList(0),
                _showPercent);
    }

    private double[] prepareVotesForDataset()
    {
        double[] votesarr = new double[2];

        for (Vote v : _votes)
        {
            if (v instanceof TextVote)
            {
                if (_question.matchesPercentage(((TextVote) v).getAnswer()))
                {
                    votesarr[0]++;
                }
                else
                {
                    votesarr[1]++;
                }
            }
        }

        return votesarr;
    }

    /**
     * Extends the default PanelClass to add a label showing the correct answer
     * 
     * @author w.posdorfer
     * 
     */
    private class TextBarPanel extends VotingEvaluationPanelUI
    {
        private static final long serialVersionUID = 1L;

        public TextBarPanel(CategoryDataset defaultDataset, Color background, boolean showAnswer,
                List<Integer> correctAnswer, ActionListener menuItemActionListener)
        {
            super(defaultDataset, background, showAnswer, correctAnswer, menuItemActionListener);
        }

        @Override
        public void setNewChartPanel(ChartPanel cp)
        {
            removeAll();
            _chartpanel = cp;
            add(_chartpanel, BorderLayout.CENTER);

            if (_showAnserText)
            {
                String cAns = Localize.getString("label.textquestion.text.correctanswer");
                JLabel answerLabel = new HTMLLabel(cAns + ":<br><b>" + _question.getAnswer() + "</b>");
                answerLabel.setHorizontalAlignment(JLabel.CENTER);
                answerLabel.setFont(answerLabel.getFont().deriveFont(22f));
                _gui.add(answerLabel, BorderLayout.NORTH);
            }

            revalidate();
        }
    }

}
