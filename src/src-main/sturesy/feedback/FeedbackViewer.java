package sturesy.feedback;

import org.json.JSONArray;
import org.json.JSONObject;
import sturesy.core.Controller;
import sturesy.feedback.gui.FeedbackViewerUI;
import sturesy.items.LectureID;
import sturesy.items.feedback.AbstractFeedbackType;
import sturesy.items.feedback.FeedbackTypeMapping;
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

    private DefaultListModel<AbstractFeedbackType> _questionList;
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
            AbstractFeedbackType mo = FeedbackTypeMapping.instantiateAndInitializeWithJson(jobj);
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
            AbstractFeedbackType mo = (AbstractFeedbackType)lsm.getSelectedValue();

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
        userPanel.setLayout(new GridBagLayout());

        JScrollPane scrollPane = new JScrollPane(userPanel);
        scrollPane.setViewportBorder(null);

        // fill horizontally
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = GridBagConstraints.RELATIVE;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.anchor = GridBagConstraints.PAGE_START;
        cons.weightx = 1;

        // gather user feedback and add it to the UI
        for (int id : user.getFeedbackIDs()) {
            AbstractFeedbackType mo = getFeedbackModelForId(id);

            JPanel responsePanel = new JPanel();
            responsePanel.setLayout(new BoxLayout(responsePanel, BoxLayout.Y_AXIS));
            responsePanel.setBorder(BorderFactory.createTitledBorder(mo.getTitle()));

            responsePanel.add(new JLabel("Description: " + mo.getDescription()));
            responsePanel.add(new JLabel("Response: " + user.getResponseForFeedbackId(id)));
            userPanel.add(responsePanel, cons);
        }

        // fill up remaining space with greedy invisible panel
        cons.weighty = 1;
        cons.fill = GridBagConstraints.BOTH;
        userPanel.add(new JPanel(), cons);

        _gui.setRightPanel(scrollPane);
    }

    private AbstractFeedbackType getFeedbackModelForId(int id)
    {
        for(Object obj : _questionList.toArray()) {
            AbstractFeedbackType mo = (AbstractFeedbackType)obj;
            if(mo.getId() == id)
                return mo;
        }
        return null;
    }

    private void showFeedbackForQuestion(AbstractFeedbackType fb)
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
        JPanel responsePanel = new JPanel(new GridBagLayout());
        responsePanel.setBorder(BorderFactory.createTitledBorder("Responses"));

        // fill horizontally
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = GridBagConstraints.RELATIVE;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.anchor = GridBagConstraints.PAGE_START;
        cons.weightx = 1;

        // get feedback for this question
        for(Object obj : _userList.toArray()) {
            FeedbackViewerUserEntry ue = (FeedbackViewerUserEntry)obj;

            String response = ue.getResponseForFeedbackId(fb.getId());
            if(response != null) {
                JPanel responseContainer = new JPanel();
                responseContainer.setLayout(new BoxLayout(responseContainer, BoxLayout.Y_AXIS));

                responseContainer.setBorder(BorderFactory.createTitledBorder("User: " + ue.getUserId()));
                responseContainer.add(new JLabel("Response: " + response));

                responsePanel.add(responseContainer, cons);
            }
        }

        // fill up remaining space with greedy invisible panel
        cons.weighty = 1;
        cons.fill = GridBagConstraints.BOTH;
        responsePanel.add(new JPanel(), cons);

        // add question details to top
        questionPanel.add(topPanel, BorderLayout.PAGE_START);

        // wrap responses in JScrollPane
        JScrollPane scrollPane = new JScrollPane(responsePanel);
        scrollPane.setViewportBorder(null);
        //scrollPane.setBorder(BorderFactory.createEmptyBorder());
        questionPanel.add(scrollPane, BorderLayout.CENTER);

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
