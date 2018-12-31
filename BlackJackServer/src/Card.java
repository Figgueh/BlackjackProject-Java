import enums.CardSuits;
import enums.CardType;

public class Card {
    private CardType cardType;
    private CardSuits cardSuit;
    private int value;
    private boolean isVisible = true;
    private String cardImage;

    public String getCardImage(){
        return buildCardUrl(this.cardType, this.cardSuit);
    }

    public void setVisible(boolean visible){
        this.isVisible = visible;
    }

    public CardType getCardType(){
        return cardType;
    }

    public int getValue(){
        return value;
    }

    public void getFaceDownCard(){
        this.isVisible = false;
        cardImage = buildCardUrl(this.cardType, this.cardSuit);
    }

    public Card(CardType cardType, CardSuits cardSuit){

        //assign the values to the card
        this.cardType = cardType;
        this.cardSuit = cardSuit;

        //Build the URL
        cardImage = buildCardUrl(this.cardType, this.cardSuit);

        //Get the trueCard value
        if(cardType.ordinal() > 10)
            value = 10;
        else
            value = cardType.ordinal() + 1;
    }

//    public Card(CardType card, CardSuits suit, boolean isVisible){
//        this(card,suit);
//        this.isVisible = isVisible;
//    }

    public String buildCardUrl(CardType card, CardSuits suit){
        int cardValue = cardType.ordinal() + 1;
        StringBuilder cardUrl = new StringBuilder("images/");

        //Get the first part of the url:
        if(card.equals(CardType.Ace))
            cardUrl.append("A");
        else if (card.equals(CardType.Jack))
            cardUrl.append("J");
        else if (card.equals(CardType.Queen))
            cardUrl.append("Q");
        else if (card.equals(CardType.King))
            cardUrl.append("K");
        else
            cardUrl.append(cardValue);

        //Get the second part of the url:
        if(suit.equals(CardSuits.Clubs))
            cardUrl.append("C");
        else if(suit.equals(CardSuits.Diamonds))
            cardUrl.append("D");
        else if(suit.equals(CardSuits.Hearts))
            cardUrl.append("H");
        else if(suit.equals(CardSuits.Spades))
            cardUrl.append("S");

        //Add the extension
        if (cardUrl != null)
            cardUrl.append(".png");

        if(!isVisible)
            return "images/blue_back.png";

        return cardUrl.toString();
    }



    @Override
    public String toString(){
        return cardType + " of " + cardSuit;
    }

}