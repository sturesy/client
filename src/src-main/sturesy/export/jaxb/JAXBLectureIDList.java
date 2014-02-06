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
package sturesy.export.jaxb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import sturesy.items.LectureID;

/**
 * A JAXB-List of LectureIDs for marshalling and unmarshalling
 * 
 * @author w.posdorfer
 * 
 */
@XmlRootElement(name = "lectureidlist")
@XmlSeeAlso(LectureID.class)
public class JAXBLectureIDList
{
    private List<LectureID> _list;

    public JAXBLectureIDList()
    {
        _list = new ArrayList<LectureID>();
    }

    public JAXBLectureIDList(List<LectureID> list)
    {
        setList(list);
    }

    public JAXBLectureIDList(Collection<LectureID> list)
    {
        this();
        _list.addAll(list);
    }

    public void add(LectureID id)
    {
        getList().add(id);
    }

    @XmlElement(name = "lectureid")
    public List<LectureID> getList()
    {
        return _list;
    }

    public void setList(List<LectureID> list)
    {
        _list = list;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof JAXBLectureIDList)
        {
            return _list.equals(((JAXBLectureIDList) obj)._list);
        }
        return false;
    }
}
