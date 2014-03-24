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
package sturesy.services;

import java.util.Collection;
import java.util.HashSet;

import sturesy.core.plugin.IPollPlugin;
import sturesy.core.plugin.Injectable;
import sturesy.items.LectureID;
import sturesy.items.QuestionModel;

/**
 * The PluginService keeps track of the used PollingPlugins and bundles method
 * calls to all the plugins
 * 
 * @author j.dallmann
 * 
 */
public class PluginService
{
    private final Injectable _injectable;
    private Collection<IPollPlugin> _pollingPlugins;

    public PluginService(Injectable injectable, Collection<IPollPlugin> pollingPlugins)
    {
        _injectable = injectable;

        if (pollingPlugins == null)
        {
            _pollingPlugins = new HashSet<IPollPlugin>();
        }
        else
        {
            _pollingPlugins = pollingPlugins;
        }
    }

    /**
     * Tell all the plugins where to inject votes
     */
    public void setInjectorInPlugins()
    {
        for (IPollPlugin ipp : _pollingPlugins)
        {
            ipp.setInjectable(_injectable);
        }
    }

    /**
     * Calls prepareVoting on all registered {@link IPollPlugin}s
     * 
     * @param lectureID
     *            used LectureId
     * @param question
     *            QuestionModel to be used
     * @see IPollPlugin#prepareVoting(LectureID, QuestionModel)
     */
    public void prepareVoting(LectureID lectureID, final QuestionModel question)
    {
        for (IPollPlugin ipp : _pollingPlugins)
        {
            ipp.prepareVoting(lectureID, question);
        }
    }

    /**
     * Tells all Plugins to stop polling and injecting votes
     */
    public void stopPolling()
    {
        for (IPollPlugin ipp : _pollingPlugins)
        {
            ipp.stopPolling();
        }
    }

    /**
     * Tells all Plugins to start polling and injecting votes
     */
    public void startPolling()
    {
        for (IPollPlugin ipp : _pollingPlugins)
        {
            ipp.startPolling();
        }
    }

}
