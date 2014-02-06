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

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import sturesy.core.Localize;
import sturesy.core.backend.Loader;

/**
 * UI for UpdateDownloader
 * 
 * @author w.posdorfer
 * 
 */
public class UpdateDownloaderUI
{

    private static final Insets ZEROINSETS = new Insets(0, 0, 0, 0);
    private JFrame _frame;
    private JProgressBar _progressBar;
    private JButton _cancelButton;
    private JLabel _label;
    private JButton _restartButton;
    private JLabel _activity;

    public UpdateDownloaderUI()
    {
        _frame = new JFrame(Localize.getString("label.download"));
        _frame.setIconImage(Loader.getImageIcon(Loader.IMAGE_STURESY).getImage());
        _label = new JLabel("<html> <br> </html>");
        _progressBar = new JProgressBar();
        _progressBar.setStringPainted(true);
        _cancelButton = new JButton(Localize.getString("button.cancel"));
        _restartButton = new JButton(Localize.getString("button.restart"));
        _restartButton.setEnabled(false);

        JPanel southpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southpanel.add(_restartButton);
        southpanel.add(_cancelButton);

        _frame.setLayout(new GridBagLayout());

        _activity = new JLabel(Loader.getImageIcon(Loader.IMAGE_GREEN_LOAD));

        _frame.add(_activity, new GridBagConstraints(0, 0, 1, 3, 1, 1, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, ZEROINSETS, 0, 0));

        _frame.add(_label, new GridBagConstraints(1, 0, 1, 1, 3, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                ZEROINSETS, 0, 0));
        _frame.add(_progressBar, new GridBagConstraints(1, 1, 1, 1, 3, 1, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 10), 0, 0));
        _frame.add(southpanel, new GridBagConstraints(1, 2, 1, 1, 3, 1, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, ZEROINSETS, 0, 0));

        _frame.setSize(400, 165);
    }

    /**
     * Sets the Value for the Progressbar
     * 
     * @param n
     *            value (0 - 100)
     */
    public void progressBarSetValue(int n)
    {
        _progressBar.setValue(n);
    }

    /**
     * Sets the Text for the progressbar
     * 
     * @param s
     *            text
     */
    public void setTextProgressBar(String s)
    {
        _progressBar.setString(s);
    }

    /**
     * Sets the Text of the label, will be wrapped in html-tags
     * 
     * @param text
     */
    public void setTextLabel(String text)
    {
        _label.setText("<html>" + text + "</html>");
    }

    /**
     * Sets the Icon of the ActivityLabel
     * 
     * @param icon
     *            Icon must be 32x32 px
     */
    public void setIconActivityLabel(Icon icon)
    {
        _activity.setIcon(icon);
    }

    /**
     * Returns the CancelButton
     * 
     * @return JButton
     */
    public JButton getCancelButton()
    {
        return _cancelButton;
    }

    /**
     * Returns the RestartButton
     * 
     * @return JButton
     */
    public JButton getRestartButton()
    {
        return _restartButton;
    }

    /**
     * Returns the JFrame
     * 
     * @return JFrame
     */
    public JFrame getFrame()
    {
        return _frame;
    }
}
