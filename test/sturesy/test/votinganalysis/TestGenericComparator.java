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
package sturesy.test.votinganalysis;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import sturesy.core.GenericComparator;

@RunWith(MockitoJUnitRunner.class)
public class TestGenericComparator
{

    private GenericComparator<String> _objectComparator;

    @Before
    public void beforeEachTest()
    {
        _objectComparator = new GenericComparator<String>();
    }

    @Test
    public void testComparatorFirstGreaterSecond()
    {
        String firstString = "1";
        String secondString = "0";
        int compareState = _objectComparator.compare(firstString, secondString);
        assertTrue(1 == compareState);
    }

    @Test
    public void testComparatorSecondGreaterFirst()
    {
        String firstString = "0";
        String secondString = "1";
        int compareState = _objectComparator.compare(firstString, secondString);
        assertTrue(-1 == compareState);
    }

    @Test
    public void testIsCompare0()
    {
        String firstString = "0";
        String secondString = "0";
        int compareState = _objectComparator.compare(firstString, secondString);
        assertTrue(0 == compareState);
    }
}
