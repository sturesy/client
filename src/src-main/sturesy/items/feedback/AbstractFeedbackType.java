/**
 * 
 */
package sturesy.items.feedback;

/**
 * Base class of which the different feedback question types inherit from
 * 
 * @author henrik
 */
public abstract class AbstractFeedbackType
{
    protected int id;
	protected String title = "New Item";
	protected String description = "Description";
    protected String extra = "";

	protected boolean mandatory = false;

	/**
	 * Optional: Called when an item is added to the sheet for the first time.
	 * This can be used to modify items in a template-like manner, since it is
	 * not called when the sheet was downloaded from the server.
	 */
	public void init() {
		return;
	}

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

    /**
     * Return Feedback ID
     * @return Feedback ID
     */
    public int getId() {
        return id;
    }

    /**
     * Set Feedback ID
     * @param id Feedback ID
     */
    public void setId(int id) {
        this.id = id;
    }

    @Override
	public String toString()
	{
		return getTitle();
	}
}
