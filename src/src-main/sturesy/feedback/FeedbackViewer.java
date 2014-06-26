package sturesy.feedback;

import org.json.JSONArray;
import org.json.JSONObject;
import sturesy.core.Controller;
import sturesy.feedback.gui.FeedbackViewerUI;
import sturesy.items.LectureID;
import sturesy.items.feedback.FeedbackTypeMapping;
import sturesy.items.feedback.FeedbackTypeModel;
import sturesy.util.CommonDialogs;
import sturesy.util.Settings;
import sturesy.util.web.WebCommands2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Iterator;

/**
 * Displays Feedback submitted to the StuReSy Server
 * Created by henrik on 6/22/14.
 */
public class FeedbackViewer implements Controller {
    private final Settings _settings;
    private FeedbackViewerUI _gui;

    public FeedbackViewer() {
        _gui = new FeedbackViewerUI();
        _settings = Settings.getInstance();

        addListeners();
    }

    @Override
    public void displayController(Component relativeTo, WindowListener listener) {
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

    private void downloadButtonAction() {
        LectureID lid = CommonDialogs.showLectureSelection();
        if (lid != null) {
            JSONArray sheet = WebCommands2.downloadFeedbackSheet(lid.getHost().toString(),
                    lid.getLectureID().toString(), lid.getPassword());
            JSONObject fb = WebCommands2.downloadFeedback(lid.getHost().toString(),
                    lid.getLectureID().toString(), lid.getPassword());

            if(sheet != null && fb != null) {
                _gui.getFeedbackResults().removeAll();
                for (int i = 0; i < sheet.length(); i++) {
                    JSONObject jobj = sheet.getJSONObject(i);
                    FeedbackTypeModel mo = FeedbackTypeMapping.instantiateAndInitializeWithJson(jobj);
                    if(mo != null) {

                        JPanel panel = new JPanel();
                        panel.setLayout(new GridLayout(0, 1));
                        panel.setBorder(BorderFactory.createTitledBorder(jobj.getString("title")));

                        panel.add(new JLabel("Description: " + mo.getDescription()));
                        panel.add(new JLabel("Responses: " + fb.getJSONArray(String.valueOf(jobj.getInt("fbid")))));

                        panel.setMaximumSize(panel.getPreferredSize());

                        _gui.getFeedbackResults().add(panel);
                    }
                }
                _gui.getFeedbackResults().revalidate();
            }
            else
                JOptionPane.showMessageDialog(_gui, "No feedback or feedback sheet available for given lecture.");
        }
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

        _gui.getDownloadButton().addActionListener(a -> downloadButtonAction());
    }

    public JFrame getFrame() {
        return _gui;
    }
}
