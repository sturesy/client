package sturesy.feedback;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    private JSONArray _sheet;
    private JSONObject _feedbackToSheet;

    public FeedbackViewer() {
        _questionList = new DefaultListModel<>();
        _userList = new DefaultListModel<>();

        _gui = new FeedbackViewerUI(_questionList, _userList);
        _settings = Settings.getInstance();

        _sheet = new JSONArray();
        _feedbackToSheet = new JSONObject();

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

    private void loadButtonAction() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a feedback file to load");
        fileChooser.setFileFilter(new FeedbackFileFilter());

        int selection = fileChooser.showOpenDialog(_gui);

        // load sheet & feedback from file, afterwards populate the ui
        if(selection == JFileChooser.APPROVE_OPTION) {
            try {
                String data = new String(Files.readAllBytes(Paths.get(fileChooser.getSelectedFile().getAbsolutePath())));
                JSONObject input = new JSONObject(data);

                if (input.has("sheet") && input.has("feedback")) {
                    _sheet = input.getJSONArray("sheet");
                    _feedbackToSheet = input.getJSONObject("feedback");

                    _questionList.clear();
                    _userList.clear();

                    populateFeedbackQuestionList(_sheet);
                    populateFeedbackUserList(_feedbackToSheet);
                }
                else
                    JOptionPane.showMessageDialog(_gui, "File contains no feedback data.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (JSONException e) {
                JOptionPane.showMessageDialog(_gui, "Invalid file format.");
            }
        }
    }

    private void saveButtonAction() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("feedback.json"));
        fileChooser.setDialogTitle("Where do you want me to save the Feedback Sheet?");
        fileChooser.setFileFilter(new FeedbackFileFilter());
        int selection = fileChooser.showSaveDialog(_gui);

        // write sheet to file
        if(selection == JFileChooser.APPROVE_OPTION) {
            try {
                PrintWriter out = new PrintWriter(fileChooser.getSelectedFile());

                JSONObject output = new JSONObject();
                output.put("sheet", _sheet);
                output.put("feedback", _feedbackToSheet);

                out.write(output.toString());
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void downloadButtonAction() {
        LectureID lid = CommonDialogs.showLectureSelection();
        if (lid != null) {
            _sheet = WebCommands2.downloadFeedbackSheet(lid.getHost().toString(),
                    lid.getLectureID(), lid.getPassword());
            _feedbackToSheet = WebCommands2.downloadFeedback(lid.getHost().toString(),
                    lid.getLectureID(), lid.getPassword());

            if(_sheet != null && _feedbackToSheet != null) {
                populateFeedbackQuestionList(_sheet);
                populateFeedbackUserList(_feedbackToSheet);
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
                        userToResponses.put(guid, new FeedbackViewerUserEntry());
                    }
                    userToResponses.get(guid).setReponse(fbid, input);
                }
            }
        }
        // populate list in GUI with extracted data
        int count = 0;
        for(FeedbackViewerUserEntry ue : userToResponses.values()) {
            ue.setId(++count);
            _userList.addElement(ue);
        }
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
        userPanel.setBorder(BorderFactory.createTitledBorder("Feedback from user " + user.toString()));
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

            //responsePanel.add(new JLabel("Description: " + mo.getDescription()));

            // "choice" items can have multiple responses stored in a json array
            if(mo.getType().startsWith("choice")) {
                JSONArray responses = new JSONArray(user.getResponseForFeedbackId(id));

                String resp = "";
                for(int i = 0; i < responses.length(); i++) {
                    resp += (i > 0 ? ", " : "") + responses.getString(i);
                }
                responsePanel.add(new JLabel(resp));
            } else
                responsePanel.add(new JLabel(user.getResponseForFeedbackId(id)));

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
        questionPanel.setLayout(new BorderLayout());

        // get top panel
        JPanel topPanel = _gui.createQuestionDataPanel(fb);

        // panel containing responses
        JPanel responsePanel = new JPanel(new GridBagLayout());

        // fill horizontally
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = GridBagConstraints.RELATIVE;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.anchor = GridBagConstraints.PAGE_START;
        cons.weightx = 1;

        // summary ui widgets and data
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new GridLayout(0, 2));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("<html><i>Summary<i>"));

        Map<String, Integer> responseCounts = new HashMap<>();
        responsePanel.add(summaryPanel, cons);

        // keeps track of submissions/selections
        int singleSubmissionCount = 0; // used to calculate percentages
        int totalSubmissionCount = 0; // used to display total submissions

        // get feedback for this question
        for(Object obj : _userList.toArray()) {
            FeedbackViewerUserEntry ue = (FeedbackViewerUserEntry)obj;

            String response = ue.getResponseForFeedbackId(fb.getId());
            if(response != null) {
                // populate panel with response by current user
                JPanel responseContainer = new JPanel();
                responseContainer.setLayout(new BoxLayout(responseContainer, BoxLayout.Y_AXIS));
                responseContainer.setBorder(BorderFactory.createTitledBorder("User " + ue.getId()));
                responsePanel.add(responseContainer, cons);

                // aggregate the multiple responses (in case of a "choice" question)
                if(fb.getType().startsWith("choice")) {
                    JSONArray responses = new JSONArray(response);
                    JLabel responseLabel = new JLabel();
                    for (int idx = 0; idx < responses.length(); idx++) {
                        String append = (idx > 0 ? ", " : "") + responses.getString(idx);
                        responseLabel.setText(responseLabel.getText() + append);

                        // gather summary data for all responses if "choice" question
                        String selectedChoice = responses.getString(idx);

                        if (responseCounts.containsKey(selectedChoice)) {
                            responseCounts.replace(selectedChoice, responseCounts.get(selectedChoice) + 1);
                        } else
                            responseCounts.put(selectedChoice, 1);
                        singleSubmissionCount++;
                    }
                    responseContainer.add(responseLabel);
                }
                // in case the question is not of the type "choice"
                else {
                    responseContainer.add(new JLabel(response));
                    singleSubmissionCount++;
                }
                totalSubmissionCount++;

            }
        }
        summaryPanel.add(new JLabel("Total Submissions: " + totalSubmissionCount));
        summaryPanel.add(new JPanel()); // add empty panel to match grid

        // add count of selections and percentage to summary panel
        for(String choice : responseCounts.keySet()) {
            int count = responseCounts.get(choice);

            JPanel voteDetailPanel = new JPanel();
            voteDetailPanel.add(new JLabel(choice + ": " + count + " vote(s)"));

            JProgressBar progressBar = new JProgressBar(0, singleSubmissionCount);
            progressBar.setStringPainted(true);
            progressBar.setValue(count);
            voteDetailPanel.add(progressBar);

            summaryPanel.add(new JLabel(choice + ": " + count + " vote(s)"));
            summaryPanel.add(progressBar);
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

        _gui.getLoadButton().addActionListener(a -> loadButtonAction());
        _gui.getSaveButton().addActionListener(a -> saveButtonAction());
        _gui.getDownloadButton().addActionListener(a -> downloadButtonAction());
        _gui.getQuestionList().addListSelectionListener(this::questionSelected);
        _gui.getUserList().addListSelectionListener(this::userSelected);
    }

    public JFrame getFrame() {
        return _gui;
    }
}
