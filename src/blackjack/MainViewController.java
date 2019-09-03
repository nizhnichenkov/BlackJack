/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML MainViewController class
 *
 * @author slav_denisov
 */
public class MainViewController implements Initializable {

    private DeckModel deck;
    private HandModel dealer, player;
    private SimpleIntegerProperty balance = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty bet = new SimpleIntegerProperty(0);
    private Boolean endMethodSemaphore = false;

    @FXML
    Text dealerScoreText;
    @FXML
    Text playerScoreText;
    @FXML
    Text userNotificationText;
    @FXML
    Text balanceText; // amount of money 
    @FXML
    Text betText;
    @FXML
    Text moneyBetText;
    @FXML
    TextField inputMoneyBet; // player enters amount to bet 
    @FXML
    HBox dealerCardsHBox;
    @FXML
    HBox playerCardsHBox;
    @FXML
    Button betInNewGameButton;
    @FXML
    Button HitButton;
    @FXML
    Button StandButton;
    @FXML
    ImageView dollarBillImageView;

    @FXML
    private void settingUpGame() {

        // initialize dealer, player objects 
        // connect them to HBOX that represent them in the GUI
        dealer = new HandModel(dealerCardsHBox.getChildren());
        player = new HandModel(playerCardsHBox.getChildren());

        // initialize the deck 
        deck = new DeckModel();

        balance.set(1000);
        balanceText.setVisible(true);

        // disable buttons and objects 
        userNotificationText.setVisible(false);
        HitButton.setVisible(false);
        StandButton.setVisible(false);
        moneyBetText.setVisible(false);
        userNotificationText.setVisible(false);

        // enable buttons and objects 
        betInNewGameButton.setVisible(true);
        dollarBillImageView.setVisible(true);
        balanceText.setVisible(true);
        dollarBillImageView.setVisible(true);
        inputMoneyBet.setVisible(true);

        // bind balanceText text field to player's current balance 
        balanceText.textProperty().bind(new SimpleStringProperty("").concat(balance.asString()));

        // bind simple integer property bet to the moneyBet text field 
        moneyBetText.textProperty().bind(new SimpleStringProperty("").concat(bet.asString()));

        // bind player, dealer integer value to GUI's text fields representing their scores  
        playerScoreText.textProperty().bind(new SimpleStringProperty("").concat(player.valueProperty().asString()));
        dealerScoreText.textProperty().bind(new SimpleStringProperty("").concat(dealer.valueProperty().asString()));

        /**
         * Validate the input bet from the user. This piece of code doesn't let the user input anything else but integers in the range [0-9]
         */
        inputMoneyBet.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // validate user input ( make only integers available to be typed in ) 
                // and replace every illegal character with "" 
                if (!newValue.matches("\\d*")) {
                    inputMoneyBet.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        player.valueProperty().addListener((obs, old, newValue) -> {
            if (newValue.intValue() >= 21) {
                if (!endMethodSemaphore) {
                    endGame();
                }
            }
        });

        dealer.valueProperty().addListener((obs, old, newValue) -> {
            if (newValue.intValue() >= 21) {
                if (!endMethodSemaphore) {
                    endGame(); // call end method 
                }
            }
        });

        /**
         * Add action to hit button. Whenever the hit button is pressed - the player will draw a card from the deck.
         */
        HitButton.setOnAction(e
                -> {
            player.takeCard(deck.drawCard());
            System.out.println("Num of drawn cards: " + player.getNumOfDrawnCards());
        }
        );

        /**
         * Add action to stand button. Whenever this button is pressed the dealer will draw a card from the deck until his points become equal to or greater than 17.
         */
        StandButton.setOnAction(e
                -> {
            while (dealer.valueProperty().get() < 17 || dealer.valueProperty().get() < player.valueProperty().get()) {
                dealer.takeCard(deck.drawCard());
            }
            if (dealer.valueProperty().intValue() < 21) {
                if (!endMethodSemaphore) {
                    endGame();
                }
            }
        }
        );

        /**
         * This method validation does not let the player to enter a bet that is higher than the available balance or less than or equal to 0.
         */
        inputMoneyBet.textProperty().addListener((obs, old, newValue) -> {
            if (newValue.equals("")) {
                betInNewGameButton.setDisable(true);
            }

            try {
                if (Integer.parseInt(newValue) <= 0 || Integer.parseInt(newValue) > balance.get()) {
                    betInNewGameButton.setDisable(true);
                }

            } catch (NumberFormatException e) {
                System.out.println("INTEGER PARSER EXCEPTION");
            }

            try {
                if (Integer.parseInt(newValue) > 0 && Integer.parseInt(newValue) <= balance.get()) {
                    betInNewGameButton.setDisable(false);
                }

            } catch (NumberFormatException e) {
                System.out.println("INTEGER PARSER EXCEPTION.");
            }

        }
        );

    }

    /**
     * Starts a new round of Blackjack
     */
    @FXML
    private void startNewRound() {
        endMethodSemaphore = false;

        player.reset();
        dealer.reset();
        deck.refill();

        bet.set(Integer.parseInt(inputMoneyBet.getText()));

        System.out.println("BALANCE BEFORE BET: " + balance.get());

        // update balance 
        balance.set((balance.get() - bet.get()));
        System.out.println("BET: " + bet.get());
        System.out.println("BALANCE AFTER BET ( CURRENT ): " + balance.get());

        // disable user notification text field 
        userNotificationText.setVisible(false);

        // disable bet in a new game button 
        betInNewGameButton.setVisible(false);

        // disable betting amount text field
        inputMoneyBet.setVisible(false);

        // set balanceText field visible and update its value 
        balanceText.setVisible(true);

        // set BET field visible and update betting amount
        betText.setVisible(true);

        // set moneyBetText visible and update accordingly 
        // update balance accordingly 
        moneyBetText.setVisible(true);

        dealer.takeCard(deck.drawCard());
        player.takeCard(deck.drawCard());
        player.takeCard(deck.drawCard());

        if (player.valueProperty().get() == 21 && player.getNumOfDrawnCards() == 2) {
            // make HIT and STAND buttons visible 
            HitButton.setVisible(false);
            StandButton.setVisible(false);
        } else {
            // make HIT and STAND buttons visible 
            HitButton.setVisible(true);
            StandButton.setVisible(true);
        }

    }

    /**
     * Determines and announces the winner of the round. Updates the balance accordingly. Calls the startNewRound method.
     */
    @FXML
    private void endGame() {

        /**
         * This semaphore prevents other processes from accessing this method while it is being used. In this way we avoid race conditions.
         */
        endMethodSemaphore = true;

        /**
         * Check for blackjack on first two cards.
         */
        if (player.valueProperty().get() == 21 && player.getNumOfDrawnCards() == 2) {
            dealer.takeCard(deck.drawCard());

            if (dealer.valueProperty().get() == 21) {
                userNotificationText.setText("PUSH!");
                userNotificationText.setVisible(true);
                System.out.println("(PUSH) BALANCE (to become): " + (balance.get() + bet.get()));
                balance.set((balance.get() + bet.get()));
                System.out.println("(PUSH) BALANCE (updated): " + balance.get());

            } else { // user has a blackjack 
                userNotificationText.setText("Blackjack!!! YOU WON " + (bet.get() * 3) + "$");
                userNotificationText.setVisible(true);
                System.out.println("BLACKJACK (to become): " + (balance.get() + (bet.get() * 3)));
                balance.set((balance.get() + (bet.get() * 3)));
                System.out.println("BLACKJACK BALANCE (updated): " + balance.get());
            }
        } /**
         * if player has a score of 21 but has drawn more than two cards - dealer has to draw cards until he has got 17 or over.
         */
        else if (player.valueProperty().get() == 21 && player.getNumOfDrawnCards() > 2) {
            while (dealer.valueProperty().get() < 17 || dealer.valueProperty().get() < player.valueProperty().get()) {
                dealer.takeCard(deck.drawCard());
            }

            System.out.println("DEALER VALUE IS: " + dealer.valueProperty().get());
            System.out.println("PLAYER VALUE IS: " + player.valueProperty().get());

            if (dealer.valueProperty().get() == 21) {
                userNotificationText.setText("PUSH!");
                userNotificationText.setVisible(true);
                System.out.println("(PUSH) BALANCE (to become): " + (balance.get() + bet.get()));
                balance.set((balance.get() + bet.get()));
                System.out.println("(PUSH) BALANCE (updated): " + balance.get());

            } else if (dealer.valueProperty().get() < 21 || dealer.valueProperty().get() > 21) {
                userNotificationText.setText("YOU WON " + (bet.get() * 2) + "$");
                userNotificationText.setVisible(true);

                System.out.println("(PLAYER WON) BALANCE (to become): " + (balance.get() + (bet.get() * 2)));
                balance.set((balance.get() + (bet.get() * 2)));
                System.out.println("(PLAYER WON) BALANCE (updated): " + balance.get());

            }
        } /**
         * If player has a score of under 21 but the dealer has a score lower than the player's - player wins.
         */
        else if ((player.valueProperty().get() < 21 && player.valueProperty().get() > dealer.valueProperty().get()) || (player.valueProperty().get() < 21 && dealer.valueProperty().get() > 21)) {
            userNotificationText.setText("YOU WON " + (bet.get() * 2) + "$");
            userNotificationText.setVisible(true);

            System.out.println("(PLAYER WON) BALANCE (to become): " + (balance.get() + (bet.get() * 2)));
            balance.set((balance.get() + (bet.get() * 2)));
            System.out.println("(PLAYER WON) BALANCE (updated): " + balance.get());

        } /**
         * If the player has a score lower than 21 but equal to the dealer's - push.
         */
        else if (player.valueProperty().get() < 21 && player.valueProperty().get() == dealer.valueProperty().get()) {
            userNotificationText.setText("PUSH!");
            userNotificationText.setVisible(true);
            System.out.println("(PUSH) BALANCE (to become): " + (balance.get() + bet.get()));
            balance.set((balance.get() + bet.get()));
            System.out.println("(PUSH) BALANCE (updated): " + balance.get());

        } /**
         * If dealer has a score of 21 or lower, but player has exceeded 21 - dealer wins.
         */
        else if (dealer.valueProperty().get() <= 21 && (player.valueProperty().get() > 21 || player.valueProperty().get() < 21)) {
            userNotificationText.setText("YOU LOST YOUR BET"); // announce winner 
            userNotificationText.setVisible(true);

            /**
             * If player runs out of money - ask him to start a new game.
             */
            if (balance.get() == 0) {
                System.out.println("END GAME");
            }

        } /**
         * If dealer has a score lower than 21 but greater than the player's - dealer wins.
         */
        else if (dealer.valueProperty().get() < 21 && dealer.valueProperty().get() > player.valueProperty().get()) {
            userNotificationText.setText("YOU LOST YOUR BET"); // announce winner 
            userNotificationText.setVisible(true);

            /**
             * If player runs out of money - ask him to start a new game.
             */
            if (balance.get() == 0) {
                System.out.println("END GAME");
            }
        }

        // enable/disable visibility of objects on display 
        betText.setVisible(false);
        moneyBetText.setVisible(false);
        balanceText.setVisible(true);

        HitButton.setVisible(false);
        StandButton.setVisible(false);

        betInNewGameButton.setVisible(true);
        inputMoneyBet.setText("");
        inputMoneyBet.setVisible(true);
        bet.set(0);

    }

    /**
     * On starting the game, the method 'settingUpGame()' will be called automatically, this method sets up the game components and initializes objects and values.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        settingUpGame();
    }

}
