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
package sturesy.votinganalysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.jfree.data.statistics.Statistics;

import sturesy.items.Vote;

/**
 * Class for calculating the arithmetic mean and median
 * 
 * @author w.posdorfer
 */
public class VoteAverage
{
    private double _timearithmeticMean = 0;
    private double _timeMedian = 0;

    /**
     * Constructer for VoteAverage, which calculates the arithmetic mean and median for the votes over time
     * @param setvotes
     */
    public VoteAverage(Set<Vote> setvotes)
    {
        if (hasVotes(setvotes))
        {
            ArrayList<Long> values = new ArrayList<Long>(setvotes.size());
            for (Vote v : setvotes)
            {
                values.add(v.getTimeDiff());
            }

            Collections.sort(values);

            double mean = Statistics.calculateMean(values);
            double median = Statistics.calculateMedian(values, false);

            _timearithmeticMean = round(mean);
            _timeMedian = round(median);
        }
    }
    
    /**
     * Checks if the given Set is not Empty
     * 
     * @param setvotes
     *            Set to check
     * @return true if Set contains elements
     */
    private boolean hasVotes(Set<Vote> setvotes)
    {
        return CollectionUtils.isNotEmpty(setvotes);
    }

    /**
     * Returns the Arithmetic Mean of the votes over time
     * 
     * @return double
     */
    public double getTimeArithmeticMean()
    {
        return _timearithmeticMean;
    }
    
    /**
     * Returns the Median of the votes over time
     * 
     * @return double
     */
    public double getTimeMedian()
    {
        return _timeMedian;
    }

    /**
     * rounds from 8123 to 8,1 or from 645 to 0,6
     * 
     * @param d
     * @return
     */
    private double round(double d)
    {
        return ((int) (d / 100)) / 10.0;
    }
}
