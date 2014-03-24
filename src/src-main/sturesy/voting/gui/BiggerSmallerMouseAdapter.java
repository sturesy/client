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
package sturesy.voting.gui;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import sturesy.core.Localize;
import sturesy.core.ui.JMenuItem2;
import sturesy.util.SturesySwingHelper;

/**
 * A MouseListener, which shows a JPopupMenu. The JPopupMenu has entries for
 * increasing and decreasing. Each entry in the JPopupMenu has a distinct method
 * to be implemented <br>
 * <br>
 * See: {@link #increaseSize()} and {@link #decreaseSize()}
 * 
 * @author w.posdorfer
 */
public abstract class BiggerSmallerMouseAdapter extends MouseAdapter
{

    /**
     * Method to be invoked when the "Increase" Button is clicked
     */
    protected abstract void increaseSize();

    /**
     * Method to be invoked when the "Decrease" Button is clicked
     */
    protected abstract void decreaseSize();

    /**
     * Method to be invoked when the JPopupMenu should be closed.<br>
     * This happens after <code>increaseSize</code> or <code>decreaseSize</code>
     * have been invoked<br>
     * <br>
     * Note: The JPopupMenu has to be closed manually by calling
     * <code>jpopmenu.setVisible(false)</code>
     * 
     * @param jpopmenu
     *            JPopupMenu to be closed
     */
    protected abstract void closePopup(JPopupMenu jpopmenu);

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (SturesySwingHelper.isRightClick(e))
        {
            JPopupMenu jpop = new JPopupMenu();

            jpop.add(getIncreaseItem(jpop));
            jpop.add(getDecreaseItem(jpop));

            jpop.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    /**
     * creates the "decrease" JMenuItem
     * 
     * @param jpop
     *            JPopupMenu to be closed after click
     * @return JMenuItem
     */
    private JMenuItem getDecreaseItem(final JPopupMenu jpop)
    {
        return new JMenuItem2(Localize.getString("label.smaller"), new AbstractAction()
        {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e)
            {
                decreaseSize();
                closePopup(jpop);
            }
        });
    }

    /**
     * creates the "increase" JMenuItem
     * 
     * @param jpop
     *            JPopupMenu to be closed after click
     * @return JMenuItem
     */
    private JMenuItem getIncreaseItem(final JPopupMenu jpop)
    {
        return new JMenuItem2(Localize.getString("label.bigger"), new AbstractAction()
        {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e)
            {
                increaseSize();
                closePopup(jpop);
            }
        });
    }

}
