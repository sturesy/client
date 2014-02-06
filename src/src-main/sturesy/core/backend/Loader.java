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
package sturesy.core.backend;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import javax.swing.ImageIcon;

import sturesy.core.Log;

/**
 * Standard Classloader to use
 * 
 * @author w.posdorfer
 * 
 */
public class Loader
{

    public static final String IMAGE_STURESY = "image/sturesy.png";
    public static final String IMAGE_STURESY_ERROR = "image/sturesy-error.png";
    public static final String IMAGE_VOTING = "image/voting.png";
    public static final String IMAGE_SETTINGS = "image/settings.png";
    public static final String IMAGE_EVALUATE = "image/evaluate.png";
    public static final String IMAGE_QUESTIONEDITOR = "image/question_gen.png";
    public static final String IMAGE_QUESTION = "image/question.png";
    public static final String IMAGE_GENERALINFO = "image/general-info.png";
    public static final String IMAGE_WEB = "image/wlan-icon.png";
    public static final String IMAGE_BARCODE = "image/barcode.png";
    public static final String IMAGE_OK = "image/ok-icon.png";
    public static final String IMAGE_COPYRIGHT = "image/copyright.png";
    public static final String IMAGE_COLORS = "image/colors.png";

    public static final String IMAGE_PLAY = "image/play.png";
    public static final String IMAGE_STOP = "image/stop.png";
    public static final String IMAGE_UNDO = "image/undo.png";
    public static final String IMAGE_NEXT = "image/next.png";
    public static final String IMAGE_PREVIOUS = "image/previous.png";
    public static final String IMAGE_RED = "image/red.png";
    public static final String IMAGE_GREEN = "image/green.png";
    public static final String IMAGE_GREEN_LOAD = "image/load.gif";
    public static final String IMAGE_YELLOW_BAR = "image/bar-yellow.png";
    public static final String IMAGE_GREEN_RED_BAR = "image/bar-greenred.png";

    public static final String IMAGE_ARROW_UP = "image/up.png";
    public static final String IMAGE_ARROW_DOWN = "image/down.png";

    public static final String IMAGE_LOADING_CIRCLE = "image/load.gif";

    public static final String CONTRIBUTORS_TEXT = "documents/contributors.txt";
    public static final String LICENSE_HTML = "documents/agpl.html";

    public static final ClassLoader cl = Loader.class.getClassLoader();

    /**
     * Retrieves an ImageIcon by specified filename
     * 
     * @param imageName
     * @return ImageIcon or <code>null</code>
     */
    public static ImageIcon getImageIcon(String imageName)
    {
        try
        {
            final URL imageURL = cl.getResource(imageName);
            if (imageURL != null)
            {
                return new ImageIcon(imageURL);
            }
        }
        catch (Exception ex)
        {
            Log.error("error loading icon:" + imageName, ex);
        }
        return null;
    }

    /**
     * Returns a localized version of an image <br>
     * by changing the name of <code>image.png</code> to
     * <code>image_en.png</code><br>
     * if the localized version is not existant it will default to the input
     * 
     * @param imageName
     * @return localized image if existant or original image or
     *         <code>null</code>
     */
    public static ImageIcon getImageIconLocalized(String imageName)
    {
        String locale = Locale.getDefault().getLanguage();

        int indexofend = imageName.lastIndexOf('.');

        String newImageName = imageName.substring(0, indexofend) + "_" + locale + imageName.substring(indexofend);

        URL imageURL = cl.getResource(newImageName);

        if (imageURL != null)
        {
            return new ImageIcon(imageURL);
        }
        else
        {
            return getImageIcon(imageName);
        }
    }

    /**
     * Combines getImageIconLocalized with a resized version
     * 
     * @see Loader#getImageIconLocalized(String)
     * @see Loader#getImageIconResized(String, int, int, int)
     * @param imageName
     *            use <code>Loader.IMAGE_XYZ</code>
     * @param width
     * @param height
     * @param hints
     *            <code>Image.SCALE_FAST, SCALE_SMOOTH, SCALE_REPLICATE ,
     *            SCALE_DEFAULT, SCALE_AREA_AVERAGING</code>
     * @return localized and resized image
     */
    public static ImageIcon getImageIconLocalizedResized(String imageName, int width, int height, int hints)
    {
        return new ImageIcon(getImageIconLocalized(imageName).getImage().getScaledInstance(width, height, hints));
    }

    /**
     * Retrieves a resized ImageIcon by specified filename and width, height and
     * hints
     * 
     * @param imageName
     *            use <code>Loader.IMAGE_XYZ</code>
     * @param width
     * @param height
     * @param hints
     *            <code>Image.SCALE_FAST, SCALE_SMOOTH, SCALE_REPLICATE ,
     *            SCALE_DEFAULT, SCALE_AREA_AVERAGING</code>
     * @return resized image
     */
    public static ImageIcon getImageIconResized(String imageName, int width, int height, int hints)
    {
        return new ImageIcon(getImageIcon(imageName).getImage().getScaledInstance(width, height, hints));
    }

    /**
     * Retrieves a File
     * 
     * @param name
     * @return
     */
    public static File getFile(String name)
    {
        try
        {
            final URL url = cl.getResource(name);
            return new File(url.toURI());
        }
        catch (Exception ex)
        {
            Log.error("error getting File:" + name, ex);
        }
        return null;
    }

    /**
     * Returns a Resource as InputStream
     * 
     * @param name
     *            Name of resource
     * @return InputStream containing the resource
     */
    public static InputStream getResourceAsStream(String name)
    {
        return cl.getResourceAsStream(name);
    }
}