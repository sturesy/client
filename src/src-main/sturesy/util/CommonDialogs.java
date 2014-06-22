package sturesy.util;

import sturesy.SturesyManager;
import sturesy.items.LectureID;

import javax.swing.*;
import java.util.Collection;

/**
 * Contains dialogs used throughout the project.
 * Created by henrik on 6/22/14.
 */
public class CommonDialogs {
    /**
     * Displays a lecture selection dialog and returns it
     * @return LectureID for selected lecture, otherwise null
     */
    public static LectureID showLectureSelection() {
        Collection<LectureID> lectures = SturesyManager.getLectureIDs();
        LectureID selectedLecture;

        if (lectures.size() == 0) {
            JOptionPane.showMessageDialog(null,
                    "No lectures added. Please add one in the settings menu.",
                    "No lectures", JOptionPane.ERROR_MESSAGE);
            selectedLecture = null;
        } else {
            selectedLecture = (LectureID) JOptionPane.showInputDialog(
                    null, "Select a lecture:",
                    "Select Lecture", JOptionPane.QUESTION_MESSAGE, null,
                    lectures.toArray(), null);
        }

        return selectedLecture;
    }
}
