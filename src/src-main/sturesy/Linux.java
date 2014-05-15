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
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            catch (Exception e)
            {
            	e.printStackTrace();
            }
		}
	}

}
