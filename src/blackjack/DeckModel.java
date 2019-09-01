/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import blackjack.CardModel.Rank;
import blackjack.CardModel.Suit;

/**
 *
 * @author slav_denisov
 */
public class DeckModel {

    // create a deck of 52 cards ( initially empty slots )
    private CardModel[] cards = new CardModel[52];

    public DeckModel() {
        refill();
    }

    /**
     * Refills an empty deck with 52 cards.
     */
    public final void refill() {
        int cardCount = 0;

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards[cardCount] = new CardModel(suit, rank);
                cardCount++;
            }
        }

    }

    /**
     * Draws a randomly selected card from the deck of 52 cards.
     *
     * @return the card object
     */
    public CardModel drawCard() {
        CardModel card = null;

        while (card == null) {
            int random = (int) (Math.random() * cards.length);
            card = cards[random];
            cards[random] = null;
        }

        return card;

    }

}
