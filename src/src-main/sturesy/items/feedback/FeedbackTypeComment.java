/**
 * 
 */
package sturesy.items.feedback;

/**
 * @author henrik
 *
 */
public class FeedbackTypeComment extends AbstractFeedbackType
{	
	@Override
	public String getTypeLong()
	{
		return "Free Comment";
	}

	@Override
	public String getType()
	{
		return "comment";
	}
}
