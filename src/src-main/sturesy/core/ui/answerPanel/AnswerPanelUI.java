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
package sturesy.core.ui.answerPanel;

import javax.swing.JPanel;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

/**
 * The ui for the answer Panel.
 * 
 * @author jens.dallmann
 * 
 */
public class AnswerPanelUI
{
    /**
     * the panel where all answers will be added It manages the layouting for
     * the different rows.
     */
    private JPanel _panel;

    /**
     * creates the ui with miglayout. It Layouts the the components in a
     * vertical flow. It also manages the resizing.
     */
    public AnswerPanelUI()
    {
        _panel = new JPanel(new MigLayout("flowy, fill, insets 0", "grow, fill", "grow, fill"));
    }

    /**
     * adds the answer with all options to the panel.
     * 
     * @param panel
     *            the panel which should be added to the ui
     */
    public void addDefineAnswerPanel(JPanel panel)
    {
        _panel.add(panel, new CC().growX().growY().width("400").gapTop("10").shrink(100));
    }

    /**
     * returns the panel of the ui where the answers were added.
     * 
     * @return JPanel
     */
    public JPanel getPanel()
    {
        return _panel;
    }
}
