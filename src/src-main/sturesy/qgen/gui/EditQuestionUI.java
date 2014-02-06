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
package sturesy.qgen.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import sturesy.core.Localize;
import sturesy.core.ui.answerPanel.GrayLabel;

/**
 * GUI for EditPanel
 * 
 * @author w.posdorfer
 * 
 */
public class EditQuestionUI
{

    private final JPanel _mainpanel = new JPanel();

    private JTextArea _questionField;

    private JTextField _durationField = new JTextField(new RealIntegerDocument(), "", 6);

    private JButton _resetButton;
    private JButton _htmlButton;

    private JPanel _swapableAnswerPanel;

    private TitledBorder _border;

    public EditQuestionUI(JPanel answerPanel)
    {
        _swapableAnswerPanel = new JPanel(new BorderLayout());
        _swapableAnswerPanel.add(answerPanel, BorderLayout.CENTER);

        _questionField = new JTextArea();
        JScrollPane textscrollpane = new JScrollPane(_questionField);
        final Dimension minSize = new Dimension(300, 60);
        final Dimension maxSize = new Dimension(2000, 250);
        textscrollpane.setMinimumSize(minSize);
        textscrollpane.setMaximumSize(maxSize);
        // textscrollpane.setPreferredSize(minSize);
        _questionField.setMinimumSize(minSize);
        _questionField.setMaximumSize(maxSize);

        _htmlButton = new JButton("<html>&lt;html&gt;</html>");
        _htmlButton.setFont(_htmlButton.getFont().deriveFont(10f));

        JLabel questionLabel = new GrayLabel(Localize.getString("label.questiontext") + ":");

        _resetButton = new JButton(Localize.getString("button.reset"));

        JPanel durationpanel = new JPanel();
        durationpanel.add(new GrayLabel(Localize.getString("label.duration")));
        durationpanel.add(_durationField);
        durationpanel.add(new GrayLabel(Localize.getString("label.duration.seconds")));

        JPanel centerpanel = new JPanel(new MigLayout("flowx,align left", "[][grow,fill][]", "[grow]"));

        centerpanel.add(questionLabel, "alignx left, aligny center");
        centerpanel.add(textscrollpane);
        centerpanel.add(_htmlButton, "wrap");
        centerpanel.add(_swapableAnswerPanel, "span 3, wrap, growx");

        JPanel soutpanel = new JPanel(new MigLayout("flowx, align left", "[grow]"));
        soutpanel.add(durationpanel, "wrap");
        soutpanel.add(_resetButton, "align right");
        centerpanel.add(soutpanel, "span 3, dock south");

        JScrollPane mainscrollpane = new JScrollPane(centerpanel);
        _mainpanel.setLayout(new BorderLayout());
        _mainpanel.add(mainscrollpane, BorderLayout.CENTER);

        _border = new TitledBorder(" ");
        _mainpanel.setBorder(_border);
        setToolTips();
    }

    private void setToolTips()
    {
        _durationField.setToolTipText(Localize.getString("tool.questiongen.duration"));
        _resetButton.setToolTipText(Localize.getString("tool.questiongen.reset"));
        _htmlButton.setToolTipText(Localize.getString("tool.questiongen.html"));
    }

    public void setBorderTitle(String s)
    {
        _border.setTitle(s);
    }

    public void setEnabled(boolean enabled)
    {
        _mainpanel.setEnabled(enabled);
        _questionField.setEditable(enabled);
        setEnabled(_mainpanel, enabled);
        setEnabled(_durationField.getParent(), enabled);
        setEnabled(_resetButton.getParent(), enabled);
    }

    private void setEnabled(Container cont, boolean enabled)
    {
        for (Component c : cont.getComponents())
        {
            c.setEnabled(enabled);
            if (c instanceof Container)
            {
                setEnabled((Container) c, enabled);
            }
        }
    }

    public boolean isEnabled()
    {
        return _mainpanel.isEnabled();
    }

    public void replaceAnswerPanel(JPanel newPanel)
    {
        _swapableAnswerPanel.removeAll();
        _swapableAnswerPanel.add(newPanel, BorderLayout.CENTER);
        _mainpanel.revalidate();
        _mainpanel.repaint();
    }

    public JPanel getJPanel()
    {
        return _mainpanel;
    }

    public JButton getResetButton()
    {
        return _resetButton;
    }

    public JTextArea getQuestionField()
    {
        return _questionField;
    }

    public JTextField getDurationField()
    {
        return _durationField;
    }

    public String getDurationFieldText()
    {
        return _durationField.getText();
    }

    public JButton getHTMLButton()
    {
        return _htmlButton;
    }
}
