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
    protected String extra = "";

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
	 * Sets title of Feedback Item
	 * 
	 * @param title Title of Feedback Item
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
	 * @param description Description of feedback item
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
	 * @param mandatory Is question mandatory?
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
	 * @param response User response
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
		return extra;
	}

    /**
     * Sets extra flags custom/advanced feedback types
     *
     * @param extra Extra flags to set
     */
    public void setExtra(String extra) { this.extra = extra; }

	@Override
	public String toString()
	{
		return getTitle();
	}
}
