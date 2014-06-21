package sturesy.feedback.gui;

import sturesy.core.backend.Loader;
import sturesy.core.ui.SFrame;

/**
 * Created by henrik on 6/22/14.
 */
public class FeedbackViewerUI extends SFrame {
    public FeedbackViewerUI()
    {
        super();
        setTitle("Feedback Viewer");
        setIconImage(Loader.getImageIcon(Loader.IMAGE_STURESY).getImage());
    }
}
