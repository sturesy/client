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
package testplugin;

import sturesy.core.plugin.IPlugin;
import sturesy.core.plugin.IPollPlugin;
import sturesy.core.plugin.ISettingsScreen;

/**
 * Test Plugin<br>
 * displays a clickable button, which adds Votes on click
 * 
 * @author w.posdorfer
 * 
 */
public class MainClass implements IPlugin
{

    @Override
    public void onLoad()
    {
    }

    @Override
    public IPollPlugin getPollPlugin()
    {
        return new Polling();
    }

    @Override
    public ISettingsScreen getSettingsScreen()
    {
        return new TestSettings();
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof MainClass;
    }

}
