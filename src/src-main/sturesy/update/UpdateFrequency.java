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
package sturesy.update;

import sturesy.core.Localize;

/**
 * Enumeration for update frequencies
 * 
 * @author w.posdorfer
 * 
 */
public enum UpdateFrequency
{

    DAILY, WEEKLY, MONTHLY, MANUALLY;

    public String getLocalized()
    {
        switch (this)
        {
        case DAILY:
            return Localize.getString("update.frequency.daily");
        case WEEKLY:
            return Localize.getString("update.frequency.weekly");
        case MONTHLY:
            return Localize.getString("update.frequency.monthly");
        default:
        case MANUALLY:
            return Localize.getString("update.frequency.manually");
        }
    }

    public static UpdateFrequency valueOf(int x)
    {
        return UpdateFrequency.values()[x];
    }
}
