package com.fitnessfunctionapp;

import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

public class FitnessFunctionApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "3NKc04Mnh7JJYLX2uHhDxsUXE4BJN1xsJQFg6ipv",
				"fLqJVvRB1lcAip2hLStxtU961kSKC4cuepKnvfez");
		
	}

}
