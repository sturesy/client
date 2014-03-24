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
package sturesy.core.ui.renderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import sturesy.items.LectureID;

/**
 * Renders a lecture id for one cell. This means that the correct text for one
 * cell will be set.
 * 
 * @author jens.dallmann
 */
public class LectureIdListRenderer extends DefaultListCellRenderer
{
    private static final long serialVersionUID = 4055902974843413058L;

    /**
     * sets the lecture id as Text for the cell.
     */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus)
    {
        LectureID val = (LectureID) value;
        Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        setText(val.getLectureID());
        return component;
    }
}
