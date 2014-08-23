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
    private final JCheckBox notificationCheckBox;
    private final JButton startStopButton;

    public LiveFeedbackUI() {
        super();
        setTitle("Live-Feedback");

        JPanel bottomPanel = new JPanel();
        startStopButton = new JButton("Start Live-Feedback");
        bottomPanel.add(startStopButton);

        notificationCheckBox = new JCheckBox("Show notification alerts", true);
        bottomPanel.add(notificationCheckBox);

        autoScrollCheckBox = new JCheckBox("Autoscroll messages", true);
        bottomPanel.add(autoScrollCheckBox);

        messagePanel = new JPanel();
        messagePanel.setLayout(new GridBagLayout());
        JScrollPane msgScrollPane = new JScrollPane(messagePanel);
        msgScrollPane.setViewportBorder(null);
        messagePanel.setBorder(BorderFactory.createTitledBorder("Messages"));

        add(msgScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.PAGE_END);

        // fill up remaining space with greedy invisible panel
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = 0;
        cons.anchor = GridBagConstraints.PAGE_START;
        cons.weightx = 1;
        cons.weighty = 1;
        cons.fill = GridBagConstraints.BOTH;
        messagePanel.add(new JPanel(), cons);

        // auto-scroll to the bottom (if enabled)
        // TODO: find a better way to do this (the current code blocks interaction with the scrollbar)
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
        GridBagConstraints cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = GridBagConstraints.RELATIVE;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.anchor = GridBagConstraints.PAGE_START;
        cons.weightx = 1;

        LiveMessagePanel panel = new LiveMessagePanel(name, subject, msg, date);

        SwingUtilities.invokeLater(() -> {
            messagePanel.add(panel, cons);

            messagePanel.revalidate();
            messagePanel.repaint();
    });
    }

    /**
     * @return Start/Stop button object
     */
    public JButton getStartStopButton() {
        return startStopButton;
    }

    /**
     * @return Notification CheckBox object
     */
    public JCheckBox getNotificationCheckBox() {
        return notificationCheckBox;
    }
}
