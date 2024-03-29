public class Card {
    private final String rank;
    private final String suit;
    private final String color;

    public Card(String rank, String suit, String color) {
        this.rank = rank;
        this.suit = suit;
        this.color = color;
    }

    // Getters for rank, suit, and color
    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return rank + " of " + suit + " (" + color + ")";
    }
}
