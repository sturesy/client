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
package sturesy.voting;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import sturesy.items.QuestionSet;

/**
 * Panel that holds the VisualQuestionPanel
 * 
 * @author w.posdorfer
 * 
 */
public class VotingPanel extends JPanel
{

    private static final long serialVersionUID = -3336782444479982058L;

    public VotingPanel()
    {
        setLayout(new GridBagLayout());

    }

    /**
     * Sets the CurrentQuestionModel
     * 
     * @param QuestionSet
     * @param QuestionModel
     * @param lecturefile
     */
    public void setCurrentQuestionModel(QuestionSet QuestionSet, int QuestionModel, String lecturefile)
    {

        Dimension minimumSize = new Dimension(100, 50);

        VisualQuestionPanel vpq = new VisualQuestionPanel(QuestionSet.getIndex(QuestionModel), lecturefile, QuestionSet);
        vpq.getPanel().setMinimumSize(minimumSize);

        removeAll();
        add(vpq.getPanel(), new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

}
