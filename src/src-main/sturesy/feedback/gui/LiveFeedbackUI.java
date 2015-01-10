package sturesy.feedback.gui;

import sturesy.core.ui.SFrame;
import sturesy.core.ui.SmartScroller;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.LinkedList;

/**
 * UI component of live-feedback functionality
 * Created by henrik on 8/16/14.
 */
public class LiveFeedbackUI extends SFrame {
    private int fontSizeOffset = 0;
    private final JPanel messagePanel;
    private final JCheckBox autoScrollCheckBox;
    private final JCheckBox notificationCheckBox;
    private final JButton startStopButton;
    private final SmartScroller smartScroller;
    private java.util.List<LiveMessagePanel> messagePanels;

    public LiveFeedbackUI() {
        super();
        setTitle("Live-Feedback");

        messagePanels = new LinkedList<>();
        messagePanel = new JPanel();
        messagePanel.setLayout(new GridBagLayout());
        JScrollPane msgScrollPane = new JScrollPane(messagePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        msgScrollPane.setViewportBorder(null);
        smartScroller = new SmartScroller(msgScrollPane);
        messagePanel.setBorder(BorderFactory.createTitledBorder("Messages"));

        JPanel bottomPanel = new JPanel();
        startStopButton = new JButton("Start Live-Feedback");
        bottomPanel.add(startStopButton);

        // notification checkbox
        notificationCheckBox = new JCheckBox("Show notification alerts", true);
        bottomPanel.add(notificationCheckBox);

        // autoscroll checkbox
        autoScrollCheckBox = new JCheckBox("Autoscroll messages", true);
        autoScrollCheckBox.addItemListener(l -> onAutoScrollCheckBox());
        bottomPanel.add(autoScrollCheckBox);
        smartScroller.setActive(true);

        // + and - buttons for font size
        JPanel fontPanel = new JPanel();
        JButton plusButton = new JButton("+");
        plusButton.setToolTipText("Increase font size");
        JButton minusButton = new JButton("-");
        plusButton.setToolTipText("Decrease font size");
        plusButton.addActionListener(l -> onPlusButton());
        minusButton.addActionListener(l -> onMinusButton());
        fontPanel.add(new JLabel("Font Size:"));
        fontPanel.add(plusButton);
        fontPanel.add(minusButton);
        bottomPanel.add(fontPanel);

        // add message and bottom panel to main view
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
    }

    /**
     * Disable/Enable the SmartScroller when the checkbox was ticked
     */
    private void onAutoScrollCheckBox() {
        smartScroller.setActive(autoScrollCheckBox.isSelected());
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
        panel.resizeFonts(fontSizeOffset);
        messagePanels.add(panel);

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

    /**
     * Returns the current font size offset
     * @return size offset
     */
    public int getFontSizeOffset() {
        return fontSizeOffset;
    }

    /**
     * Sets the font size offset used to up/downscale the messages
     * @param fontSizeOffset size offset
     */
    public void setFontSizeOffset(int fontSizeOffset) {
        this.fontSizeOffset = fontSizeOffset;
    }

    /**
     * Minus button pressed. Increase font size and update panels.
     */
    public void onMinusButton() {
        if(fontSizeOffset >= -4)
            fontSizeOffset--;

        for(LiveMessagePanel panel : messagePanels) {
            panel.resizeFonts(fontSizeOffset);
        }
    }

    /**
     * Plus button pressed. Increase font size and update panels.
     */
    public void onPlusButton() {
        if(fontSizeOffset <= 14)
            fontSizeOffset++;

        for(LiveMessagePanel panel : messagePanels) {
            panel.resizeFonts(fontSizeOffset);
        }
    }
}
