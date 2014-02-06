/*
 * StuReSy - Student Response System
 * Copyright (C) 2012-2013  StuReSy-Team
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
package sturesy.core.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sturesy.core.Localize;

/**
 * A simple Colorpicker with sliders<br>
 * <br>
 * 
 * This was initially created by me for Spark-Instant-Messenger in 2011 and has
 * been improved for sturesy
 * 
 * @author w.posdorfer
 * 
 */
public class ColorPick extends JPanel implements ChangeListener
{

    private static final long serialVersionUID = 2709297435120012839L;
    private JSlider[] _sliderarray;
    private JLabel[] _labels;
    private JLabel _preview;

    private Color _color;
    private boolean _allowedStateChange;

    /**
     * Creates a Colorpicker with initial color: {@link Color#black}
     * 
     * @param opacity
     *            true if Opacity Slider should be visible
     */
    public ColorPick(boolean opacity)
    {
        _color = Color.black;
        this.setLayout(new GridBagLayout());

        JLabel red = new JLabel(Localize.getString("color.red"));
        JLabel green = new JLabel(Localize.getString("color.green"));
        JLabel blue = new JLabel(Localize.getString("color.blue"));
        JLabel opaq = new JLabel(Localize.getString("color.opacity"));

        _sliderarray = new JSlider[] { new JSlider(0, 255), new JSlider(0, 255), new JSlider(0, 255),
                new JSlider(0, 255) };

        _labels = new JLabel[] { new JLabel("255"), new JLabel("255"), new JLabel("255"), new JLabel("255") };

        for (JSlider s : _sliderarray)
        {
            s.addChangeListener(this);
            s.setMajorTickSpacing(256 / 3);
            s.setMinorTickSpacing(0);
            s.setMinimum(0);
            s.setMaximum(255);
            s.setPaintTicks(true);
            s.setPaintLabels(true);
        }

        _preview = new JLabel("   ");
        _preview.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        _preview.setOpaque(true);

        Insets defaultInsets = new Insets(5, 5, 5, 5);
        this.add(red, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                defaultInsets, 0, 0));
        this.add(_sliderarray[0], new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInsets, 0, 0));
        this.add(_labels[0], new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInsets, 0, 0));

        this.add(green, new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInsets, 0, 0));
        this.add(_sliderarray[1], new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInsets, 0, 0));
        this.add(_labels[1], new GridBagConstraints(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInsets, 0, 0));

        this.add(blue, new GridBagConstraints(0, 2, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                defaultInsets, 0, 0));
        this.add(_sliderarray[2], new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInsets, 0, 0));
        this.add(_labels[2], new GridBagConstraints(2, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInsets, 0, 0));

        if (opacity)
        {
            this.add(opaq, new GridBagConstraints(0, 3, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH, defaultInsets, 0, 0));
            this.add(_sliderarray[3], new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH, defaultInsets, 0, 0));
            this.add(_labels[3], new GridBagConstraints(2, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH, defaultInsets, 0, 0));
        }

        this.add(_preview, new GridBagConstraints(3, 0, 1, 4, 0.1, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInsets, 0, 0));

    }

    /**
     * Creates a Colorpicker with initial values provided by color
     * 
     * @param opacity
     *            true if Opacity Slider should be visible
     * @param color
     *            the initial Color
     */
    public ColorPick(boolean opacity, Color color)
    {
        this(opacity);
        setColor(color);
    }

    /**
     * Returns the Color of the Current View
     * 
     * @return
     */
    public Color getColor()
    {
        return _color;
    }

    /**
     * Sets the Color of this Colorpicker, also updates the sliders, texts and
     * preview
     * 
     * @param c
     */
    public void setColor(Color c)
    {
        _color = c;
        updateSliders();
        updateTexts();
        updatePreview();
        this.revalidate();
    }

    private void updatePreview()
    {
        _preview.setBackground(_color);
        _preview.setForeground(_color);
        this.revalidate();
        this.repaint();
    }

    private void updateSliders()
    {
        _allowedStateChange = false;

        _sliderarray[0].setValue(_color.getRed());
        _sliderarray[1].setValue(_color.getGreen());
        _sliderarray[2].setValue(_color.getBlue());
        _sliderarray[3].setValue(_color.getAlpha());

        _allowedStateChange = true;
    }

    private void updateTexts()
    {
        _labels[0].setText("" + _color.getRed());
        _labels[1].setText("" + _color.getGreen());
        _labels[2].setText("" + _color.getBlue());
        _labels[3].setText("" + _color.getAlpha());
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {

        if (_allowedStateChange)
        {
            _color = new Color(_sliderarray[0].getValue(), _sliderarray[1].getValue(), _sliderarray[2].getValue(),
                    _sliderarray[3].getValue());

            updateTexts();
            updatePreview();
        }
    }

}
