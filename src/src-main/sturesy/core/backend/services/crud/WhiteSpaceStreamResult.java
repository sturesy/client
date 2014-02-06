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
package sturesy.core.backend.services.crud;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.stream.StreamResult;

/**
 * Normal StreamResult replaces whitespaces with %20, this is not supported
 * 
 * @author w.posdorfer
 * 
 */
public class WhiteSpaceStreamResult extends StreamResult
{

    private String _systemId;

    /**
     * Creates a new WhiteSpace conserving StreamResult with given File
     * 
     * @param file
     *            destination File
     */
    public WhiteSpaceStreamResult(File file)
    {
        try
        {
            setSystemId(file.getCanonicalPath());
        }
        catch (IOException e)
        {
            setSystemId(file.getAbsolutePath());
        }
    }

    @Override
    public void setSystemId(String systemId)
    {
        _systemId = systemId;
    }

    @Override
    public String getSystemId()
    {
        return _systemId;
    }

}
