/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import blackjack.CardModel.Rank;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 *
 * @author slav_denisov
 */
public class HandModel {

    // we create an observable list of cards 
    // so that we could add cards dynamically 
    private ObservableList<Node> cards;

    // integer property can be bind to something or a listener could be added to it 
    private SimpleIntegerProperty value = new SimpleIntegerProperty(0);

    private int aces = 0; // stores # of aces in the current hand 

    private int numOfDrawnCards = 0;

    public HandModel(ObservableList<Node> cards) {
        this.cards = cards;
    }

    public void takeCard(CardModel card) {
        numOfDrawnCards += 1;
        
        cards.add(card);

        // check if there's an ACE in current hand
        if (card.rank == Rank.ACE) {
            aces++;
        }

        // if there's an ACE in current hand and value exceeds 21
        // make adjustments ( ACE becomes 1 point ) 
        if (value.get() + card.value > 21 && aces > 0) {
            value.set(value.get() + card.value - 10);
            aces--;
        } // else - update value by adding the value of the newest card taken from the deck 
        // to the current total value of the hand 
        else {
            value.set(value.get() + card.value);
        }

    }

    // reset current game 
    public void reset() {
        cards.clear();
        value.set(0);
        aces = 0;
        numOfDrawnCards = 0;
    }

    public SimpleIntegerProperty valueProperty() {
        return value;

    }

    public int getNumOfDrawnCards() {
        return numOfDrawnCards;
    }

}
