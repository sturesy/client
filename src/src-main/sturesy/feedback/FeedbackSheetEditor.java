package sturesy.feedback;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sturesy.core.Controller;
import sturesy.feedback.gui.FeedbackSheetEditorUI;
import sturesy.qgen.gui.QuestionEditorUI.MenuItems;
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
