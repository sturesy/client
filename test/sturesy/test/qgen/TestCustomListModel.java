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
package sturesy.test.qgen;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import sturesy.core.ui.SwapableListModel;

public class TestCustomListModel
{
    private SwapableListModel listModel;
    private Object obj1;
    private Object obj2;
    private Object obj3;
    private Object obj4;

    @Before
    public void beforeEachTest()
    {
        listModel = new SwapableListModel();
        obj1 = new Object();
        obj2 = new Object();
        obj3 = new Object();
        obj4 = new Object();
        listModel.add(0, obj1);
        listModel.add(1, obj2);
        listModel.add(2, obj3);
        listModel.add(3, obj4);
    }

    @Test
    public void testCustomListModelSwapBefore()
    {
        listModel.swap(0, 1);
        assertSame(obj2, listModel.get(0));
        assertSame(obj1, listModel.get(1));
        assertSame(obj3, listModel.get(2));
        assertSame(obj4, listModel.get(3));
        assertTrue(4 == listModel.size());
    }

    @Test
    public void testCustomListModelSwapBehind()
    {
        listModel.swap(1, 0);
        assertSame(obj2, listModel.get(0));
        assertSame(obj1, listModel.get(1));
        assertSame(obj3, listModel.get(2));
        assertSame(obj4, listModel.get(3));
        assertTrue(4 == listModel.size());
    }

    @Test
    public void testSwapOverMoreElements()
    {
        listModel.swap(0, 3);
        assertSame(obj4, listModel.get(0));
        assertSame(obj2, listModel.get(1));
        assertSame(obj3, listModel.get(2));
        assertSame(obj1, listModel.get(3));
        assertTrue(4 == listModel.size());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testIndexRangeLow()
    {
        listModel.swap(-1, 3);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testIndexRangeUp()
    {
        listModel.swap(0, 4);
    }
}
