package sturesy.feedback.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FeedbackQuestionEditorPanelUI extends JPanel
{
	private static final long serialVersionUID = -9134589290300982271L;
	
	private JComboBox _questiontype;
	private JCheckBox _optionalCheckbox;
	
	private JTextField _questiontitle;
	private JTextArea _questiondesc;
	
	private JButton _submitbutton;
	private JButton _fetchbutton;
	private JButton _clearbutton;

	public FeedbackQuestionEditorPanelUI()
	{
		super();
		
		setLayout(new BorderLayout());
		
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
		add(questionEditPanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(_submitbutton);
		buttonPanel.add(_fetchbutton);
		buttonPanel.add(_clearbutton);
		add(buttonPanel, BorderLayout.SOUTH);
	}
}
