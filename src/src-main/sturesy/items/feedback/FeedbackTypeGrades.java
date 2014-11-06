package sturesy.items.feedback;


public class FeedbackTypeGrades extends FeedbackTypeChoice
{
	public FeedbackTypeGrades() {
		super();

		for(int i = 1; i <= 6; i++)
			addChoice(Integer.toString(i));

		setMultipleChoice(false);
	}

	@Override
	public String getTypeLong()
	{
		return "School Grades";
	}
}
