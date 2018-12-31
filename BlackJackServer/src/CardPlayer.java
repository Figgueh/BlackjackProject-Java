import enums.CardType;

import java.util.ArrayList;
import java.util.Random;

public class CardPlayer {
    private ArrayList<Card> hand = new ArrayList<>();
    protected int handValue;
    private boolean hasAce = false;

    public ArrayList<Card> getHand(){
        return hand;
    }

    public void addCardToHand(Deck playingCards){
        //If the deck runs out of cards
        if(playingCards.getDeckList().size() == 0)
            //Rebuild the deck
            playingCards.buildDeck();

        if(handValue <= 21){
            Card newCard;

            //Get the amount of cards in the deck
            int totalCardsInPlay = playingCards.getDeckList().size();

            //Get a new random number based on the number of cards we have in play.
            Random random = new Random();
            int randomNumber = random.nextInt(totalCardsInPlay);

            //Find the card at that number
            newCard = playingCards.getDeckList().get(randomNumber);

            //Remove it from the deck
            playingCards.getDeckList().remove(newCard);

            //Add it to the list of cards in the hand
            hand.add(newCard);

            //Get the new handValue
            getHandValue();

        }
    }

    public void addCardToHand(Deck playingCards, boolean isVisible){
        //If the deck runs out of cards
        if(playingCards.getDeckList().size() == 0)
            //Rebuild the deck
            playingCards.buildDeck();

        if(handValue <= 21){
            Card newCard;

            //Get the amount of cards in the deck
            int totalCardsInPlay = playingCards.getDeckList().size();

            //Get a new random number based on the number of cards we have in play.
            Random random = new Random();
            int randomNumber = random.nextInt(totalCardsInPlay);

            //Find the card at that number
            newCard = playingCards.getDeckList().get(randomNumber);

            if(isVisible == false)
                newCard.getFaceDownCard();

            //Remove it from the deck
            playingCards.getDeckList().remove(newCard);

            //Add it to the list of cards in the hand
            hand.add(newCard);

            //Get the new handValue
            getHandValue();

        }
    }

    public void getHandValue()
    {
        //reset hand value
        handValue = 0;

        //Get the value of the dealers hand:
        for (var card : hand)
        {
            if (card.getCardType() == CardType.Ace)
                hasAce = true;

            //Add up the card types.
            handValue += card.getValue();
        }

        //Check if we have a value of 11 or less
        if (handValue <= 11 && hasAce)
            //If there's an ace in the hand, then the ace is worth 11
            handValue += 10;

        //We only add 10 because we already counted the ace as 1.

    }
}
