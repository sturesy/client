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

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import sturesy.core.plugin.Injectable;
import sturesy.items.Vote;
import sturesy.items.vote.SingleVote;

/**
 * SerialReaderClass
 * 
 * @author w.posdorfer
 * 
 */
public class SerialReader implements SerialPortEventListener
{
    private final int BYTELENGTH = 10;
    public final int HITT_OK = 0;
    public final int HITT_ERROR = 1;

    private InputStream _inputStream;
    private byte[] buffer = new byte[BYTELENGTH];
    private final Injectable _injectable;
    private final long _votingStartTime;

    private ExecClass _executor;

    private boolean _allowedToPoll;

    /**
     * Creates a new SerialReader reading from an inputstream and injecting
     * Votes into the Injectable
     * 
     * @param inputstream
     *            which InputStream to use
     * @param injectable
     *            which {@link Injectable} to inject votes into
     * @param startingtime
     *            when did the voting start
     */
    public SerialReader(InputStream inputstream, Injectable injectable, long startingtime)
    {

        _inputStream = inputstream;
        _injectable = injectable;
        _votingStartTime = startingtime;
        _executor = new ExecClass();
        _allowedToPoll = false;
    }

    @Override
    public void serialEvent(SerialPortEvent event)
    {

        try
        {
            _inputStream.read(buffer);
            int[] intarr = new int[BYTELENGTH];
            for (int i = 0; i < BYTELENGTH; i++)
            {
                byte b = buffer[i];
                intarr[i] = (0xFF & (int) b);
            }

            if (!_allowedToPoll)
            {
                if (Calendar.getInstance().getTimeInMillis() - _votingStartTime < 600)
                    return; // killing old votes in device-buffer
                else
                    _allowedToPoll = true;
            }

            HittVote hvote = _executor.getResults(intarr);
            if (hvote.error == HITT_OK)
            {
                long time = Calendar.getInstance().getTimeInMillis() - _votingStartTime;
                Vote v = new SingleVote("C" + hvote.guid, time, hvote.vote - 1);
                _injectable.injectVote(v);
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            // happens sometimes when closing the port, doesnt really matter
        }

    }

}
