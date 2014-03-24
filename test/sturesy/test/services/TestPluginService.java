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
package sturesy.test.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import sturesy.core.plugin.IPollPlugin;
import sturesy.core.plugin.Injectable;
import sturesy.items.LectureID;
import sturesy.items.QuestionModel;
import sturesy.items.SingleChoiceQuestion;
import sturesy.services.PluginService;

@RunWith(MockitoJUnitRunner.class)
public class TestPluginService
{
    private Collection<IPollPlugin> _plugins;
    @Mock
    private IPollPlugin _pollPlugin1;
    @Mock
    private IPollPlugin _pollPlugin2;
    @Mock
    private Injectable _injectable;

    @Before
    public void beforeEachTest()
    {
        MockitoAnnotations.initMocks(this);
        _plugins = new ArrayList<IPollPlugin>();

        _plugins.add(_pollPlugin1);
        _plugins.add(_pollPlugin2);
    }

    @Test
    public void testPluginServiceSetInjectorPlugins()
    {
        PluginService pluginService = new PluginService(_injectable, _plugins);
        pluginService.setInjectorInPlugins();
        verify(_pollPlugin1, times(1)).setInjectable(_injectable);
        verify(_pollPlugin2, times(1)).setInjectable(_injectable);
    }

    @Test
    public void testPluginServiceStopPolling()
    {
        PluginService pluginService = new PluginService(_injectable, _plugins);
        pluginService.stopPolling();
        verify(_pollPlugin1, times(1)).stopPolling();
        verify(_pollPlugin2, times(1)).stopPolling();
    }

    @Test
    public void testPluginServiceStartPolling()
    {
        PluginService pluginService = new PluginService(_injectable, _plugins);
        pluginService.startPolling();
        verify(_pollPlugin1, times(1)).startPolling();
        verify(_pollPlugin2, times(1)).startPolling();
    }

    @Test
    public void testPluginServicePrepareVoting()
    {
        LectureID id = mock(LectureID.class);
        QuestionModel model = new SingleChoiceQuestion("A question", Arrays.asList(new String[] { "1", "2", "3" }), 0, -1);

        PluginService pluginService = new PluginService(_injectable, _plugins);
        pluginService.prepareVoting(id, model);
        verify(_pollPlugin1, times(1)).prepareVoting(id, model);
        verify(_pollPlugin2, times(1)).prepareVoting(id, model);
    }
}
