/**
 * 
 */
package edu.txstate.cs4398.vc.desktop.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.txstate.cs4398.vc.desktop.controller.VideoController;
import edu.txstate.cs4398.vc.desktop.model.VideoModel;
import edu.txstate.cs4398.vc.model.ModelEvent;
import edu.txstate.cs4398.vc.model.Person;
import edu.txstate.cs4398.vc.model.Rating;
import edu.txstate.cs4398.vc.model.Video;

/**
 * @author Ed
 * 
 */
@SuppressWarnings("serial")
public class VideoView extends JFrameView {

	private JFormattedTextField upc;
	private JTextField title;
	private JComboBox director;
	private JComboBox rated;
	private JFormattedTextField runtime;
	private JSpinner year;
	private JComboBox category;
	private JSlider myRating;
	private JTextArea notes;

	public VideoView(VideoModel model, VideoController controller) {
		super(model, controller);

		// close the application when the frame closes
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// set the window title
		setWindowTitle();

		// get the video being edited from the model
		Video video = getModel().getVideo();

		// create the fields
		JPanel fieldPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc;
		Insets insets = new Insets(5, 5, 5, 5);

		gbc = new GridBagConstraints();
		gbc.insets = insets;
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		fieldPanel.add(new JLabel("UPC"), gbc);

		upc = new JFormattedTextField(video.getUpc());
		gbc = new GridBagConstraints();
		gbc.insets = insets;
		gbc.gridx = 1; gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		fieldPanel.add(upc, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = insets;
		gbc.gridx = 0; gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		fieldPanel.add(new JLabel("Title"), gbc);

		title = new JTextField(video.getTitle());
		gbc = new GridBagConstraints();
		gbc.insets = insets;
		gbc.gridx = 1; gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		fieldPanel.add(title, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = insets;
		gbc.gridx = 0; gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		fieldPanel.add(new JLabel("Director"), gbc);

		director = new JComboBox(getModel().getPeople().toArray());
		director.setSelectedItem(video.getDirector());
		director.setEditable(true);
		gbc = new GridBagConstraints();
		gbc.insets = insets;
		gbc.gridx = 1; gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		fieldPanel.add(director, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = insets;
		gbc.gridx = 0; gbc.gridy = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		fieldPanel.add(new JLabel("Rated"), gbc);

		rated = new JComboBox(Rating.values());
		rated.setSelectedItem(video.getRated());
		gbc = new GridBagConstraints();
		gbc.insets = insets;
		gbc.gridx = 1; gbc.gridy = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		fieldPanel.add(rated, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = insets;
		gbc.gridx = 0; gbc.gridy = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		fieldPanel.add(new JLabel("Runtime"), gbc);

		runtime = new JFormattedTextField(video.getRuntime());
		gbc = new GridBagConstraints();
		gbc.insets = insets;
		gbc.gridx = 1; gbc.gridy = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		fieldPanel.add(runtime, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = insets;
		gbc.gridx = 0; gbc.gridy = 5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		fieldPanel.add(new JLabel("Year"), gbc);

		year = new JSpinner();
		year.setValue(video.getYear());
		gbc = new GridBagConstraints();
		gbc.insets = insets;
		gbc.gridx = 1; gbc.gridy = 5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		fieldPanel.add(year, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = insets;
		gbc.gridx = 0; gbc.gridy = 6;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		fieldPanel.add(new JLabel("Category"), gbc);

		category = new JComboBox(getModel().getCategories().toArray());
		category.setSelectedItem(video.getCategory());
		category.setEditable(true);
		gbc = new GridBagConstraints();
		gbc.insets = insets;
		gbc.gridx = 1; gbc.gridy = 6;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		fieldPanel.add(category, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = insets;
		gbc.gridx = 0; gbc.gridy = 7;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		fieldPanel.add(new JLabel("My Rating"), gbc);

		myRating = new JSlider(0, 5, video.getMyRating());
		myRating.setMajorTickSpacing(1);
		myRating.setPaintTicks(true);
		myRating.setSnapToTicks(true);
		gbc = new GridBagConstraints();
		gbc.insets = insets;
		gbc.gridx = 1; gbc.gridy = 7;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		fieldPanel.add(myRating, gbc);

		gbc = new GridBagConstraints();
		gbc.insets = insets;
		gbc.gridx = 0; gbc.gridy = 8;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.PAGE_START;
		fieldPanel.add(new JLabel("Notes"), gbc);

		notes = new JTextArea(video.getNotes());
		notes.setRows(4);
		notes.setLineWrap(true);
		notes.setWrapStyleWord(true);
		JScrollPane notesScroll = new JScrollPane(notes);
		gbc = new GridBagConstraints();
		gbc.insets = insets;
		gbc.gridx = 1; gbc.gridy = 8;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.PAGE_START;
		fieldPanel.add(notesScroll, gbc);

		add(fieldPanel, BorderLayout.CENTER);

		// create the buttons
		JPanel buttonPanel = new JPanel();
		JButton button = new JButton("Save");
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				getController().save();
			}});
		buttonPanel.add(button);
		button = new JButton("Close");
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				getController().close();
			}});
		buttonPanel.add(button);
		add(buttonPanel, BorderLayout.SOUTH);

		// size everything accordingly
		pack();

		// display centered in the screen now that we know the width
		this.setLocationRelativeTo(null);
	}

	@Override
	public VideoController getController() {
		return (VideoController) super.getController();
	}

	@Override
	public VideoModel getModel() {
		return (VideoModel) super.getModel();
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return (String) category.getSelectedItem();
	}

	/**
	 * @return the director
	 */
	public Person getDirector() {
		// see if the user selected a value
		if (director.getSelectedIndex() == -1) {
			// user didn't select a value, there may be an entry to parse
			String text = (String) director.getSelectedItem();
			// TODO: implement logic
			return null;
		} else {
			// user selected a value
			return (Person) director.getSelectedItem();
		}
	}

	/**
	 * @return the myRating
	 */
	public byte getMyRating() {
		return (byte) myRating.getValue();
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes.getText();
	}

	/**
	 * @return the rating
	 */
	public Rating getRated() {
		return (Rating) rated.getSelectedItem();
	}

	/**
	 * @return the runtime
	 */
	public int getRuntime() {
		return (Integer) runtime.getValue();
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title.getText();
	}

	/**
	 * @return the upc
	 */
	public String getUpc() {
		return upc.getText();
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return (Integer) year.getValue();
	}

	@Override
	public void modelChanged(ModelEvent event) {
		// TODO Auto-generated method stub
	}

	private void setWindowTitle() {
		StringBuilder builder = new StringBuilder();
		builder.append("Video Collector");
		builder.append(" - ");
		Video video = getModel().getVideo();
		if (video.getTitle() != null) {
			builder.append(video.getTitle());
		} else {
			builder.append("Untitled Video");
		}
		setTitle(builder.toString());
	}
}
