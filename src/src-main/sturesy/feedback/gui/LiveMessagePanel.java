package sturesy.feedback.gui;

import sturesy.core.ui.TextDisplay;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Used to display a message sent via live feedback
 * Created by henrik on 8/16/14.
 */
public class LiveMessagePanel extends JPanel {
    private static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private JLabel infoLabel;
    private TextDisplay msgText;

    // these hold the default font sizes determined by java
    private int initialSizeInfo;
    private int initialSizeMessage;

    public LiveMessagePanel(String name, String subject, String message, Date date) {
        super();
        setBorder(BorderFactory.createTitledBorder(name));
        setLayout(new BorderLayout());

        String time = dateFormat.format(date);
        String sub = (subject.length() > 0 ? subject : "<no subject>");

        // make info label 1pt smaller
        infoLabel = new JLabel("At " + time + " regarding: " + sub);
        initialSizeInfo = infoLabel.getFont().getSize() - 1;
        add(infoLabel, BorderLayout.NORTH);

        // make message 2pt larger
        msgText = new TextDisplay(message);
        initialSizeMessage = msgText.getFont().getSize() + 2;
        add(msgText, BorderLayout.CENTER);
    }

    /**
     * Used to scale font sizes of the labels displaying the message
     * @param offset Offset for scaling
     */
    public void resizeFonts(int offset) {
        Font infoFont = infoLabel.getFont();
        infoLabel.setFont(new Font(infoFont.getFontName(), Font.ITALIC, initialSizeInfo + offset));

        Font msgFont = msgText.getFont();
        msgText.setFont(new Font(msgFont.getFontName(), Font.BOLD, initialSizeMessage + offset));
    }
}
