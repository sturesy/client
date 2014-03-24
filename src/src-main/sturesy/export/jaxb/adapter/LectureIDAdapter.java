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
package sturesy.export.jaxb.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import sturesy.util.Crypt;

/**
 * Provides help when marshalling/unmarshalling of encrypted passwords
 * 
 * @author w.posdorfer
 * 
 */
public class LectureIDAdapter extends XmlAdapter<String, String>
{
    public String unmarshal(String value) throws Exception
    {
        return Crypt.decrypt(value);
    }

    public String marshal(String value) throws Exception
    {
        return Crypt.encrypt(value);
    }
}
