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
package sturesy.voting;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import sturesy.core.Localize;
import sturesy.core.Log;
import sturesy.core.backend.filter.filechooser.QuestionSetFileFilter;
import sturesy.core.ui.loaddialog.AbstractQuestionLoadDialogController;
import sturesy.core.ui.loaddialog.LabeledCombobox;
import sturesy.core.ui.renderer.LectureIdListRenderer;
import sturesy.items.LectureID;
import sturesy.items.QuestionSet;
import sturesy.items.SingleChoiceQuestion;
import sturesy.util.Settings;
import sturesy.util.web.WebVotingHandler;

/**
 * 
 * @author w.posdorfer
 * 
 */
public class VotingLoadDialog extends AbstractQuestionLoadDialogController
{
    private JButton _spontaneousButton;
    private LabeledCombobox<LectureID> _labeledCombobox;
    private Collection<LectureID> _lectureIds;
    private File _lecturesDirectory;

    private static final short MINIMUMANSWERS = 2;
    private static final short MAXIMUMANSWERS = 10;

    public VotingLoadDialog(Collection<LectureID> lectureIds, File lecturesDirectory)
    {
        super(new QuestionSetFileFilter(), lecturesDirectory);
        _lectureIds = lectureIds;
        _lecturesDirectory = lecturesDirectory;

        _labeledCombobox = new LabeledCombobox<LectureID>("label.lecture.id", getLectures(),
                new LectureIdListRenderer());
        _spontaneousButton = new JButton(Localize.getString("label.spontaneous"));
        addExtraLoadDialogButton(_spontaneousButton);
        addLabeledCombobox(_labeledCombobox);
        createLectureList();
        registerListeners();
    }

    private void spontaneousAction(JButton source)
    {
        final JPopupMenu jpop = new JPopupMenu();

        for (int i = MINIMUMANSWERS; i <= MAXIMUMANSWERS; i++)
        {
            final int ans = i;
            JMenuItem jmen = new JMenuItem(i + " " + Localize.getString("label.answers"));
            jmen.addActionListener(new ActionListener()
            {

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    QuestionSet qset = new QuestionSet();

                    SingleChoiceQuestion qmodel = new SingleChoiceQuestion();
                    ArrayList<String> arrlist = new ArrayList<String>();
                    for (int j = 0; j < ans; j++)
                    {
                        arrlist.add(Localize.getString("label.answer") + " " + (char) ('A' + j));
                    }
                    qmodel.setAnswers(arrlist);
                    qset.addQuestionModel(qmodel);
                    File f = new File(_lecturesDirectory + "/spontaneous.xml");
                    if (!f.exists())
                    {
                        try
                        {
                            f.createNewFile();
                        }
                        catch (IOException e1)
                        {
                            Log.error("error creating spontanous.xml", e1);
                        }
                    }
                    setLoadedFile(f);
                    setLoadedQuestionSet(qset);
                    jpop.setVisible(false);
                    closeLoadDialog();
                }
            });
            jpop.add(jmen);
        }
        setLoadedDialogNonModal();

        jpop.show(source, source.getX(), source.getY());
    }

    private Vector<LectureID> getLectures()
    {
        Vector<LectureID> result = new Vector<LectureID>();

        Collection<LectureID> ids = _lectureIds;
        String adress = Settings.getInstance().getString(Settings.SERVERADDRESS);
        for (LectureID id : ids)
        {
            if (id.getHost().toString().equals(adress))
            {
                result.add(id);
            }
        }

        Collections.sort(result, new Comparator<LectureID>()
        {
            @Override
            public int compare(LectureID o1, LectureID o2)
            {
                return o1.getLectureID().compareTo(o2.getLectureID());
            }
        });

        result.add(0, WebVotingHandler.NOLECTUREID);
        return result;
    }

    /**
     * Returns the Selected Lecture-ID
     */
    public LectureID getLectureID()
    {
        return _labeledCombobox.getSelectedItem();
    }

    public void show()
    {
        showLoadDialog();
    }

    private void registerListeners()
    {
        _spontaneousButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                spontaneousAction((JButton) e.getSource());
            }
        });
        _labeledCombobox.addKeyListener(new KeyAdapter()
        {
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    _loadDialog.loadInternalFile();
                }
            }
        });
    }
}
