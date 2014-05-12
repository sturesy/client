/**
 * 
 */
package sturesy.items.feedback;

/**
 * @author henrik
 *
 */
public class FeedbackTypeComment extends FeedbackTypeModel
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
