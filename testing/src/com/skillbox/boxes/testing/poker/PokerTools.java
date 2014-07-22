package com.skillbox.boxes.testing.poker;

public class PokerTools {
	public static void main(String[] args) {

	}

	public static PokerHand strongestHand(PokerHand[] hands) {
		PokerHand bestBet = hands[0];

		for (int i = 1; i < hands.length; i++) {
			if (hands[i].strengthScore() > bestBet.strengthScore()) {
				bestBet = hands[i];
			}
		}
		return bestBet;
	}

}
