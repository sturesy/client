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
package sturesy.test.core.services;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import org.junit.Test;

import sturesy.core.backend.services.ImageService;

public class TestImageService
{
    @Test
    public void testImageServiceImageIconIsNull()
    {
        ImageService service = new ImageService();
        String encodedImage = service.encodeImage(null);
        assertEquals("", encodedImage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImageServiceEmptyImageIcon()
    {
        ImageService service = new ImageService();
        service.encodeImage(new ImageIcon());
    }

    @Test
    public void testImageServiceEncodableImage()
    {
        String expected = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAC0lEQVR42mNgAAIAAAUAAen63NgAAAAASUVORK5CYII=";

        ImageService service = new ImageService();
        
        String actual = service.encodeImage(getImageIcon());

        assertEquals(expected, actual);
    }

    /**
     * Create a 1x1 black pixel ImageIcon
     * 
     * @return 1x1 black pixel
     */
    private ImageIcon getImageIcon()
    {
        BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        g.setColor(Color.black);
        g.drawLine(1, 1, 1, 1);
        return new ImageIcon(bi);
    }

}
