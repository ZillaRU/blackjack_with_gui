import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.regex.Pattern;

/**
 * @author zilla0148
 */
public class GameApp extends Application {
    private static Pattern NUMBER_PATTERN = Pattern.compile("^[-\\+]?[\\d]*$");
    private Deck deck = new Deck();
    private Player player = new Player("Zoe",100);
    private Hand dealerHand, playerHand;
    private int currBet = 30;
    private Text message = new Text();
    private SimpleBooleanProperty playable = new SimpleBooleanProperty(false);

    private HBox dealerCardsBox = new HBox(20);
    private HBox playerCardsBox = new HBox(20);

    private Parent createUIContent() {
        dealerHand = new Hand(dealerCardsBox.getChildren());
        playerHand = new Hand(playerCardsBox.getChildren());

        Pane root = new Pane();
        root.setPrefSize(900, 600);

        Region background = new Region();

        HBox rootLayout = new HBox(10);
        rootLayout.setPadding(new Insets(5, 5, 5, 5));
        Rectangle leftBG = new Rectangle(640, 560);
        leftBG.setArcWidth(50);
        leftBG.setArcHeight(50);
        leftBG.setFill( Color.AQUAMARINE);
        Rectangle rightBG = new Rectangle(235, 560);
        rightBG.setArcWidth(50);
        rightBG.setArcHeight(50);
        rightBG.setFill(Color.LIGHTSALMON);

        // LEFT
        VBox leftVBox = new VBox(50);
        leftVBox.setAlignment( Pos.TOP_CENTER);

        Text dealerScore = new Text("Dealer: ");
        Text playerScore = new Text("Player: ");

        leftVBox.getChildren().addAll(dealerScore, dealerCardsBox, message, playerCardsBox, playerScore);

        // RIGHT
        VBox rightVBox = new VBox(20);
        rightVBox.setAlignment(Pos.CENTER);

        final TextField betTxt = new TextField("BET default 30");
        final TextField nameTxt= new TextField( "Name default Zoe");
        nameTxt.setDisable( false );
        betTxt.setDisable(false);
        nameTxt.setMaxWidth( 130 );
        betTxt.setMaxWidth(130);
        Text moneyRest = new Text( "Money:" );

        Button btnPlay = new Button("PLAY");
        Button btnHit = new Button("HIT");
        Button btnStand = new Button("STAND");

        HBox buttonsHBox = new HBox(15, btnHit, btnStand);
        buttonsHBox.setAlignment(Pos.CENTER);

        rightVBox.getChildren().addAll(nameTxt,betTxt, btnPlay, moneyRest, buttonsHBox);

        // ADD BOTH STACKS TO ROOT LAYOUT

        rootLayout.getChildren().addAll(new StackPane(leftBG, leftVBox), new StackPane(rightBG, rightVBox));
        root.getChildren().addAll(background, rootLayout);

        // BIND PROPERTIES

        btnPlay.disableProperty().bind(playable);
        btnHit.disableProperty().bind(playable.not());
        btnStand.disableProperty().bind(playable.not());

        playerScore.textProperty().bind(new SimpleStringProperty("Player: ").concat(playerHand.getTotalProperty().asString()));
        dealerScore.textProperty().bind(new SimpleStringProperty("Dealer: ").concat(dealerHand.getTotalProperty().asString()));
        moneyRest.textProperty().bind(new SimpleStringProperty("Money ").concat(player.moneyProperty()));

        checkBust( betTxt, playerHand );
        checkBust( betTxt, dealerHand );

        // INIT BUTTONS

        btnPlay.setOnAction(event -> {
            nameTxt.setDisable( true );
            betTxt.setDisable( true );
            String name=nameTxt.getText();
            if(!"Name default Zoe".equals( name )){
                player.setNickname(name);
            }
            if(isInteger(betTxt.getText())) {
                currBet = Integer.valueOf(betTxt.getText());
            }
            startNewGame();
        });

        btnHit.setOnAction(event -> playerHand.addCard(deck.drawCard()) );

        btnStand.setOnAction(event -> {
            while (dealerHand.getTotalProperty().get() < 17) {
                dealerHand.addCard(deck.drawCard());
            }
            endGame();
            if(player.getMoney()>0) {
                betTxt.setDisable( false );
            }
        });

        return root;
    }

    private void checkBust(TextField betTxt, Hand dealerHand) {
        dealerHand.getTotalProperty().addListener((obs, old, newValue) -> {
            if (newValue.intValue() >= 21) {
                endGame();
                if(player.getMoney()>0) {
                    betTxt.setDisable( false );
                }
            }
        });
    }

    private void startNewGame() {
        if(player.getMoney()>=currBet){
            playable.set(true);
            message.setText("");

            deck.refillDeck();

            dealerHand.clearHand();
            playerHand.clearHand();

            dealerHand.addCard(deck.drawCard());
            dealerHand.addCard(deck.drawCard());
            playerHand.addCard(deck.drawCard());
            playerHand.addCard(deck.drawCard());
        }else {
            message.setText( "Without enough money!!!!!" );
        }
    }

    private void endGame() {
        playable.set(false);

        int dealerValue = dealerHand.getTotalProperty().get();
        int playerValue = playerHand.getTotalProperty().get();
        String winner = "Exceptional case: d: " + dealerValue + " p: " + playerValue;
        String detail="";
        if (dealerValue==21&&dealerValue==playerValue){
            if(playerHand.getSize()==2){
                winner="Player "+player.getNickname();
                player.setMoney( player.getMoney()+currBet*3/2 );
            }else if(dealerHand.getSize()==2){
                winner="Dealer";
                player.setMoney( player.getMoney()-currBet );
            }else{
                winner="";
                detail="DRAW. YOU RETAIN THE BET.";
            }
        }
        else if(dealerValue == 21 || playerValue > 21
                || (dealerValue < 21 && dealerValue > playerValue)) {
            winner = "DEALER";
            player.setMoney( player.getMoney()-currBet );
        }
        else if (playerValue == 21 || dealerValue > 21 || playerValue > dealerValue) {
            winner = "PLAYER "+player.getNickname();
            if(playerValue==21&&playerHand.getSize()==2){
                detail="BLACKJACK";
                player.setMoney( player.getMoney()+currBet*3/2 );
            }
            player.setMoney( player.getMoney()+currBet );
        }else if(dealerValue == playerValue){
            winner="";
            detail="DRAW. YOU RETAIN THE BET.";
        }
        if(player.getMoney()<=0){
            detail=detail.concat( " GAME OVER! YOU HAVE NOTHING NOW." );
        }
        message.setText(winner + " WON. "+detail);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(new Scene(createUIContent()));
        primaryStage.setWidth(900);
        primaryStage.setHeight(600);
        primaryStage.setResizable(false);
        primaryStage.setTitle("BlackJack_2018OOAD");
        primaryStage.show();
    }

    private static boolean isInteger(String str) {
        return NUMBER_PATTERN.matcher(str).matches();
    }

    public static void main(String[] args) {
        launch(args);
    }
}