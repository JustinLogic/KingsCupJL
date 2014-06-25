package com.justinlogic.kingscupjl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.justinlogic.kingscupjl.R;

import android.os.Bundle;
import android.os.Handler;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class GameplayActivity extends Activity {
	HorizontalScrollView scrollView;
	RelativeLayout mainView;
	RelativeLayout fadeView;
	Deck deck;
	List<Button> buttons;
	ImageView table;
	Animation animScale;
	Animation animTrans;
	int numPlayers;
	List<Player> players;
	TextView turnView;
	LinearLayout infoView;
	LinearLayout rulesView;
	int playerTurn;
	TextView text;
	int kings;
	List<Rule> rules;
	Button cardClicked;
	TextView ruleView;
	List<String> playerNames;
	boolean clickable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_gameplay);
		clickable = true;
		table = (ImageView) findViewById(R.id.imageView1);
		text = new TextView(this);
		mainView= (RelativeLayout) findViewById(R.id.mainView);
		fadeView= (RelativeLayout) findViewById(R.id.fadeView);
		ruleView = new TextView(this);
		ruleView.setId(32);
		ruleView.setOnClickListener(new TextView.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fadeClick(v);
			}
		});
		turnView = (TextView) findViewById(R.id.turnView);
		infoView = (LinearLayout) findViewById(R.id.infoView);
		rulesView = (LinearLayout) findViewById(R.id.scrollRulesView);
		//handleContent = (LinearLayout) findViewById(R.id.content);
		//handle = (Button) findViewById(R.id.handle);
		//handle.setOnClickListener(new Button.OnClickListener() {
		//	public void onClick(View v) {
		//		slideHandle(v);
	    //    }
		//});
		scrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView1);
		scrollView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				scrollView.getLayoutParams();
				float posX = scrollView.getScrollX();
				float length = scrollView.getWidth();
				float angle = (posX / length) * 360.0f;
				Matrix matrix=new Matrix();
				table.setScaleType(ScaleType.MATRIX);   //required
				float pivX = table.getDrawable().getBounds().width()/2;
				float pivY = table.getDrawable().getBounds().height()/2;
				matrix.postRotate((float) angle, pivX, pivY);
				table.setImageMatrix(matrix);
				return false;
			}
		});
		animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
		//animScale.setFillAfter(true);
		//animScale.setFillEnabled(true);
		animScale.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				//cardView.removeView(cardClicked);
			}
		});
		animTrans = AnimationUtils.loadAnimation(this, R.anim.anim_trans);
		animTrans.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				//Fade in background color
				ObjectAnimator colorFade = ObjectAnimator.ofObject(fadeView, "backgroundColor", new ArgbEvaluator(), Color.argb(0,0,0,0), Color.argb(150,0,0,0));
				colorFade.setDuration(1000);
				colorFade.start();
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				//cardView.removeView(cardClicked);
				final ViewGroup parent = (ViewGroup)cardClicked.getParent();
				parent.post(new Runnable(){
					public void run() {
						//remove card from scrollView
						LinearLayout childView = (LinearLayout) scrollView.getChildAt(0);
						childView.removeView(cardClicked);
						final Handler handler = new Handler();
						handler.postDelayed(new Runnable() {
						  @Override
						  public void run() {
							  displayCard();
						  }
						}, 100);
						
					}
				});
			}
		});
		deck = new Deck(true);
		buttons = new ArrayList<Button>();
		players = new ArrayList<Player>();
		playerNames = new ArrayList<String>();
		kings = 0;
		expandScrollView();
		rules = new ArrayList<Rule>();
		readRules();
		popupNumPlayers();
		
	}
	//Function for populating horizontal scrollView with cards
	public void expandScrollView(){
		for(int n = 0; n < 52; n++){
			Button card = new Button(this);
			card.setId(n);
			card.setBackgroundResource(R.drawable.bg_cardsmallglow);
			card.setHeight(300);
			card.setWidth(150);
			card.setText(deck.DrawRandom().getValueString());
			card.setTextColor(android.R.drawable.btn_default);
			card.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					click(v);
		        }
			});
			buttons.add(card);
			card.setText("");
			LinearLayout childView = (LinearLayout) scrollView.getChildAt(0);
			childView.addView(card);
		}
	}
	//Function for when the user clicks on a card
	public void click(View v) {
		if(clickable == true){
			clickable = false;
			cardClick(v);
		}
	}
	public void fadeClick(View v){
		if(mainView.findViewById(ruleView.getId()) != null){
			//Remove rules
			mainView.removeView(ruleView);
			clickable = true;
			//Fade out background color
			ObjectAnimator colorFade = ObjectAnimator.ofObject(fadeView, "backgroundColor", new ArgbEvaluator(), Color.argb(150,0,0,0), Color.argb(0,0,0,0));
			colorFade.setDuration(1000);
			colorFade.start();
		}
	}
	public void cardClick(View v) {
		int id = v.getId();
		cardClicked = (Button)v;
		//Animate draw
		cardClicked.startAnimation(animTrans);
		//If card is a King
		if(deck.graveyard.get(id).value == 12){
			kings++;
			ImageView image = null;
			switch(kings){
				case 1: image = (ImageView) findViewById(R.id.imageView1);
						break;
				case 2: image = (ImageView) findViewById(R.id.imageView2);
						break;
				case 3: image = (ImageView) findViewById(R.id.imageView3);
						break;
				case 4: image = (ImageView) findViewById(R.id.imageView4);
						break;
			}
			image.setImageResource(R.drawable.king2);
		}
		
		//displayCard();
		//Update player hand and toast card
		players.get(playerTurn).addCard(deck.graveyard.get(id));
		infoViewUpdate();
		Toast.makeText(getApplicationContext(), deck.graveyard.get(id).getWholeString(), Toast.LENGTH_SHORT).show();
	}
	public void incrimentTurn(){
		if(kings < 4){
			//Increment player turn
			playerTurn++;
			if(playerTurn >= numPlayers){
				playerTurn = 0;
			}
			turnView.setText("It's " + players.get(playerTurn).name + "'s turn!");
		}
		else{
			endGame();
		}
	}
	public void displayCard(){
		
		//Change card attributes
		cardClicked.setText(deck.graveyard.get(cardClicked.getId()).getValueString());
		cardClicked.setTextColor(Color.YELLOW);
		cardClicked.setTextSize(16.0f);
		switch(deck.graveyard.get(cardClicked.getId()).suit){
			case 0: cardClicked.setBackgroundResource(R.drawable.spade);
					break;
			case 1: cardClicked.setBackgroundResource(R.drawable.club);
					break;
			case 2: cardClicked.setBackgroundResource(R.drawable.diamond);
					break;
			case 3: cardClicked.setBackgroundResource(R.drawable.heart);
					break;
		}
		//Add card to mainView for animation
		mainView.addView(cardClicked);
		moveViewToScreenCenter(cardClicked);
		mainView.removeView(cardClicked);
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
		  @Override
		  public void run() {
		    displayRule();
		  }
		}, 1200);
	}
	public void displayRule(){
		
		int i = deck.graveyard.get(cardClicked.getId()).value;
		ruleView.setText(rules.get(i).name.replace(" ", "\n"));
		ruleView.setTextColor(Color.YELLOW);
		ruleView.setTextSize(16.0f);
		ruleView.setAlpha(0.0f);
		ruleView.setGravity(Gravity.CENTER);
		ruleView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		//Display display = getWindowManager().getDefaultDisplay();
		//Point size = new Point();
		//display.getSize(size);
		//ruleView.setWidth(size.x);
		
		mainView.addView(ruleView);
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
		  @Override
		  public void run() {
			  ruleView.setAlpha(1.0f);
			  moveViewToScreenCenter(ruleView);
			  incrimentTurn();
			  //ruleView.setWidth(ruleView.getWidth() * 2);
			  //ruleView.setHeight(ruleView.getHeight() * 4);
		  }
		}, 0);
		
	}
	//Function for animating selected cards
	private void moveViewToScreenCenter( View view )
	{
	    //RelativeLayout root = (RelativeLayout) findViewById( R.id.mainView);
	    DisplayMetrics dm = new DisplayMetrics();
	    this.getWindowManager().getDefaultDisplay().getMetrics( dm );
	    //int statusBarOffset = dm.heightPixels - root.getMeasuredHeight();

	    int originalPos[] = new int[2];
	    view.getLocationOnScreen( originalPos );
	    
	    int centerX = (dm.widthPixels / 2) - ((view.getWidth() / 2));
	    int slideOffset = centerX - originalPos[0];

	    int xDest = dm.widthPixels/2;
	    xDest -= (view.getMeasuredWidth()/2);
	    int yDest = dm.heightPixels/2 - ((view.getMeasuredHeight()/2));// - statusBarOffset;
	    //TranslateAnimation anim = new TranslateAnimation(slideOffset, xDest, 0, yDest);

	    TranslateAnimation anim = new TranslateAnimation( slideOffset, xDest - originalPos[0] , 0, yDest - originalPos[1] );
	    anim.setDuration(1000);
	    anim.setFillAfter( true );
	    //animScale.setFillAfter( true );
	    anim.setFillEnabled( true );
	    //animScale.setFillEnabled( true );
	    AnimationSet animSet = new AnimationSet(true);
	    //animSet.setStartOffset(600);;
	    animSet.addAnimation(animScale);
	    animSet.addAnimation(anim);
	    animSet.setFillAfter(true);
	    animSet.setFillEnabled( true );
	    view.startAnimation(animSet);
	}
	//Function for dialog to get number of players
	public void popupNumPlayers(){
		final Dialog dialog = new Dialog(this);
        dialog.setTitle("How many players?");
        dialog.setContentView(R.layout.dialog_num_players);	
        dialog.setCancelable(false);
        Button b1 = (Button) dialog.findViewById(R.id.buttonNumPlayer);
        final NumberPicker np = (NumberPicker) dialog.findViewById(R.id.numberPicker1);
        np.setMaxValue(13); // max value 100
        np.setMinValue(1);   // min value 0
        np.setWrapSelectorWheel(true);
        //np.setOnValueChangedListener(this);
        b1.setOnClickListener(new Button.OnClickListener()
        {
           @Override
           public void onClick(View v) {
           numPlayers = np.getValue();
           for( int n = 0; n < numPlayers; n++){
        	   players.add(new Player("Player" + (n+1)));
        	   //popupNamePlayers();
     	   }
     	   infoViewUpdate();
     	   popupVerifyNamePlayers();
     	   playerTurn = 0;
    	   turnView.setText("It's " + players.get(playerTurn).name + "'s turn!");
    	   turnView.setTextSize(24.0f);
    	   turnView.setTextColor(Color.YELLOW);
     	   dialog.dismiss();
           }    
        });
        dialog.show();
	}
	//Function for dialog to set names of players
	public void popupVerifyNamePlayers(){
		final Dialog dialog = new Dialog(this);
        dialog.setTitle("Give the players names?");
        dialog.setContentView(R.layout.dialog_verify_name_players);	
        dialog.setCancelable(false);
        Button b1 = (Button) dialog.findViewById(R.id.buttonVerifyNameYes);
        Button b2 = (Button) dialog.findViewById(R.id.buttonVerifyNameNo);
        b1.setOnClickListener(new Button.OnClickListener(){
           @Override
           public void onClick(View v) {
        	   popupNamePlayers();
        	   dialog.dismiss();
           }    
        });
        b2.setOnClickListener(new Button.OnClickListener(){
           @Override
           public void onClick(View v) {
        	   dialog.dismiss();
           }    
        });
        dialog.show();
	}
	public void popupNamePlayers(){
		for(int n = players.size() - 1; n >= 0; n--){
			final Dialog dialog = new Dialog(this);
	        dialog.setTitle(players.get(n).name);
	        dialog.setContentView(R.layout.dialog_name_player);	
	        dialog.setCancelable(false);
	        final EditText textBox = (EditText) dialog.findViewById(R.id.editPlayerName);
	        Button b1 = (Button) dialog.findViewById(R.id.buttonNamePlayer);
	        b1.setOnClickListener(new Button.OnClickListener(){
	           @Override
	           public void onClick(View v) {
	        	   String name = textBox.getText().toString();
	        	   playerNames.add(name);
	        	   if(playerNames.size() == players.size()){
	        		   setNewNames();
	        	   }
	        	   dialog.dismiss();
	           }    
	        });
	        dialog.show();
		}
	}
	public void setNewNames(){
		for(int n = 0; n < players.size(); n++){
			if(playerNames.get(n).length() > 0){
				players.get(n).setName(playerNames.get(n));
			}
		}
		infoViewUpdate();
		turnView.setText("It's " + players.get(playerTurn).name + "'s turn!");
	}
	//function for adding player card data to infoView
	public void infoViewUpdate(){
		infoView.removeAllViews();
		rulesView.removeAllViews();
		for(int p = 0; p < players.size(); p++){
			infoView.addView(players.get(p).getTextView(this, rules).get(0));
			rulesView.addView(players.get(p).getTextView(this, rules).get(1));
		}
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
	public void endGame(){
		scrollView.removeAllViews();
		turnView.setText(players.get(playerTurn).name + " drinks the King's Cup!");
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gameplay, menu);
		return true;
	}
	@Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("End Game?");
        dialog.setContentView(R.layout.dialog_verify_name_players);	
        dialog.setCancelable(false);
        Button b1 = (Button) dialog.findViewById(R.id.buttonVerifyNameYes);
        Button b2 = (Button) dialog.findViewById(R.id.buttonVerifyNameNo);
        final Activity act = this;
        b1.setOnClickListener(new Button.OnClickListener(){//yes
           @Override
           public void onClick(View v) {
        	   Intent intent = new Intent(act, TitleScreen.class);
       		   startActivity(intent);
           }    
        });
        b2.setOnClickListener(new Button.OnClickListener(){//no
           @Override
           public void onClick(View v) {
        	   dialog.dismiss();
           }    
        });
        dialog.show();
    }    

}
