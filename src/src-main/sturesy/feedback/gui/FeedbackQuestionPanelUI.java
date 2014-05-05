package sturesy.feedback.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class FeedbackQuestionPanelUI extends JPanel
{
	private static final long serialVersionUID = -7052345684471728797L;
	
	private JList<String> _questionlist;
	
	private JButton _addbutton;
	private JButton _delbutton;
	
	private JButton _mvupbutton;
	private JButton _mvdownbutton;

	public FeedbackQuestionPanelUI()
	{
		super();
		
		// TODO: Localize
		setBorder(BorderFactory.createTitledBorder("Feedback Questions"));
		setLayout(new BorderLayout());
		
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
		add(listScrollPane, BorderLayout.CENTER);
		
		// button toolbar at the bottom
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout());
		buttonPanel.add(_addbutton);
		buttonPanel.add(_delbutton);
		buttonPanel.add(_mvupbutton);
		buttonPanel.add(_mvdownbutton);
		add(buttonPanel, BorderLayout.SOUTH);
	}

}
