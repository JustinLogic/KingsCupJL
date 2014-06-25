package com.justinlogic.kingscupjl;

import com.justinlogic.kingscupjl.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;

public class TitleScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_title_screen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.title_screen, menu);
		return true;
	}
	
	public void newGame(View view){
		Intent intent = new Intent(this, GameplayActivity.class);
		startActivity(intent);
	}
	
	public void changeRules(View view){
		Intent intent = new Intent(this, RulesActivity.class);
		startActivity(intent);
	}

}
