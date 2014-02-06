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
package sturesy.update;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import sturesy.Folder;
import sturesy.core.Controller;
import sturesy.core.Localize;
import sturesy.core.Log;
import sturesy.core.Operatingsystem;
import sturesy.core.backend.Loader;
import sturesy.core.error.ErrorController;
import sturesy.util.BackgroundWorker;
import sturesy.util.MathUtils;
import sturesy.util.ZIPExtract;

/**
 * Controller to handle downloading updates
 * 
 * @author w.posdorfer
 * 
 */
public class UpdateDownloader implements Controller
{
    private static final String DOWNLOADFOLDER = "update/";
    private static final String DOWNLOADFILENAME = DOWNLOADFOLDER + "update.zip";
    private static final String UPDATERFILENAME = DOWNLOADFOLDER + "sturesy-update.jar";

    private UpdateDownloaderUI _gui;
    private boolean _isCanceled = false;

    private UpdateInfos _updateInfos;

    /**
     * Creates a new UpdateDownloader
     */
    public UpdateDownloader()
    {
        _gui = new UpdateDownloaderUI();
        addListeners();
    }

    /**
     * Starts the download process
     * 
     * @param updateinfos
     *            which file to download
     */
    public void startDownloading(UpdateInfos updateinfos)
    {
        _updateInfos = updateinfos;
        executeDownload();
    }

    /**
     * Starts the Download of given fileURL in a BackgroundThread
     * 
     */
    private void executeDownload()
    {
        new BackgroundWorker()
        {
            public void inBackground()
            {
                if (_updateInfos == null)
                    return;

                URLConnection connection;
                InputStream input = null;
                OutputStream output = null;
                try
                {
                    URL url = new URL(Operatingsystem.isMac() ? _updateInfos._macDownloadURL
                            : _updateInfos._mirrorList.get(0));
                    connection = url.openConnection();
                    int lenghtOfFile = connection.getContentLength();
                    input = new BufferedInputStream(url.openStream());
                    File f = new File(Folder.getBaseFolder(), DOWNLOADFILENAME);
                    f.mkdirs();
                    f.delete();
                    output = new FileOutputStream(f);

                    byte data[] = new byte[1024];

                    long total = 0;
                    int count = 0;

                    _gui.setTextLabel(Localize.getString("label.download.downloading") + "<br> ");

                    while ((count = input.read(data)) != -1 && !_isCanceled)
                    {
                        total += count;
                        _gui.progressBarSetValue((int) ((total * 100) / lenghtOfFile));
                        _gui.setTextProgressBar(calculateText(total, lenghtOfFile));
                        output.write(data, 0, count);
                    }

                    if (total == lenghtOfFile)
                    {
                        _gui.setTextLabel(Localize.getString("label.download.extract") + "<br> ");

                        extractZIP();

                        _gui.setIconActivityLabel(Loader.getImageIconResized(Loader.IMAGE_OK, 32, 32, 2));
                        _gui.setTextLabel(Localize.getString("label.download.finished") + "<br>"
                                + Localize.getString("label.download.click.restart"));

                        _gui.getRestartButton().setEnabled(true);
                    }
                }
                catch (IOException e)
                {
                    Log.error("download error", e);
                    showErrorInGui(e);
                }
                finally
                {
                    try
                    {
                        if (output != null && input != null)
                        {
                            output.flush();
                            output.close();
                            input.close();
                        }
                    }
                    catch (IOException e)
                    {
                    }
                }

            }
        }.execute();
    }

    /**
     * Calculates the text to be displayed within the progressbar
     * 
     * @param current
     *            amount already downloaded
     * @param total
     *            total filesize
     * @return "OMB / 10MB"
     */
    private String calculateText(long current, long total)
    {
        final int MB = 1000000;
        double mbCurrent = (double) current / MB;
        double mbtotal = (double) total / MB;
        return MathUtils.roundToDecimals(mbCurrent, 1) + "MB / " + MathUtils.roundToDecimals(mbtotal, 1) + "MB";
    }

    /**
     * Extracts the ZIP
     */
    private void extractZIP()
    {
        try
        {
            ZIPExtract.extractFolder(DOWNLOADFILENAME, DOWNLOADFOLDER);
        }
        catch (IOException e)
        {
            Log.error("unziperror", e);
            showError(e);
        }
    }

    /**
     * Action invoked on cancel-button
     */
    private void cancelAction()
    {
        _isCanceled = true;

        File f = new File(DOWNLOADFOLDER);
        if (f.exists())
        {
            f.delete();
        }
        WindowEvent wev = new WindowEvent(_gui.getFrame(), WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }

    /**
     * Action invoked on restart-button
     */
    private void restartAction()
    {
        try
        {
            Desktop.getDesktop().open(new File(UPDATERFILENAME).getCanonicalFile());
            System.exit(0);
        }
        catch (IOException e)
        {
            Log.error("Restart Error", e);
            showError(e);
        }
    }

    private void showErrorInGui(Exception e)
    {
        _gui.setTextLabel(e.getLocalizedMessage());
        _gui.getRestartButton().setText("Wiederholen");
    }

    private void showError(Exception e)
    {
        ErrorController error = new ErrorController();
        error.insertError(Localize.getString("label.download"), e);
        error.show();
    }

    @Override
    public void displayController(Component relativeTo, WindowListener listener)
    {
        if (listener != null)
        {
            _gui.getFrame().addWindowListener(listener);
        }
        _gui.getFrame().setLocationRelativeTo(relativeTo);
        _gui.getFrame().setVisible(true);
    }

    /**
     * Adds Listeners to gui-elements
     */
    private void addListeners()
    {
        _gui.getCancelButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                cancelAction();
            }
        });
        _gui.getRestartButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (true) // TODO restart download action
                    restartAction();
                else
                    executeDownload();
            }
        });
        _gui.getFrame().addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                _isCanceled = true;
            }
        });
    }
}
