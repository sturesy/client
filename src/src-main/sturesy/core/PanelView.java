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
package sturesy.core;

import java.awt.BorderLayout;
import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * Abstract class for UI that is a JPanel
 * 
 * @author w.posdorfer
 *
 */
public abstract class PanelView
{

    protected JPanel _panel;

    /**
     * Creates a JPanel with BorderLayout
     */
    public PanelView()
    {
        this(new BorderLayout());
    }

    /**
     * Creates a JPanel with specified layout
     * 
     * @param layout
     */
    public PanelView(LayoutManager layout)
    {
        _panel = new JPanel(layout);
    }

    /**
     * Returns this Panel
     * 
     * @return JPanel
     */
    public JPanel getPanel()
    {
        return _panel;
    }

}
