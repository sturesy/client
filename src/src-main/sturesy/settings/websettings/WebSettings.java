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
package sturesy.settings.websettings;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sturesy.core.Localize;
import sturesy.core.backend.Loader;
import sturesy.core.plugin.ISettingsScreen;
import sturesy.items.LectureID;
import sturesy.util.BackgroundWorker;
import sturesy.util.Settings;
import sturesy.util.web.WebCommands2;

/**
 * Websettings like Host, Pollfrequency
 * 
 * @author w.posdorfer
 * 
 */
public class WebSettings implements ISettingsScreen
{
    private String _name;
    private ImageIcon _icon;

    private JTabbedPane _component;
    private WebSettingsUI _gui;
    private JPanel _passwordComponent;
    private Settings _settings = Settings.getInstance();

    private BackgroundWorker _checkHost;

    /**
     * Creates a new WebSettings-Panel
     * 
     * @param lectureId
     *            A Collection of LectureIDs
     */
    public WebSettings(Collection<LectureID> lectureId)
    {

        _name = Localize.getString(Localize.WEBSETTINGS);
        _icon = Loader.getImageIcon(Loader.IMAGE_WEB);
        _component = new JTabbedPane();
        _gui = new WebSettingsUI(_settings.getString(Settings.SERVERADDRESS));
        _passwordComponent = new WebSettingsPassword(lectureId).getPanel();
        _component.add(getName(), _gui);
        _component.add(Localize.getString("web.settings.tab.password"), _passwordComponent);

        initWebSettingsUI();
        registerListeners();
    }

    /**
     * Initialize some components in the UI
     */
    private void initWebSettingsUI()
    {
        boolean enabled = _settings.getBoolean(Settings.WEB_PLUGIN_ENABLED);
        _gui.getWebEnabled().setSelected(enabled);
        webEnabledStateChanged();

        int x = _settings.getInteger(Settings.POLL_FREQUENCY);

        if (x == -1)
        {
            x = 5000;
            _settings.setProperty(Settings.POLL_FREQUENCY, x);
        }

        _gui.setPollFrequencyText(String.valueOf(x));

    }

    @Override
    public String getName()
    {
        return _name;
    }

    @Override
    public ImageIcon getIcon()
    {
        return _icon;
    }

    @Override
    public Component getPanel()
    {
        return _component;
    }

    @Override
    public void saveSettings() throws MalformedURLException
    {
        String clientaddress = "";
        String serveraddress = "";
        try
        {
            serveraddress = getServerAdress().toString();
            clientaddress = serveraddress.replace("relay.php", "index.php");
            _gui.getServerField().setText(serveraddress);
        }
        catch (MalformedURLException e)
        {
            throw new MalformedURLException(Localize.getString("error.malformed.url.websettings"));
        }

        _settings.setProperty(Settings.SERVERADDRESS, serveraddress);
        _settings.setProperty(Settings.CLIENTADDRESS, clientaddress);
        _settings.setProperty(Settings.POLL_FREQUENCY, _gui.getPollFrequency());
        _settings.setProperty(Settings.WEB_PLUGIN_ENABLED, _gui.isWebPluginEnabled());

    }

    /**
     * Checks if the Host is a valid response server
     */
    private void checkHost()
    {
        if (_gui.isWebPluginEnabled() && _checkHost == null || _checkHost.isDone() || _checkHost.isPending())
        {
            setupBackgroundWorker();
            _checkHost.execute();
        }
    }

    private URL getServerAdress() throws MalformedURLException
    {
        String url = _gui.getServerAdress();

        if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://"))
        {
            url = "http://" + url;
        }
        if (!url.endsWith("/") && !url.endsWith(".php"))
        {
            url += "/relay.php";
        }
        if (url.endsWith("/"))
        {
            url += "relay.php";
        }

        return new URL(url);
    }

    /**
     * Toggles all components in the Gui to enabled/disabled depending on the
     * status of the checkbox
     */
    private void webEnabledStateChanged()
    {
        for (Component c : _gui.getComponents())
        {
            if (c != _gui.getWebEnabled())
            {
                c.setEnabled(_gui.isWebPluginEnabled());
            }
        }
    }

    /**
     * Register KeyListeners
     */
    private void registerListeners()
    {
        _gui.getServerCorrect().addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                checkHost();
            }
        });
        _gui.getServerField().addKeyListener(new KeyAdapter()
        {
            public void keyReleased(KeyEvent e)
            {
                _gui.getServerCorrect().setIcon(WebSettingsUI.QUESTION);
            }
        });
        _gui.getWebEnabled().addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                webEnabledStateChanged();
            }
        });
    }

    private void setupBackgroundWorker()
    {
        _checkHost = new BackgroundWorker()
        {
            public void inBackground()
            {
                boolean validresult = false;

                _gui.getServerCorrect().setIcon(WebSettingsUI.LOADING);

                try
                {
                    URL url = getServerAdress();

                    String result = WebCommands2.getInfo(url.toString());
                    validresult = result.startsWith("sturesy") && result.length() < 80
                            && result.matches("sturesy [0-9\\.]*");
                }
                catch (Exception e)
                {
                    validresult = false;
                }
                _gui.setServerIconEnabled(validresult);
            }
        };

    }

}
