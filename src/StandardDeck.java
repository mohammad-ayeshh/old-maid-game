import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StandardDeck implements Deck {
    private List<Card> cards;

    public StandardDeck() {
        initializeDeck();
    }

    private void initializeDeck() {

        cards = new ArrayList<>();
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        String[] suits = {"HEARTS", "DIAMONDS", "CLUBS", "SPADES"};

        for (String suit : suits) {
            for (String rank : ranks) {
                String color = (suit.equals("HEARTS") || suit.equals("DIAMONDS")) ? "Red" : "Black";
                cards.add(new Card(rank, suit, color));
            }
        }
        cards.add(new Card("Joker", "Joker", "None"));

        shuffle();
    }

    @Override
    public void shuffle() {
        Collections.shuffle(cards);
    }

    @Override
    public Card drawCard() {
        if (!isEmpty()) {
            return cards.remove(0);
        } else {
            return null;
        }
    }

    @Override
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    @Override
    public int getDeckSize() {
        return cards.size();
    }
}