package sturesy.feedback.editcontroller;

import sturesy.core.ui.ButtonColumn;
import sturesy.items.feedback.AbstractFeedbackType;
import sturesy.items.feedback.FeedbackTypeChoice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;

/**
 * EditController for single/multiple choice module
 * Created by henrik on 9/5/14.
 */
public class FeedbackEditControllerChoice extends FeedbackEditControllerBasic {

    private final JButton addButton;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JCheckBox multipleChoice;

    public FeedbackEditControllerChoice() {
        super();

        multipleChoice = new JCheckBox("Multiple Choice");
        topPanel.add(multipleChoice);

        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(BorderFactory.createTitledBorder("Choices"));

        // top panel containing "add" button
        JPanel header = new JPanel(new BorderLayout());
        addButton = new JButton("Add Choice");
        header.add(addButton, BorderLayout.LINE_END);

        // table with choices
        tableModel = new DefaultTableModel(new String[]{"Choice", "Delete"}, 0);
        table = new JTable(tableModel);
        table.setTableHeader(null);

        // leave 'edit mode' when the cell has lost focus
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        container.add(header, BorderLayout.NORTH);
        container.add(new JScrollPane(table), BorderLayout.CENTER);
        centerPanel.add(container);

        initListeners();
    }

    private void addButtonPressed() {
        addChoice("");
    }

    /**
     * Called when a choice has been added/modified/deleted
     */
    private void tableChanged() {
        FeedbackTypeChoice model = (FeedbackTypeChoice)_item;

        java.util.List<String> choices = new LinkedList<>();

        for (int row = 0; row < tableModel.getRowCount(); row++) {
            String choice = (String)tableModel.getValueAt(row, 0);
            choices.add(choice);
        }

        model.setChoices(choices);

        // update data in JList
        if(_observer != null)
            _observer.update();
    }

    /**
     * Called when the multiple choice checkbox is selected/deselected
     */
    private void checkboxListener() {
        FeedbackTypeChoice model = (FeedbackTypeChoice)_item;
        model.setMultipleChoice(multipleChoice.isSelected());

        // update data in JList
        if(_observer != null)
            _observer.update();
    }

    /**
     * Register listeners
     */
    private void initListeners() {
        addButton.addActionListener(l -> addButtonPressed());
        tableModel.addTableModelListener(l -> tableChanged());
        multipleChoice.addActionListener(l -> checkboxListener());
    }

    /**
     * JTable ,,Delete'' button action
     */
    private Action delete = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
            JTable table = (JTable)e.getSource();
            int modelRow = Integer.valueOf( e.getActionCommand() );
            ((DefaultTableModel)table.getModel()).removeRow(modelRow);
        }
    };

    /**
     * Adds another choice to the table
     * @param text Text for choice
     */
    private void addChoice(String text) {
        tableModel.addRow(new Object[]{text, "Delete"});
        new ButtonColumn(table, delete, 1);
    }

    // Called, when the EditController is given a (new) feedback question
    @Override
    public void setFeedbackItem(AbstractFeedbackType item) {
        super.setFeedbackItem(item);

        FeedbackTypeChoice model = (FeedbackTypeChoice)item;

        // reset the choices when the editcontroller is initialized with a new feedback question object
        List<String> choices = model.getChoices();
        tableModel.setRowCount(0);
        choices.forEach(this::addChoice);

        multipleChoice.setSelected(model.isMultipleChoice());
    }
}
