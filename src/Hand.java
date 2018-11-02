import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 * @author zilla0148
 */
@SuppressWarnings("AlibabaUndefineMagicConstant")

public class Hand {

    private ObservableList<Node> myCards;
    private SimpleIntegerProperty total = new SimpleIntegerProperty(0);

    private int aceCount = 0;

    public int getSize(){
        return myCards.size();
    }

    public Hand(ObservableList<Node> cards) {
        this.myCards = cards;
    }

    /** add one card to the hand, and update the total
     *
     * @param card new card from deck
     */
    public void addCard(Card card) {
        myCards.add(card);
        if (card.getFaceValue() == Card.Rank.ACE) {
            aceCount++;
        }
        // if the total exceeding 21 when regard the ACE's value as 11
        if (total.get() + card.getValue() > 21 && aceCount > 0) {
            total.set(total.get() + card.getValue() - 10);
            aceCount--;
        }
        else {
            total.set(total.get() + card.getValue());
        }
    }

    public void clearHand() {
        myCards.clear();
        total.set(0);
        aceCount = 0;
    }

    public SimpleIntegerProperty getTotalProperty() {
        return total;
    }
}