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
package sturesy.update;

import java.io.IOException;
import java.io.StringReader;
import java.util.Calendar;

import javax.swing.JOptionPane;

import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import sturesy.core.Localize;
import sturesy.core.Log;
import sturesy.util.Settings;
import sturesy.util.Version;
import sturesy.util.web.WebCommands;

/**
 * Checks if updates are available
 * 
 * @author w.posdorfer
 * 
 */
public class UpdateChecker
{

    private static final String UPDATECHECKURL = "http://sturesy.sourceforge.net/sturesyversion/version.xml";

    private UpdateInfos _updateinfos = new UpdateInfos();


    /**
     * Query Server for updates
     * 
     * @param silent
     *            if silent there wont be a message saying that no updates are
     *            available
     */
    public void checkForUpdate(boolean silent)
    {
        String result = WebCommands.sendPost(UPDATECHECKURL, "");

        markLastUpdate();

        StringReader reader = new StringReader(result);
        XmlPullParser pull = new MXParser();
        try
        {
            pull.setInput(reader);
            processXML(pull);

            proceed(silent);
        }
        catch (IOException e)
        {
            Log.error("error", e);
        }
        catch (XmlPullParserException e)
        {
            Log.error("error", e);
        }

    }

    private void markLastUpdate()
    {
        Settings.getInstance().setProperty(Settings.UPDATE_LASTUPDATE, Calendar.getInstance().getTime());
    }

    private void proceed(boolean silent)
    {

        if (Version.isHigherVersion(_updateinfos._nextVersion))
        {
            int result = JOptionPane.showConfirmDialog(null, Localize.getString("label.update.yes"),
                    Localize.getString("label.update"), JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION)
            {
                UpdateDownloader downloader = new UpdateDownloader();
                downloader.displayController(null, null);
                downloader.startDownloading(_updateinfos);
            }
        }
        else
        {
            if (!silent)
            {
                JOptionPane.showMessageDialog(null, Localize.getString("label.update.no"),
                        Localize.getString("label.update"), JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void processXML(XmlPullParser xpp) throws XmlPullParserException, IOException
    {

        int eventType = xpp.getEventType();
        String name = "";

        do
        {
            if (eventType == XmlPullParser.START_TAG)
            {
                name = xpp.getName();

                if (name.equals("version"))
                {
                    _updateinfos._nextVersion = xpp.nextText();
                }
                else if (name.equals("download"))
                {
                    _updateinfos._mirrorList.add(xpp.nextText());
                }
                else if(name.equals("mac.download"))
                {
                    _updateinfos._macDownloadURL = xpp.nextText();
                }
            }
            eventType = xpp.next();
        }
        while (eventType != XmlPullParser.END_DOCUMENT);
    }

}
