package com.fitnessfunctionapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.parse.ParseObject;
import com.parse.ParseUser;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class LegsActivity extends ActionBarActivity implements SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private float mSensorAccY;
    private float averageAcc;
    private boolean recording;
    private List<Float> accYValuesList;
    private CountDownTimer timer;
    TextView textViewTimer, textViewAverageAcc;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_legs);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		mSensorAccY = 0;
		averageAcc = 0;
		
		accYValuesList = new ArrayList<Float>();
		
		//textViewXValue = (TextView)findViewById(R.id.armScore);
		textViewTimer = (TextView)findViewById(R.id.timer);
		textViewAverageAcc = (TextView)findViewById(R.id.averageAcc);
		
		//textViewXValue.setText("X axis: " + String.valueOf(mSensorAccY));
		
		// Get an instance of the SensorManager
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
				
		// Get an instance of the accelerometer 
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.legs, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_legs, container,
					false);
			return rootView;
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub

		Sensor eventSensor = event.sensor;

		// Ensure the reference to the sensor of the event detected is an
		// accelerometer
		if (eventSensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			
			mSensorAccY = event.values[1];
			Log.e(", LegsActivity,",
					String.valueOf(mSensorAccY));
			accYValuesList.add(mSensorAccY);
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
		recording = false;
	}

	public void beginStrengthTest(View view) {

		// Register the accelerometer with the sensor manager to listen for
		// events
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
		recording = true;

		timer = new CountDownTimer(10000, 1000) {

			public void onTick(long millisUntilFinished) {
				textViewTimer.setText("Seconds remaining: "
						+ millisUntilFinished / 1000);
			}

			public void onFinish() {
				textViewTimer.setText("Done!");
				mSensorManager.unregisterListener(LegsActivity.this);
				LegsActivity.this.sortaccYValuesList();
				LegsActivity.this.calculateAverageAcceleration();
				//String averageAccText = String.valueOf(averageAcc);
				textViewAverageAcc.setText(String.format("%.1f", averageAcc));
				storeValues(averageAcc);
			}
		}.start();

	}

	private void sortaccYValuesList() {
		Collections.sort(accYValuesList);
		Collections.reverse(accYValuesList);
	}

	private void calculateAverageAcceleration() {

		float sumOfFirstFiveAcc = 0;

		for (int i = 0; i < 5; i++) {
			sumOfFirstFiveAcc += accYValuesList.get(i);
		}

		averageAcc = sumOfFirstFiveAcc / 5;
		
	}
	
	private void storeValues(float averageAcceleration){
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null){
			ParseObject legsScore = new ParseObject("LegsScore");
			legsScore.put("score", averageAcceleration);
			legsScore.put("playerName", currentUser.getUsername());
			legsScore.saveInBackground();
		}
	}
}
