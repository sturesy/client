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
    private JButton saveButton;
    private JButton loadButton;
    private final JPanel feedbackResults;

    public FeedbackViewerUI()
    {
        super();
        setTitle("Feedback Viewer");
        setIconImage(Loader.getImageIcon(Loader.IMAGE_STURESY).getImage());

        JPanel buttonPanel = createButtonPanel();
        feedbackResults = new JPanel();
        feedbackResults.setLayout(new BoxLayout(feedbackResults, BoxLayout.PAGE_AXIS));

        JLabel emptyLabel = new JLabel("No Feedback loaded.");
        emptyLabel.setAlignmentX(CENTER_ALIGNMENT);
        feedbackResults.add(emptyLabel);

        JScrollPane scrollPane = new JScrollPane(feedbackResults, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.PAGE_END);
    }

    private JPanel createButtonPanel()
    {
        JPanel b = new JPanel();

        downloadButton = new JButton("Download Feedback");
        saveButton = new JButton("Save to File");
        loadButton = new JButton("Load from File");

        b.add(downloadButton);
        b.add(saveButton);
        b.add(loadButton);

        b.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        return b;
    }

    public JButton getDownloadButton() {
        return downloadButton;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getLoadButton() {
        return loadButton;
    }

    public JPanel getFeedbackResults() {
        return feedbackResults;
    }
}
