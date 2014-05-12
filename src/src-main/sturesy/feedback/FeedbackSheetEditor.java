package sturesy.feedback;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import sturesy.core.Controller;
import sturesy.core.ui.JMenuItem2;
import sturesy.feedback.gui.FeedbackSheetEditorUI;
import sturesy.items.feedback.FeedbackTypeModel;
import sturesy.items.feedback.FeedbackTypeComment;
import sturesy.items.feedback.FeedbackTypeGrades;
import sturesy.util.Settings;

/**
 * @author henrik
 *
 */
public class FeedbackSheetEditor implements Controller
{
	private FeedbackSheetEditorUI _gui;
	private Settings _settings;
	
	private List<Class<? extends FeedbackTypeModel>> _questionTypes;
	private DefaultListModel<FeedbackTypeModel> _questions;
	
	public FeedbackSheetEditor()
	{
		_settings = Settings.getInstance();
		_questions = new DefaultListModel<FeedbackTypeModel>();
		_gui = new FeedbackSheetEditorUI(_questions);
		_questionTypes = new LinkedList<Class<? extends FeedbackTypeModel>>();
		
		initQuestionTypes();
		addListeners();
	}
	
	/*
	 * Add all the available feedback question types
	 * TODO: It'd be nice to do this dynamically.
	 */
	private void initQuestionTypes()
	{
		_questionTypes.add(FeedbackTypeComment.class);
		_questionTypes.add(FeedbackTypeGrades.class);
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
     * {@link ActionListener} for the Add-Button (+)
     */
    private void addButtonAction()
    {
        JPopupMenu menu = new JPopupMenu();
        
		for(Class<? extends FeedbackTypeModel> c : _questionTypes)
		{
			try
			{
				FeedbackTypeModel m = c.newInstance();
		        menu.add(new JMenuItem2(m.getTypeLong(), new ActionListener()
		        {
		            public void actionPerformed(ActionEvent e)
		            {
		            	_questions.addElement(m);
		            }
		        }));
			} catch (IllegalArgumentException | IllegalAccessException
					| InstantiationException | SecurityException e)
			{
				e.printStackTrace();
			}
		}
		menu.show(_gui.getAddButton(), _gui.getAddButton().getX(), _gui.getAddButton().getY());
    }
    
    /**
     * {@link ActionListener} for the Remove-Button (-)
     */
    private void delButtonAction()
    {
    	int[] selected = _gui.getQuestionList().getSelectedIndices();
    	for(int i = selected.length-1; i >=0; i--) {
    		_questions.remove(i);
    	}
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
        _gui.getAddButton().addActionListener(e -> addButtonAction());
        _gui.getDelButton().addActionListener(e -> delButtonAction());
    }
	
	public JFrame getFrame()
	{
		return _gui;
	}

}
