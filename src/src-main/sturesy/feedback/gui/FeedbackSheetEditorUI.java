package sturesy.feedback.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;

import sturesy.core.ui.SFrame;
import sturesy.feedback.editcontroller.IFeedbackEditController;
import sturesy.items.feedback.FeedbackTypeModel;

public class FeedbackSheetEditorUI extends SFrame
{
	private static final long serialVersionUID = 1640976438402064082L;
	
	private JSplitPane _splitpane;
	
	// Left Panel
	private JList<FeedbackTypeModel> _questionlist;
	private JButton _addbutton;
	private JButton _delbutton;
	private JButton _mvupbutton;
	private JButton _mvdownbutton;
	
	private JPanel _rightpanel;
	
	// Action Buttons
	private JButton _submitbutton;
	private JButton _fetchbutton;
	private JButton _clearbutton;
	
	public FeedbackSheetEditorUI(DefaultListModel<FeedbackTypeModel> questions)
	{
		super();
		_splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		JPanel leftpanel = createLeftPanel(questions);
		_rightpanel = createRightPanel(getEmptyEditorPanel());
		
		_splitpane.setLeftComponent(leftpanel);
		_splitpane.setRightComponent(_rightpanel);
		
		add(_splitpane);
	}
	
	private JPanel createLeftPanel(DefaultListModel<FeedbackTypeModel> questions)
	{
		JPanel panel = new JPanel();
		
		// TODO: Localize
		panel.setBorder(BorderFactory.createTitledBorder("Feedback Questions"));
		panel.setLayout(new BorderLayout());
		
		_questionlist = new JList<FeedbackTypeModel>(questions);
		_addbutton = new JButton("+");
		_delbutton = new JButton("-");
		_mvupbutton = new JButton("↑");
		_mvdownbutton = new JButton("↓");
		
		_submitbutton = new JButton("Submit Sheet");
		_fetchbutton = new JButton("Fetch Current Sheet");
		_clearbutton = new JButton("Clear");
		
		// list of questions
		JScrollPane listScrollPane = new JScrollPane(_questionlist);
		_questionlist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		_questionlist.setVisibleRowCount(-1);
		panel.add(listScrollPane, BorderLayout.CENTER);
		
		// button toolbar at the bottom
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout());
		buttonPanel.add(_addbutton);
		buttonPanel.add(_delbutton);
		buttonPanel.add(_mvupbutton);
		buttonPanel.add(_mvdownbutton);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		
		return panel;
	}
	
	public void updateEditorPanel(IFeedbackEditController controller)
	{
		int oldDividerLocation = getDividerLocation();
		JPanel epanel = controller == null ? getEmptyEditorPanel() : controller.getPanel();
		_splitpane.setRightComponent(createRightPanel(epanel));
		
		 // reset divider location as setRightComponent resets the splitpane
		setDividerLocation(oldDividerLocation);
	}
	
	private JPanel getEmptyEditorPanel()
	{
		JPanel panel = new JPanel();
		panel.add(new JLabel("No Item selected"));
		return panel;
	}
	
	private JPanel createRightPanel(JPanel editorPanel)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		panel.add(editorPanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(_submitbutton);
		buttonPanel.add(_fetchbutton);
		buttonPanel.add(_clearbutton);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		
		return panel;
	}
	
	public void setDividerLocation(int location)
	{
		if(location == -1) // no location set thus far
			_splitpane.setDividerLocation(30);
		else
			_splitpane.setDividerLocation(location);
	}
	
	public int getDividerLocation()
	{
		return _splitpane.getDividerLocation();
	}

	public JButton getAddButton()
	{
		return _addbutton;
	}

	public JButton getDelButton()
	{
		return _delbutton;
	}

	public JList<FeedbackTypeModel> getQuestionList()
	{
		return _questionlist;
	}

	public JButton getSubmitButton()
	{
		return _submitbutton;
	}
}
