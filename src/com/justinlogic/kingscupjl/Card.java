package com.justinlogic.kingscupjl;

public class Card {
	int value;
	int suit;
	public boolean active;
	final CharSequence[] values = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
	final CharSequence[] suits = {"Spades", "Clubs", "Diamonds", "Hearts"};
	
	public Card (int suit, int value, boolean active){
		this.suit = suit;
		this.value = value;
		this.active = active;
	}
	
	public void kill(){
		active = false;
	}
	
	public void revive(){
		active = true;
	}
	
	public boolean active(){
		return active;
	}
	
	public String getValueString(){
		return values[value].toString();
	}
	
	public String getWholeString(){
		return values[value] + " of " + suits[suit];
	}
}


