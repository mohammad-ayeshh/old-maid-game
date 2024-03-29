import java.util.List;

public interface Player extends Runnable {
    void receiveCard(Card card);

    void formPairs();

    boolean hasLost();

    boolean hasWon();

    void reportStatus();

    public int getIndex();

    public void printHand();

    List<Card> getHand();

    void StartGameplay();
}
