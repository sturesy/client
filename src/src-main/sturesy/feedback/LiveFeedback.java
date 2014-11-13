package sturesy.feedback;

import org.json.JSONArray;
import org.json.JSONObject;
import sturesy.core.Controller;
import sturesy.core.ui.NotificationService;
import sturesy.feedback.gui.LiveFeedbackUI;
import sturesy.items.LectureID;
import sturesy.util.CommonDialogs;
import sturesy.util.Settings;
import sturesy.util.web.WebCommands2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Frontend to activate and process live-feedback
 * Created by henrik on 8/16/14.
 */
public class LiveFeedback implements Controller {
    // TODO: make these changeable in settings menu
    private static final long POLLING_TIME = 2;
    private static final TimeUnit POLLING_TIMEUNIT = TimeUnit.SECONDS;

    private LiveFeedbackUI _gui;
    private final Settings _settings;

    private final NotificationService notificationService;

    private boolean liveActive = false;
    private LectureID selectedLecture;
    private final ScheduledExecutorService scheduler;
    private ScheduledFuture<?> pollingTaskHandle;

    public LiveFeedback() {
        _gui = new LiveFeedbackUI();
        _settings = Settings.getInstance();
        _gui.setFontSizeOffset(_settings.getInteger(Settings.LIVEFEEDBACKFONTSIZEOFFSET));
        notificationService = NotificationService.getInstance();
        scheduler = Executors.newScheduledThreadPool(1);

        addListeners();
        initNotificationService();
    }

    private void initNotificationService() {
        String position = _settings.getString(Settings.NOTIFICATION_POSITION);
        int screen = _settings.getInteger(Settings.NOTIFICATION_SCREEN);

        notificationService.setPosition(position);
        notificationService.setScreen(screen);
    }

    /**
     * Displays the Controller
     *
     * @param relativeTo display it relative to this component
     * @param listener window listener
     */
    @Override
    public void displayController(Component relativeTo, WindowListener listener) {
        _gui.setSize(_settings.getDimension(Settings.LIVEFEEDBACKSIZE));
        _gui.setLocationRelativeTo(relativeTo);
        _gui.setVisible(true);
        _gui.addWindowListener(listener);
    }

    /**
     * Called when this Window is Closing, to save the size
     */
    private void windowIsClosing() {
        Settings settings = _settings;
        settings.setProperty(Settings.LIVEFEEDBACKSIZE, _gui.getSize());
        settings.setProperty(Settings.LIVEFEEDBACKFONTSIZEOFFSET, _gui.getFontSizeOffset());
        settings.save();

        if(liveActive) {
            WebCommands2.setLiveFeedbackState(selectedLecture.getHost().toString(),
                    selectedLecture.getLectureID(), selectedLecture.getPassword(), false);
            stopPolling();
        }
    }

    /**
     * Called when the Start/Stop Button is pressed.
     * Needs to enable live-feedback and initiate polling
     */
    private void startStopButton() {
        if(!liveActive) {
            selectedLecture = CommonDialogs.showLectureSelection();
            if (selectedLecture != null) {
                if(WebCommands2.setLiveFeedbackState(selectedLecture.getHost().toString(),
                        selectedLecture.getLectureID(), selectedLecture.getPassword(), true)) {
                    liveActive = true;
                    startPolling();
                }
            }
        }
        else {
            WebCommands2.setLiveFeedbackState(selectedLecture.getHost().toString(),
                    selectedLecture.getLectureID(), selectedLecture.getPassword(), false);
            liveActive = false;
            stopPolling();
        }


    }

    /**
     * Called when polling is to be started
     */
    private void startPolling() {
        _gui.getStartStopButton().setText("Stop Live-Feedback");
        pollingTaskHandle = scheduler.scheduleAtFixedRate(pollingTask, 0, POLLING_TIME, POLLING_TIMEUNIT);

    }

    /**
     * Called when polling is to be stopped
     */
    private void stopPolling() {
        _gui.getStartStopButton().setText("Start Live-Feedback");
        pollingTaskHandle.cancel(true);
    }

    /**
     * The task that is responsible for polling the server as well as fetching new messages
     */
    private Runnable pollingTask = new Runnable() {
        @Override
        public void run() {
            JSONArray messages = WebCommands2.getLiveFeedback(selectedLecture.getHost().toString(),
                    selectedLecture.getLectureID(), selectedLecture.getPassword());

            for (int i = 0; i < messages.length(); i++) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                JSONObject msg = messages.getJSONObject(i);

                // extract data from json response
                String name = msg.getString("name");
                String subject = msg.getString("subject");
                String message = msg.getString("message");

                // parse to java.util.Date, use current date in case parsing fails
                Date date = new Date();
                try {
                    date = dateFormat.parse(msg.getString("date"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                _gui.addMessage(name, subject, message, date);

                if(_gui.getNotificationCheckBox().isSelected())
                    notificationService.addNotification(name + ": " + (subject.length() > 0 ? subject : "<no subject>"),
                            message, 10);
            }
        }
    };

    /**
     * Adds listeners to all the GUI-Elements
     */
    private void addListeners() {
        _gui.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                windowIsClosing();
            }

            // this fixes delayed processing of edt events on some platforms (e.g. linux)
            // in this case: force processing received messages while the window was hidden
            @Override
            public void windowDeiconified(WindowEvent e) {
                super.windowDeiconified(e);
                _gui.repaint();
            }
        });

        _gui.getStartStopButton().addActionListener(l -> startStopButton());
    }

    public JFrame getFrame() {
        return _gui;
    }
}
