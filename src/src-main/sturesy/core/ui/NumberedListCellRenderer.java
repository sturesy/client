package sturesy.core.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Prefixes JList entries with their index in the model (starting at 1)
 * Created by henrik on 11/6/14.
 */
public class NumberedListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setText((index + 1) + ": " + label.getText());

        return label;
    }
}
