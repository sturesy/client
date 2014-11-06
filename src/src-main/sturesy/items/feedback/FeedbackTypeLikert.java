package sturesy.items.feedback;

/**
 * Template for the choice module to display a Likert-type scale
 * Created by henrik on 11/6/14.
 */
public class FeedbackTypeLikert extends FeedbackTypeChoice {
    @Override
    public void init() {
        addChoice("strongly agree");
        addChoice("agree");
        addChoice("neutral");
        addChoice("disagree");
        addChoice("strongly disagree");

        setMultipleChoice(false);
    }

    /**
     * @return Human readable type
     */
    @Override
    public String getTypeLong() {
        return "Likert Scale";
    }

    /**
     * @return Machine readable type
     */
    @Override
    public String getType() {
        return super.getType() + ":likert";
    }
}
