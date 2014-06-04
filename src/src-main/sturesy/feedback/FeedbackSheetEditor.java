package sturesy.feedback;

import org.json.JSONArray;
import org.json.JSONObject;
import sturesy.SturesyManager;
import sturesy.core.Controller;
import sturesy.core.ui.JMenuItem2;
import sturesy.core.ui.UIObserver;
import sturesy.feedback.editcontroller.IFeedbackEditController;
import sturesy.feedback.gui.FeedbackSheetEditorUI;
import sturesy.items.LectureID;
import sturesy.items.feedback.FeedbackTypeMapping;
import sturesy.items.feedback.FeedbackTypeModel;
import sturesy.util.Settings;
import sturesy.util.web.WebCommands2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author henrik
 */
public class FeedbackSheetEditor implements Controller, UIObserver {
    private FeedbackSheetEditorUI _gui;
    private Settings _settings;

    private DefaultListModel<FeedbackTypeModel> _questions;

    public FeedbackSheetEditor() {
        _settings = Settings.getInstance();
        _questions = new DefaultListModel<>();
        _gui = new FeedbackSheetEditorUI(_questions);

        addListeners();
    }

    @Override
    public void displayController(Component relativeTo, WindowListener listener) {
        _gui.setSize(_settings.getDimension(Settings.FEEDBACKEDITORSIZE));
        _gui.setDividerLocation(_settings
                .getInteger(Settings.FEEDBACKEDITORDIVIDER));
        _gui.setLocationRelativeTo(relativeTo);
        _gui.setVisible(true);
        _gui.addWindowListener(listener);
    }

    /**
     * Called when this Window is Closing, to save the size
     */
    private void windowIsClosing() {
        Settings settings = _settings;
        settings.setProperty(Settings.FEEDBACKEDITORSIZE, _gui.getSize());
        settings.setProperty(Settings.FEEDBACKEDITORDIVIDER,
                _gui.getDividerLocation());
        settings.save();
    }

    /**
     * {@link ActionListener} for the Add-Button (+)
     */
    private void addButtonAction() {
        JPopupMenu menu = new JPopupMenu();
        for (FeedbackTypeModel mo : FeedbackTypeMapping.getAllFeedbackTypes()) {
            menu.add(new JMenuItem2(mo.getTypeLong(), e -> _questions.addElement(mo)));
        }

        menu.show(_gui.getAddButton(), _gui.getAddButton().getX(), _gui
                .getAddButton().getY());
    }

    /**
     * {@link ActionListener} for the Remove-Button (-)
     */
    private void delButtonAction() {
        int[] selected = _gui.getQuestionList().getSelectedIndices();
        for (int i = selected.length - 1; i >= 0; i--) {
            _questions.remove(selected[i]);
        }
    }

    /**
     * {@link ActionListener} for Item Selection
     */
    private void questionSelected() {
        FeedbackTypeModel sel = _gui.getQuestionList().getSelectedValue();
        IFeedbackEditController controller = FeedbackTypeMapping.getControllerForTypeClass(sel.getClass());

        // avoid unwanted listener calls during panel switch
        controller.setObserver(null);

        controller.setFeedbackItem(sel);
        _gui.updateEditorPanel(controller);
        controller.setObserver(this);
    }

    @Override
    public void update() {
        int sel = _gui.getQuestionList().getSelectedIndex();

        IFeedbackEditController controller = FeedbackTypeMapping.
                getControllerForTypeClass(_questions.get(sel).getClass());
        _questions.setElementAt(controller.getFeedbackItem(), sel);
    }

    private LectureID showLectureSelection() {
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

    private void uploadButtonAction() {
        LectureID selectedLecture = showLectureSelection();
        if (selectedLecture != null) {
            // convert ListModel to List
            List<FeedbackTypeModel> fbList = new LinkedList<>();
            for (Object obj : _questions.toArray()) {
                fbList.add((FeedbackTypeModel) obj);
            }

            String response = WebCommands2.updateFeedbackSheet(selectedLecture.getHost()
                            .toString(), selectedLecture.getLectureID(),
                    selectedLecture.getPassword(), fbList);
            System.out.println(response);
        }
    }

    private void downloadButtonAction() {
        LectureID selectedLecture = showLectureSelection();
        if (selectedLecture != null) {
            JSONArray response = WebCommands2.downloadFeedbackSheet(selectedLecture.getHost().toString(),
                    selectedLecture.getLectureID(), selectedLecture.getPassword());
            if (response != null) {
                // clear current list
                _questions.clear();

                for (int i = 0; i < response.length(); i++) {
                    JSONObject jobj = response.getJSONObject(i);

                    // deserialize json
                    if(jobj.has("type")) {
                        FeedbackTypeModel mo = FeedbackTypeMapping.instantiateObjectForType(jobj.getString("type"));
                        mo.setTitle(jobj.getString("title"));
                        mo.setDescription(jobj.getString("desc"));
                        mo.setMandatory(jobj.getBoolean("mandatory"));
                        mo.setExtra(jobj.getString("extra"));

                        _questions.addElement(mo);
                    }
                }
            }
        }
    }

    /**
     * Adds listeners to all the GUI-Elements
     */
    private void addListeners() {
        _gui.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                windowIsClosing();
            }
        });

        _gui.getQuestionList().addListSelectionListener(
                e -> {
                    if (!e.getValueIsAdjusting()
                            && !_gui.getQuestionList().isSelectionEmpty())
                        questionSelected();
                });

        _gui.getAddButton().addActionListener(e -> addButtonAction());
        _gui.getDelButton().addActionListener(e -> delButtonAction());
        _gui.getUploadButton().addActionListener(e -> uploadButtonAction());
        _gui.getDownloadButton().addActionListener(e -> downloadButtonAction());
    }

    public JFrame getFrame() {
        return _gui;
    }
}
