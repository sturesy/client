/**
 * 
 */
package sturesy.feedback.editcontroller;

import javax.swing.JPanel;

import sturesy.core.ui.UIObservable;
import sturesy.core.ui.UIObserver;
import sturesy.items.feedback.FeedbackTypeModel;

/**
 * @author henrik
 *
 */
public interface IFeedbackEditController extends UIObservable
{
	/*
	 * Associates this controller with a feedback item
	 * and copies its data to the UI widgets.
	 * 
	 * @param item Feedback Item
	 */
	public void setFeedbackItem(FeedbackTypeModel item);
	
	/*
	 * @return The feedback item associated with this controller
	 */
	public FeedbackTypeModel getFeedbackItem();
	
	/*
	 * Copies data of the UI widgets to the ListModel entry
	 */
	public void synchronizeData();
	
	/*
	 * @return The panel associated with this controller
	 */
	public JPanel getPanel();
	
	public void setObserver(UIObserver o);
}
