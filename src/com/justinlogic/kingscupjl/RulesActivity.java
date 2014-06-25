package com.justinlogic.kingscupjl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.justinlogic.kingscupjl.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RulesActivity extends Activity {
	LinearLayout rulesView;
	LinearLayout rulesViewCard;
	LinearLayout rulesViewName;
	LinearLayout rulesViewDescription;
	final CharSequence[] cards = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
	List<Rule> rules;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_rules);
		rulesView = (LinearLayout) findViewById(R.id.scrollRulesView);
		rulesViewCard = (LinearLayout) findViewById(R.id.rulesViewCard);
		rulesViewName = (LinearLayout) findViewById(R.id.rulesViewName);
		rulesViewDescription = (LinearLayout) findViewById(R.id.rulesViewDescription);
		rules = new ArrayList<Rule>();
		readRules();
		for(int i = 0; i < 13; i++){
			rulesViewCard.addView(getRuleView(i, "card"));
			rulesViewName.addView(getRuleView(i, "name"));
			rulesViewDescription.addView(getRuleView(i, "description"));
		}
	}
	
	public View getRuleView(int i, String type){
		View view = new View(this);
		if(type == "card"){
			EditText cardName = new EditText(this);
			cardName.setText(cards[i] + ":");
			cardName.setTextColor(Color.CYAN);
			cardName.setTextSize(24.0f);
			cardName.setFocusable(false);
			view = cardName;
		}
		else if(type == "name"){
			EditText ruleName = new EditText(this);
			ruleName.setText(rules.get(i).name);
			ruleName.setTextColor(Color.YELLOW);
			ruleName.setTextSize(24.0f);
			view = ruleName;
		}
		else if(type == "description"){
			EditText ruleDescription = new EditText(this);
			ruleDescription.setText(rules.get(i).description);
			ruleDescription.setTextColor(Color.GREEN);
			ruleDescription.setTextSize(24.0f);
			view = ruleDescription;
		}
		return view;
	}
	
	//Rules
	public void setRules(){
		rules.add(new Rule("Waterfall!", "All Players participate in Waterfall!"));//Ace
		rules.add(new Rule("Two is for You!", "Make another player drink!"));//Two
		rules.add(new Rule("Three is for Me!", "You take a drink!"));//Three
		rules.add(new Rule("Four is for Whores!", "All the ladies drink!"));//Four
		rules.add(new Rule("Make a Rule!", "Create your own rule!"));//Five
		rules.add(new Rule("Six is for Dicks!", "All the men drink!"));//Six
		rules.add(new Rule("Heaven!", "Last person with their arms up takes a drink!"));//Seven
		rules.add(new Rule("Pick a Mate!", "Pick a Player to be your mate. They now have to drink whenever you drink."));//Eight
		rules.add(new Rule("Rhyme!", "Start a rhyme! First person to fail at rhyming your word must drink!"));//Nine
		rules.add(new Rule("Categories!", "Pick a category, first person that fails to say something in that category must drink!"));//Ten
		rules.add(new Rule("Thumb Master!", "You are now the Thumb Master! Put your thumb down at any time, last person with their thumb down must drink!"));//Jack
		rules.add(new Rule("Questions!", "First person that fails at asking a question must drink!"));//Queen
		rules.add(new Rule("King!", "Pour some of your drink into the King's Cup! If you drew the last King, drink the whole cup!"));//King
	}
	
	public void save(View v){
		String filename = "rules";
		FileOutputStream outputStream;
		try {
		  outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
		  for(int i = 0; i < rules.size(); i++){
			  EditText textName = (EditText) rulesViewName.getChildAt(i);
			  outputStream.write((textName.getText() + "\n").getBytes());
			  EditText textDescription = (EditText) rulesViewDescription.getChildAt(i);
			  outputStream.write((textDescription.getText() + "\n").getBytes());
		  }
		  outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
		Toast.makeText(getApplicationContext(), "Rules Saved!", Toast.LENGTH_SHORT).show();
	}
	
	private void readRules() {
	    try {
	        InputStream inputStream = openFileInput("rules");
	        if ( inputStream != null ) {//if custom rules exist
	            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
	            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	            StringBuilder stringBuilder = new StringBuilder();
	            String name = "";
	            String description = "";
	            String receiveString = "";
	            int line = 0;
	            while ( (receiveString = bufferedReader.readLine()) != null ) {
	                stringBuilder.append(receiveString);
	                if(line == 0){
	                	name = receiveString;
	                	line++;
	                }
	                else{
	                	description = receiveString;
	                	rules.add(new Rule(name, description));
	                	line = 0;
	                }
	            }
	            inputStream.close();
	        }
	        else{//set default rules
	        	setRules();
	        }
	    }
	    catch (FileNotFoundException e) {
	        Log.e("login activity", "File not found: " + e.toString());
	        setRules();
	    } catch (IOException e) {
	        Log.e("login activity", "Can not read file: " + e.toString());
	    }
	}
	
	public void restoreDefaults(View v){
		rules.clear();
		setRules();
		rulesViewCard.removeAllViews();
		rulesViewName.removeAllViews();
		rulesViewDescription.removeAllViews();
		for(int i = 0; i < 13; i++){
			rulesViewCard.addView(getRuleView(i, "card"));
			rulesViewName.addView(getRuleView(i, "name"));
			rulesViewDescription.addView(getRuleView(i, "description"));
		}
		Toast.makeText(getApplicationContext(), "Default rules have been restored!", Toast.LENGTH_SHORT).show();
		save(v);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rules, menu);
		return true;
	}

}
