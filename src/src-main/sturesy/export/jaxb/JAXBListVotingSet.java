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
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * A Class to save the {@link JAXBVotingSet} in a List<br>
 * JAXB-Class
 * 
 * @author w.posdorfer
 * 
 */
@XmlRootElement(name = "results")
public class JAXBListVotingSet
{

    @XmlElement(name = "q")
    private List<JAXBVotingSet> _list;

    /**
     * JAXB ONLY
     */
    @SuppressWarnings("unused")
    private JAXBListVotingSet()
    {
        this(new ArrayList<JAXBVotingSet>());
    }

    /**
     * Creates a list
     * 
     * @param list
     *            the list to use
     */
    public JAXBListVotingSet(List<JAXBVotingSet> list)
    {
        _list = list;
    }

    /**
     * Returns the list
     * 
     * @return the list
     */
    public List<JAXBVotingSet> getList()
    {
        return _list;
    }

}
