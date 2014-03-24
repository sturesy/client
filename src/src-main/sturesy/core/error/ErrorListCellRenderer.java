/*
 * StuReSy - Student Response System
 * Copyright (C) 2012-2014  StuReSy-Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sturesy.core.error;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import sturesy.core.Pair;

/**
 * A Custom ListCellRenderer, that displays the Name of the Plugin that Crashed
 * in a bigger Font, and the reason it crashed below in a slightly smaller font
 * 
 * @author w.posdorfer
 */
public class ErrorListCellRenderer extends DefaultListCellRenderer
{
    private static final long serialVersionUID = 8153347144046228709L;

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus)
    {
        @SuppressWarnings("unchecked")
        Pair<String, String> message = (Pair<String, String>) value;

        JPanel panel = createPanel(message.getFirst(), message.getSecond());

        return panel;
    }

    /**
     * Creates the JPanel
     * 
     * @param plugin
     *            the plugin that crashed
     * @param message
     *            the message why it crashed
     * @return constructed JPanel
     */
    private JPanel createPanel(String plugin, String message)
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel label1 = new JLabel();

        Font f = label1.getFont();
        label1.setFont(f.deriveFont((float) (f.getSize() + 6f)));
        label1.setText(plugin);
        label1.setOpaque(false);

        int width = getPreferredSize().width;
        width = width < 240 ? 240 : width;

        JLabel label2 = new JLabel();
        String descriptionText = "<html><body style='width:" + width + "px'>" + message + "</body></html>";
        label2.setText(descriptionText);
        label2.setOpaque(false);
        label2.setFont(f.deriveFont(f.getSize() - 2f));

        panel.add(label1, BorderLayout.CENTER);
        panel.add(label2, BorderLayout.SOUTH);

        return panel;
    }
}
