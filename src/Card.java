import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * @author zilla0148
 */
public class Card extends Parent {

    private static final int CARD_W = 90;
    private static final int CARD_H = 130;
    private Suit suit;
    private Rank faceValue;
    private int value;

    public Rank getFaceValue() {
        return faceValue;
    }


    public int getValue() {
        return value;
    }

    enum Suit{
        /**
         * 红桃
         */
        HEART,
        /*
         * 黑桃
         */
        SPADE,
        /**
         * 方块
         */
        DIAMOND,
        /*
         * 梅花
         */
        CLUB;

        final Image image;

        Suit() {
            image = new Image(Card.class.getResourceAsStream("imgs/" + name().toLowerCase()+".png" ),
                    24,24,true,true);
        }
    }

    enum Rank {
        /*
        2-9 value==faceValue
         */
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9),
        /*
        10-13 value=10
         */
        TEN(10),JACK(10), QUEEN(10), KING(10),
        /*
        ACE with value of 1 or 11, depending on whether the total of the hand will bust or not.
         */
        ACE(11);

        final int value;
        Rank(int value) {
            this.value = value;
        }

        String showName() {
            return ordinal() < 9 ? String.valueOf(value) : name().substring(0, 1);
        }
    }

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.faceValue = rank;
        this.value = rank.value;

        Rectangle cardRect = new Rectangle(CARD_W, CARD_H);
        cardRect.setArcWidth(20);
        cardRect.setArcHeight(20);
        cardRect.setFill( Color.WHITE);

        Text rankTxt = new Text(rank.showName());
        rankTxt.setFont( Font.font(18));
        rankTxt.setX(CARD_W - rankTxt.getLayoutBounds().getWidth() - 10);
        rankTxt.setY(rankTxt.getLayoutBounds().getHeight());

        Text rankTxt2 = new Text(rankTxt.getText());
        rankTxt2.setFont(Font.font(18));
        rankTxt2.setX(10);
        rankTxt2.setY(CARD_H - 10);

        ImageView view = new ImageView(suit.image);
        view.setRotate(180);
        view.setX(CARD_W - 32);
        view.setY(CARD_H - 32);

        getChildren().addAll(cardRect, new ImageView(suit.image), view, rankTxt, rankTxt2);

//        System.out.println( this.toString() );
    }

    @Override
    public String toString() {
        return suit.toString()+" "+faceValue.toString();
    }
}

