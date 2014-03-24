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
package sturesy.qgen;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import net.atlanticbb.tantlinger.shef.HTMLEditorPane;
import sturesy.core.Localize;

/**
 * Controller to display a SHEF-Editor in a JFrame
 * 
 * @author w.posdorfer
 * 
 */
public class ShefHTML
{

    private static final String HTML_START = "<html>";
    private static final String HTML_END = "</html>";
    private final JFrame _frame;
    private final HTMLEditorPane _htmlpane;
    private final JButton _save;
    private JTextComponent _textcomponent;

    /**
     * Creates a new ShefHTML Controller
     * 
     * @param textcomponent
     *            the textcomponent where text should be placed in
     * @param listener
     *            a windowlistener to attach
     */
    public ShefHTML(JTextComponent textcomponent, WindowListener listener)
    {
        _textcomponent = textcomponent;

        _frame = new JFrame();
        _htmlpane = new HTMLEditorPane();
        _save = new JButton(Localize.getString("button.save"));

        _frame.setLayout(new BorderLayout());
        _frame.add(_htmlpane, BorderLayout.CENTER);

        JPanel southpanel = new JPanel();
        southpanel.add(_save);
        _frame.add(southpanel, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(_htmlpane.getEditMenu());
        menuBar.add(_htmlpane.getFormatMenu());
        menuBar.add(_htmlpane.getInsertMenu());
        _frame.setJMenuBar(menuBar);

        _frame.setTitle("HTML");

        _frame.addWindowListener(listener);

        registerListeners();

        if (_textcomponent.getText().length() != 0)
        {
            _htmlpane.setText(_textcomponent.getText());
        }

        _frame.setSize(650, 400);
        _frame.setLocationRelativeTo(null);
        _frame.setVisible(true);
    }

    /**
     * Registers listeners
     */
    private void registerListeners()
    {
        _save.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String text = _htmlpane.getText();

                if (!text.startsWith(HTML_START))
                {
                    text = HTML_START + text;
                }
                if (!text.endsWith(HTML_END))
                {
                    text = text + HTML_END;
                }

                _textcomponent.setText(text);

                WindowEvent wev = new WindowEvent(_frame, WindowEvent.WINDOW_CLOSING);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
            }
        });
    }
}