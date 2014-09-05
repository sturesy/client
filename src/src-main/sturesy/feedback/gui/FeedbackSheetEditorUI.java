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

import sturesy.core.backend.Loader;
import sturesy.core.ui.SFrame;
import sturesy.feedback.editcontroller.IFeedbackEditController;
import sturesy.items.feedback.AbstractFeedbackType;

public class FeedbackSheetEditorUI extends SFrame
{
	private static final long serialVersionUID = 1640976438402064082L;
	
	private JSplitPane _splitpane;
	
	// Left Panel
	private JList<AbstractFeedbackType> _questionlist;
	private JButton _addbutton;
	private JButton _delbutton;
	private JButton _mvupbutton;
	private JButton _mvdownbutton;

    // Action Buttons
	private JButton _uploadbutton;
	private JButton _downloadbutton;
	private JButton _clearbutton;
	
	public FeedbackSheetEditorUI(DefaultListModel<AbstractFeedbackType> questions)
	{
		super();

        setTitle("Feedback-Sheet Editor");
        setIconImage(Loader.getImageIcon(Loader.IMAGE_STURESY).getImage());

		_splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		JPanel leftpanel = createLeftPanel(questions);
        JPanel rightpanel = createRightPanel(getEmptyEditorPanel());
		
		_splitpane.setLeftComponent(leftpanel);
		_splitpane.setRightComponent(rightpanel);
		
		add(_splitpane);
	}
	
	private JPanel createLeftPanel(DefaultListModel<AbstractFeedbackType> questions)
	{
		JPanel panel = new JPanel();
		
		// TODO: Localize
		panel.setBorder(BorderFactory.createTitledBorder("Feedback Questions"));
		panel.setLayout(new BorderLayout());
		
		_questionlist = new JList<>(questions);
		_addbutton = new JButton("+");
		_delbutton = new JButton("-");
		_mvupbutton = new JButton("↑");
		_mvdownbutton = new JButton("↓");
		
		_uploadbutton = new JButton("Upload Sheet");
		_downloadbutton = new JButton("Download Sheet");
		_clearbutton = new JButton("Clear Sheet on Server");
		
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
		buttonPanel.add(_uploadbutton);
		buttonPanel.add(_downloadbutton);
		buttonPanel.add(_clearbutton);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		return panel;
	}
	
	public void setDividerLocation(int location)
	{
		if(location == -1) // no location set thus far
			_splitpane.setDividerLocation(200);
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

	public JList<AbstractFeedbackType> getQuestionList()
	{
		return _questionlist;
	}

	public JButton getUploadButton() { return _uploadbutton; }

    public JButton getDownloadButton() { return _downloadbutton; }

    public JButton getClearButton() {
        return _clearbutton;
    }

    public JButton getMoveUpButton() {
        return _mvupbutton;
    }

    public JButton getMoveDownButton() {
        return _mvdownbutton;
    }
}
