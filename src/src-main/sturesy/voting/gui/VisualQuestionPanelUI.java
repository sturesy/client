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
package sturesy.voting.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import sturesy.core.Localize;
import sturesy.core.ui.HTMLLabel;
import sturesy.core.ui.MessageWindow;
import sturesy.core.ui.VerticalLayout;
import sturesy.util.Settings;

/**
 * The graphical unit for the VisualQuestionPanel
 * 
 * @author w.posdorfer
 */
public class VisualQuestionPanelUI
{
    public static final int ERROR_MESSAGE_WINDOW_DURATION = 2000;

    /**
     * Questionlabel
     */
    private JLabel _question;
    /**
     * Arraylist of JLabels containing the answertexts
     */
    private ArrayList<DoubleLabel> _answerLabels;
    /**
     * Centerpanel
     */
    private JPanel _centerpanel;

    private Color _color;

    /**
     * Create a new link VisualQuestionPanelUI
     */
    public VisualQuestionPanelUI()
    {
        _answerLabels = new ArrayList<DoubleLabel>();
        _question = new JLabel();

        _centerpanel = new JPanel();
        _centerpanel.setLayout(new VerticalLayout(VerticalLayout.DEFAULT_GAP, VerticalLayout.BOTH,
                VerticalLayout.CENTER));

        _color = Settings.getInstance().getColor("color.voting.main");
    }

    /**
     * Clears the Panel and sets the Layout<br>
     * <br>
     * Call this before {@link #addQuestionLabel(String, Font, MouseListener)}
     */
    public void clearPanel()
    {
        _centerpanel.removeAll();
    }

    /**
     * Adds a new QuestionLabel given the Text <br>
     * <br>
     * Call this after {@link #clearPanel()} and before
     * {@link #addAnswers(ArrayList, Font, MouseListener)}
     * 
     * @param text
     *            text to display
     * @param fontsize
     *            fontsize
     * @param mouselis
     *            mouseListener to attach to labels
     */
    public void addQuestionLabel(String text, float fontsize, MouseListener mouselis)
    {
        _question = new HTMLLabel(text);
        _question.addMouseListener(mouselis);
        _question.setFont(_question.getFont().deriveFont(fontsize));
        _centerpanel.add(_question);
        _centerpanel.setBackground(_color);
    }

    /**
     * Adds Answerlabels specified by a Stringlist, Font and MouseListener<br>
     * <br>
     * Call this after {@link #addQuestionLabel(String, Font, MouseListener)}
     * 
     * @param answers
     *            Answers
     * @param fontsize
     *            fontsize to use
     * @param mouselis
     *            MouseListener to attach to labels
     */
    public void addAnswers(List<String> answers, float fontsize, MouseListener mouselis)
    {
        _answerLabels.clear();
        for (int i = 0; i < answers.size(); i++)
        {
            DoubleLabel label = new DoubleLabel((char) ('A' + i) + ": ", answers.get(i));
            label.setBackground(_color);
            
            label.setFont(label.getLeft().getFont().deriveFont(fontsize));
            _centerpanel.add(label);
            label.addMouseListener(mouselis);
            _answerLabels.add(label);
        }
    }

    /**
     * Refreshes the graphical user interface
     */
    public void refreshUI()
    {
        _centerpanel.repaint();
        _centerpanel.revalidate();
    }

    /**
     * Returns the Center Panel of this Component
     * 
     * @return JPanel
     */
    public JPanel getCenterPanel()
    {
        return _centerpanel;
    }

    /**
     * Shows an error message window for 2 seconds
     * 
     * @param ressourceKey
     *            Resource key from the i18n-Files, like "
     *            <code>error.error</code>"
     */
    public void showMessageWindowError(String ressourceKey)
    {
        MessageWindow.showMessageWindowError(Localize.getString(ressourceKey), ERROR_MESSAGE_WINDOW_DURATION);
    }
}
