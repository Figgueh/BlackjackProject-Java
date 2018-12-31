import enums.CardSuits;
import enums.CardType;

import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> deckList = new ArrayList<>();

    public Deck(){
        buildDeck();
    }

    public ArrayList<Card> getDeckList(){
        return deckList;
    }

    public void buildDeck(){
        CardSuits newCardSuit;
        CardType newCardType;
        final int NUMBEROFSUITS = CardSuits.values().length;
        final int NUMBEROFTYPES = CardType.values().length;

        //Loop threw each of the possible suite values
        for (int cardSuitVal = 0; cardSuitVal < NUMBEROFSUITS; cardSuitVal++)
        {
            newCardSuit =  CardSuits.values()[cardSuitVal];

            //Loop threw each of the possible type values
            for (int cardTypeVal = 0; cardTypeVal < NUMBEROFTYPES; cardTypeVal++)
            {
                newCardType = CardType.values()[cardTypeVal];

                //Create a card with each of the items in both enums
                deckList.add(new Card(newCardType, newCardSuit));
            }
        }
    }
}
