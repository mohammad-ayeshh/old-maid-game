import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OldMaidPlayer implements Player {
    private final String name;
    private final int index;
    private String status;
    private final List<Card> hand;
    final SharedPlayersQueue SQ;

    public int getIndex() {
        return index;
    }

    public String getStatus() {
        return status;
    }

    public OldMaidPlayer(String name, int index, SharedPlayersQueue SQ) {
        this.name = name + ": " + index;
        this.index = index;
        this.hand = new ArrayList<>();
        this.SQ = SQ;
        this.status = "playing";
    }

    @Override
    public void reportStatus() {
        synchronized (SQ) {
            if (hasWon())
                this.status = name + ": Winner!!";
            else if (hasLost())
                this.status = name + ": Loser!!";
            System.out.println(this.status);
        }
    }

    @Override
    public boolean hasLost() {
        return hand.size() == 1 && hand.get(0).getRank().equals("Joker");
    }

    @Override
    public boolean hasWon() {
        return hand.size() == 0;
    }

    @Override
    public void run() {
        synchronized (SQ) {
            while (!hasWon() && !hasLost()) {
                if (index >= 0) {
                    try {
                        while (SQ.getTurn() != index) {
                            System.out.println(name + " in wait");
                            SQ.wait();
                        }
                        System.out.println(name + " not in wait");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    consume();
                    logic();
                    produce();
                }
            }
        }
    }

    public void StartGameplay() {
        synchronized (SQ) {
            if (!hand.isEmpty()) {
                System.out.println(name + ": first notify");
                Random random = new Random();
                int randomIndex = random.nextInt(hand.size());
                Card removedCard = hand.remove(randomIndex);
                SQ.enqueue(removedCard);
                System.out.println(name + " in Produce + has Produced " + removedCard);
                SQ.setTurn(SQ.getTurn() + 1);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                SQ.notifyAll();
            }
        }
    }

    private void consume() {
        synchronized (SQ) {
            Card takenCard = SQ.dequeue();
            System.out.println(name + " in Consume + has consumed " + takenCard);
            hand.add(takenCard);
        }
    }

    private void logic() {
        formPairs();
    }

    private void produce() {
        synchronized (SQ) {
            if (hand.size() != 0) {
                Card removedCard = hand.remove(0);
                SQ.enqueue(removedCard);
                System.out.println(name + " in Produce + has Produced " + removedCard);
                if (SQ.getTurn() < SQ.getMaxTurn()) {
                    SQ.setTurn(SQ.getTurn() + 1);
                } else {
                    SQ.setTurn(0);
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                SQ.notifyAll();
            }
        }
    }

    public void printHand() {
        System.out.println(name + "'s hand:");

        if (hand.isEmpty()) {
            System.out.println("Empty");
        } else {
            int cardsPerRow = 3;
            int cardCount = 0;

            for (Card card : hand) {
                System.out.print(card + " // ");
                cardCount++;

                if (cardCount == cardsPerRow) {
                    System.out.println();
                    cardCount = 0;
                }
            }

            if (cardCount != 0) {
                System.out.println();
            }
        }
    }

    @Override
    public void receiveCard(Card card) {
        hand.add(card);
    }

    @Override
    public void formPairs() {
        synchronized (SQ) {
            System.out.println(name + " in form pairs");
            List<Card> pairedCards = new ArrayList<>();

            for (int i = 0; i < hand.size(); i++) {
                Card currentCard = hand.get(i);
                String currentRank = currentCard.getRank();
                String currentColor = currentCard.getColor();

                for (int j = i + 1; j < hand.size(); j++) {
                    Card otherCard = hand.get(j);
                    String otherRank = otherCard.getRank();
                    String otherColor = otherCard.getColor();

                    if (currentRank.equals(otherRank) && currentColor.equals(otherColor)) {

                        pairedCards.add(currentCard);
                        pairedCards.add(otherCard);

                        hand.remove(currentCard);
                        hand.remove(otherCard);

                        break;
                    }
                }
            }

            SQ.notifyAll();
            if (pairedCards.size() != 0) {
                System.out.println(pairedCards + " was removed from the " + name + "'s hand");
            } else {
                System.out.println("nothing was removed from " + name + "'s hand");
                printHand();
            }
        }
    }

    @Override
    public List<Card> getHand() {
        return hand;
    }
}
