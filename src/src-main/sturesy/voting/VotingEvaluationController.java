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
package sturesy.voting;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import sturesy.items.MultipleChoiceQuestion;
import sturesy.items.QuestionModel;
import sturesy.items.SingleChoiceQuestion;
import sturesy.items.TextQuestion;
import sturesy.items.Vote;
import sturesy.voting.chartcontroller.IBarChartController;
import sturesy.voting.chartcontroller.MultipleChoiceBarChartController;
import sturesy.voting.chartcontroller.SingleChoiceBarChartController;
import sturesy.voting.chartcontroller.TextBarChartController;

/**
 * EvaluationController Manages the different actual Evaluation-Panels.<br>
 * Depending on the class of the Question the SingleChoice, MultipleChoice or
 * TextQuestionbarCharts will be displayed
 * 
 * @author w.posdorfer
 * 
 */
public class VotingEvaluationController implements ActionListener
{

    private Set<Vote> _votes = new HashSet<Vote>();

    private boolean _showPercent = true;

    private JPanel _mainPanel;

    private Map<Class<? extends QuestionModel>, IBarChartController> _controllers = new HashMap<Class<? extends QuestionModel>, IBarChartController>();

    private QuestionModel _currentQuestion;

    public VotingEvaluationController()
    {
        _controllers.put(SingleChoiceQuestion.class, new SingleChoiceBarChartController(this));
        _controllers.put(MultipleChoiceQuestion.class, new MultipleChoiceBarChartController(this));
        _controllers.put(TextQuestion.class, new TextBarChartController(this));

        _mainPanel = new JPanel(new BorderLayout());
    }

    private IBarChartController getControllerForCurrent()
    {
        if (_currentQuestion == null)
        {
            return _controllers.get(SingleChoiceQuestion.class);
        }
        else
        {
            return _controllers.get(_currentQuestion.getClass());
        }
    }

    public void setCurrentQuestion(QuestionModel question)
    {
        _currentQuestion = question;
    }

    private void updatePanel()
    {
        _mainPanel.removeAll();
        getControllerForCurrent().updateUI();
        _mainPanel.add(getControllerForCurrent().getPanel(), BorderLayout.CENTER);
    }

    /**
     * Set if the correct Answer Bar should be highlighted in green and all
     * others in red
     * 
     * @param visible
     *            should the correct answer be shown in green?
     */
    public void setAnswerVisible(boolean visible)
    {
        getControllerForCurrent().applyVotesToChart(_currentQuestion, _votes);
        getControllerForCurrent().setAnswerVisible(visible);
        getControllerForCurrent().updateUI();
    }

    public void setShowPercentage(boolean showPercentage)
    {
        _showPercent = showPercentage;
        getControllerForCurrent().setShowPercentage(_showPercent);
        getControllerForCurrent().updateUI();
    }

    /**
     * Pass a Set of Votes to have this Panel display a BarChart with the
     * voting-results
     * 
     * @param votes
     * @param questionText
     * @param answers
     */
    public void applyVotesToChart(Set<Vote> votes, QuestionModel question)
    {
        _votes = votes;
        _currentQuestion = question;
        if (question != null)
        {
            getControllerForCurrent().setShowPercentage(_showPercent);
            getControllerForCurrent().applyVotesToChart(question, _votes);
        }
        else
        {
            getControllerForCurrent().displayEmptyChart();
        }
        updatePanel();
    }

    /**
     * Returns the Visual Component
     */
    public JPanel getPanel()
    {
        return _mainPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        _showPercent = !_showPercent;
        getControllerForCurrent().setShowPercentage(_showPercent);
        getControllerForCurrent().updateUI();
    }
}
