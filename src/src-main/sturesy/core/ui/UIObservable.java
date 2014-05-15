/**
 * 
 */
package sturesy.core.ui;

/**
 * @author henrik
 * Simplified Observer Pattern to notify a single class
 * that a UI change has occured.
 */
public interface UIObservable
{
	/*
	 * Observer that is to be notified when data changes in the UI widgets
	 * @param o Observer to be notified
	 */
	public void setObserver(UIObserver o);
}
