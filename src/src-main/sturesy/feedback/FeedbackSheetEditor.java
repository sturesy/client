package sturesy.feedback;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
	
	private int _currentQuestion;
	
	public FeedbackSheetEditor()
	{
		_settings = Settings.getInstance();
		_questions = new DefaultListModel<FeedbackTypeModel>();
		_gui = new FeedbackSheetEditorUI(_questions);
		_questionTypes = new LinkedList<Class<? extends FeedbackTypeModel>>();
		_currentQuestion = -1;
		
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
    	_currentQuestion = -1;
    	
    	int[] selected = _gui.getQuestionList().getSelectedIndices();
    	for(int i = selected.length-1; i >=0; i--) {
    		_questions.remove(selected[i]);
    	}
    }
    
    /**
     * {@link ActionListener} for Item Selection
     */
    private void questionSelected()
    {
    	//updateQuestionFromUI();
    	
    	FeedbackTypeModel sel = _gui.getQuestionList().getSelectedValue();
    	
    	_gui.getMandatoryCheckbox().setSelected(sel.isMandatory());
    	_gui.getQuestionTitle().setText(sel.getTitle());
    	_gui.getQuestionDescription().setText(sel.getDescription());
    	
    	_currentQuestion = _gui.getQuestionList().getSelectedIndex();
    }
    
    private void updateQuestionFromUI()
    {
    	if(_currentQuestion != -1) {
    		FeedbackTypeModel currentQuestion = _questions.getElementAt(_currentQuestion);
    		
    		currentQuestion.setTitle(_gui.getQuestionTitle().getText());
    		currentQuestion.setDescription(_gui.getQuestionDescription().getText());
    		currentQuestion.setMandatory(_gui.getMandatoryCheckbox().isSelected());
    		
    		_questions.setElementAt(currentQuestion, _currentQuestion);
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
        
        _gui.getQuestionList().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if (!e.getValueIsAdjusting() && !_gui.getQuestionList().isSelectionEmpty())
					questionSelected();
				else
					_currentQuestion = -1;
			}
		});
        
        DocumentListener uiDocListener = new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateQuestionFromUI();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateQuestionFromUI();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateQuestionFromUI();
			}
		};
        
        _gui.getAddButton().addActionListener(e -> addButtonAction());
        _gui.getDelButton().addActionListener(e -> delButtonAction());
        
        _gui.getQuestionTitle().getDocument().addDocumentListener(uiDocListener);
        _gui.getQuestionDescription().getDocument().addDocumentListener(uiDocListener);
        _gui.getMandatoryCheckbox().addActionListener(e -> updateQuestionFromUI());
        
    }
	
	public JFrame getFrame()
	{
		return _gui;
	}

}
