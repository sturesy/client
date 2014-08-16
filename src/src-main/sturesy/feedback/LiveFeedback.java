package sturesy.feedback;

import sturesy.core.Controller;
import sturesy.feedback.gui.LiveFeedbackUI;
import sturesy.util.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Frontend to activate and process live-feedback
 * Created by henrik on 8/16/14.
 */
public class LiveFeedback implements Controller {
    private LiveFeedbackUI _gui;
    private final Settings _settings;

    public LiveFeedback() {
        _gui = new LiveFeedbackUI();
        _settings = Settings.getInstance();

        addListeners();
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
        settings.save();
    }

    /**
     * Adds listeners to all the GUI-Elements
     */
    private void addListeners() {
        _gui.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                windowIsClosing();
            }
        });
    }

    public JFrame getFrame() {
        return _gui;
    }
}
