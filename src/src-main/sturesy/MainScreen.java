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

import java.awt.AWTException;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.swing.JFrame;

import org.jfree.util.Log;

import sturesy.core.Localize;
import sturesy.core.Operatingsystem;
import sturesy.core.backend.Loader;
import sturesy.core.plugin.ISettingsScreen;
import sturesy.export.LectureIDExport;
import sturesy.feedback.FeedbackSheetEditor;
import sturesy.items.LectureID;
import sturesy.items.VotingSet;
import sturesy.qgen.QuestionEditor;
import sturesy.settings.SettingsController;
import sturesy.settings.mainsettings.MainSettingsListener;
import sturesy.update.UpdateChecker;
import sturesy.update.UpdateFrequency;
import sturesy.util.DateUtils;
import sturesy.util.Settings;
import sturesy.util.SturesyConstants;
import sturesy.voting.VotingController;
import sturesy.voting.VotingLoadDialog;
import sturesy.votinganalysis.AnalysisLoadDialog;
import sturesy.votinganalysis.VotingAnalysis;

/**
 * Mainscreen of the Application provides Access to Settings, Question Editor,
 * Voting and Voting Analysis
 * 
 * @author w.posdorfer
 * 
 */
public class MainScreen extends WindowAdapter
{
    private MainScreenUI _gui;

    private QuestionEditor _qgen;
    private SettingsController _settingswindow;
    private VotingController _votingwindow;
    private FeedbackSheetEditor _feedbacksheet;

    public MainScreen()
    {
    	init();

        checkForUpdates();
    }

    public void init()
    {
        _gui = new MainScreenUI();

        _gui.addWindowListener(getWindowListener());

        registerListeners();

        _gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        _gui.setMenuBar(createMenuBar());

        createTrayIcon();

        Macintosh.setUpDockMenu(createMenuItems());
        Macintosh.setUpQuitHandler(getExitAction());
    }

    private void checkForUpdates()
    {
        int difference = 1;

        Settings sett = Settings.getInstance();
        int frequency = sett.getInteger(Settings.UPDATE_FREQUENCY);

        if (frequency == SturesyConstants.UNDEFINEDINTEGER)
        {
            sett.setProperty(Settings.UPDATE_FREQUENCY, 1);
            frequency = 1;
        }
        switch (UpdateFrequency.valueOf(frequency))
        {
        case DAILY:
            difference = 1;
            break;
        case WEEKLY:
            difference = 7;
            break;
        case MONTHLY:
            difference = 30;
            break;
        case MANUALLY:
            difference = -1;
            break;
        }

        Date lastDate = sett.getDate(Settings.UPDATE_LASTUPDATE);
        Date today = Calendar.getInstance().getTime();

        if (lastDate == null)
        {
            sett.setProperty(Settings.UPDATE_LASTUPDATE, today);
            lastDate = today;
        }
        if (difference != -1 && difference < Math.abs(DateUtils.differenceInDays(lastDate, today)))
        {
            new UpdateChecker().checkForUpdate(true);
        }

    }

    /**
     * Returns the Main Frame
     * 
     * @return {@link JFrame}
     */
    public JFrame getJFrame()
    {
        return _gui;
    }

    private MenuItem[] createMenuItems()
    {
        MenuItem qgenmenu = new MenuItem(Localize.getString(Localize.QUESTIONEDITOR));
        MenuItem votingmenu = new MenuItem(Localize.getString(Localize.VOTING));
        MenuItem evaluatemenu = new MenuItem(Localize.getString(Localize.VOTINGANALYSIS));
        MenuItem settingsmenu = new MenuItem(Localize.getString(Localize.SETTINGS));

        qgenmenu.addActionListener(e -> getQuestionEditorAction());
        votingmenu.addActionListener(e -> getVotingAction());
        evaluatemenu.addActionListener(e -> getEvaluateAction());
        settingsmenu.addActionListener(e -> getSettingsAction());

        return new MenuItem[] { votingmenu, evaluatemenu, qgenmenu, settingsmenu };
    }

    /**
     * Creates the menubar for the mainscreen
     */
    private MenuBar createMenuBar()
    {
        MenuBar menubar = new MenuBar();
        Menu menu = new Menu(Localize.getString("menu.selection"));

        MenuItem[] items = createMenuItems();

        for (MenuItem item : items)
        {
            menu.add(item);
        }

        MenuItem update = new MenuItem(Localize.getString("label.update"));
        update.addActionListener(e -> new UpdateChecker().checkForUpdate(false));
        menu.add(update);

        if (!Operatingsystem.isMac())
        {
            MenuItem about = new MenuItem("About");
            about.addActionListener(e -> AboutMenuUI.showMenu());
            menu.add(about);
        }

        menubar.add(menu);
        return menubar;
    }

