/**
 * 
 */
package sturesy;

import javax.swing.UIManager;

import sturesy.core.Operatingsystem;

/**
 * Changes look and feel from standard Java rendering to GTK
 * @author henrik
 *
 */
public class Linux
{
	public static void setLookAndFeelLinux()
	{
		if(Operatingsystem.isLinux())
		{
            try
            {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            }
            catch (Exception e)
            {
            	e.printStackTrace();
            }
		}
	}

}
