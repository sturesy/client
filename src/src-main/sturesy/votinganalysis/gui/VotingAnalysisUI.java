/*
 * StuReSy - Student Response System
 * Copyright (C) 2012-2013  StuReSy-Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package sturesy.votinganalysis.gui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import net.miginfocom.swing.MigLayout;
import sturesy.Macintosh;
import sturesy.core.Localize;
import sturesy.core.backend.Loader;
import sturesy.core.ui.JGap;
import sturesy.core.ui.MessageWindow;

/**
 * Graphical component of the voting analysis
 * 
 * @author w.posdorfer
 */
public class VotingAnalysisUI extends JFrame
{

    private static final long serialVersionUID = 8859194873010623035L;
    /**
     * Iconsize for Icons {@value #ICONSIZE}
     */
    private static final int ICONSIZE = 16;
    /**
     * The "Next Question" Button
     */
    private JButton _nextButton;
    /**
     * The "Previous Question" Button
     */
    private JButton _previousButton;
    /**
     * The Label holding the current questions
     */
    private JLabel _currentQuestionLabel;
    /**
     * Centerpanel of this frame
     */
    private JScrollPane _centerPanel;
    /**
     * Table to hold absolute voting details
     */
    private JTable _table;
    /**
     * Button to export to comma-separated-file
     */
    private JButton _exportCSV;
    /**
     * Arithmetic Mean - Label
     */
    private JLabel _mean;
    /**
     * Arithmetic Median Label
     */
    private JLabel _median;

    /**
     * Creates a new Frame
     * 
     * @param qamount
     *            amount of questions total
     * @param evalp
     *            evaluationPanelUI of the current voting
     * @param timechart
     *            timechartUI of the current voting
     */
    public VotingAnalysisUI(int qamount, JPanel evalp, JPanel timechart)
    {
        Macintosh.enableFullScreen(this);
        setIconImage(Loader.getImageIcon(Loader.IMAGE_STURESY).getImage());
        setLayout(new BorderLayout());
        JPanel toolbarpanel = new JPanel();
        _nextButton = new JButton(Loader.getImageIconResized(Loader.IMAGE_NEXT, ICONSIZE, ICONSIZE, Image.SCALE_SMOOTH));
        _previousButton = new JButton(Loader.getImageIconResized(Loader.IMAGE_PREVIOUS, ICONSIZE, ICONSIZE,
                Image.SCALE_SMOOTH));
        _currentQuestionLabel = new JLabel("1 / " + qamount);
        toolbarpanel.add(_previousButton);
        toolbarpanel.add(_currentQuestionLabel);
        toolbarpanel.add(_nextButton);

        JPanel scrollpaneView = new JPanel(new MigLayout("flowy,align center"));

        _table = new JTable(new DefaultTableModel());
        JPanel tablePanel = new JPanel(new BorderLayout());

        tablePanel.add(_table.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(_table, BorderLayout.CENTER);

        _centerPanel = new JScrollPane(scrollpaneView);
        _centerPanel.getVerticalScrollBar().setUnitIncrement(15);

        _table.setAutoCreateRowSorter(true);
        _table.getTableHeader().setReorderingAllowed(false);

        _exportCSV = new JButton("CSV Export");
        _exportCSV.setToolTipText(Localize.getString("tool.analysis.export.csv"));

        JPanel mathPanel = new JPanel();
        _mean = new JLabel(Localize.getString("label.mean", 0));
        _median = new JLabel(Localize.getString("label.median", 0));
        mathPanel.add(_mean);
        mathPanel.add(new JGap(10, 1));
        mathPanel.add(_median);

        scrollpaneView.add(evalp);
        scrollpaneView.add(timechart);
        scrollpaneView.add(mathPanel, "align center");
        scrollpaneView.add(_exportCSV, "align center");
        scrollpaneView.add(tablePanel, "grow");

        add(toolbarpanel, BorderLayout.NORTH);
        add(_centerPanel, BorderLayout.CENTER);
    }

    /**
     * Returns the Next Question Button
     * 
     * @return JButton
     */
    public JButton getNextButton()
    {
        return _nextButton;
    }

    /**
     * Returns the Previous Question Button
     * 
     * @return JButton
     */
    public JButton getPreviousButton()
    {
        return _previousButton;
    }

    /**
     * Return the Export to CSV Button
     * 
     * @return JButton
     */
    public JButton getExportCSV()
    {
        return _exportCSV;
    }

    /**
     * Sets the Label to "current / amount"
     * 
     * @param current
     *            current amount
     * @param amount
     *            maximum amount
     */
    public void setLabelText(int current, int amount)
    {
        _currentQuestionLabel.setText(current + " / " + amount);
    }

    /**
     * sets the Tablemodel of the Table displaying voting details
     * 
     * @param model
     *            model to set on the table
     * 
     * @param trs
     *            tablerowsorter which handles the sorting
     */
    public void setTableModel(final TableModel model, TableRowSorter<TableModel> trs, int[] tablewidth)
    {
        _table.setModel(model);

        for (int i = 0; i < tablewidth.length; i++)
        {
            _table.getColumnModel().getColumn(i).setMinWidth(tablewidth[i]);
            // _table.getColumnModel().getColumn(i).setPreferredWidth(tablewidth[i]);
        }
        _table.setRowSorter(trs);
    }

    /**
     * Returns the Tablemodel of the Table
     * 
     * @return tablemodel
     */
    public TableModel getTableModel()
    {
        return _table.getModel();
    }

    /**
     * Returns the Median Label
     * 
     * @return JLabel
     */
    public JLabel getMedian()
    {
        return _median;
    }

    /**
     * Returns the Mean Label
     * 
     * @return JLabel
     */
    public JLabel getMean()
    {
        return _mean;
    }

    /**
     * Sets the Text to be displayed int the arithmetic mean text
     * 
     * @param arithmeticMeanText
     *            the text
     */
    public void setArithmeticMeanText(String arithmeticMeanText)
    {
        _mean.setText(arithmeticMeanText);
    }

    /**
     * Sets the Text to be displayed int the median text
     * 
     * @param timeMedian
     *            the text
     */
    public void setMedianText(String timeMedian)
    {
        _median.setText(timeMedian);
    }

    /**
     * Shows a JFileChooser-SaveDialog to select a file
     * 
     * @return selected File
     */
    public File acceptSaveFromUser()
    {
        JFileChooser jfc = new JFileChooser();

        jfc.showSaveDialog(this);

        return jfc.getSelectedFile();
    }

    public void showMessageWindowError(String message)
    {
        MessageWindow.showMessageWindowError(message, 1500);
    }
}
