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
package sturesy.items;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import sturesy.core.Log;
import sturesy.export.jaxb.adapter.LectureIDAdapter;
import sturesy.util.Crypt;

/**
 * Container to hold lecture-id, password and hostname
 * 
 * @author w.posdorfer
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class LectureID
{
    @XmlID
    private final String _lectureID;

    @XmlElement
    @XmlJavaTypeAdapter(LectureIDAdapter.class)
    private final String _password;
    
    @XmlElement
    private URL _host;

    /**
     * Empty Constructor for JAXB
     */
    public LectureID()
    {
        this("", "", "http://localhost", false);
    }

    /**
     * Creates a new LectureID
     * 
     * @param id
     * @param pw
     *            decrypted password
     * @param url
     */
    public LectureID(String id, String pw, String url)
    {
        this(id, pw, url, false);
    }

    /**
     * Creates a new LectureID with given ID, Password and URL, if
     * <code>isEncrypted == true</code> the password will be decrypted
     * 
     * @param isEncrypted
     *            is the password encrypted
     */
    public LectureID(String id, String pw, String url, boolean isEncrypted)
    {
        _lectureID = id;
        try
        {
            _host = new URL(url);
        }
        catch (MalformedURLException e)
        {
            _host = null;
        }

        _password = isEncrypted ? Crypt.decrypt(pw) : pw;
    }

    public String getLectureID()
    {
        return _lectureID;
    }

    public String getPassword()
    {
        return _password;
    }

    public URL getHost()
    {
        return _host;
    }

    /**
     * @deprecated
     */
    public String toXML()
    {
        StringBuffer buffer = new StringBuffer();
        try
        {
            buffer.append("<lectureid>");
            buffer.append("<id>").append(_lectureID).append("</id>");
            buffer.append("<pw>").append(Crypt.encrypt(_password)).append("</pw>");
            buffer.append("<host>").append(_host.toString()).append("</host>");
            buffer.append("</lectureid>");
        }
        catch (Exception e)
        {
            Log.error("Error generating XML for LectureID", e);
        }

        return buffer.toString();
    }

    @Override
    public boolean equals(Object obj)
    {

        if (obj instanceof LectureID)
        {
            LectureID other = (LectureID) obj;
            boolean hasEqualLectureID = _lectureID.equals(other._lectureID);
            boolean hasEqualPassword = _password.equals(other._password);
            boolean hasEqualHost = _host.equals(other._host);
            return hasEqualLectureID && hasEqualPassword
                    && hasEqualHost;
        }
        else
            return false;
    }

    @Override
    public int hashCode()
    {
        return _lectureID.hashCode();
    }

    @Override
    public String toString()
    {
        return "LectureID [_lectureID=" + _lectureID + ", _password=" + _password + ", _host=" + _host + "]";
    }

}
