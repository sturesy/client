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
package sturesy;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

import sturesy.core.Localize;
import sturesy.core.backend.Loader;
import sturesy.util.Version;

/**
 * Graphical Component of the MainScreen
 * 
 * @author w.posdorfer
 * 
 */
public class MainScreenUI extends JFrame
{

    private static final long serialVersionUID = -5798737880047902334L;

    private JButton _settings;
    private JButton _evaluate;
    private JButton _question;
    private JButton _voting;
    private JButton _feedbacksheet;
    private JButton _livefeedback;

    public MainScreenUI()
    {
        setTitle("StuReSy " + Version.CURRENTVERSION);
        setIconImage(Loader.getImageIcon(Loader.IMAGE_STURESY).getImage());

        _settings = new JButton(Localize.getString(Localize.SETTINGS), Loader.getImageIcon(Loader.IMAGE_SETTINGS));
        _evaluate = new JButton(Localize.getString(Localize.VOTINGANALYSIS), Loader.getImageIcon(Loader.IMAGE_EVALUATE));
        _question = new JButton(Localize.getString(Localize.QUESTIONEDITOR),
                Loader.getImageIcon(Loader.IMAGE_QUESTIONEDITOR));
        _voting = new JButton(Localize.getString(Localize.VOTING), Loader.getImageIcon(Loader.IMAGE_VOTING));
        _feedbacksheet = new JButton("Feedback-Sheets", Loader.getImageIcon(Loader.IMAGE_FEEDBACK));
        _livefeedback = new JButton("Live-Feedback", Loader.getImageIcon(Loader.IMAGE_FEEDBACK_LIVE));
        
        _voting.setHorizontalAlignment(SwingConstants.LEFT);
        _question.setHorizontalAlignment(SwingConstants.LEFT);
        _evaluate.setHorizontalAlignment(SwingConstants.LEFT);
        _settings.setHorizontalAlignment(SwingConstants.LEFT);

        _settings.setToolTipText(Localize.getString("tool.settings"));
        _evaluate.setToolTipText(Localize.getString("tool.analysis"));
        _question.setToolTipText(Localize.getString("tool.questiongen"));
        _voting.setToolTipText(Localize.getString("tool.vote"));

        setLayout(new GridLayout(2, 2));

        add(_voting);
        add(_evaluate);
        add(_question);
        add(_feedbacksheet);
        add(_livefeedback);
        add(_settings);
    }

    public JButton getSettings()
    {
        return _settings;
    }

    public JButton getEvaluate()
    {
        return _evaluate;
    }

    public JButton getQuestion()
    {
        return _question;
    }

    public JButton getVoting()
    {
        return _voting;
    }
    
    public JButton getFeedbackSheet()
    {
    	return _feedbacksheet;
    }

    public JButton getLiveFeedback() {
        return _livefeedback;
    }
}
