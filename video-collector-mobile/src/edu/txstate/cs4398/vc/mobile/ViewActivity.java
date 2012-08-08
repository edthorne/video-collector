package edu.txstate.cs4398.vc.mobile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewActivity extends Activity {

	private String title;
	private String category;
	private String director;
	private int year;
	private int runtime;
	private String rating;
	private String notes;
	private byte[] imageBytes;
	private TextView titleField;
	private TextView catField;
	private TextView dirField;
	private TextView yearField;
	private TextView runField;
	private TextView ratingField;
	private TextView notesField;
	private ImageView image;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.view_layout);

		Log.i("Interfaces", "In new activity");
		title = getIntent().getExtras().getString("title");
		category = getIntent().getExtras().getString("category");
		director = getIntent().getExtras().getString("director");
		year = getIntent().getExtras().getInt("year");
		runtime = getIntent().getExtras().getInt("runtime");
		rating = getIntent().getExtras().getString("rating");
		notes = getIntent().getExtras().getString("notes");
		imageBytes = getIntent().getExtras().getByteArray("image");

		titleField = (TextView) findViewById(R.id.videoTitle);
		catField = (TextView) findViewById(R.id.videoCat);
		dirField = (TextView) findViewById(R.id.videoDirector);
		yearField = (TextView) findViewById(R.id.videoYear);
		runField = (TextView) findViewById(R.id.videoRuntime);
		ratingField = (TextView) findViewById(R.id.videoRated);
		image = (ImageView) findViewById(R.id.thumb_img);
		notesField = (TextView)findViewById(R.id.notes_text);
		Log.i("Interfaces", "my notes: " + notes);
		titleField.setText(title);
		catField.setText(category);
		dirField.setText(director);
		yearField.setText("" + year);
		runField.setText("" + runtime);
		ratingField.setText(rating);
		notesField.setText(notes);
		if (imageBytes != null && imageBytes.length > 0) {
			InputStream is = new ByteArrayInputStream(imageBytes);
			Bitmap bmp = BitmapFactory.decodeStream(is);
			image.setImageBitmap(Bitmap.createBitmap(bmp));
		}

	}
}
