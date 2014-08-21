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
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Displays Feedback submitted to the StuReSy Server
 * Created by henrik on 6/22/14.
 */
public class FeedbackViewer implements Controller {
    private final Settings _settings;
    private FeedbackViewerUI _gui;

    private DefaultListModel<FeedbackTypeModel> _questionList;
    private DefaultListModel<FeedbackViewerUserEntry> _userList;

    public FeedbackViewer() {
        _questionList = new DefaultListModel<>();
        _userList = new DefaultListModel<>();

        _gui = new FeedbackViewerUI(_questionList, _userList);
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
                    lid.getLectureID(), lid.getPassword());
            JSONObject fb = WebCommands2.downloadFeedback(lid.getHost().toString(),
                    lid.getLectureID(), lid.getPassword());

            if(sheet != null && fb != null) {
                populateFeedbackQuestionList(sheet);
                populateFeedbackUserList(fb);
            }
            else
                JOptionPane.showMessageDialog(_gui, "No feedback or feedback sheet available for given lecture.");
        }
    }

    private void populateFeedbackQuestionList(JSONArray sheet) {
        // delete previous data
        _questionList.clear();

        // fill view with feedback sheet
        for (int i = 0; i < sheet.length(); i++) {
            JSONObject jobj = sheet.getJSONObject(i);
            FeedbackTypeModel mo = FeedbackTypeMapping.instantiateAndInitializeWithJson(jobj);
            if(mo != null)
                _questionList.addElement(mo);
        }
    }

    private void populateFeedbackUserList(JSONObject fb) {
        // delete previous data
        _userList.clear();

        Map<String, FeedbackViewerUserEntry> userToResponses = new HashMap<>();
        // map feedback data to userIDs
        for (Object keyObj : fb.keySet()) {
            if(keyObj instanceof String) {
                String key = (String)keyObj;
                JSONArray responses = fb.getJSONArray(key);

                // iterate through responses for current feedback item
                for(int i = 0; i < responses.length(); i++) {
                    JSONObject response = responses.getJSONObject(i);
                    String guid = response.getString("guid");
                    String input = response.getString("response");
                    int fbid = Integer.valueOf(key);

                    if(!userToResponses.containsKey(guid)) {
                        userToResponses.put(guid, new FeedbackViewerUserEntry(guid));
                    }
                    userToResponses.get(guid).setReponse(fbid, input);
                }
            }
        }
        // populate list in GUI with extracted data
        userToResponses.values().forEach(_userList::addElement);
    }

    /**
     * Called, when a question has been selected
     * @param e triggered ListSelectionEvent
     */
    private void questionSelected(ListSelectionEvent e)
    {
        JList lsm = (JList)e.getSource();
        if(!lsm.isSelectionEmpty()) {
            _gui.getUserList().clearSelection();
            FeedbackTypeModel mo = (FeedbackTypeModel)lsm.getSelectedValue();

            showFeedbackForQuestion(mo);
        }
    }

    /**
     * Called, when a user has been selected
     * @param e triggered ListSelectionEvent
     */
    private void userSelected(ListSelectionEvent e)
    {
        JList lsm = (JList)e.getSource();
        if(!lsm.isSelectionEmpty()) {
            _gui.getQuestionList().clearSelection();
            FeedbackViewerUserEntry ue = (FeedbackViewerUserEntry)lsm.getSelectedValue();

            showFeedbackFromUser(ue);
        }
    }

    private void showFeedbackFromUser(FeedbackViewerUserEntry user)
    {
        JPanel userPanel = new JPanel();
        userPanel.setBorder(BorderFactory.createTitledBorder("Feedback from " + user.toString()));
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));

        for (int id : user.getFeedbackIDs()) {
            FeedbackTypeModel mo = getFeedbackModelForId(id);

            JPanel responsePanel = new JPanel();
            responsePanel.setBorder(BorderFactory.createTitledBorder(mo.getTitle()));
            responsePanel.setLayout(new BoxLayout(responsePanel, BoxLayout.Y_AXIS));

            responsePanel.add(new JLabel("Description: " + mo.getDescription()));
            responsePanel.add(new JLabel("Response: " + user.getResponseForFeedbackId(id)));
            userPanel.add(responsePanel);
        }

        _gui.setRightPanel(userPanel);
    }

    private FeedbackTypeModel getFeedbackModelForId(int id)
    {
        for(Object obj : _questionList.toArray()) {
            FeedbackTypeModel mo = (FeedbackTypeModel)obj;
            if(mo.getId() == id)
                return mo;
        }
        return null;
    }

    private void showFeedbackForQuestion(FeedbackTypeModel fb)
    {
        JPanel questionPanel = new JPanel();
        questionPanel.setBorder(BorderFactory.createTitledBorder(fb.getTitle()));
        questionPanel.setLayout(new BorderLayout());

        JLabel descLabel = new JLabel("Description: " + fb.getDescription());
        JCheckBox mandatoryCheck = new JCheckBox("Response is mandatory", fb.isMandatory());
        mandatoryCheck.setEnabled(false);

        // panel containing question data
        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createTitledBorder("Question Data"));
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(descLabel);
        topPanel.add(mandatoryCheck);

        // panel containing responses
        JPanel responsePanel = new JPanel();
        responsePanel.setBorder(BorderFactory.createTitledBorder("Responses"));

        // get feedback for this question
        for(Object obj : _userList.toArray()) {
            FeedbackViewerUserEntry ue = (FeedbackViewerUserEntry)obj;

            String response = ue.getResponseForFeedbackId(fb.getId());
            if(response != null) {
                responsePanel.add(new JLabel(ue.getUserId() + ": " + response));
            }
        }

        questionPanel.add(topPanel, BorderLayout.PAGE_START);
        questionPanel.add(responsePanel, BorderLayout.CENTER);

        _gui.setRightPanel(questionPanel);
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
        _gui.getQuestionList().addListSelectionListener(this::questionSelected);
        _gui.getUserList().addListSelectionListener(this::userSelected);
    }

    public JFrame getFrame() {
        return _gui;
    }
}
