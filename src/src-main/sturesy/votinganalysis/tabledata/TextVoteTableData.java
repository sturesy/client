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
package sturesy.votinganalysis.tabledata;

import java.util.Set;

import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import sturesy.core.GenericComparator;
import sturesy.core.Localize;
import sturesy.core.ui.UneditableTableModel;
import sturesy.items.QuestionModel;
import sturesy.items.Vote;
import sturesy.items.vote.TextVote;

/**
 * Provides TableData for TextChoice
 * 
 * @author w.posdorfer
 * 
 */
public class TextVoteTableData implements ITableDataProvider
{

    @Override
    public Object[][] createVoteTableValues(QuestionModel questionModel, Set<Vote> votesToDisplay)
    {
        Vote[] votes = votesToDisplay.toArray(new Vote[0]);
        Object[][] tablevalues = new Object[votes.length][3];

        for (int i = 0; i < votes.length; i++)
        {
            final TextVote vote = (TextVote) votes[i];
            if (vote.getVote() < questionModel.getAnswerSize())
            {
                tablevalues[i][0] = vote.getGuid();
                tablevalues[i][1] = vote.getAnswer();
                tablevalues[i][2] = vote.getTimeDiff() / 1000;
            }
        }
        return tablevalues;
    }

    @Override
    public String[] createTableHeaders(QuestionModel questionModel)
    {
        return new String[] { "ID", Localize.getString("label.answer"), DELTA_STRING + " in s" };
    }

    @Override
    public TableRowSorter<TableModel> createConfiguredTableRowSorter(UneditableTableModel tablemodel)
    {
        TableRowSorter<TableModel> tablerrowsorter = new TableRowSorter<TableModel>(tablemodel);
        tablerrowsorter.setComparator(0, new GenericComparator<String>());
        tablerrowsorter.setComparator(1, new GenericComparator<String>());
        tablerrowsorter.setComparator(2, new GenericComparator<Integer>());
        return tablerrowsorter;
    }

}
