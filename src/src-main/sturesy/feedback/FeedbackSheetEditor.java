package sturesy.feedback;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.json.JSONArray;
import org.json.JSONObject;

import sturesy.SturesyManager;
import sturesy.core.Controller;
import sturesy.core.ui.JMenuItem2;
import sturesy.core.ui.UIObserver;
import sturesy.feedback.editcontroller.FeedbackEditControllerBasic;
import sturesy.feedback.editcontroller.IFeedbackEditController;
import sturesy.feedback.gui.FeedbackSheetEditorUI;
import sturesy.items.LectureID;
import sturesy.items.feedback.FeedbackTypeComment;
import sturesy.items.feedback.FeedbackTypeGrades;
import sturesy.items.feedback.FeedbackTypeModel;
import sturesy.util.Settings;
import sturesy.util.web.WebCommands2;

/**
 * @author henrik
 *
 */
public class FeedbackSheetEditor implements Controller, UIObserver
{
	private FeedbackSheetEditorUI _gui;
	private Settings _settings;

	private Map<Class<? extends FeedbackTypeModel>, IFeedbackEditController> _questionTypes;

	private DefaultListModel<FeedbackTypeModel> _questions;

	public FeedbackSheetEditor()
	{
		_settings = Settings.getInstance();
		_questions = new DefaultListModel<FeedbackTypeModel>();
		_gui = new FeedbackSheetEditorUI(_questions);
		_questionTypes = new HashMap<Class<? extends FeedbackTypeModel>, IFeedbackEditController>();

		initQuestionTypes();
		addListeners();
	}

	/*
	 * Add all the available feedback question types and associate them with
	 * their controllers.
	 */
	private void initQuestionTypes()
	{
		_questionTypes.put(FeedbackTypeComment.class,
				new FeedbackEditControllerBasic());
		_questionTypes.put(FeedbackTypeGrades.class,
				new FeedbackEditControllerBasic());
	}

	@Override
	public void displayController(Component relativeTo, WindowListener listener)
	{
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
	private void windowIsClosing()
	{
		Settings settings = _settings;
		settings.setProperty(Settings.FEEDBACKEDITORSIZE, _gui.getSize());
		settings.setProperty(Settings.FEEDBACKEDITORDIVIDER,
				_gui.getDividerLocation());
		settings.save();
	}

	/**
	 * {@link ActionListener} for the Add-Button (+)
	 */
	private void addButtonAction()
	{
		JPopupMenu menu = new JPopupMenu();

		for (Class<? extends FeedbackTypeModel> c : _questionTypes.keySet())
		{
			try
			{
				FeedbackTypeModel m = c.newInstance();
				menu.add(new JMenuItem2(m.getTypeLong(), new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						_questions.addElement(m);
					}
				}));
			} catch (IllegalArgumentException | IllegalAccessException
					| InstantiationException | SecurityException e)
			{
				e.printStackTrace();
			}
		}
		menu.show(_gui.getAddButton(), _gui.getAddButton().getX(), _gui
				.getAddButton().getY());
	}

	/**
	 * {@link ActionListener} for the Remove-Button (-)
	 */
	private void delButtonAction()
	{
		int[] selected = _gui.getQuestionList().getSelectedIndices();
		for (int i = selected.length - 1; i >= 0; i--)
		{
			_questions.remove(selected[i]);
		}
	}

	/**
	 * {@link ActionListener} for Item Selection
	 */
	private void questionSelected()
	{
		FeedbackTypeModel sel = _gui.getQuestionList().getSelectedValue();
		IFeedbackEditController controller = _questionTypes.get(sel.getClass());

		// avoid unwanted listener calls during panel switch
		controller.setObserver(null);

		controller.setFeedbackItem(sel);
		_gui.updateEditorPanel(controller);
		controller.setObserver(this);
	}

	@Override
	public void update()
	{
		int sel = _gui.getQuestionList().getSelectedIndex();
		IFeedbackEditController controller = _questionTypes.get(_questions.get(
				sel).getClass());
		_questions.setElementAt(controller.getFeedbackItem(), sel);
	}

	private void submitButtonAction()
	{
		Collection<LectureID> lectures = SturesyManager.getLectureIDs();

		if (lectures.size() == 0)
		{
			JOptionPane.showMessageDialog(null,
					"No lectures added. Please add one in the settings menu.",
					"No lectures", JOptionPane.ERROR_MESSAGE);
			return;
		}

		LectureID selectedLecture = (LectureID) JOptionPane.showInputDialog(
				null, "Select the lecture this feed should be uploaded to:",
				"Select Lecture", JOptionPane.QUESTION_MESSAGE, null,
				lectures.toArray(), null);

		if (selectedLecture != null)
		{
			// convert ListModel to ArrayList
			List<FeedbackTypeModel> fbList = new ArrayList<FeedbackTypeModel>();
			for (Object obj : _questions.toArray())
			{
				fbList.add((FeedbackTypeModel)obj);
			}

			String response = WebCommands2.updateFeedbackSheet(selectedLecture.getHost()
					.toString(), selectedLecture.getLectureID(),
					selectedLecture.getPassword(), fbList);
			System.out.println(response);
		}
	}

	/**
	 * Adds listeners to all the GUI-Elements
	 */
	private void addListeners()
	{
		_gui.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				windowIsClosing();
			}
		});

		_gui.getQuestionList().addListSelectionListener(
				new ListSelectionListener()
				{
					@Override
					public void valueChanged(ListSelectionEvent e)
					{
						if (!e.getValueIsAdjusting()
								&& !_gui.getQuestionList().isSelectionEmpty())
							questionSelected();
					}
				});

		_gui.getAddButton().addActionListener(e -> addButtonAction());
		_gui.getDelButton().addActionListener(e -> delButtonAction());
		_gui.getSubmitButton().addActionListener(e -> submitButtonAction());
	}

	public JFrame getFrame()
	{
		return _gui;
	}
}
