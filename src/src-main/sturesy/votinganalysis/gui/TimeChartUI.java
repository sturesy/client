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
package sturesy.votinganalysis.gui;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;

/**
 * Chart displaying the voting-process
 * 
 * @author w.posdorfer
 * 
 */
public class TimeChartUI extends JPanel
{

    private static final long serialVersionUID = -2985050044160622720L;

    /**
     * Creates a Panel holding the TimeChart
     * 
     * @param panel
     */
    public TimeChartUI(ChartPanel panel)
    {
        add(panel);
    }

    /**
     * Removes all previous contents and adds the new Panel
     * 
     * @param panel
     *            Chartpanel to add
     */
    public void setPanel(ChartPanel panel)
    {
        removeAll();
        add(panel);
    }

}
