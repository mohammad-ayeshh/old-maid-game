import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class SharedPlayersQueue {
    private final List<Card> queue;
    private int turn;
    private int maxTurn;

    public int getMaxTurn() {
        return maxTurn;
    }

    public void setMaxTurn(int maxTurn) {
        this.maxTurn = maxTurn;
    }

    public SharedPlayersQueue(int turn, int maxTurn) {
        this.queue = Collections.synchronizedList(new ArrayList<>());
        this.turn = turn;
        this.maxTurn = maxTurn;
    }

    public void enqueue(Card item) {
        queue.add(item);
    }

    public Card dequeue() {
        return queue.isEmpty() ? null : queue.remove(0);
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int newTurn) {
        turn = newTurn;
    }
}