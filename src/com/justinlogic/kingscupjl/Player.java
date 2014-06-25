package com.justinlogic.kingscupjl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

public class Player {
	String name;
	List<Card> hand;

	public Player(String name) {
		this.name = name;
		hand = new ArrayList<Card>();
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void addCard(Card card){
		hand.add(card);
	}
	
	public List<TextView> getTextView(Context context, List<Rule> rules){
		List<TextView> views = new ArrayList<TextView>(2);
		views.add(new TextView(context));//cards
		views.add(new TextView(context));//rules
		//TextView textCard = new TextView(context);
		String c = "";
		String r = "";
		if(hand.size() <= 0){
			c = "No cards have been drawn!";
		}
		for(int n = 0; n < hand.size(); n++){
			
			Card card = hand.get(n);
			//int start = cards.length();
			//int end = start + card.getValueString().length();
			c = c + card.getWholeString() + "\n";
			r = r + " //" + rules.get(card.value).name + ": " + rules.get(card.value).description + "\n";
			//cards.setSpan(new ForegroundColorSpan(Color.RED), start, end, 0);  
			//textView.setText(text, BufferType.SPANNABLE);
		}
		views.get(0).setText(name + "\n" + c);
		views.get(0).setTextColor(Color.CYAN);
		views.get(0).setTextSize(18.0f);
		views.get(1).setText("\n" + r);
		views.get(1).setTextColor(Color.GREEN);
		views.get(1).setTextSize(18.0f);
		return views;
	}

}
