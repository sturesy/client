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
package sturesy.qgen.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import sturesy.core.Localize;
import sturesy.core.Log;
import sturesy.core.backend.Loader;
import sturesy.core.backend.services.crud.QuestionCRUDService;
import sturesy.core.ui.MessageWindow;
import sturesy.items.QuestionSet;

/**
 * Class for Saving new internal Questions
 * 
 * @author w.posdorfer
 * 
 */
public class QuestionSaveWindowUI extends JDialog
{
    private static final long serialVersionUID = -7455962909632633756L;

    private JList _lecturelist;
    private DefaultListModel _lecturelistmodel;

    private JTextField _titleField;

    private QuestionSet _questionSet;

    private File _savedFile = null;

    private final File _lecturesDirectory;

    public QuestionSaveWindowUI(QuestionSet set, File lecturesDirectory)
    {
        _lecturesDirectory = lecturesDirectory;
        setIconImage(Loader.getImageIcon(Loader.IMAGE_STURESY).getImage());
        setTitle(Localize.getString("label.save.question.set"));
        _questionSet = set;
        _lecturelistmodel = new DefaultListModel();
        _lecturelist = new JList(_lecturelistmodel);

        for (File f : lecturesDirectory.listFiles())
        {
            if (f.isDirectory())
                _lecturelistmodel.addElement(f.getName());
        }

        _lecturelist.setSelectedIndex(0);

        JScrollPane lecturelistscrollpane = new JScrollPane(_lecturelist);
        lecturelistscrollpane.setBorder(new TitledBorder(Localize.getString("label.lecture")));

        _titleField = new JTextField();
        _titleField.setPreferredSize(new Dimension(300, _titleField.getPreferredSize().height + 25));
        _titleField.setBorder(new TitledBorder(Localize.getString("label.title.questionset")));

        setLayout(new BorderLayout());

        JPanel southpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JPanel southwestpanel = new JPanel();

        JButton plusButton = new JButton("+");
        JButton minusButton = new JButton("-");
        southwestpanel.add(plusButton);
        southwestpanel.add(minusButton);

        JButton cancelButton = new JButton(Localize.getString("button.cancel"));

        JButton saveButton = new JButton(Localize.getString("button.save"));
        southpanel.add(cancelButton);
        southpanel.add(saveButton);

        JPanel westpanel = new JPanel(new BorderLayout());
        westpanel.add(lecturelistscrollpane, BorderLayout.CENTER);
        westpanel.add(southwestpanel, BorderLayout.SOUTH);
        add(westpanel, BorderLayout.WEST);

        JPanel titlefieldpanel = new JPanel();
        titlefieldpanel.add(_titleField);
        add(titlefieldpanel, BorderLayout.CENTER);
        add(southpanel, BorderLayout.SOUTH);

        plusButton.addActionListener(getPlusButtonAction());
        minusButton.addActionListener(getMinusButtonAction());
        saveButton.addActionListener(getSaveButtonAction());
        cancelButton.addActionListener(getCancelButtonAction());

        this.pack();
        setModal(true);
    }

    private ActionListener getCancelButtonAction()
    {

        return new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                QuestionSaveWindowUI.this.dispose();
            }
        };
    }

    /**
     * Adds a new Lecture to the maindirectory
     */
    private ActionListener getPlusButtonAction()
    {
        return new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                String str = JOptionPane.showInputDialog(QuestionSaveWindowUI.this,
                        Localize.getString("label.provide.lecture.name"), Localize.getString("label.lecture"), 1);

                File f = new File(_lecturesDirectory + "/" + str);

                if (str == null)
                    return;

                while (f.exists())
                {
                    str = JOptionPane.showInputDialog(QuestionSaveWindowUI.this,
                            Localize.getString("label.provide.lecture.name.another", str),
                            Localize.getString("label.lecture.random.name"), 1);

                    if (str == null)
                        return;

                    f = new File(_lecturesDirectory + "/" + str);
                }
                f.mkdir();
                _lecturelistmodel.addElement(f.getName());
                _lecturelist.setSelectedValue(f.getName(), true);

            }
        };
    }

    /**
     * Removes selected Lecture from Directory, also deleting all of its
     * contents
     */
    private ActionListener getMinusButtonAction()
    {
        return new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                int result = JOptionPane.showConfirmDialog(QuestionSaveWindowUI.this,
                        Localize.getString("label.confirm.deletion"),
                        Localize.getString("label.title.confirm.deletion"), JOptionPane.YES_NO_OPTION);

                if (result == JOptionPane.YES_OPTION)
                {
                    String name = (String) _lecturelist.getSelectedValue();

                    File deleteFile = new File(_lecturesDirectory + "/" + name);
                    if (deleteFile.exists())
                        deleteFile.delete();
                    _lecturelistmodel.removeElement(_lecturelist.getSelectedValue());
                    _lecturelist.setSelectedIndex(0);
                }
            }
        };
    }

    /**
     * Saves the given Questionset by its name and selected Lecture, also
     * closing this window
     */
    private ActionListener getSaveButtonAction()
    {
        return new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {

                if (_lecturelist.getSelectedIndex() == -1)
                {
                    MessageWindow.showMessageWindowError(Localize.getString("error.select.lecture"), 1500);
                    return;
                }
                if (_titleField.getText().length() == 0)
                {
                    MessageWindow.showMessageWindowError(Localize.getString("error.input.name"), 1500);
                    return;
                }

                String title = _titleField.getText().endsWith(".xml") ? _titleField.getText() : _titleField.getText()
                        + ".xml";
                _savedFile = new File(_lecturesDirectory + "/" + _lecturelist.getSelectedValue() + "/" + title);

                try
                {
                    QuestionCRUDService questionWriter = new QuestionCRUDService();
                    questionWriter.createAndUpdateQuestionSet(_savedFile, _questionSet);
                    MessageWindow.showMessageWindowSuccess(Localize.getString("message.save.questionset"), 1500);
                }
                catch (Exception e1)
                {
                    MessageWindow.showMessageWindow(Localize.getString("error.save.questionset"), 2000, Color.RED);
                    Log.error("Error saving Questionset", e1);
                }

                WindowEvent wev = new WindowEvent(QuestionSaveWindowUI.this, WindowEvent.WINDOW_CLOSING);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);

            }
        };
    }

    public File getFile()
    {
        return _savedFile;
    }
}
