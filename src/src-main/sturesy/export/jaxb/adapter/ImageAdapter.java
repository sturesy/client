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
package sturesy.export.jaxb.adapter;

import javax.swing.ImageIcon;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.codec.binary.Base64;

import sturesy.core.backend.services.ImageService;

/**
 * ImageAdapter that encodes an ImageIcon into a Base64 String and decodes a
 * Base64 String into an ImageIcon <br>
 * for infos see {@link ImageService#encodeImage(ImageIcon)}<br>
 * and {@link Base64#decodeBase64(String)}
 * 
 * @author w.posdorfer
 * 
 */
public class ImageAdapter extends XmlAdapter<String, ImageIcon>
{

    @Override
    public ImageIcon unmarshal(String v) throws Exception
    {
        byte[] bytes = Base64.decodeBase64(v);
        return new ImageIcon(bytes);
    }

    @Override
    public String marshal(ImageIcon v) throws Exception
    {
        if (v.getIconHeight() != -1 && v.getIconWidth() != -1)
        {
            ImageService imageservice = new ImageService();
            return imageservice.encodeImage(v);
        }
        else
        {
            return "";
        }
    }

}
