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

import sturesy.core.ui.UneditableTableModel;
import sturesy.items.QuestionModel;
import sturesy.items.Vote;

/**
 * Provides TableData depending on QuestionModel
 * 
 * @author w.posdorfer
 */
public interface ITableDataProvider
{
    public static final String DELTA_STRING = "\u0394";

    Object[][] createVoteTableValues(QuestionModel questionModel, Set<Vote> votesToDisplay);

    String[] createTableHeaders(QuestionModel questionModel);

    TableRowSorter<TableModel> createConfiguredTableRowSorter(UneditableTableModel tablemodel);
    
    int[] getPreferredTableWidth(UneditableTableModel tablemodel);

}
