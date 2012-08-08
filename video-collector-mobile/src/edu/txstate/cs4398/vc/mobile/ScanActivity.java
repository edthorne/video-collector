package edu.txstate.cs4398.vc.mobile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import edu.txstate.cs4398.vc.mobile.utils.IntentIntegrator;
import edu.txstate.cs4398.vc.mobile.utils.IntentResult;
import edu.txstate.cs4398.vc.mobile.video.VideoMobile;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.kobjects.base64.Base64;
import org.ksoap2.serialization.SoapObject;

public class ScanActivity extends Activity implements View.OnClickListener,
		Listener {
	Button scanButton;
	Button addVideoButton;
	Button lookupUpcButton;
	Button lookupTitleButton;
	Button clearButton;
	EditText upcText;
	EditText videoTitle;
	EditText videoDirector;
	EditText videoYear;
	EditText videoRuntime;
	Spinner ratedSpinner;
	ImageView image;
	private String ipAddress;
	private String imgUrl;
	ProgressBar progressCircle;
	VideoApp appState;
	private static final String UPC = "UPC";
	private static final String TITLE = "TITLE";
	private byte[] currentImageBytes;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
		appState = ((VideoApp) getApplicationContext());
		ipAddress = appState.getWebServiceAddress();

		scanButton = (Button) this.findViewById(R.id.btnScan);
		scanButton.setOnClickListener(this);
		addVideoButton = (Button) this.findViewById(R.id.btnAdd);
		addVideoButton.setOnClickListener(this);
		upcText = (EditText) this.findViewById(R.id.upcText);
		upcText.setInputType(InputType.TYPE_CLASS_NUMBER);
		videoDirector = (EditText) this.findViewById(R.id.videoDirector);
		videoYear = (EditText) this.findViewById(R.id.videoYear);
		videoYear.setInputType(InputType.TYPE_CLASS_NUMBER);
		videoRuntime = (EditText) this.findViewById(R.id.videoRuntime);
		videoRuntime.setInputType(InputType.TYPE_CLASS_NUMBER);
		videoTitle = (EditText) this.findViewById(R.id.videoTitle);
		ratedSpinner = (Spinner) this.findViewById(R.id.ratedSpinner);
		ratedSpinner.setSelection(5);
		progressCircle = (ProgressBar) this.findViewById(R.id.progressScan);
		progressCircle.setVisibility(View.INVISIBLE);
		lookupUpcButton = (Button) this.findViewById(R.id.lookupUpc);
		lookupUpcButton.setOnClickListener(this);
		lookupTitleButton = (Button) this.findViewById(R.id.lookupTitle);
		lookupTitleButton.setOnClickListener(this);
		clearButton = (Button) this.findViewById(R.id.clearAll);
		clearButton.setOnClickListener(this);
		image = (ImageView) this.findViewById(R.id.image);
	}

	public void onClick(View v) {
		if (v.equals(clearButton)) {
			clearAllFields();
		}

		if (v.equals(scanButton)) {
			System.out.println("In onClick dunction");
			IntentIntegrator.initiateScan(this);
		}

		if (v.equals(addVideoButton)) {
			AddVideoTask addTask = new AddVideoTask(this);
			if (currentImageBytes != null)
				addTask.execute(ipAddress, upcText.getText().toString(),
						videoTitle.getText().toString(), videoDirector
								.getText().toString(), ratedSpinner
								.getSelectedItem().toString(), videoRuntime
								.getText().toString(), videoYear.getText()
								.toString(), imgUrl, Base64
								.encode(currentImageBytes));
			else
				addTask.execute(ipAddress, upcText.getText().toString(),
						videoTitle.getText().toString(), videoDirector
								.getText().toString(), ratedSpinner
								.getSelectedItem().toString(), videoRuntime
								.getText().toString(), videoYear.getText()
								.toString(), imgUrl, "");
		}

		if (v.equals(lookupUpcButton)) {
			if (upcText.getText().toString().length() != 12 && !isFinishing()) {
				AlertDialog ad = new AlertDialog.Builder(this).create();
				ad.setCancelable(false); // This blocks the 'BACK' button
				ad.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				ad.setMessage("UPC must be 12 digits");
				ad.show();
			} else {
				GetVideoTask getTask = new GetVideoTask(this);
				progressCircle.setVisibility(View.VISIBLE);
				getTask.execute(ipAddress, UPC, upcText.getText().toString());
			}

		}

		if (v.equals(lookupTitleButton)) {
			if (videoTitle.getText().toString().length() < 1 && !isFinishing()) {
				AlertDialog ad = new AlertDialog.Builder(this).create();
				ad.setCancelable(false); // This blocks the 'BACK' button
				ad.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				ad.setMessage("Title can not be blank");
				ad.show();
			} else {
				GetVideoTask getTask = new GetVideoTask(this);

				progressCircle.setVisibility(View.VISIBLE);
				getTask.execute(ipAddress, TITLE, videoTitle.getText()
						.toString());
			}
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case IntentIntegrator.REQUEST_CODE: {
			if (resultCode != RESULT_CANCELED) {
				IntentResult scanResult = IntentIntegrator.parseActivityResult(
						requestCode, resultCode, data);
				if (scanResult != null) {
					String upc = scanResult.getContents();

					upcText.setText(upc);
					progressCircle.setVisibility(View.VISIBLE);
					GetVideoTask getTask = new GetVideoTask(this);
					getTask.execute(ipAddress, UPC, upc);

				}
			}
		}
		}
	}

	private void transformSoapResponse(SoapObject response) {

		String title = response.getPropertySafelyAsString("title", "");
		if (!title.isEmpty()) {
			videoTitle.setText(title);

			// if Director doesn't exist... leave it empty
			SoapObject director = (SoapObject) response.getPropertySafely("director", new SoapObject("", ""));
			if (!director.toString().isEmpty()) {
				String first = director.getPropertySafelyAsString("firstName",
						"");
				String last = director
						.getPropertySafelyAsString("lastName", "");
				if (!(last.isEmpty() && first.isEmpty())) {
					videoDirector.setText(first + " " + last);
				} else {
					videoDirector.setText("");
				}

			} else {
				videoDirector.setText("");
			}

			String rated = response.getPropertySafelyAsString("rated",
					"UNRATED");
			if (rated.equals("G"))
				ratedSpinner.setSelection(0);
			else if (rated.equals("PG"))
				ratedSpinner.setSelection(1);
			else if (rated.equals("PG13"))
				ratedSpinner.setSelection(2);
			else if (rated.equals("R"))
				ratedSpinner.setSelection(3);
			else if (rated.equals("NC17"))
				ratedSpinner.setSelection(4);
			else
				ratedSpinner.setSelection(5);

			videoYear.setText(response.getPropertySafelyAsString("year", ""));
			videoRuntime.setText(response.getPropertySafelyAsString("runtime",
					""));

			imgUrl = response.getPropertySafelyAsString("imageURL", "");
			if (!imgUrl.isEmpty()) {
				try {
					currentImageBytes = Base64.decode(response
							.getPropertySafelyAsString("image"));
					InputStream is = new ByteArrayInputStream(currentImageBytes);
					Bitmap bmp = BitmapFactory.decodeStream(is);
					image.setImageBitmap(Bitmap.createScaledBitmap(bmp,
							image.getMeasuredWidth(),
							image.getMeasuredHeight(), false));
				} catch (Exception e) {
					image.setImageResource(R.drawable.blank);
				}
			}
		} else if (!isFinishing()) {
			AlertDialog ad = new AlertDialog.Builder(this).create();
			ad.setCancelable(false); // This blocks the 'BACK' button
			ad.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			ad.setMessage("No results found");
			ad.show();

		}

	}

	public void onEvent(TaskEvent task) {

		TaskEvent.Status taskStatus = task.getStatus();
		String action = task.getTask();

		if ("GET_VIDEO".equals(action)) {
			progressCircle.setVisibility(View.INVISIBLE);
			if (taskStatus == TaskEvent.Status.SUCCESS) {
				Log.i("Interfaces", "GET_VIDEO Success");
				transformSoapResponse((SoapObject) task.getResult());
			} else if (!isFinishing()) {
				AlertDialog ad = new AlertDialog.Builder(this).create();
				ad.setCancelable(false); // This blocks the 'BACK' button
				ad.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				ad.setMessage("No response from video lookup services");
				ad.show();
			}
		} else if ("ADD_VIDEO".equals(action)) {
			if (taskStatus == TaskEvent.Status.SUCCESS) {
				String response = ((SoapObject) task.getResult())
						.getPropertyAsString(0);
				if (!"success".equals(response)) {
					if (!isFinishing()) {
						AlertDialog ad = new AlertDialog.Builder(this).create();
						ad.setCancelable(false); // This blocks the 'BACK'
													// button
						ad.setButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						ad.setMessage(response);
						ad.show();
					}
				} else {
					// Show toast notification
					appState.getVideoList().add(getVideoFromFields());
					Toast.makeText(
							this.getApplicationContext(),
							videoTitle.getText().toString()
									+ " succesfully added to collection!",
							Toast.LENGTH_SHORT).show();
					clearAllFields();
				}
			} else if (!isFinishing()) {

				AlertDialog ad = new AlertDialog.Builder(this).create();
				ad.setCancelable(false); // This blocks the 'BACK' button
				ad.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				ad.setMessage("Error sending video to server");
				ad.show();
			}
		}

	}

	private void clearAllFields() {
		upcText.setText("");
		videoDirector.setText("");
		videoYear.setText("");
		videoRuntime.setText("");
		videoTitle.setText("");
		ratedSpinner.setSelection(5);
		image.setImageResource(R.drawable.blank);
		currentImageBytes = null;
	}

	private VideoMobile getVideoFromFields() {
		VideoMobile video = new VideoMobile();
		AlertDialog ad = new AlertDialog.Builder(this).create();
		ad.setCancelable(false); // This blocks the 'BACK' button
		ad.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		ad.setMessage("Error sending video to server");

		if (!videoTitle.getText().toString().isEmpty())
			video.setTitle(videoTitle.getText().toString());
		else {
			ad.setMessage("Title must be Entered");
			ad.show();
			return null;
		}

		video.setDirector(videoDirector.getText().toString());
		video.setRated(ratedSpinner.getSelectedItem().toString());
		if (videoYear.getText().toString().length() > 0)
			video.setYear(Integer.parseInt(videoYear.getText().toString()));
		if (videoRuntime.getText().toString().length() > 0)
			video.setRuntime(Integer
					.parseInt(videoRuntime.getText().toString()));
		video.setImageURL(imgUrl);
		video.setImageBytes(currentImageBytes);
		return video;
	}
}
