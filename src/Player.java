import javafx.beans.property.SimpleIntegerProperty;

/**
 * @author zilla0148
 */
public class Player {
    private String nickname;
    private SimpleIntegerProperty money;

    public Player(String nickname, int money) {
        this.nickname = nickname;
        this.money = new SimpleIntegerProperty(money);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getMoney() {
        return money.get();
    }

    public SimpleIntegerProperty moneyProperty() {
        return money;
    }

    public void setMoney(int money) {
        this.money.set( money );
    }
}
