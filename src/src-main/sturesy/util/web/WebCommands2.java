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
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sturesy.core.Log;
import sturesy.core.ui.HTMLStripper;
import sturesy.items.MultipleChoiceQuestion;
import sturesy.items.QuestionModel;
import sturesy.items.SingleChoiceQuestion;
import sturesy.items.TextQuestion;
import sturesy.items.feedback.FeedbackTypeModel;
import sturesy.util.Crypt;

public class WebCommands2
{

    /**
     * Cleans a database from the participated votes
     * 
     * @param lecturename
     * @param password
     * @return
     */
    public static String cleanVotes(String url, String lecturename, String password)
    {
        JSONObject js = new JSONObject();
        js.put("command", "clean");
        js.put("name", encode(lecturename));
        js.put("time", System.currentTimeMillis() / 1000);

        return sendJSONObject(url, js, password);
    }

    public static String getVotes(String url, String lecturename, String password)
    {
        JSONObject js = new JSONObject();
        js.put("command", "get");
        js.put("name", encode(lecturename));
        js.put("time", System.currentTimeMillis() / 1000);

        return sendJSONObject(url, js, password);
    }

    public static String getVotes(URL url, String lecturename, String password)
    {
        JSONObject js = new JSONObject();
        js.put("command", "get");
        js.put("name", encode(lecturename));
        js.put("time", System.currentTimeMillis() / 1000);

        return sendJSONObject(url, js, password);
    }

    public static String redeemToken(String url, String token)
    {
        JSONObject js = new JSONObject();
        js.put("token", token);
        js.put("command", "redeem");
        js.put("time", System.currentTimeMillis() / 1000);

        return sendJSONObject(url, js, "info");
    }

    public static String updateQuestion(String url, String lecturename, String password, QuestionModel model)
    {
        JSONObject js = new JSONObject();
        js.put("command", "update");
        js.put("name", encode(lecturename));
        js.put("time", System.currentTimeMillis() / 1000);

        js.put("type", model.getType());

        js.put("question", prepareForSend(model.getQuestion()));

        List<String> newAnswers = new ArrayList<String>();
        for (String s : model.getAnswers())
        {
            newAnswers.add(prepareForSend(s));
        }
        js.put("answers", newAnswers);
        js.put("correct", model.hasCorrectAnswer());

        if (model instanceof MultipleChoiceQuestion)
        {
            MultipleChoiceQuestion mq = (MultipleChoiceQuestion) model;
            js.put("answer", mq.getCorrectAnswers());
        }
        else if (model instanceof TextQuestion)
        {
            TextQuestion tq = (TextQuestion) model;
            js.put("answer", prepareForSend(tq.getAnswer()));
        }
        else if (model instanceof SingleChoiceQuestion)
        {
            SingleChoiceQuestion sq = (SingleChoiceQuestion) model;
            js.put("answer", sq.getCorrectAnswer());
        }

        return sendJSONObject(url, js, password);
    }
    
    /**
     * Updates a Feedback Sheet for a given lecture
     * 
     * @param url
     * 		URL of StuReSy relay
     * @param lecturename
     * 		Name of lecture
     * @param password
     * 		Password for Lecture
     * @param sheet
     * 		List of Feedback Questions
     */
    public static String updateFeedbackSheet(String url, String lecturename, String password, List<FeedbackTypeModel> sheet)
    {
    	JSONObject js = new JSONObject();
    	js.put("command", "update");
    	js.put("time", System.currentTimeMillis() / 1000);
    	js.put("target", "fbsheet");
    	js.put("name", encode(lecturename));
    	
    	JSONArray fbarray = new JSONArray();
    	for (FeedbackTypeModel fb : sheet)
		{
    		JSONObject fbObject = new JSONObject();
    		fbObject.put("title", fb.getTitle());
    		fbObject.put("desc", fb.getDescription());
    		fbObject.put("type", fb.getType());
    		fbObject.put("extra", fb.getExtra());
            fbObject.put("mandatory", fb.isMandatory());
    		
    		fbarray.put(fbObject);
			
		}
    	js.put("sheet", fbarray);
    	
    	return sendJSONObject(url, js, password);
    }

    /**
     * Retrieves the feedback sheet of a specific lecture
     * @param url URL of StuReSy relay
     * @param lecturename Name of lecture
     * @param password Password for Lecture
     * @return List of Feedback Questions
     */
    public static JSONArray downloadFeedbackSheet(String url, String lecturename, String password)
    {
        JSONObject js = new JSONObject()
                .put("command", "get")
                .put("time", System.currentTimeMillis() / 1000)
                .put("target", "fbsheet")
                .put("name", encode(lecturename));
        String response = sendJSONObject(url, js, password);

        JSONArray sheet;
        try {
            sheet = new JSONArray(response);
        }
        catch(JSONException e) {
            e.printStackTrace();
            return null;
        }

        return sheet;
    }

    public static String getInfo(String url)
    {
        JSONObject js = new JSONObject();
        js.put("command", "info");
        js.put("time", System.currentTimeMillis() / 1000);

        return sendJSONObject(url, js, "info");
    }

    /**
     * Sends a post to the specified url
     * 
     * @param url
     *            URL
     * @param data
     *            Base64 encoded JSONObject
     * @param hashed
     *            Hash of data used for verification
     * @return response
     * @throws IOException
     */
    public static String sendPost(URL url, String data, String hashed) throws IOException
    {
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);

        if (connection instanceof HttpURLConnection)
        {
            ((HttpURLConnection) connection).setRequestMethod("POST");
        }
        else if (connection instanceof HttpsURLConnection)
        {
            ((HttpsURLConnection) connection).setRequestMethod("POST");
        }

        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write("data=" + data + "&hash=" + hashed);
        wr.flush();

        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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
     *            URL
     * @param data
     *            Base64 encoded JSONObject
     * @param hash
     *            Hash of data used for verification
     * @return response
     */
    public static String sendPost(String url, String data, String hash)
    {
        try
        {
            return sendPost(new URL(url), data, hash);
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
     * Sends a post to the specified url in String format
     * 
     * @param url
     *            URL
     * @param obj
     *            JSONObject
     * @param hash
     *            Hash used for verification
     * @return response
     */
    public static String sendJSONObject(String url, JSONObject obj, String hash)
    {
        try
        {
            return sendJSONObject(new URL(url), obj, hash);
        }
        catch (MalformedURLException e)
        {
            Log.error(e);
            return "";
        }
    }

    /**
     * Sends a post to the specified url in String format
     * 
     * @param url
     *            URL
     * @param obj
     *            JSONObject
     * @param hash
     *            Hash used for verification
     * @return response
     */
    public static String sendJSONObject(URL url, JSONObject obj, String hash)
    {
        String jsString = obj.toString();

        jsString = Base64.encodeBase64String(jsString.getBytes());

        String hmac = Crypt.hmac_sha256(jsString, hash);

        try
        {
            return sendPost(url, jsString, hmac);
        }
        catch (IOException e)
        {
            Log.error(e);
            return "";
        }
    }

    public static String prepareForSend(String s)
    {
        String stripped = HTMLStripper.stripHTML2(s);
        return StringEscapeUtils.escapeHtml4(StringEscapeUtils.unescapeHtml4(stripped));
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

    static
    {
        System.setProperty("jsse.enableSNIExtension", "false");
        try
        {
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
            {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
                {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
                {
                }

                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
            } };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = new HostnameVerifier()
            {
                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            };

            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        }
        catch (Exception e)
        {
        }
    }

}
