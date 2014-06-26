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
package sturesy.core.ui;

import javax.swing.DefaultListModel;

/**
 * Adds Method to swap Elements in {@link DefaultListModel}
 * 
 * @author w.posdorfer
 */
public class SwappableListModel<E> extends DefaultListModel<E>
{

    private static final long serialVersionUID = -3309301645496963657L;

    /**
     * Swaps the elements at the specified positions in this list<br>
     * Taken from <code>java.util.Collections.swap(List,int,int)</code>
     * 
     * @param i
     *            the index of one element to be swapped.
     * @param j
     *            the index of the other element to be swapped.
     * @throws IndexOutOfBoundsException
     *             if either <tt>i</tt> or <tt>j</tt> is out of range (i &lt; 0
     *             || i &gt;= list.size() || j &lt; 0 || j &gt;= list.size()).
     * @author Josh Bloch
     * @author Neal Gafter
     */
    public void swap(int i, int j)
    {
        super.set(i, super.set(j, super.get(i)));
    }

}
