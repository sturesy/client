/**
 * 
 */
package sturesy.feedback.editcontroller;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import sturesy.core.ui.UIObserver;
import sturesy.items.feedback.FeedbackTypeModel;

/**
 * @author henrik
 *
 */
public class FeedbackEditControllerBasic implements IFeedbackEditController
{
	private JPanel _panel;
	private JCheckBox _mandatoryCheckbox;
	private JTextField _questiontitle;
	private JTextArea _questiondesc;
	
	private FeedbackTypeModel _item;
	
	private UIObserver _observer;
	
	public FeedbackEditControllerBasic()
	{
		_panel = new JPanel();
		
		_panel.setLayout(new BorderLayout());
		
		//_questiontype = new JComboBox();
		_mandatoryCheckbox = new JCheckBox("Answer is mandatory");
		
		_questiontitle = new JTextField();
		_questiondesc = new JTextArea();
		
		JPanel questionEditPanel = new JPanel();
		questionEditPanel.setLayout(new BoxLayout(questionEditPanel, BoxLayout.PAGE_AXIS));
		questionEditPanel.setBorder(BorderFactory.createTitledBorder("Question Editor"));
		
		//JPanel labelPan = new JPanel(new FlowLayout(FlowLayout.LEFT));
		//labelPan.add(new JLabel("Question Type"));
		//questionEditPanel.add(labelPan);
		
		JPanel qtypePanel = new JPanel();
		qtypePanel.setLayout(new BoxLayout(qtypePanel, BoxLayout.LINE_AXIS));
		//qtypePanel.add(_questiontype);
		qtypePanel.add(_mandatoryCheckbox);
		questionEditPanel.add(qtypePanel);
		
		_questiontitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, _questiontitle.getPreferredSize().height));
		questionEditPanel.add(_questiontitle);
		questionEditPanel.add(_questiondesc);
		_panel.add(questionEditPanel, BorderLayout.CENTER);
		
		initListeners();
	}

	@Override
	public void setFeedbackItem(FeedbackTypeModel item)
	{
		_item = item;
		
		_questiontitle.setText(item.getTitle());
		_questiondesc.setText(item.getDescription());
		_mandatoryCheckbox.setSelected(item.isMandatory());
	}

	@Override
	public JPanel getPanel()
	{
		return _panel;
	}

	@Override
	public void synchronizeData()
	{		
		if(_observer != null) {
			_item.setTitle(_questiontitle.getText());
			_item.setDescription(_questiondesc.getText());
			_item.setMandatory(_mandatoryCheckbox.isSelected());
			_observer.update();
		}
	}
	
	private void initListeners()
	{
      DocumentListener uiDocListener = new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				synchronizeData();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				synchronizeData();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				synchronizeData();
			}
		};
		
		_questiontitle.getDocument().addDocumentListener(uiDocListener);
		_questiondesc.getDocument().addDocumentListener(uiDocListener);
		_mandatoryCheckbox.addActionListener(e -> synchronizeData());
	}

	@Override
	public FeedbackTypeModel getFeedbackItem()
	{
		return _item;
	}

	@Override
	public void setObserver(UIObserver o)
	{
		_observer = o;
	}
}
