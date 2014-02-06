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
package sturesy.core;

/**
 * A Pair of Values
 * 
 * @author w.posdorfer
 * 
 * @param <First>
 *            Type of first value
 * @param <Second>
 *            Type of second value
 */
public class Pair<First, Second>
{

    private final First _first;
    private final Second _second;

    /**
     * Creates a new Pair
     * 
     * @param a
     *            the first value
     * @param b
     *            the second value
     */
    public Pair(First a, Second b)
    {
        _first = a;
        _second = b;
    }

    /**
     * Returns the first value
     */
    public First getFirst()
    {
        return _first;
    }

    /**
     * Returns the second value
     */
    public Second getSecond()
    {
        return _second;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Pair<?, ?>)
        {
            Pair<?, ?> otherPair = (Pair<?, ?>) obj;
            return equals(_first, otherPair._first) && equals(_second, otherPair._second);
        }
        else
        {
            return false;
        }
    }

    private boolean equals(Object x, Object y)
    {
        return (x == null && y == null) || (x != null && x.equals(y));
    }

    @Override
    public String toString()
    {
        return "Pair<" + _first.toString() + "," + _second.toString() + ">";
    }

    public static <A, B> Pair<A, B> of(A a, B b)
    {
        return new Pair<A, B>(a, b);
    }
}
