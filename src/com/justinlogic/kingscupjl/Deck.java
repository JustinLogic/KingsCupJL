package com.justinlogic.kingscupjl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
	//public Card[] cards;
	List<Card> cards;
	List<Card> graveyard;
	Random random = new Random();
	
	public Deck(boolean active){
		cards = new ArrayList<Card>();
		for(int c = 0, s = 0, v = 0; c < 52; c++, v++){
			//if(s >= 4){
			//	s = 0;
			//}
			if(v >= 13){
				v = 0;
				s++;
			}
			//int s = c / 13;
			//int v = c / 4;
			cards.add(new Card(s, v, active)); 
		}
		graveyard = new ArrayList<Card>();
	}
	
	public List<Card> Cards(){
		return cards;
	}
	
	public Card DrawRandom(){
		int rand = random.nextInt(cards.size());
		graveyard.add(cards.get(rand));
		cards.remove(rand);
		return graveyard.get(graveyard.size() - 1);
	}
}
