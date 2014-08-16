package sturesy.feedback.gui;

import sturesy.core.ui.SFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

/**
 * UI component of live-feedback functionality
 * Created by henrik on 8/16/14.
 */
public class LiveFeedbackUI extends SFrame {

    private final JPanel messagePanel;
    private final JCheckBox autoScrollCheckBox;

    public LiveFeedbackUI() {
        super();
        setTitle("Live-Feedback");

        JPanel topPanel = new JPanel();
        topPanel.add(new JButton("Start"));
        topPanel.add(new JCheckBox("Show notification alerts"));

        autoScrollCheckBox = new JCheckBox("Autoscroll messages", true);
        topPanel.add(autoScrollCheckBox);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        JScrollPane msgScrollPane = new JScrollPane(messagePanel);
        msgScrollPane.setBorder(BorderFactory.createTitledBorder("Messages"));

        for (int i = 0; i < 5; i++) {
            addMessage("Name " + i, "Subject", "Lorem Ipsum", new Date());
        }

        add(topPanel, BorderLayout.PAGE_START);
        add(msgScrollPane, BorderLayout.CENTER);

        // auto-scroll to the bottom (if enabled)
        msgScrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            if(autoScrollCheckBox.isSelected())
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());
        });
    }

    /**
     * Adds a message to the message panel
     * @param name Name of sender
     * @param subject Subject of message
     * @param msg Message
     * @param date Date of submission
     */
    public void addMessage(String name, String subject, String msg, Date date) {
        LiveMessagePanel panel = new LiveMessagePanel(name, subject, msg, date);
        messagePanel.add(panel);
    }
}
