public interface Deck {
    void shuffle();
    Card drawCard();
    boolean isEmpty();
    int getDeckSize();
}
