package sturesy.feedback;

import sturesy.core.Controller;
import sturesy.feedback.gui.FeedbackViewerUI;
import sturesy.util.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by henrik on 6/22/14.
 */
public class FeedbackViewer implements Controller {
    private final Settings _settings;
    private FeedbackViewerUI _gui;

    public FeedbackViewer()
    {
        _gui = new FeedbackViewerUI();
        _settings = Settings.getInstance();

        addListeners();
    }

    @Override
    public void displayController(Component relativeTo, WindowListener listener)
    {
        _gui.setSize(_settings.getDimension(Settings.FEEDBACKVIEWERSIZE));
        _gui.setLocationRelativeTo(relativeTo);
        _gui.setVisible(true);
        _gui.addWindowListener(listener);
    }

    /**
     * Called when this Window is Closing, to save the size
     */
    private void windowIsClosing() {
        Settings settings = _settings;
        settings.setProperty(Settings.FEEDBACKVIEWERSIZE, _gui.getSize());
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
