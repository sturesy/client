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
package sturesy.core.backend.services;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.codec.binary.Base64;

import sturesy.core.Log;

public class ImageService
{
    /**
     * Encodes the Image into a Base64-encoded String
     * 
     * @return Base64-encoded String
     */
    public String encodeImage(ImageIcon _questionImage)
    {
        if (_questionImage == null)
            return "";

        BufferedImage bi = new BufferedImage(_questionImage.getIconWidth(), _questionImage.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bi.createGraphics();
        g2.drawImage(_questionImage.getImage(), 0, 0, null);
        g2.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            ImageIO.write(bi, "png", baos);

            baos.flush();
        }
        catch (IOException e)
        {
            Log.error("error converting image to bytearray", e);
        }
        finally
        {
            try
            {
                baos.close();
            }
            catch (IOException e)
            {
            }
        }

        return Base64.encodeBase64String(baos.toByteArray());
    }
}
