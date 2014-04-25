package com.fitnessfunctionapp;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageView;
import android.widget.Toast;


public class ArmsTutorial extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		
	

                MyPagerAdapter adapter = new MyPagerAdapter();
                ViewPager myPager = (ViewPager)findViewById(R.id.myfivepanelpager);
               myPager.setAdapter(adapter);
	}
        
        
        public void farLeftButtonClick(View v)
        {
                Toast.makeText(this, "Far Left Button Clicked", Toast.LENGTH_SHORT).show(); 

        }

        public void farRightButtonClick(View v)
        {
                Toast.makeText(this, "Far Right Elephant Button Clicked", Toast.LENGTH_SHORT).show(); 

        }

        private class MyPagerAdapter extends PagerAdapter {

                public int getCount() {
                        return 2;
                }

                public Object instantiateItem(View collection, int position) {

                        LayoutInflater inflater = (LayoutInflater) collection.getContext()
                                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        int resId = 0;
                        switch (position) {
                        case 0:
                                resId = R.layout.howtofirst;
                                break;
                        case 1:
                                resId = R.layout.howtosecond;
                                break;
                       
                        }

                        View view = inflater.inflate(resId, null);

                        ((ViewPager) collection).addView(view, 0);

                        return view;
                }

                @Override
                public void destroyItem(View arg0, int arg1, Object arg2) {
                        ((ViewPager) arg0).removeView((View) arg2);

                }

                @Override
                public void finishUpdate(View arg0) {
                        // TODO Auto-generated method stub

                }

                @Override
                public boolean isViewFromObject(View arg0, Object arg1) {
                        return arg0 == ((View) arg1);

                }

                @Override
                public void restoreState(Parcelable arg0, ClassLoader arg1) {
                        // TODO Auto-generated method stub

                }

                @Override
                public Parcelable saveState() {
                        // TODO Auto-generated method stub
                        return null;
                }

                @Override
                public void startUpdate(View arg0) {
                        // TODO Auto-generated method stub

                }

        }


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.arms_tutorial, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_arms_tutorial,
					container, false);
			return rootView;
		}
	}

}
