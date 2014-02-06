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
package sturesy.core.ui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.text.JTextComponent;

/**
 * A {@link FocusListener} that automatically selects the text of a
 * {@link JTextComponent} when focus is gained. This is the expected behavior
 * for most operatingsystems.
 * 
 * @author w.posdorfer
 * 
 */
public class TextSelectionFocusListener implements FocusListener
{
    /** TextComponent to listen to, and invoke text selection */
    private final JTextComponent _textcomponent;

    /**
     * A {@link FocusListener} that automatically selects the text of a
     * {@link JTextComponent} when focus is gained. This is the expected
     * behavior for most operatingsystems.<br>
     * <br>
     * Creates a new FocusListener and registers it
     * 
     * @param textfield
     *            {@link JTextComponent} to listen to and select text
     */
    public TextSelectionFocusListener(JTextComponent textfield)
    {
        _textcomponent = textfield;
        _textcomponent.addFocusListener(this);
    }

    public void focusGained(FocusEvent e)
    {
        _textcomponent.selectAll();
    }

    public void focusLost(FocusEvent e)
    {
    }
}
