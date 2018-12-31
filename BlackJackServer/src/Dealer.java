import java.util.Random;

public class Dealer extends CardPlayer {

    public void initializeDealer(Deck playingCards){
        addCardToHand(playingCards, false);
        addCardToHand(playingCards);
    }

    public void dealersTurn(Deck deck){
        while (handValue < 17){
            addCardToHand(deck);
        }
    }

    @Override
    public String toString(){
        String cardsInHand = "";
        for (Card card : getHand()) {
            cardsInHand += "," + card.getCardImage();
        }
        return cardsInHand;
    }
}
