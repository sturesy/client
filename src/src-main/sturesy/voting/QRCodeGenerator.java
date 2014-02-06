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
package sturesy.voting;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import sturesy.util.Settings;
import sturesy.util.web.WebCommands;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Class for generating QR-Codes
 * 
 * @author w.posdorfer
 */
public class QRCodeGenerator
{

    /**
     * Return a QR-Code-Image from the saved URL adding the lecture-id to the
     * end<br>
     * http://someurl.com/index.php?lecture=lectureid
     */
    public static ImageIcon getQRImageForSavedAdress(String lectureid, int size)
    {
        return getQRImage(
                Settings.getInstance().getString(Settings.CLIENTADDRESS) + "?lecture=" + WebCommands.encode(lectureid),
                size);
    }

    /**
     * Generates a QR-Code-Image from given String
     * 
     * @param url
     * @param width
     * @param height
     * @throws WriterException
     */
    public static ImageIcon getQRImage(String url, int size)
    {
        try
        {
            BitMatrix bitMatrix;
            QRCodeWriter writer = new QRCodeWriter();
            bitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, size, size);
            BufferedImage buff = MatrixToImageWriter.toBufferedImage(bitMatrix);
            final int black = -16777216;

            int firstx = 0;
            int firsty = 0;

            int lastx = 0;

            for (int y = 0; y < buff.getHeight(); y++)
            {
                for (int x = 0; x < buff.getWidth(); x++)
                {
                    if (buff.getRGB(x, y) == black)
                    {
                        firstx = x;
                        firsty = size - y;
                        break;
                    }
                }
            }

            for (int x = buff.getWidth() - 1; x > firstx; x--)
            {
                if (buff.getRGB(x, firsty) == black)
                {
                    lastx = x;
                    break;
                }
            }

            buff = buff.getSubimage(firstx - 10, firsty - 10, lastx - firstx + 20, lastx - firstx + 20);

            return new ImageIcon(buff);
        }
        catch (WriterException e)
        {
            return new ImageIcon();
        }
    }
}
