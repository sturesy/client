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
package sturesy.test.items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import sturesy.items.Vote;
import sturesy.items.vote.SingleVote;

public class TestVote
{

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testEquals()
    {

        Vote v1 = new SingleVote("W1", 0, 100);
        Vote v2 = new SingleVote("W2", 0, 100);

        Vote v3 = new SingleVote("W1", 1, 200);

        assertEquals(v3, v1);

        assertFalse(v1.equals(v2));

        assertEquals(v1, v1);

        assertEquals(v1.hashCode(), v3.hashCode());
        assertEquals(v1.hashCode(), v1.hashCode());
        assertFalse(v1.equals(null));
        assertFalse(v1.equals(new String()));

        Vote vnull = new SingleVote(null, 0, 100);
        Vote vnull2 = new SingleVote(null, 0, 100);

        assertFalse(vnull.equals(v1));
        assertEquals(vnull, vnull2);
    }

}
