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
package hitt;

import gnu.io.CommPortIdentifier;
import hitt.HittPolling.Delegate;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import sturesy.core.error.ErrorController;
import sturesy.core.plugin.Injectable;
import sturesy.core.plugin.proxy.PLoader;
import sturesy.core.plugin.proxy.PSettings;
import sturesy.core.ui.JGap;
import sturesy.items.SingleChoiceQuestion;
import sturesy.items.Vote;

public class HittSettingsComponent extends JPanel implements Injectable, Delegate
{

    private static final Insets ZEROINSETS = new Insets(0, 0, 0, 0);

    private static final int DEFAULT_BAUDRATE = 19200;

    private static final long serialVersionUID = 3330055618523478170L;

    private JTextArea _baudRate = new JTextArea();
    private JTextArea _deviceName = new JTextArea();

    private JCheckBox _pluginEnabled;

    private JComboBox _devicecombobox;

    private JLabel _success = new JLabel();

    private HittPolling _polling;

    private ImageIcon _loadIcon = new ImageIcon(MainEntry.PATHOFPLUGIN + "/load.gif");

    private JButton _testbutton;

    public HittSettingsComponent()
    {

        String devicename = PSettings.getString(HittSettings.SETTINGS_DEVICE);

        int baud = PSettings.getInteger(HittSettings.SETTINGS_BAUD);
        // Set baudrate if not set
        baud = baud == -1 ? DEFAULT_BAUDRATE : baud;

        setUpUI(devicename, baud);

        registerListeners();

        if (PSettings.getBoolean(HittSettings.SETTINGS_ENABLED))
        {
            _pluginEnabled.doClick();
        }
        else
        {
            _pluginEnabled.doClick();
            _pluginEnabled.doClick();
        }
    }

    private void setUpUI(String devicename, int baud)
    {
        setLayout(new GridBagLayout());

        _pluginEnabled = new JCheckBox(Localizer.getString("enabled"));

        JLabel baudRateLabel = new JLabel(Localizer.getString("baudrate"));
        _baudRate.setDocument(new IntegerDocument());
        _baudRate.setText(Integer.toString(baud));
        _baudRate.setPreferredSize(new Dimension(350, _baudRate.getPreferredSize().height));

        JLabel deviceNameLabel = new JLabel(Localizer.getString("devicename"));
        _deviceName.setText(devicename);
        _deviceName.setPreferredSize(new Dimension(350, _deviceName.getPreferredSize().height));

        JLabel availableDevices = new JLabel(Localizer.getString("available.device"));
        @SuppressWarnings("unchecked")
        Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();

        Vector<String> items = new Vector<String>();
        while (portEnum.hasMoreElements())
        {
            CommPortIdentifier portIdentifier = portEnum.nextElement();

            items.add(portIdentifier.getName());
        }
        _devicecombobox = new JComboBox(items);
        _devicecombobox.setSelectedItem(devicename);
        _testbutton = new JButton(Localizer.getString("test.device"));
        _testbutton.setToolTipText(Localizer.getString("tooltip.test.device"));

        int ycounter = 0;

        add(_pluginEnabled, new GridBagConstraints(0, ycounter, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, ZEROINSETS, 0, 0));

        add(new JGap(10), new GridBagConstraints(0, ++ycounter, 1, 1, 1.0, 0.1, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, ZEROINSETS, 0, 0));

        add(baudRateLabel, new GridBagConstraints(0, ++ycounter, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, ZEROINSETS, 0, 0));
        add(_baudRate, new GridBagConstraints(1, ycounter, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, ZEROINSETS, 0, 0));

        add(new JGap(2), new GridBagConstraints(0, ++ycounter, 1, 1, 1.0, 0.05, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, ZEROINSETS, 0, 0));

        add(deviceNameLabel, new GridBagConstraints(0, ++ycounter, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, ZEROINSETS, 0, 0));
        add(_deviceName, new GridBagConstraints(1, ycounter, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, ZEROINSETS, 0, 0));

        add(new JGap(2), new GridBagConstraints(0, ++ycounter, 1, 1, 1.0, 0.05, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, ZEROINSETS, 0, 0));

        add(availableDevices, new GridBagConstraints(0, ++ycounter, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, ZEROINSETS, 0, 0));
        add(_devicecombobox, new GridBagConstraints(1, ycounter, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, ZEROINSETS, 0, 0));

        add(new JGap(2), new GridBagConstraints(0, ++ycounter, 1, 1, 1.0, 0.05, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, ZEROINSETS, 0, 0));

        add(_testbutton, new GridBagConstraints(0, ++ycounter, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, ZEROINSETS, 0, 0));
        add(_success, new GridBagConstraints(1, ycounter, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, ZEROINSETS, 0, 0));

        add(new JGap(10), new GridBagConstraints(0, ++ycounter, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, ZEROINSETS, 0, 0));

    }

    /**
     * Returns the Selected Device Name
     * 
     * @return returns COM1 on Windows and dev/cu.usb1237123 on mac/linux
     */
    public String getDeviceName()
    {
        return _deviceName.getText();
    }

    /**
     * Returns the selected BaudRate as Text
     * 
     * @return
     */
    public String getBaudRateText()
    {
        return _baudRate.getText();
    }

    /**
     * Returns the selected BaudRate like {@value #DEFAULT_BAUDRATE}
     * 
     * @return baudrate for device
     */
    public int getBaudRate()
    {
        return Integer.parseInt(_baudRate.getText());
    }

    /**
     * is this plugin enabled?
     * 
     * @return true if enabled
     */
    public boolean getPluginEnabled()
    {
        return _pluginEnabled.isSelected();
    }

    /**
     * Action performed on Check Box Selection
     * 
     * @param e
     *            the actionevent which triggered this
     */
    private void pluginEnabledCheckBoxAction(ActionEvent e)
    {
        boolean selected = _pluginEnabled.isSelected();
        for (Component c : this.getComponents())
        {
            if (c != _pluginEnabled)
            {
                c.setEnabled(selected);
            }
        }
    }

    @Override
    public void injectVote(Vote vote)
    {
        _success.setText(Localizer.getString("success"));
        _success.setIcon(PLoader.getImageIconResized(PLoader.IMAGE_OK, 32, 32, 0));
        _polling.stopPolling();
    }

    /**
     * Stops the current Polling
     */
    public void stopPolling()
    {
        if (_polling != null)
        {
            _polling.stopPolling();
            _polling = null;
        }
    }

    /**
     * Start polling to see if the device is a hitt-receiver
     */
    private void testPolling()
    {

        stopPolling();

        _polling = new HittPolling(getDeviceName(), Integer.parseInt(_baudRate.getText()));
        _polling.setInjectable(this);

        _polling._errorDelegate = this;

        _polling.prepareVoting(null, new SingleChoiceQuestion());

        _success.setText(Localizer.getString("waiting"));
        _success.setIcon(_loadIcon);

        _polling.startPolling();
    }

    private void registerListeners()
    {
        _devicecombobox.addItemListener(new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                _deviceName.setText((String) _devicecombobox.getSelectedItem());
                _success.setText("");
                _success.setIcon(null);
            }
        });
        _pluginEnabled.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                pluginEnabledCheckBoxAction(e);
            }
        });
        _testbutton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                testPolling();
            }
        });
    }

    @Override
    public void reportException(Exception ex)
    {
        _success.setIcon(PLoader.getImageIconResized(PLoader.IMAGE_RED, 32, 32, 0));
        _success.setText("Error");
        ErrorController con = new ErrorController();
        con.insertError("H-iTT Plugin Error", ex);
        con.show();
    }

}
