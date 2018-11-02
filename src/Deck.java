import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zilla0148
 */
public class Deck {
    private List<Card> cardSet = new ArrayList<>();
    private int remainSize;

    public Deck() {
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cardSet.add(new Card(suit, rank));
            }
        }
        remainSize=52;
        shuffleCards();
    }

    public void refillDeck(){
        remainSize=52;
        shuffleCards();
    }

    private void shuffleCards(){
        Collections.shuffle( cardSet );
    }

    public Card drawCard() {
        if(remainSize>0) {
            remainSize--;
            return cardSet.get(remainSize-1);
        }else {
            System.out.println( "CAUTION: NO CARDS IN THIS DECK. Some error(s) had occurred!!!!!" );
            return null;
        }
    }

    public List<Card> getCardSet() {
        return cardSet;
    }
}