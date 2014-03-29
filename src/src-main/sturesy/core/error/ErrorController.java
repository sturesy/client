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
package sturesy.core.error;

import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;

import sturesy.core.Pair;

/**
 * A Controller to be notified about unsuccessfully loaded plugins, that
 * displays them in a list later
 * 
 * @author w.posdorfer
 * 
 */
public class ErrorController
{

    private ErrorUI _gui;
    private final Map<String, Throwable> _map;

    /**
     * Creates a new Controller
     */
    public ErrorController()
    {
        _map = new HashMap<String, Throwable>();
        _gui = new ErrorUI();
        registerListeners();

    }

    /**
     * Have errors occured?
     * 
     * @return <code>true</code> if errors have already occured
     */
    public boolean errorsOccured()
    {
        return _map.size() != 0;
    }

    /**
     * Tells the controller about a Component that caused an Error
     * 
     * @param name
     *            the Name or ClassName of the Plugin
     * @param t
     *            the reason why it wasn't loaded
     */
    public void insertError(String name, Throwable t)
    {
        _map.put(name, t);
    }

    /**
     * Shows the ErrorDialog in a modal dialog
     */
    public void show()
    {
        setUp();
        _gui.showDialog();
    }

    /**
     * Places the elements in the List
     * 
     * @param map
     */
    private void setUp()
    {
        DefaultListModel model = (DefaultListModel) _gui.getPluginsList().getModel();
        model.clear();
        for (String key : _map.keySet())
        {
            model.addElement(new Pair<String, String>(key, _map.get(key).getLocalizedMessage()));
        }
    }

    /**
     * Registers all Listeners
     */
    private void registerListeners()
    {
        _gui.getOkButton().addActionListener(e -> _gui.hideDialog());
    }
}
