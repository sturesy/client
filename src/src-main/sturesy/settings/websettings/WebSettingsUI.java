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
package sturesy.settings.websettings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.ui.IntegerDocument;

import sturesy.core.Localize;
import sturesy.core.Log;
import sturesy.core.backend.Loader;
import sturesy.core.plugin.ISettingsScreen;
import sturesy.core.ui.JGap;

/**
 * The Panel which gets displayed on {@link ISettingsScreen#getPanel()}
 * 
 * @author w.posdorfer
 * 
 */
public class WebSettingsUI extends JPanel
{
    private static final long serialVersionUID = 1862591371331599840L;
    private JTextField _frequencyField;

    private JButton _serverCorrect;
    private JTextField _serverField;

    private JCheckBox _webEnabled;

    private static final int ICONSIZE = 24;
    static final ImageIcon VALID = Loader.getImageIconResized(Loader.IMAGE_OK, ICONSIZE, ICONSIZE, Image.SCALE_FAST);
    static final ImageIcon INVALID = Loader.getImageIconResized(Loader.IMAGE_RED, ICONSIZE, ICONSIZE, Image.SCALE_FAST);
    static final ImageIcon LOADING = Loader.getImageIconResized(Loader.IMAGE_GREEN_LOAD, ICONSIZE, ICONSIZE,
            Image.SCALE_FAST);
    static final ImageIcon QUESTION = Loader.getImageIconResized(Loader.IMAGE_QUESTION, ICONSIZE, ICONSIZE,
            Image.SCALE_FAST);

    public WebSettingsUI(String serveraddress)
    {
        setLayout(new GridBagLayout());
        JLabel serverLabel = new JLabel(Localize.getString("label.server.adress"));

        _webEnabled = new JCheckBox(Localize.getString("label.web.plugin.enabled"));

        _serverCorrect = new JButton(INVALID);
        _serverCorrect.setSize(ICONSIZE, ICONSIZE);

        _serverField = new JTextField(serveraddress, 40);

        JLabel frequencyLabel = new JLabel(Localize.getString("label.frequency"));
        JLabel secondsLabel = new JLabel(Localize.getString("label.milliseconds"));
        _frequencyField = new JTextField();
        _frequencyField.setDocument(new IntegerDocument());

        add(_webEnabled, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        
        add(new JGap(10), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.1, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        add(serverLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        add(_serverField, new GridBagConstraints(1, 2, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        add(_serverCorrect, new GridBagConstraints(3, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        add(new JGap(10), new GridBagConstraints(0, 3, 1, 1, 1.0, 0.1, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        
        add(frequencyLabel, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        add(_frequencyField, new GridBagConstraints(1, 4, 1, 1, 0.4, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        add(secondsLabel, new GridBagConstraints(2, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        add(new JGap(10), new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        setupToolTips();
    }

    private void setupToolTips()
    {
        _webEnabled.setToolTipText(Localize.getString("tool.settings.websettings.enabled"));
        _serverField.setToolTipText(Localize.getString("tool.settings.websettings.serverfield"));
        _frequencyField.setToolTipText(Localize.getString("tool.settings.websettings.frequencyfield"));
        _serverCorrect.setToolTipText(Localize.getString("tool.settings.websettings.checkhost"));
    }

    /**
     * Returns the Server Adress
     */
    public String getServerAdress()
    {
        return _serverField.getText();
    }

    /**
     * Returns the Poll Frequency in Milliseconds
     */
    public int getPollFrequency()
    {
        int result = 0;

        try
        {
            result = Integer.parseInt(_frequencyField.getText());
        }
        catch (NumberFormatException nfe)
        {
            Log.error("error parsing frequency-field", nfe);
        }

        return result;
    }

    /**
     * Sets the PollFrequency text
     * 
     * @param s
     *            some text
     */
    public void setPollFrequencyText(String s)
    {
        _frequencyField.setText(s);
    }

    /**
     * Returns the Server-Address field
     * 
     * @return JTextField
     */
    public JTextField getServerField()
    {
        return _serverField;
    }

    /**
     * Returns the Server-Validation-Button
     * 
     * @return JButton
     */
    public JButton getServerCorrect()
    {
        return _serverCorrect;
    }

    /**
     * Returns the Plugin-Is-Enabled Checkbox
     * 
     * @return JCheckBox
     */
    public JCheckBox getWebEnabled()
    {
        return _webEnabled;
    }

    /**
     * Is the Webplugin enabled?
     */
    public boolean isWebPluginEnabled()
    {
        return _webEnabled.isSelected();
    }

    /**
     * Sets the Server-Valid icon depending on the input to either green or red
     * 
     * @param valid
     *            true = green , false = red
     */
    public void setServerIconEnabled(boolean valid)
    {
        _serverCorrect.setIcon(valid ? VALID : INVALID);
    }

}