    private void createTrayIcon()
    {
        TrayIcon icon = new TrayIcon(Loader.getImageIcon(Loader.IMAGE_STURESY).getImage());
        icon.setImageAutoSize(true);

        PopupMenu menu = new PopupMenu();
        MenuItem mainscreen = new MenuItem(Localize.getString("main.screen"));
        menu.add(mainscreen);
        for (MenuItem item : createMenuItems())
        {
            menu.add(item);
        }
        MenuItem close = new MenuItem(Localize.getString("menu.close"));
        menu.add(close);

        icon.setPopupMenu(menu);
        try
        {
            SystemTray.getSystemTray().add(icon);
        }
        catch (AWTException e)
        {
            Log.error("Error setting SystemTrayicon", e);
        }
        mainscreen.addActionListener(e -> _gui.setVisible(true));
        close.addActionListener(e -> closeWindow());
    }

    private void closeWindow()
    {
        WindowEvent wev = new WindowEvent(_gui, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }

    /**
     * Shows the SettingsWindow
     */
    private void getSettingsAction()
    {
        if (_settingswindow == null)
        {
            String maindirectoryAbsolutePath = SturesyManager.getMainDirectory().getAbsolutePath();
            MainSettingsListener maindirectoryListener = new MainSettingsListener()
            {
                @Override
                public void maindirectoryChanged(String maindir)
                {
                    SturesyManager.setMainDirectory(maindir);
                }
            };
            Set<ISettingsScreen> settingscreens = SturesyManager.getSettingsScreens();
            Collection<LectureID> lectureIDs = SturesyManager.getLectureIDs();
            _settingswindow = new SettingsController(settingscreens, maindirectoryListener, maindirectoryAbsolutePath,
                    SturesyManager.getPluginsDirectory(), lectureIDs);

            _settingswindow.displayController(_gui, this);
            _gui.setVisible(false);

        }
        else
        {
            _settingswindow.getFrame().toFront();
        }
    }

    /**
     * Shows the Voting Evaluation Window
     */
    private void getEvaluateAction()
    {
        File lecturesDirectory = SturesyManager.getLecturesDirectory();
        AnalysisLoadDialog dialog = new AnalysisLoadDialog(lecturesDirectory);
        dialog.show();

        if (dialog.getLoadedQuestionSet() != null && dialog.getVotes() != null)
        {
            VotingAnalysis va = new VotingAnalysis(dialog.getLoadedQuestionSet(), dialog.getVotes());
            va.displayController(_gui, this);
        }
    }

    /**
     * Shows the Voting Window
     */
    private void getVotingAction()
    {
        if (_votingwindow == null)
        {
            VotingLoadDialog dialog = new VotingLoadDialog(SturesyManager.getLectureIDs(),
                    SturesyManager.getLecturesDirectory());
            dialog.show();

            if (dialog.getLoadedQuestionSet() != null)
            {
                _votingwindow = new VotingController(SturesyManager.getLoadedPollPlugins());
                _votingwindow.setQuestionSet(dialog.getLoadedQuestionSet(), dialog.getLectureID(),
                        dialog.getLoadedFileAbsolutePath(), new VotingSet());

                _votingwindow.displayController(_gui, this);
                _gui.setVisible(false);
            }
        }
        else
        {
            _votingwindow.getFrame().toFront();
        }
    }

    /**
     * Shows the Questioneditor Window
     */
    private void getQuestionEditorAction()
    {
        if (_qgen == null)
        {
            _qgen = new QuestionEditor(SturesyManager.getLecturesDirectory());
            _qgen.displayController(_gui, this);
            _gui.setVisible(false);
        }
        else
        {
            _qgen.getFrame().toFront();
        }
    }
    
    private void getFeedbackSheetEditorAction()
    {
    	if(_feedbacksheet == null)
    	{
    		_feedbacksheet = new FeedbackSheetEditor();
    		_feedbacksheet.displayController(_gui, this);
    		_gui.setVisible(false);
    	}
    	else
    	{
    		_feedbacksheet.getFrame().toFront();
    	}
    	
    }

    /**
     * save the settings before window closes
     */
    private WindowListener getWindowListener()
    {
        return new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                getExitAction().performAction();
            }
        };
    }

    private ExitAction getExitAction()
    {
        return new ExitAction()
        {
            public void performAction()
            {
                SturesyManager.getSettings().save();
                LectureIDExport.marshallLectureIDs(SturesyManager.getLectureIDs(), LectureIDExport.FILENAME);
            }
        };
    }

    /**
     * register Listeners for the JButtons of the Gui
     */
    private void registerListeners()
    {
        _gui.getSettings().addActionListener(e -> getSettingsAction());
        _gui.getVoting().addActionListener(e -> getVotingAction());
        _gui.getEvaluate().addActionListener(e -> getEvaluateAction());
        _gui.getQuestion().addActionListener(e -> getQuestionEditorAction());
        _gui.getFeeedbackSheet().addActionListener(e -> getFeedbackSheetEditorAction());
    }

    @Override
    public void windowClosing(WindowEvent e)
    {
        if (_qgen != null && e.getSource() == _qgen.getFrame())
        {
            _qgen = null;
        }
        else if (_votingwindow != null && e.getSource() == _votingwindow.getFrame())
        {
            _votingwindow = null;
        }
        else if (_settingswindow != null && e.getSource() == _settingswindow.getFrame())
        {
            _settingswindow = null;
        }
        else if (_feedbacksheet != null && e.getSource() == _feedbacksheet.getFrame())
        {
        	_feedbacksheet = null;
        }
        _gui.setVisible(true);
    }
}
