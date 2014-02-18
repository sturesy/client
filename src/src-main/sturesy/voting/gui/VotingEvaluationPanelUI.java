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
package sturesy.voting.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.TextAnchor;

import sturesy.core.Localize;
import sturesy.voting.gui.renderer.AnswersBarRenderer;
import sturesy.voting.gui.renderer.AppendPercentRenderer;

/**
 * UIclass for EvalPanel
 * 
 * @author w.posdorfer
 * 
 */
public class VotingEvaluationPanelUI extends JPanel
{

    private static final long serialVersionUID = -5556355720966529589L;
    protected ChartPanel _chartpanel;
    private ActionListener _menuItemActionListener;

    /**
     * Creates a new UI for VotingEvaluationPanel
     * 
     * @param defaultDataset
     *            a defaulDataSet to display
     * @param background
     *            the backgroundcolor
     * @param showAnswer
     *            highlight the correct answer in green?
     * @param correctAnswer
     *            index of correct answer
     * @param menuItemActionListener
     *            ActionListener to attach to the JMenuItem
     */
    public VotingEvaluationPanelUI(CategoryDataset defaultDataset, Color background, boolean showAnswer,
            List<Integer> correctAnswer, ActionListener menuItemActionListener)
    {
        setLayout(new BorderLayout());
        _menuItemActionListener = menuItemActionListener;

        _chartpanel = new ChartPanel(createChart(defaultDataset, "", background, showAnswer, correctAnswer, true));

        add(_chartpanel, BorderLayout.CENTER);
    }

    /**
     * Sets a new ChartPanel into this JPanel
     * 
     * @param cp
     */
    public void setNewChartPanel(ChartPanel cp)
    {
        remove(_chartpanel);
        _chartpanel = cp;
        add(_chartpanel, BorderLayout.CENTER);
        revalidate();
    }

    /**
     * creates a new ChartPanel inside this UI
     * 
     * @param categoryDataset
     *            the Data
     * @param questionText
     *            the questiontext to display above
     * @param background
     *            background color
     * @param showAnswers
     *            highlight correct answer in green?
     * @param correctAnswer
     *            index of correct answer
     * @param showPercent
     *            should it show percent values or absolute values?
     */
    public void createNewChartPanel(CategoryDataset categoryDataset, String questionText, Color background,
            boolean showAnswers, List<Integer> correctAnswers, boolean showPercent)
    {
        JFreeChart chart = createChart(categoryDataset, questionText, background, showAnswers, correctAnswers,
                showPercent);
        ChartPanel chartPanel = new ChartPanel(chart);
        setNewChartPanel(chartPanel);

        String menuItemText = Localize.getString(showPercent ? "label.jfreechart.switch.absolute"
                : "label.jfreechart.switch.percent");
        JMenuItem menuItem = new JMenuItem(menuItemText);

        chartPanel.getPopupMenu().addSeparator();
        chartPanel.getPopupMenu().add(menuItem);

        menuItem.addActionListener(_menuItemActionListener);
    }

    private JFreeChart createChart(final CategoryDataset dataset, String questiontext, Color background,
            boolean showAnswers, List<Integer> correctAnswers, boolean showPercent)
    {
        String valueAxisLabel = Localize.getString(showPercent ? "label.votes.percent" : "label.votes.absolute");

        final JFreeChart chart = ChartFactory.createBarChart(questiontext, Localize.getString("label.answers"),
                valueAxisLabel, dataset, PlotOrientation.VERTICAL, false, true, false);

        chart.setBackgroundPaint(background);

        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setNoDataMessage("NO DATA!");

        final CategoryItemRenderer renderer = new AnswersBarRenderer(showAnswers, correctAnswers);

        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelFont(renderer.getBaseItemLabelFont().deriveFont(16.0f));

        final ItemLabelPosition p = new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER, TextAnchor.CENTER,
                45.0);

        renderer.setBasePositiveItemLabelPosition(p);

        plot.setRenderer(renderer);

        // change the margin at the top of the range axis...
        final ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setLowerMargin(0.15);
        rangeAxis.setUpperMargin(0.15);
        double upperrange = rangeAxis.getRange().getUpperBound();

        if (showPercent)
        {
            renderer.setBaseItemLabelGenerator(new AppendPercentRenderer());
            rangeAxis.setRange(new Range(0, upperrange > 100 ? 100 : upperrange), false, false);
        }
        else
        {
            renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        }

        plot.getDomainAxis().setMaximumCategoryLabelLines(5);
        return chart;

    }

}
