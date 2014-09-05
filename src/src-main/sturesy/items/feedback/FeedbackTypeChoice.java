package sturesy.items.feedback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Single or Multiple Choice feedback question module
 * Created by henrik on 9/5/14.
 */
public class FeedbackTypeChoice extends AbstractFeedbackType {
    private List<String> choices;
    private boolean multipleChoice;

    public FeedbackTypeChoice() {
        choices = new LinkedList<>();
        multipleChoice = false;
    }

    /**
     * Whether multiple choices are allowed for this question
     * @param multipleChoice multiple choices allowed?
     */
    public void setMultipleChoice(boolean multipleChoice) {
        this.multipleChoice = multipleChoice;
    }

    /**
     * @return Whether multiple choices are allowed for this question
     */
    public boolean isMultipleChoice() {
        return multipleChoice;
    }

    /**
     * Adds a choice to the list
     * @param choice Text for choice
     */
    public void addChoice(String choice) {
        choices.add(choice);
    }

    /**
     * Removes a choice at given index
     * @param index Index of choice
     */
    public void deleteChoice(int index) {
        choices.remove(index);
    }

    /**
     * Sets all choices
     * @param choices List of choices
     */
    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    /**
     * Returns all choices
     * @return List of choices
     */
    public List<String> getChoices() {
        return Collections.unmodifiableList(choices);
    }

    /**
     * Returns extra flags for custom/advanced feedback types
     *
     * @return extra flags
     */
    @Override
    public String getExtra() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("multiplechoice", multipleChoice);
        jsonObject.put("choices", choices);

        return jsonObject.toString();
    }

    /**
     * Sets extra flags custom/advanced feedback types
     *
     * @param extra Extra flags to set
     */
    @Override
    public void setExtra(String extra) {
        JSONObject jsonObject = new JSONObject(extra);

        multipleChoice = jsonObject.getBoolean("multiplechoice");

        JSONArray jsonArrayChoices = jsonObject.getJSONArray("choices");
        for (int i = 0; i < jsonArrayChoices.length(); i++) {
            String choice = jsonArrayChoices.getString(i);
            choices.add(choice);
        }

        super.setExtra(extra);
    }

    /**
     * @return Human readable type
     */
    @Override
    public String getTypeLong() {
        return "Single/Multiple Choice";
    }

    /**
     * @return Machine readable type
     */
    @Override
    public String getType() {
        return "choice";
    }
}
