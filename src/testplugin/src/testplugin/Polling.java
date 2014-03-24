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
package testplugin;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sturesy.core.plugin.IPollPlugin;
import sturesy.core.plugin.Injectable;
import sturesy.core.plugin.QuestionVoteMatcher;
import sturesy.items.LectureID;
import sturesy.items.MultipleChoiceQuestion;
import sturesy.items.QuestionModel;
import sturesy.items.SingleChoiceQuestion;
import sturesy.items.Vote;
import sturesy.qgen.gui.RealIntegerDocument;

/**
 * Test polling
 * 
 * @author w.posdorfer
 * 
 */
public class Polling implements IPollPlugin
{

    private Injectable _injectable;
    private int _size = 0;

    private int _current = 0;

    private JFrame _frame;
    private long _startingTime = 0;
    private Random _random = new Random();
    private QuestionModel _model;
    
    
    private ArrayList<JButton> _buttons = new ArrayList<JButton>();
    private JTextField _field;

    public Polling()
    {
        _frame = new JFrame();
        _frame.setLayout(new BorderLayout());

        _field = new JTextField("5");
        _field.setDocument(new RealIntegerDocument());

        _frame.add(_field, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 5));
        _frame.add(grid, BorderLayout.CENTER);

        for (int i = 0; i < 10; i++)
        {
            final int iii = i;
            JButton button = new JButton("" + (char) (i + 'A'));
            _buttons.add(button);
            button.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    for(int i = 0; i < Integer.parseInt(_field.getText());i++)
                    {
                        add(iii);
                    }
                }
            });
            grid.add(button);
        }

        JButton button2 = new JButton("Add Random 50");
        button2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                for (int i = 0; i < 50; i++)
                {
                    add(-1);
                }
            }
        });
        _frame.add(button2, BorderLayout.SOUTH);
        _frame.pack();
    }

    @Override
    public void setInjectable(Injectable injectable)
    {
        _injectable = injectable;
    }

    @Override
    public void prepareVoting(LectureID lectureid, final QuestionModel model)
    {
        _model = model;
        _size = model.getAnswerSize();
    }

    @Override
    public void startPolling()
    {
        _startingTime = Calendar.getInstance().getTimeInMillis();
        _frame.setVisible(true);

    }

    @Override
    public void stopPolling()
    {
        _frame.setVisible(false);
        _current = 0;

    }

    private void add(int i)
    {
        String id = String.format("T%08d", _current);

        long timediff = Calendar.getInstance().getTimeInMillis() - _startingTime;

        Object[] params = null;

        if (_model instanceof SingleChoiceQuestion)
        {
            params = new Object[] { i == -1 ? _random.nextInt(_size) : i };
        }
        else if (_model instanceof MultipleChoiceQuestion)
        {
            short[] arr = new short[] { (short) _random.nextInt(_size) };
            params = new Object[] { arr };
        }
        else
        {
            params = new Object[] { "Answer" };
        }

        Vote v = QuestionVoteMatcher.instantiateVoteFor(_model, id, timediff, params);

        _injectable.injectVote(v);
        _current++;
    }
}
