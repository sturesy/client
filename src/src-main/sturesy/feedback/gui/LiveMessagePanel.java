package sturesy.feedback.gui;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Used to display a message sent via live feedback
 * Created by henrik on 8/16/14.
 */
public class LiveMessagePanel extends JPanel {
    private static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public LiveMessagePanel(String name, String subject, String message, Date date) {
        super();
        setBorder(BorderFactory.createTitledBorder(name));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        String time = dateFormat.format(date);
        add(new JLabel("At " + time + " regarding: " + subject));
        add(new JLabel(message));
    }
}
