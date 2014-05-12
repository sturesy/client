package sturesy.feedback.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import sturesy.core.ui.SFrame;

public class FeedbackSheetEditorUI extends SFrame
{
	private static final long serialVersionUID = 1640976438402064082L;
	
	private JSplitPane _splitpane;
	
	// Left Panel
	private JList<String> _questionlist;
	private JButton _addbutton;
	private JButton _delbutton;
	private JButton _mvupbutton;
	private JButton _mvdownbutton;
	
	// Right Panel
	private JComboBox _questiontype;
	private JCheckBox _optionalCheckbox;
	private JTextField _questiontitle;
	private JTextArea _questiondesc;
	private JButton _submitbutton;
	private JButton _fetchbutton;
	private JButton _clearbutton;
	
	public FeedbackSheetEditorUI()
	{
		super();
		_splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JPanel leftpanel = createLeftPanel();
		JPanel rightpanel = createRightPanel();
		
		_splitpane.setLeftComponent(leftpanel);
		_splitpane.setRightComponent(rightpanel);
		
		add(_splitpane);
	}
	
	private JPanel createLeftPanel()
	{
		JPanel panel = new JPanel();
		
		// TODO: Localize
		panel.setBorder(BorderFactory.createTitledBorder("Feedback Questions"));
		panel.setLayout(new BorderLayout());
		
		_questionlist = new JList<String>();
		_addbutton = new JButton("+");
		_delbutton = new JButton("-");
		_mvupbutton = new JButton("↑");
		_mvdownbutton = new JButton("↓");
		
		// list of questions
		JScrollPane listScrollPane = new JScrollPane(_questionlist);
		_questionlist.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		_questionlist.setLayoutOrientation(JList.HORIZONTAL_WRAP);
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
	
	private JPanel createRightPanel()
	{
		JPanel panel = new JPanel();
		
		panel.setLayout(new BorderLayout());
		
		_questiontype = new JComboBox();
		_optionalCheckbox = new JCheckBox("Answer is mandatory");
		
		_questiontitle = new JTextField();
		_questiondesc = new JTextArea();
		
		_submitbutton = new JButton("Submit Sheet");
		_fetchbutton = new JButton("Fetch Current Sheet");
		_clearbutton = new JButton("Clear");
		
		JPanel questionEditPanel = new JPanel();
		questionEditPanel.setLayout(new BoxLayout(questionEditPanel, BoxLayout.PAGE_AXIS));
		questionEditPanel.setBorder(BorderFactory.createTitledBorder("Question Editor"));
		
		JPanel labelPan = new JPanel(new FlowLayout(FlowLayout.LEFT));
		labelPan.add(new JLabel("Question Type"));
		questionEditPanel.add(labelPan);
		
		JPanel qtypePanel = new JPanel();
		qtypePanel.setLayout(new BoxLayout(qtypePanel, BoxLayout.LINE_AXIS));
		qtypePanel.add(_questiontype);
		qtypePanel.add(_optionalCheckbox);
		questionEditPanel.add(qtypePanel);
		
		_questiontitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, _questiontitle.getPreferredSize().height));
		questionEditPanel.add(_questiontitle);
		questionEditPanel.add(_questiondesc);
		panel.add(questionEditPanel, BorderLayout.CENTER);
		
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
}
