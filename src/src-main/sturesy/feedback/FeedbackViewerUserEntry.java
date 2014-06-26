package sturesy.feedback;

import java.util.HashMap;
import java.util.Map;

/**
 * Class used to represent user feedback in a JList
 * Created by henrik on 6/26/14.
 */
public class FeedbackViewerUserEntry {
    private Map<Integer, String> responses;
    private String userId;

    /**
     * Constructor for FeedbackViewerUserEntry
     * @param userId User ID of feedback submitter
     */
    public FeedbackViewerUserEntry(String userId)
    {
        this.responses = new HashMap<>();
        this.userId = userId;
    }

    /**
     * Maps a response to a feedback ID
     * @param fbid Feedback ID
     * @param response Response
     */
    public void setReponse(int fbid, String response)
    {
        responses.put(fbid, response);
    }

    @Override
    public String toString() {
        return userId;
    }
}
