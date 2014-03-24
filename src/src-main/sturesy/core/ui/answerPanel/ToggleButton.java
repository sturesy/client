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
package sturesy.core.ui.answerPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * A custom variant of a JToggleButton.<br>
 * This variant doesn't highlight a selected button it changes the icon
 * 
 * @author w.posdorfer
 * 
 */
public class ToggleButton extends JButton
{

    private static final long serialVersionUID = -8118993763113995075L;
    /** Icon for unselected state */
    private final ImageIcon _unselectedIcon;
    /** Icon for selected state */
    private final ImageIcon _selectedIcon;

    private boolean _isSelected;

    /**
     * Creates a new ToggleButton in unselected state
     * 
     * @param unselected
     *            Icon to be used in unselected state
     * @param selected
     *            Icon to be used in selected state
     */
    public ToggleButton(ImageIcon unselected, ImageIcon selected)
    {
        super(unselected);
        _isSelected = false;
        _unselectedIcon = unselected;
        _selectedIcon = selected;
    }

    /**
     * Changes the Icon
     */
    private void changeIcon()
    {
        if (_isSelected)
        {
            setIcon(_selectedIcon);
        }
        else
        {
            setIcon(_unselectedIcon);
        }
    }

    @Override
    public void setSelected(boolean selected)
    {
        if (selected != _isSelected) // only change on new state
        {
            _isSelected = selected;
            changeIcon();
        }
    }

    @Override
    public boolean isSelected()
    {
        return _isSelected;
    }

    @Override
    protected void fireActionPerformed(ActionEvent event)
    {
        super.fireActionPerformed(event);
        _isSelected = !_isSelected; // change Value
        changeIcon();
        fireItemStateChanged(getItemEvent());
    }

    /**
     * Returns the appropriate ItemEvent
     * 
     * @return some itemevent
     */
    private ItemEvent getItemEvent()
    {
        return new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED, this, _isSelected ? ItemEvent.SELECTED
                : ItemEvent.DESELECTED);
    }
}
