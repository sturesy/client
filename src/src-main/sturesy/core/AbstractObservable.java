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

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Provides basic registering and unregistering features
 * 
 * @author w.posdorfer
 * 
 * @param <T>
 *            some objecttype
 */
public class AbstractObservable<T>
{

    protected Set<T> _listeners;

    public AbstractObservable()
    {
        _listeners = new LinkedHashSet<T>();
    }

    /**
     * Registers a listener
     * 
     * @param listener
     *            listener to add
     */
    public void registerListener(T listener)
    {
        _listeners.add(listener);
    }

    /**
     * Removes a listener
     * 
     * @param listener
     *            listener to be removed
     */
    public void removeListener(T listener)
    {
        _listeners.remove(listener);
    }

}