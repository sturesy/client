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
package sturesy.core.error;

/**
 * Models a wrapper Exception for every Exception that occurs while
 * marshalling/unmarshalling
 * 
 * @author w.posdorfer
 * 
 */
public class XMLException extends Throwable
{

    private static final long serialVersionUID = -6476918990532281769L;

    /**
     * Creates a new XMLException
     * 
     * @param message
     */
    public XMLException(String message)
    {
        super(message);
    }

    /**
     * Creates a new XCMLException
     * 
     * @param cause
     */
    public XMLException(Throwable cause)
    {
        super(cause);
    }
}
