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
package sturesy.update;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * Shows localized Versions of the UpdateFrequencies
 * 
 * @author w.posdorfer
 */
public class UpdateFrequencyCellRenderer extends DefaultListCellRenderer
{
    private static final long serialVersionUID = 4854333481820795049L;

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus)
    {
        Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        UpdateFrequency upd = (UpdateFrequency) value;

        setText(upd.getLocalized());

        return component;
    }
}
