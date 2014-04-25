package com.fitnessfunctionapp;

import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class WelcomeActivity extends ActionBarActivity {

	public static final String TAG = WelcomeActivity.class.getSimpleName();
	protected TextView textViewWelcome, textViewAverageScore;
	private float averageScore;
	private String armScore;
	private String legScore;
	private boolean stopped = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		textViewWelcome = (TextView)findViewById(R.id.textViewWelcome);
		textViewAverageScore = (TextView)findViewById(R.id.textViewAverageScore);
		
		armScore="0.0";
		legScore="0.0";
		averageScore = 0;
		

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			Intent intent = new Intent(this, LogonActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
		}
		else {
			String userName = currentUser.getUsername();
			Log.i(TAG, userName);
			textViewWelcome.append(userName + "!");
			
			//getLatestArmScoreByUser(currentUser);
			//getLatestLegScoreByUser(currentUser);
			
			//averageScore = (Float.valueOf(armScore) + Float.valueOf(legScore))/2;
			//textViewAverageScore.setText(String.format("%.1f", averageScore));
		}
		
		
	}

	private void getLatestArmScoreByUser(ParseUser currentUser) {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("ArmScore");
		query.whereEqualTo("playerName", currentUser.getUsername());
		//query.setLimit(1);
		query.getFirstInBackground(new GetCallback<ParseObject>() {
		  public void done(ParseObject object, ParseException e) {
		    if (object == null) {
		      Log.d(TAG, "The getFirst request failed.");
		    } else {
		      Log.d(TAG, "Retrieved the object.");
		      String fetchedArmScore = object.get("score").toString();
		      WelcomeActivity.this.armScore = fetchedArmScore;
		    }
		  }
		});
	}
	
	private void getLatestLegScoreByUser(ParseUser currentUser) {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("LegsScore");
		query.whereEqualTo("playerName", currentUser.getUsername());
		query.getFirstInBackground(new GetCallback<ParseObject>() {
		  public void done(ParseObject object, ParseException e) {
		    if (object == null) {
		      Log.d(TAG, "The getFirst request failed.");
		    } else {
		      Log.d(TAG, "Retrieved the object.");
		      WelcomeActivity.this.legScore = object.get("score").toString();
		    }
		  }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_welcome,
					container, false);
			return rootView;
		}
	}

	public void goToArmsActivity(View view){
		
		Intent intent = new Intent(this, ArmsActivity.class);
		startActivity(intent);
	}
	
	public void goToLegsActivity(View view){
		
		Intent intent = new Intent(this, LegsActivity.class);
		startActivity(intent);
	}

	public void goToStatsActivity(View view){
		
		Intent intent = new Intent(this, StatsActivity.class);
		startActivity(intent);
	}
	
	protected void onResume() {
		super.onResume();
		
			ParseUser currentUser = ParseUser.getCurrentUser();
			if (currentUser == null) {
				Intent intent = new Intent(this, LogonActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
			}
			else {
				String userName = currentUser.getUsername();
				Log.i(TAG, userName);
				
				
				getLatestArmScoreByUser(currentUser);
				getLatestLegScoreByUser(currentUser);
				
				averageScore = (Float.valueOf(armScore) + Float.valueOf(legScore))/2;
				textViewAverageScore.setText(String.format("%.1f", averageScore));
			}
			
		
		
	}
	
}
