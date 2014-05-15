/**
 * 
 */
package sturesy.items.feedback;


/**
 * Base class of which the different feedback question types inherit from
 * @author henrik
 */
public abstract class FeedbackTypeModel
{
	protected String title = "New Item";
	protected String description = "Description";
	
	protected boolean mandatory = false;
	protected String response;
	
	abstract public String getTypeLong();
	abstract public String getType();
	
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public boolean isMandatory()
	{
		return mandatory;
	}
	public void setMandatory(boolean mandatory)
	{
		this.mandatory = mandatory;
	}
	public String getResponse()
	{
		return response;
	}
	public void setResponse(String response)
	{
		this.response = response;
	}
	@Override
	public String toString()
	{
		return getTitle();
	}
}
