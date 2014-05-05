package sturesy.feedback;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import sturesy.core.Controller;
import sturesy.feedback.gui.FeedbackSheetEditorUI;
import sturesy.util.Settings;

/**
 * @author henrik
 *
 */
public class FeedbackSheetEditor implements Controller
{
	private FeedbackSheetEditorUI _gui;
	private Settings _settings;
	
	public FeedbackSheetEditor()
	{
		_settings = Settings.getInstance();
		_gui = new FeedbackSheetEditorUI();
		
		addListeners();
	}

	@Override
	public void displayController(Component relativeTo, WindowListener listener)
	{
        _gui.setSize(_settings.getDimension(Settings.FEEDBACKEDITORSIZE));
        _gui.setDividerLocation(_settings.getInteger(Settings.FEEDBACKEDITORDIVIDER));
        _gui.setLocationRelativeTo(relativeTo);
        _gui.setVisible(true);
        _gui.addWindowListener(listener);
	}
	
    /**
     * Called when this Window is Closing, to save the size
     */
    private void windowIsClosing()
    {
        Settings settings = _settings;
        settings.setProperty(Settings.FEEDBACKEDITORSIZE, _gui.getSize());
        settings.setProperty(Settings.FEEDBACKEDITORDIVIDER, _gui.getDividerLocation());
        settings.save();
    }
    
    /**
     * Adds listeners to all the GUI-Elements
     */
    private void addListeners()
    {
        _gui.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                windowIsClosing();
            }
        });
    }
	
	public JFrame getFrame()
	{
		return _gui;
	}

}
