package sturesy.core.ui;

import javax.swing.*;

/**
 * This subclasses JTextArea but emulates a JLabel look.
 * It is used to display longer, line-wrapped content.
 * Created by henrik on 11/13/14.
 */
public class TextDisplay extends JTextArea {
    public TextDisplay(String text) {
        super(text);

        setLineWrap(true);
        setEditable(false);
        setBackground(null);
        setBorder(null);
        setWrapStyleWord(true);
        setFocusable(false);
        setOpaque(false);
        setFont(UIManager.getFont("Label.font"));
    }
}
