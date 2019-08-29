/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author slav_denisov
 */
public class CardModel extends Parent {

    enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    };

    enum Rank {
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10),
        ACE(11);

        final int value;

        private Rank(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public final Suit suit;
    public final Rank rank;
    public final int value;

    public CardModel(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
        this.value = rank.value;
        
        Image image = new Image(getClass().getResourceAsStream("images/" + this.rank + "-" + this.suit + ".png"));
        
        
        // create a rectangle and fit the image into it
        Rectangle cardImg = new Rectangle(120, 170);
        
        // make the rectangle have curvy corners 
        cardImg.setArcWidth(20);
        cardImg.setArcHeight(20);
        
        // fit the image into the rectangle 
        cardImg.setFill(new ImagePattern(image));
        
        // paint the rectangle on screen 
        getChildren().add(new StackPane(cardImg));

    }

    @Override
    public String toString() {
        return rank.toString() + " of " + suit.toString();
    }

}
