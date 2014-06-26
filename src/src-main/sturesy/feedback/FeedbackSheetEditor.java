package sturesy.feedback;

import org.json.JSONArray;
import org.json.JSONObject;
import sturesy.core.Controller;
import sturesy.core.ui.JMenuItem2;
import sturesy.core.ui.SwappableListModel;
import sturesy.core.ui.UIObserver;
import sturesy.feedback.editcontroller.IFeedbackEditController;
import sturesy.feedback.gui.FeedbackSheetEditorUI;
import sturesy.items.LectureID;
import sturesy.items.feedback.FeedbackTypeMapping;
import sturesy.items.feedback.FeedbackTypeModel;
import sturesy.util.CommonDialogs;
import sturesy.util.Settings;
import sturesy.util.web.WebCommands2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import java.util.List;

/**
 * @author henrik
 */
public class FeedbackSheetEditor implements Controller, UIObserver {
    private FeedbackSheetEditorUI _gui;
    private Settings _settings;

    private SwappableListModel<FeedbackTypeModel> _questions;
    private List<Integer> _deletedFeedbackIds;

    public FeedbackSheetEditor() {
        _settings = Settings.getInstance();
        _questions = new SwappableListModel<>();
        _gui = new FeedbackSheetEditorUI(_questions);
        _deletedFeedbackIds = new LinkedList<Integer>();

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
            // mark for deletion when the new sheet is being uploaded
            FeedbackTypeModel mo = _questions.get(selected[i]);
            if(mo.getId() != 0)
                _deletedFeedbackIds.add(mo.getId());

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

    private void uploadButtonAction() {
        LectureID selectedLecture = CommonDialogs.showLectureSelection();
        if (selectedLecture != null) {
            // convert ListModel to List
            List<FeedbackTypeModel> fbList = new LinkedList<FeedbackTypeModel>();
            for (Object obj : _questions.toArray()) {
                fbList.add((FeedbackTypeModel) obj);
            }

            // delete remotely that were marked for removal locally
            if(_deletedFeedbackIds.size() > 0)
                WebCommands2.deleteFeedbackQuestions(selectedLecture.getHost().toString(),
                    selectedLecture.getLectureID().toString(), selectedLecture.getPassword(), _deletedFeedbackIds);

            // upload new sheet
            WebCommands2.updateFeedbackSheet(selectedLecture.getHost().toString(),
                    selectedLecture.getLectureID(), selectedLecture.getPassword(), fbList);
            downloadLecture(selectedLecture);
        }
    }

    private void downloadButtonAction() {
        LectureID selectedLecture = CommonDialogs.showLectureSelection();
        if (selectedLecture != null) {
            downloadLecture(selectedLecture);
        }
    }

    private void downloadLecture(LectureID lecture) {
        JSONArray response = WebCommands2.downloadFeedbackSheet(lecture.getHost().toString(),
                lecture.getLectureID(), lecture.getPassword());
        if (response != null) {
            // clear current list
            _questions.clear();

            for (int i = 0; i < response.length(); i++) {
                JSONObject jobj = response.getJSONObject(i);
                FeedbackTypeModel mo = FeedbackTypeMapping.instantiateAndInitializeWithJson(jobj);

                if (mo != null) {
                    _questions.addElement(mo);
                } else
                    System.err.println("Invalid Feedback type: " + jobj.getString("type"));
            }
            _deletedFeedbackIds.clear();
            _gui.getQuestionList().updateUI();
            _gui.updateEditorPanel(null);
        }
        else
            JOptionPane.showMessageDialog(this.getFrame(), "Could not download the feedback sheet.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void moveDownButtonAction() {
        int selected = _gui.getQuestionList().getSelectedIndex();
        if(selected != -1 && _questions.size() > selected+1) {
            _questions.swap(selected, selected+1);
            _gui.getQuestionList().setSelectedIndex(selected+1);
            _gui.getQuestionList().updateUI();
        }
    }

    private void moveUpButtonAction() {
        int selected = _gui.getQuestionList().getSelectedIndex();
        if(selected > 0) {
            _questions.swap(selected, selected-1);
            _gui.getQuestionList().setSelectedIndex(selected-1);
            _gui.getQuestionList().updateUI();
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
        _gui.getMoveDownButton().addActionListener(e -> moveDownButtonAction());
        _gui.getMoveUpButton().addActionListener(e -> moveUpButtonAction());
    }

    public JFrame getFrame() {
        return _gui;
    }
}
