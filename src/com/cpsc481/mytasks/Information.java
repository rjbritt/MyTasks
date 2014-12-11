package com.cpsc481.mytasks;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
 * This is an empty Activity class that simply holds the text to show the Information view.
 * @author rjbritt
 *
 */
public class Information extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_information);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_information, menu);
		return true;
	}

}
