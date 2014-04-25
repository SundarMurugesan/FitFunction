package com.fitnessfunctionapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class ArmsActivity extends ActionBarActivity implements
		SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private float mSensorAccX;
    private float averageAcc;
    private boolean recording;
    private List<Float> accXValuesList;
    private CountDownTimer timer;
    TextView textViewTimer, textViewAverageAcc;
    public static final String TAG = ArmsActivity.class.getSimpleName();
    //private boolean userHasScore = false;
    //private String userObjectId;

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_arms);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		mSensorAccX = 0;
		averageAcc = 0;
		
		accXValuesList = new ArrayList<Float>();
		
		//textViewXValue = (TextView)findViewById(R.id.armScore);
		textViewTimer = (TextView)findViewById(R.id.timer);
		textViewAverageAcc = (TextView)findViewById(R.id.averageAcc);
		
		//textViewXValue.setText("X axis: " + String.valueOf(mSensorAccX));
		
		// Get an instance of the SensorManager
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
				
		// Get an instance of the accelerometer 
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.arms, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_arms, container,
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
			
			mSensorAccX = event.values[0];
			Log.e(", ArmsActivity,",
					String.valueOf(mSensorAccX));
			accXValuesList.add(mSensorAccX);
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

public void beginArmsTutorial(View view){
		
		Intent intent = new Intent(this, ArmsTutorial.class);
		startActivity(intent);
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
				mSensorManager.unregisterListener(ArmsActivity.this);
				ArmsActivity.this.sortAccXValuesList();
				ArmsActivity.this.calculateAverageAcceleration();
				//String averageAccText = String.valueOf(averageAcc);
				textViewAverageAcc.setText(String.format("%.1f", averageAcc));
				storeValues(averageAcc);
			}
		}.start();

	}

	private void sortAccXValuesList() {
		Collections.sort(accXValuesList);
		Collections.reverse(accXValuesList);
	}

	private void calculateAverageAcceleration() {

		float sumOfFirstFiveAcc = 0;

		for (int i = 0; i < 5; i++) {
			sumOfFirstFiveAcc += accXValuesList.get(i);
		}

		averageAcc = sumOfFirstFiveAcc / 5;
		
	}
	
	private void storeValues(float averageAcceleration){
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		
		if (currentUser != null){
			
			//Check if there is an armscore object
		
			
			//if(!userHasScore){
				ParseObject armScore = new ParseObject("ArmScore");
				armScore.put("score", averageAcceleration);
				armScore.put("playerName", currentUser.getUsername());
				armScore.saveInBackground();
				//userObjectId = armScore.getObjectId();
				//userHasScore = true;
			/*}else{
				
				ParseQuery<ParseObject> query = ParseQuery.getQuery("ArmScore");
				query.getInBackground(userObjectId, 
						
					new GetCallback<ParseObject>() {
						  
						public void done(ParseObject fetchedArmScore, ParseException e) {
							    if (e == null) {
							      
							    	// object will be your arm score
							    	//String armScoreObjectId = fetchedArmScore.getObjectId();
							    	fetchedArmScore.put("score", ArmsActivity.this.averageAcc);
							    	fetchedArmScore.saveInBackground();
							    } else {
							      // something went wrong
							    	Log.e(TAG, "storeValues");
							    }
							  }
						}
				);
			}*/		
			
		}
	}

}
