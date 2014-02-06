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
package hitt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Executes a command in a native-commandline file
 * 
 * @author w.posdorfer
 * 
 */
public class ExecClass
{

    public final static int HITT_OK = 0;
    public final static int HITT_ERROR = 1;

    private static final String _sourcefileMac = MainEntry.PATHOFPLUGIN + "/hittcommand ";
    private static final String _sourcefileLinux = _sourcefileMac.replace(" ", "_linux ");
    private static final String _sourcefileWin32 = _sourcefileMac.replace(" ", ".exe ");
    private static final String _sourcefileWin64 = _sourcefileMac.replace(" ", "64.exe ");

    private final String _sourcefile;

    public ExecClass()
    {
        String osname = System.getProperty("os.name").toLowerCase();

        if (osname.contains("mac os x"))
        {
            _sourcefile = _sourcefileMac;
            fixPermissionsMac();
        }
        else if (osname.contains("windows"))
        {
            int arch = Integer.parseInt(System.getProperty("sun.arch.data.model"));
            switch (arch)
            {
            default:
            case 32:
                _sourcefile = _sourcefileWin32;
                break;
            case 64:
                _sourcefile = _sourcefileWin64;
                break;
            }

        }
        else if (osname.contains("linux"))
        {
            _sourcefile = _sourcefileLinux;
        }
        else
        {
            // extend compatibility???
            _sourcefile = "UNSUPPORTED OS";
        }

    }

    /**
     * creates a String for use with the executable file from an Integer-Array <br>
     * <br>
     * <code>new int[]{0 1 2 3 4 5 6 7 8 9}</code> <br>
     * returns <br>
     * <code>new String("0 1 2 3 4 5 6 7 8 9")</code>
     * 
     * @param arr
     */
    private String makeString(int[] arr)
    {
        StringBuffer b = new StringBuffer();

        for (int i = 0; i < 10; i++)
        {
            b.append(arr[i]);
            if (i != 9)
                b.append(" ");
        }
        return b.toString();
    }

    /**
     * Tries to convert the encrypted code into a readable format
     * 
     * @param arr
     * @return {@link HittVote}
     * @throws IOException
     * @throws InterruptedException
     */
    public HittVote getResults(int[] arr) throws IOException, InterruptedException
    {
        HittVote result = new HittVote(0, 0, 0);

        String cmd = _sourcefile + makeString(arr);
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(cmd);
        pr.waitFor();
        BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line = "";

        StringBuffer buffer = new StringBuffer();
        while ((line = buf.readLine()) != null)
        {
            buffer.append(line).append(",");
        }

        String[] split = buffer.toString().split(",");

        result.error = Integer.parseInt(split[0].split(":")[1]);

        result.guid = Integer.parseInt(split[1].split(":")[1]);

        result.vote = Integer.parseInt(split[2].split(":")[1]);

        return result;
    }

    private void fixPermissionsMac()
    {
        try
        {
            String cmd = "chmod +x " + _sourcefileMac;
            Runtime run = Runtime.getRuntime();
            run.exec(cmd);
        }
        catch (IOException e)
        {
        }

    }
}
