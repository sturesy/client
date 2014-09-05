/**
 * 
 */
package sturesy.feedback.editcontroller;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import sturesy.core.ui.UIObserver;
import sturesy.items.feedback.AbstractFeedbackType;

/**
 * @author henrik
 *
 */
public class FeedbackEditControllerBasic implements IFeedbackEditController
{
    private JPanel _panel;
    private JLabel _typeLabel;
	private JCheckBox _mandatoryCheckbox;
	private JTextField _questiontitle;
	private JTextArea _questiondesc;

	protected AbstractFeedbackType _item;
	protected UIObserver _observer;
    protected final JPanel topPanel;
    protected final JPanel centerPanel;

    public FeedbackEditControllerBasic()
	{
		_panel = new JPanel();
		
		_panel.setLayout(new BorderLayout());
		
		_typeLabel = new JLabel("Type: ");
		_mandatoryCheckbox = new JCheckBox("Answer is mandatory");
		
		_questiontitle = new JTextField();
		_questiondesc = new JTextArea();
        _questiondesc.setLineWrap(true);

        centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));
		centerPanel.setBorder(BorderFactory.createTitledBorder("Question Editor"));

        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
		topPanel.add(_typeLabel);
        topPanel.add(Box.createHorizontalGlue());
		topPanel.add(_mandatoryCheckbox);
		centerPanel.add(topPanel);
		
		_questiontitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, _questiontitle.getPreferredSize().height));
		centerPanel.add(_questiontitle);
		centerPanel.add(_questiondesc);
		_panel.add(centerPanel, BorderLayout.CENTER);
		
		initListeners();
	}

	@Override
	public void setFeedbackItem(AbstractFeedbackType item)
	{
		_item = item;

        _typeLabel.setText("Type: " + item.getTypeLong());
		_questiontitle.setText(item.getTitle());
		_questiondesc.setText(item.getDescription());
		_mandatoryCheckbox.setSelected(item.isMandatory());
	}

	@Override
	public JPanel getPanel()
	{
		return _panel;
	}

	private void synchronizeData()
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
	public AbstractFeedbackType getFeedbackItem()
	{
		return _item;
	}

	@Override
	public void setObserver(UIObserver o)
	{
		_observer = o;
	}
}
