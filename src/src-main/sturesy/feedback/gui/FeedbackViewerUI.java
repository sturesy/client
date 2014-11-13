package sturesy.feedback.gui;

import sturesy.core.backend.Loader;
import sturesy.core.ui.NumberedListCellRenderer;
import sturesy.core.ui.SFrame;
import sturesy.feedback.FeedbackViewerUserEntry;
import sturesy.items.feedback.AbstractFeedbackType;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * UI components of the Feedback Viewer
 * Created by henrik on 6/22/14.
 */
public class FeedbackViewerUI extends SFrame {
    private JButton loadButton;
    private JButton saveButton;
    private JButton downloadButton;

    private JSplitPane splitPane;

    private JList<AbstractFeedbackType> questionList;
    private JList<FeedbackViewerUserEntry> userList;

    /**
     * Loads the Bootstrap font containing the glyphs
     * @param size Desired font size
     * @return Font object for Bootstrap Glyph Font
     * @throws FontFormatException
     * @throws IOException
     */
    private Font loadGlyphFont(float size) throws FontFormatException, IOException {
        Font fontRaw = Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/resource/image/glyphicons-halflings-regular.ttf"));
        Font fontBase = fontRaw.deriveFont(16f);
        return fontBase.deriveFont(Font.PLAIN, size);
    }

    public FeedbackViewerUI(DefaultListModel<AbstractFeedbackType> questionListModel, DefaultListModel<FeedbackViewerUserEntry> userListModel)
    {
        super();
        setTitle("Feedback Viewer");
        setIconImage(Loader.getImageIcon(Loader.IMAGE_STURESY).getImage());

        // Button Panel containing download/upload/etc. buttons
        JPanel buttonPanel = createButtonPanel();

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        questionList = new JList<>(questionListModel);
        questionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        questionList.setCellRenderer(new NumberedListCellRenderer());
        userList = new JList<>(userListModel);
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

        loadButton = new JButton("Load Feedback");
        saveButton = new JButton("Save Feedback");
        downloadButton = new JButton("Download Feedback");

        b.add(loadButton);
        b.add(saveButton);
        b.add(downloadButton);

        b.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        return b;
    }

    /**
     * Used to create the top panel describing the current feedback question
     * @param fb Feedback question to describe
     * @return Panel with title and description
     */
    public JPanel createQuestionDataPanel(AbstractFeedbackType fb) {
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new GridBagLayout());
        questionPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        GridBagConstraints leftBoxConst = new GridBagConstraints();
        leftBoxConst.anchor = GridBagConstraints.LINE_START;
        leftBoxConst.gridx = 0;
        leftBoxConst.gridy = 0;
        leftBoxConst.gridheight = 2;

        JLabel titleLabel = new JLabel(fb.getTitle());
        GridBagConstraints titleLabelConst = new GridBagConstraints();
        titleLabelConst.anchor = GridBagConstraints.CENTER;
        titleLabelConst.gridx = 1;
        titleLabelConst.gridy = 0;
        titleLabelConst.weightx = 1.0;
        Font titleLabelFont = titleLabel.getFont();
        titleLabel.setFont(new Font(titleLabelFont.getName(), Font.BOLD, titleLabelFont.getSize()+4)); // increase label font size by 2

        JLabel descLabel = new JLabel(fb.getDescription());
        GridBagConstraints descLabelConst = new GridBagConstraints();
        descLabelConst.anchor = GridBagConstraints.CENTER;
        descLabelConst.gridx = 1;
        descLabelConst.gridy = 1;
        descLabelConst.weightx = 1.0;

        JLabel exclLabel = new JLabel("!");
        exclLabel.setAlignmentX(RIGHT_ALIGNMENT);
        exclLabel.setToolTipText("Response is mandatory");
        try {
            exclLabel.setFont(loadGlyphFont(titleLabelFont.getSize()+4));
            exclLabel.setText("\ue101");
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        GridBagConstraints rightBoxConst = new GridBagConstraints();
        rightBoxConst.anchor = GridBagConstraints.LINE_END;
        rightBoxConst.gridx = 2;
        rightBoxConst.gridy = 0;
        rightBoxConst.gridheight = 2;
        rightBoxConst.ipadx = 4;

        questionPanel.add(new JPanel(), leftBoxConst);
        questionPanel.add(titleLabel, titleLabelConst);
        questionPanel.add(descLabel, descLabelConst);
        questionPanel.add(fb.isMandatory() ? exclLabel : new JPanel(), rightBoxConst);

        return questionPanel;
    }

    public JButton getLoadButton() {
        return loadButton;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getDownloadButton() {
        return downloadButton;
    }

    public JList<sturesy.items.feedback.AbstractFeedbackType> getQuestionList() {
        return questionList;
    }

    public JList<FeedbackViewerUserEntry> getUserList() {
        return userList;
    }

    public void setRightPanel(Component component)
    {
        splitPane.setRightComponent(component);
    }
}
