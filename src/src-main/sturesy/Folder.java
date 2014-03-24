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
package sturesy;

import java.io.File;
import java.io.IOException;

/**
 * Returns Folder paths
 * 
 * @author w.posdorfer
 */
public final class Folder
{

    /**
     * Returns the Base Folder, where properties, lid.xml, logs and internal
     * plugin folder reside<br>
     * under windows/linux it will return <b>"."</b> <br>
     * under mac it will return <b>user.home/Library/Application
     * Support/sturesy/</b>
     */
    public static File getBaseFolder()
    {

        String folderoverride = System.getProperty("sturesy.settingsfolder");

        File result = null;
        if (folderoverride != null)
        {
            result = new File(folderoverride.replace("$USERHOME$", System.getProperty("user.home")));
        }
        else
        {
            try
            {
                result = new File(".").getCanonicalFile();
            }
            catch (IOException e)
            {
                result = new File(".");
            }
        }

        return result;
    }

    private Folder()
    {
    }
}
