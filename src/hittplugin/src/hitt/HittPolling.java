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
package hitt;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.TooManyListenersException;

import sturesy.core.plugin.IPollPlugin;
import sturesy.core.plugin.Injectable;
import sturesy.core.plugin.proxy.PLog;
import sturesy.core.plugin.proxy.PSettings;
import sturesy.items.LectureID;
import sturesy.items.QuestionModel;
import sturesy.items.SingleChoiceQuestion;

/**
 * Polling Plugin for the H-iTT Devices
 * 
 * @author w.posdorfer
 * 
 */
public class HittPolling implements IPollPlugin
{

    private Injectable _injectable;

    private CommPortIdentifier _comPortIdent;

    private SerialPortEventListener _serialReader;

    private CommPort _comPort;

    private long _votingStartTime = 0;

    private boolean _usable = false;

    private int _baudrate;

    private SerialPort _serialPort;

    private boolean _isQuestionSupported = false;

    public Delegate _errorDelegate = null;

    public HittPolling()
    {
        this(PSettings.getString(HittSettings.SETTINGS_DEVICE), PSettings.getInteger(HittSettings.SETTINGS_BAUD));
    }

    public HittPolling(String comport, int baudrate)
    {
        _baudrate = baudrate;
        try
        {
            CommPortIdentifier com = CommPortIdentifier.getPortIdentifier(comport);
            _comPortIdent = com;

            _usable = true;
        }
        catch (NoSuchPortException e)
        {
            _usable = false;
            PLog.error("There is no COM-Port called: " + comport, e);
        }
    }

    @Override
    public void setInjectable(Injectable injectable)
    {
        _injectable = injectable;
    }

    @Override
    public void prepareVoting(LectureID lecturenid, final QuestionModel model)
    {
        _isQuestionSupported = (model instanceof SingleChoiceQuestion);
    }

    @Override
    public void startPolling()
    {
        if (!_usable || !_isQuestionSupported)
        {
            return;
        }

        _votingStartTime = Calendar.getInstance().getTimeInMillis();
        try
        {
            _comPort = _comPortIdent.open(this.getClass().getName(), 2000);

            if (_comPort instanceof SerialPort)
            {
                _serialPort = (SerialPort) _comPort;
                _serialPort.setSerialPortParams(_baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);

                InputStream in = _serialPort.getInputStream();

                _serialReader = new SerialReader(in, _injectable, _votingStartTime);

                _serialPort.addEventListener(_serialReader);
                _serialPort.notifyOnDataAvailable(true);
            }
        }
        catch (PortInUseException e)
        {
            reportToDelegate(e);
            PLog.error("Port is alreay in use by " + e.currentOwner, e);
        }
        catch (UnsupportedCommOperationException e)
        {
            reportToDelegate(e);
            PLog.error("Error on setting SerialPort params", e);
        }
        catch (IOException e)
        {
            reportToDelegate(e);
            PLog.error("Error while getting Inputstream", e);
        }
        catch (TooManyListenersException e)
        {
            reportToDelegate(e);
            PLog.error("Error trying to add new EventListener", e);
        }

    }

    private void reportToDelegate(Exception e)
    {
        if (_errorDelegate != null)
        {
            _errorDelegate.reportException(e);
        }
    }

    @Override
    public void stopPolling()
    {
        if (_comPort != null && _usable && _isQuestionSupported)
        {
            ((SerialPort) _comPort).removeEventListener();
            _comPort.close();
        }
    }

    public interface Delegate
    {
        void reportException(Exception ex);
    }

}
