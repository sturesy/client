/**
 * 
 */
package sturesy.feedback.editcontroller;

import javax.swing.JPanel;

import sturesy.core.ui.UIObservable;
import sturesy.items.feedback.AbstractFeedbackType;

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
	public void setFeedbackItem(AbstractFeedbackType item);

	/*
	 * @return The feedback item associated with this controller
	 */
	public AbstractFeedbackType getFeedbackItem();

    /*
     * @return The panel associated with this controller
     */
	public JPanel getPanel();
}
