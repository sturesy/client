package sturesy.feedback.gui;

import javax.swing.JSplitPane;

import sturesy.core.ui.SFrame;

public class FeedbackSheetEditorUI extends SFrame
{
	private static final long serialVersionUID = 1640976438402064082L;
	
	private JSplitPane _splitpane;
	private FeedbackQuestionPanelUI _qpanel;
	private FeedbackQuestionEditorPanelUI _epanel;
	
	public FeedbackSheetEditorUI()
	{
		super();
		_splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		_qpanel = new FeedbackQuestionPanelUI();
		_epanel = new FeedbackQuestionEditorPanelUI();
		
		_splitpane.setLeftComponent(_qpanel);
		_splitpane.setRightComponent(_epanel);
		
		add(_splitpane);
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
