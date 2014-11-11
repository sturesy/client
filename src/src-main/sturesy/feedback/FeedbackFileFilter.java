package sturesy.feedback;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * FileFilter for Feedback Sheets or responses stored in JSON
 * Created by henrik on 11/11/14.
 */
public class FeedbackFileFilter extends FileFilter {
    /**
     * Whether the given file is accepted by this filter.
     *
     * @param f Selected File
     */
    @Override
    public boolean accept(File f) {
        // allow navigating the filesystem
        if(f.isDirectory()) {
            return true;
        }

        return getExtension(f) != null && getExtension(f).equals("json");
    }

    /**
     * Get the extension of a file
     * @param f File
     * @return File extension
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    /**
     * The description of this filter.
     */
    @Override
    public String getDescription() {
        return "Feedback Sheet or Responses as JSON";
    }
}
