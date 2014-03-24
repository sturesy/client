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
package sturesy.voting;

import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import sturesy.core.backend.services.crud.QuestionCRUDService;
import sturesy.items.QuestionModel;
import sturesy.items.QuestionSet;
import sturesy.util.BackgroundWorker;
import sturesy.voting.gui.BiggerSmallerMouseAdapter;
import sturesy.voting.gui.VisualQuestionPanelUI;

/**
 * Class to display a Question and its associated Answers, it also provides a
 * {@link JPopupMenu} for increasing/decreasing the font of answers and question
 * 
 * @author w.posdorfer
 * 
 */
public class VisualQuestionPanel
{

    private static final int PLUSFONT = 5;
    private QuestionModel _question;
    private String _lectureFile;
    private final QuestionSet _questionSet;
    private VisualQuestionPanelUI _gui;
    private final QuestionCRUDService _questionWriter;

    /**
     * Creates a VisualQuestionPanel
     * 
     * @param questionModel
     *            QuestionModel to be displayed by this Controller
     * @param lectureFile
     *            File from which the QuestionModel was loaded
     * @param questionSet
     *            QuestionSet in which the QuestionModel is located
     */
    public VisualQuestionPanel(QuestionModel questionModel, String lectureFile, QuestionSet questionSet)
    {
        this(new VisualQuestionPanelUI(), new QuestionCRUDService(), questionModel, lectureFile, questionSet);
    }

    /**
     * Creates a VisualQuestionPanel
     * 
     * @param ui
     *            UserInterface to be used by this Controller
     * @param questionWriter
     *            QuestionCRUDService to be used by this Controller
     * @param questionModel
     *            QuestionModel to be displayed by this Controller
     * @param lectureFile
     *            File from which the QuestionModel was loaded
     * @param questionSet
     *            QuestionSet in which the QuestionModel is located
     */
    public VisualQuestionPanel(VisualQuestionPanelUI ui, QuestionCRUDService questionWriter,
            QuestionModel questionModel, String lectureFile, QuestionSet questionSet)
    {
        _gui = ui;
        _questionWriter = questionWriter;
        _lectureFile = lectureFile;
        _question = questionModel;
        _questionSet = questionSet;
        setUp();
    }

    /**
     * Returns the user interface for this Controller
     */
    public JPanel getPanel()
    {
        return _gui.getCenterPanel();
    }

    private void setUp()
    {
        _gui.clearPanel();

        float qfont = _question.getQuestionFont();
        float afont = _question.getAnswerFont();
        
        _gui.addQuestionLabel(_question.getQuestion(), qfont, createFontMouseListener(true));
        _gui.addAnswers(_question.getAnswers(), afont, createFontMouseListener(false));
        _gui.getCenterPanel();
    }

    /**
     * Increases the size of the Question-font
     */
    public void increaseQuestionSize()
    {
        float f = _question.getQuestionFont();
        _question.setQuestionFont(f + PLUSFONT);
    }

    /**
     * Dencreases the size of the Question-font
     */
    public void decreaseQuestionSize()
    {
        float f = _question.getQuestionFont();
        _question.setQuestionFont(f - PLUSFONT);
    }

    /**
     * Increases the size of the Answer-font
     */
    public void increaseAnswerSize()
    {
        float f = _question.getAnswerFont();
        _question.setAnswerFont(f + PLUSFONT);
    }

    /**
     * Decreases the size of the Answer-font
     */
    public void decreaseAnswerSize()
    {
        float f = _question.getAnswerFont();
        _question.setAnswerFont(f - PLUSFONT);
    }

    /**
     * Saves the font-changes made to the QuestionModel
     */
    public void save()
    {
        new BackgroundWorker()
        {
            public void inBackground()
            {
                _questionWriter.createAndUpdateQuestionSet(_lectureFile, _questionSet);
            }
        }.execute();
    }

    /**
     * Refreshes the UI
     */
    public void refreshData()
    {
        setUp();
        _gui.refreshUI();
    }

    /**
     * Creates a MouseListener, which shows a JPopupMenu. The JPopupMenu has
     * entries for resizing the font of either the questiontext or the
     * answertext
     * 
     * @param isQuestion
     *            <code>true</code> increases/decreases the font for the
     *            QuestionLabel<br>
     *            <code>false</code> increases/decreases the font for the
     *            answerlabel
     * @return MouseListener
     */
    public MouseListener createFontMouseListener(final boolean isQuestion)
    {
        return new BiggerSmallerMouseAdapter()
        {
            protected void closePopup(JPopupMenu jpopmenu)
            {
                save();
                refreshData();
                jpopmenu.setVisible(false);
            }

            protected void increaseSize()
            {
                if (isQuestion)
                {
                    increaseQuestionSize();
                }
                else
                {
                    increaseAnswerSize();
                }
            }

            protected void decreaseSize()
            {
                if (isQuestion)
                {
                    decreaseQuestionSize();
                }
                else
                {
                    decreaseAnswerSize();
                }
            }
        };
    }
}
