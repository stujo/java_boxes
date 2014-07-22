package com.skillbox.boxes.testing.poker;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;

public class PokerToolsTest {

	@Test
	public void testBestHand() {

		PokerHand[] hands = { Mockito.mock(PokerHand.class),
				Mockito.mock(PokerHand.class), Mockito.mock(PokerHand.class),
				Mockito.mock(PokerHand.class) };

		Mockito.when(hands[0].strengthScore()).thenReturn(100);
		Mockito.when(hands[1].strengthScore()).thenReturn(300);
		Mockito.when(hands[2].strengthScore()).thenReturn(50);
		Mockito.when(hands[3].strengthScore()).thenReturn(200);

		PokerHand bestBet = PokerTools.strongestHand(hands);

		assertSame(bestBet, hands[1]);
	}

}
