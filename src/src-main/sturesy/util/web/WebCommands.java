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
package sturesy.util.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import sturesy.core.Log;
import sturesy.core.ui.HTMLStripper;
import sturesy.items.QuestionModel;

/**
 * Contains the Commands needed for the php-server
 * 
 * @author w.posdorfer
 * @deprecated
 */
public class WebCommands
{

    /**
     * Cleans a database from the participated votes
     * 
     * @param lecturename
     * @param password
     * @return
     */
    public static String cleanCommand(String lecturename, String password)
    {
        return "command=clean&name=" + encode(lecturename) + "&pwd=" + encode(password);
    }

    /**
     * Requests all current Votes<br>
     * in format: <code>VOTES:guid,vote;guid,vote;</code>
     * 
     * @param lecturename
     * @param password
     */
    public static String getVotesCommand(String lecturename, String password)
    {
        return "command=get&name=" + encode(lecturename) + "&pwd=" + encode(password);
    }

    /**
     * Updates the Lecturetype
     * 
     * @param lecturename
     * @param password
     * @param model
     */
    public static String updateAnswerCommand(String lecturename, String password, QuestionModel model)
    {
        final String command = "command=update&name=%s&pwd=%s&type=%s&count=%s&text=%s&answers=%s";

        final String type = model.hasCorrectAnswer() ? "1" : "-1";

        final String questiontext = encode(StringEscapeUtils.escapeHtml4(StringEscapeUtils.unescapeHtml4((HTMLStripper
                .stripHTML2(model.getQuestion())))));
        final String jsonAnswers = encode(getJSONAnswerTexts(model.getAnswers()));
        final String formatted = String.format(command, encode(lecturename), encode(password), type,
                model.getAnswerSize(), questiontext, jsonAnswers);

        return formatted;
    }

    public static String getJSONAnswerTexts(List<String> list)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");

        for (int i = 0; i < list.size(); i++)
        {
            String anstext = StringEscapeUtils.escapeHtml4(encode(HTMLStripper.stripHTML2(list.get(i))));

            buffer.append("\"" + i + "\":\"" + anstext + "\"");

            if (i < list.size() - 1)
            {
                buffer.append(",");
            }
        }

        buffer.append("}");
        return buffer.toString();
    }

    public static String getLectureListInfo(Collection<String> lectures)
    {
        String string = lectures.toString();
        String stringWithoutFirstCharacter = string.substring(1, string.length() - 1);
        return "command=lectureinfo&lectures=" + removeWhitespace(stringWithoutFirstCharacter);
    }

    private static String removeWhitespace(String string)
    {
        return string.replace(" ", "");
    }

    public static String getInfo()
    {
        return "command=info";
    }

    /**
     * Redeems a Token to get lecturename and password
     * 
     * @param token
     * @return http-request-string
     */
    public static String redeemToken(String token)
    {
        return "command=redeem&token=" + token;
    }

    /**
     * Sends a post to the specified URL
     * 
     * @param url
     * @param post
     * @return result from server, if any
     * @throws IOException
     */
    public static String sendPost(URL url, String post) throws IOException
    {

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(post);
        wr.flush();

        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;

        StringBuffer buffer = new StringBuffer();
        while ((line = rd.readLine()) != null)
        {
            buffer.append(line);
        }
        wr.close();
        rd.close();

        return buffer.toString();
    }

    /**
     * Sends a post to the specified url in String format
     * 
     * @param url
     * @param post
     * @return result from server, if any
     */
    public static String sendPost(String url, String post)
    {
        try
        {
            return sendPost(new URL(url), post);
        }
        catch (MalformedURLException e)
        {
            Log.error("URL MALFORMED ERROR", e);
            return "";
        }
        catch (IOException e)
        {
            Log.error(e);
            return "";
        }
    }

    /**
     * Encode a String to a URL-conform String using <i>UTF-8</i> as encoding
     */
    public static String encode(String s)
    {
        try
        {
            final String encoding = "UTF-8";
            return URLEncoder.encode(s, encoding);
        }
        catch (UnsupportedEncodingException e)
        {
            Log.error("Error Encoding String", e);
        }
        return s;
    }
}
