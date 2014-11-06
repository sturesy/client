package sturesy.items.feedback;


public class FeedbackTypeGrades extends FeedbackTypeChoice
{
	@Override
	public void init() {
		for(int i = 1; i <= 6; i++)
			addChoice(Integer.toString(i));

		setMultipleChoice(false);
	}

	@Override
	public String getTypeLong()
	{
		return "School Grades";
	}

	/**
	 * @return Machine readable type
	 */
	@Override
	public String getType() {
		return super.getType() + ":grades";
	}
}
