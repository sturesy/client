/**
 * 
 */
package sturesy.items.feedback;

/**
 * Base class of which the different feedback question types inherit from
 * 
 * @author henrik
 */
public abstract class FeedbackTypeModel
{
	protected String title = "New Item";
	protected String description = "Description";

	protected boolean mandatory = false;
	protected String response;

	/**
	 * 
	 * @return Human readable type
	 */
	abstract public String getTypeLong();

	/**
	 * 
	 * @return Machine readable type
	 */
	abstract public String getType();

	/**
	 * 
	 * @return Title of question
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * Sets title of question
	 * 
	 * @param title
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * 
	 * @return Description of question
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Sets description
	 * 
	 * @param description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * 
	 * @return Whether this question has to be answered
	 */
	public boolean isMandatory()
	{
		return mandatory;
	}

	/**
	 * Sets whether this question is mandatory
	 * 
	 * @param mandatory
	 */
	public void setMandatory(boolean mandatory)
	{
		this.mandatory = mandatory;
	}

	/**
	 * 
	 * @return Response to this question
	 */
	public String getResponse()
	{
		return response;
	}

	/**
	 * Sets response to this question
	 * 
	 * @param response
	 */
	public void setResponse(String response)
	{
		this.response = response;
	}

	/**
	 * Returns extra flags for custom/advanced feedback types
	 * 
	 * @return extra flags
	 */
	public String getExtra()
	{
		return "";
	}

	@Override
	public String toString()
	{
		return getTitle();
	}
}
