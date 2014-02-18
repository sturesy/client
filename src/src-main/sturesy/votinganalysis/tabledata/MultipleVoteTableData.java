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

import java.util.List;
import java.util.Set;

import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import sturesy.core.GenericComparator;
import sturesy.core.Localize;
import sturesy.core.ui.UneditableTableModel;
import sturesy.items.MultipleChoiceQuestion;
import sturesy.items.QuestionModel;
import sturesy.items.Vote;
import sturesy.items.vote.MultipleVote;

/**
 * Provides TableData for MultipleChoice
 * 
 * @author w.posdorfer
 * 
 */
public class MultipleVoteTableData implements ITableDataProvider
{

    @Override
    public Object[][] createVoteTableValues(QuestionModel questionModel, Set<Vote> votesToDisplay)
    {

        MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) questionModel;

        Vote[] votes = votesToDisplay.toArray(new Vote[0]);
        Object[][] tablevalues = new Object[votes.length][3 + questionModel.getAnswerSize()];

        for (int i = 0; i < votes.length; i++)
        {
            final MultipleVote vote = (MultipleVote) votes[i];

            tablevalues[i][0] = vote.getGuid();
            tablevalues[i][1] = vote.getTimeDiff() / 1000;
            tablevalues[i][2] = vote.getVotes().length;

            List<Short> votesList = vote.asList();
            for (short j = 0; j < questionModel.getAnswerSize(); j++)
            {
                if (votesList.contains(j))
                {
                    if (mcq.getCorrectAnswers().contains((int) j))
                    {
                        tablevalues[i][3 + j] = "\u2713";
                    }
                    else
                    {
                        tablevalues[i][3 + j] = "X";
                    }
                }
                else
                {
                    tablevalues[i][3 + j] = " ";
                }
            }

        }
        return tablevalues;
    }

    @Override
    public String[] createTableHeaders(QuestionModel questionModel)
    {
        String[] result = new String[3 + questionModel.getAnswerSize()];

        result[0] = "ID";
        result[1] = DELTA_STRING + " in s";
        result[2] = Localize.getString("label.votes");

        for (int i = 0; i < questionModel.getAnswerSize(); i++)
        {
            result[3 + i] = "" + (char) ('A' + i);
        }
        return result;
    }

    @Override
    public TableRowSorter<TableModel> createConfiguredTableRowSorter(UneditableTableModel tablemodel)
    {
        TableRowSorter<TableModel> tablerrowsorter = new TableRowSorter<TableModel>(tablemodel);
        tablerrowsorter.setComparator(0, new GenericComparator<String>());
        tablerrowsorter.setComparator(1, new GenericComparator<Integer>());
        tablerrowsorter.setComparator(2, new GenericComparator<Integer>());
        for (int i = 3; i < tablemodel.getColumnCount(); i++)
        {
            tablerrowsorter.setComparator(i, new GenericComparator<String>());
        }
        return tablerrowsorter;
    }

}
