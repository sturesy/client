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

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Window;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.swing.UIManager;

import sturesy.core.Log;
import sturesy.core.Operatingsystem;
import sturesy.core.backend.Loader;

/**
 * Bundles the Mac OSX-only code
 * 
 * @author w.posdorfer
 * 
 */
public class Macintosh
{

    /**
     * This fixes a hanger when the .Jar file is started via double-click
     */
    public static void setLookAndFeelForFileChooser()
    {
        if (Operatingsystem.isMac())
        {
            try
            {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            }
            catch (Exception e)
            {
            }
        }
    }

    public static void setLookAndFeelToAqua()
    {
        if (Operatingsystem.isMac())
        {
            try
            {
                UIManager.setLookAndFeel("com.apple.laf.AquaLookAndFeel");
            }
            catch (Exception e)
            {
            }
        }
    }

    /**
     * Set the Dock Icon to the Standard Icon and set the About-Menu, because
     * the classes in com.apple.*.* are only available on an OSX-JDK we'll be
     * using reflection here to not trigger compile and runtime errors on other
     * operating systems
     */
    public static void executeMacOnlyCode()
    {
        if (Operatingsystem.isMac())
        {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            // Application.getApplication().setDockIconImage(Loader.getImageIcon(Loader.IMAGE_STURESY).getImage());
            // Application.getApplication().setAboutHandler(new AboutHandler()
            // {
            // public void handleAbout(AboutEvent arg0)
            // {
            // AboutMenuUI.showMenu();
            // }
            // });

            try
            {
                Class<?> clazz = Class.forName("com.apple.eawt.Application");
                Method getApplication = clazz.getMethod("getApplication");
                Method setDockIcon = clazz.getMethod("setDockIconImage", Image.class);

                Object application = getApplication.invoke(null);
                setDockIcon.invoke(application, Loader.getImageIcon(Loader.IMAGE_STURESY).getImage());

                Class<?> abouthandlerclass = Class.forName("com.apple.eawt.AboutHandler");
                Method setAboutHandler = clazz.getMethod("setAboutHandler", abouthandlerclass);

                Object abouthandler = Proxy.newProxyInstance(Startup.class.getClassLoader(),
                        new Class<?>[] { abouthandlerclass }, new InvocationHandler()
                        {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
                            {
                                if (method.getName().equals("handleAbout"))
                                {
                                    AboutMenuUI.showMenu();
                                }
                                return null;
                            }
                        });

                setAboutHandler.invoke(application, abouthandler);

            }
            catch (Exception e)
            {
                Log.error("error setting dockicon", e);
            }
        }

    }

    public static void setUpQuitHandler(final ExitAction actionevent)
    {
        // Application.getApplication().setQuitHandler(new QuitHandler()
        // {
        // public void handleQuitRequestWith(QuitEvent arg0, QuitResponse arg1)
        // {
        // System.exit(0);
        // }
        // });

        if (Operatingsystem.isMac())
        {
            try
            {
                Class<?> clazz = Class.forName("com.apple.eawt.Application");
                Method getApplication = clazz.getMethod("getApplication");
                Object application = getApplication.invoke(null);

                Class<?> quithandlerclass = Class.forName("com.apple.eawt.QuitHandler");
                Method setQuitHanlder = clazz.getMethod("setQuitHandler", quithandlerclass);

                Object quithandler = Proxy.newProxyInstance(Startup.class.getClassLoader(),
                        new Class<?>[] { quithandlerclass }, new InvocationHandler()
                        {
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
                            {
                                if (method.getName().equals("handleQuitRequestWith"))
                                {
                                    actionevent.performAction();
                                    System.exit(0);
                                }
                                return null;
                            }
                        });

                setQuitHanlder.invoke(application, quithandler);

            }
            catch (Exception e)
            {
                Log.error("error setting quithandler", e);
            }
        }
    }

    /**
     * Setup the DockMenu items
     * 
     * @param items
     *            MenuItems to use
     */
    public static void setUpDockMenu(MenuItem[] items)
    {
        // Application.getApplication().setDockMenu(popupmenu);
        if (Operatingsystem.isMac())
        {
            try
            {
                Class<?> clazz;
                clazz = Class.forName("com.apple.eawt.Application");
                Method getApplication = clazz.getMethod("getApplication");

                Object application = getApplication.invoke(null);

                Method setDockMenu = clazz.getMethod("setDockMenu", PopupMenu.class);

                PopupMenu dockmenu = new PopupMenu();
                setDockMenu.invoke(application, dockmenu);

                for (MenuItem item : items)
                {
                    dockmenu.add(item);
                }
            }
            catch (Exception e)
            {
                Log.error("error setting dockmenu", e);
            }
        }
    }

    /**
     * Enables the fullscreen features introduced in 10.7
     * 
     * @param window
     *            window to be made available for fullscreen
     */
    public static void enableFullScreen(Window window)
    {
        if (Operatingsystem.isMac())
        {
            try
            {
                Class<?> util = Class.forName("com.apple.eawt.FullScreenUtilities");
                Method method = util.getMethod("setWindowCanFullScreen", new Class[] { Window.class, Boolean.TYPE });
                method.invoke(util, window, true);
            }
            catch (Exception e)
            {
                Log.error("error setting fullscreen", e);
            }
        }
    }
}