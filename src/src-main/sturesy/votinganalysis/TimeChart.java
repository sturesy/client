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
package sturesy.votinganalysis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import sturesy.core.Localize;
import sturesy.items.Vote;
import sturesy.votinganalysis.gui.TimeChartUI;

/**
 * Class that holds the TimeChart
 * 
 * @author w.posdorfer
 * 
 */
public class TimeChart
{

    private TimeChartUI _ui;

    private ChartPanel _chartpanel;

    private Color _background;

    /**
     * Create a new TimeChart
     */
    public TimeChart(Color background)
    {
        _background = background;
        if (_background == null)
        {
            _background = Color.GRAY;
        }
        _chartpanel = getXYSeriesChart(new HashSet<Vote>());
        _ui = new TimeChartUI(_chartpanel);
    }

    /**
     * Resets the Current Chart and starts fresh from the given votes
     * 
     * @param votes
     *            Set of Votes to display
     */
    public void applyVotesToChart(Set<Vote> votes)
    {
        _chartpanel = getXYSeriesChart(votes);
        _ui.setPanel(_chartpanel);
    }

    /**
     * Returns the graphical Component
     * 
     * @return JPanel
     */
    public JPanel getPanel()
    {
        return _ui;
    }

    /**
     * Creates an XYSeries-ChartPanel
     * 
     * @param votes
     *            votes to use
     * @return ChartPanel
     */
    private ChartPanel getXYSeriesChart(Set<Vote> votes)
    {

        final XYSeries series = new XYSeries(Localize.getString("label.votes.over.time"));

        if (votes.size() != 0)
        {
            double[] dubble = createArrayOfVotes(votes);

            for (int i = 0; i < dubble.length; i++)
            {
                series.add(i, dubble[i]);
            }
        }
        final XYSeriesCollection data = new XYSeriesCollection(series);

        final JFreeChart chart = ChartFactory.createXYLineChart(Localize.getString("label.votes.over.time"),
                Localize.getString("label.time.seconds"), Localize.getString("label.amount.votes"), data,
                PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(_background);
        chart.getPlot().setNoDataMessage("NO DATA");

        chart.getXYPlot()
                .getRenderer()
                .setSeriesStroke(0,
                        new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, null, 0.0f));
        chart.getXYPlot().getRenderer().setSeriesPaint(0, new Color(255, 140, 0));

        chart.getXYPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        ChartPanel chartpanel = new ChartPanel(chart);
        return chartpanel;
    }

    /**
     * Converts a set of Votes to an Array of Votes over Time
     * 
     * @param setvotes
     *            Set of Votes
     * @return Array of Votes over Time
     */
    private double[] createArrayOfVotes(Set<Vote> setvotes)
    {
        ArrayList<Vote> votes = new ArrayList<Vote>(setvotes);
        Collections.sort(votes, new Comparator<Vote>()
        {

            @Override
            public int compare(Vote o1, Vote o2)
            {
                Long i1 = o1.getTimeDiff();
                Long i2 = o2.getTimeDiff();
                return i1.compareTo(i2);
            }
        });

        int totalduration = (int) (votes.get(votes.size() - 1).getTimeDiff() / 1000);

        double[] dubble = new double[totalduration + 1];

        for (Vote v : votes)
        {
            int slot = ((int) (v.getTimeDiff() / 1000));
            dubble[slot]++;
        }

        return dubble;
    }

}
