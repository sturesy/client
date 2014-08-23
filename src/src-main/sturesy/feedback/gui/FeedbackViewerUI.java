package sturesy.feedback.gui;

import sturesy.core.backend.Loader;
import sturesy.core.ui.SFrame;

import javax.swing.*;
import java.awt.*;

/**
 * UI components of the Feedback Viewer
 * Created by henrik on 6/22/14.
 */
public class FeedbackViewerUI extends SFrame {

    private JButton downloadButton;

    private JSplitPane splitPane;

    private JList questionList;
    private JList userList;

    public FeedbackViewerUI(DefaultListModel questionListModel, DefaultListModel userListModel)
    {
        super();
        setTitle("Feedback Viewer");
        setIconImage(Loader.getImageIcon(Loader.IMAGE_STURESY).getImage());

        // Button Panel containing download/upload/etc. buttons
        JPanel buttonPanel = createButtonPanel();

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        questionList = new JList(questionListModel);
        questionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList = new JList(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane questionListScrollPane = new JScrollPane(questionList);
        JScrollPane userListScrollPane = new JScrollPane(userList);

        // Populate left panel of SplitPane
        leftPanel.add(new JLabel("Select by Question:"));
        leftPanel.add(questionListScrollPane);
        leftPanel.add(new JLabel("Select by User:"));
        leftPanel.add(userListScrollPane);

        splitPane = new JSplitPane();
        splitPane.setLeftComponent(leftPanel);


        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.PAGE_END);

        setRightPanel(new JPanel());
    }

    private JPanel createButtonPanel()
    {
        JPanel b = new JPanel();

        downloadButton = new JButton("Download Feedback");
        b.add(downloadButton);

        b.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        return b;
    }

    public JButton getDownloadButton() {
        return downloadButton;
    }

    public JList getQuestionList() {
        return questionList;
    }

    public JList getUserList() {
        return userList;
    }

    public void setRightPanel(Component component)
    {
        splitPane.setRightComponent(component);
    }
}
