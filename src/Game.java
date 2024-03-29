import java.util.ArrayList;
import java.util.List;

public class Game {
    private final int numPlayers;
    private final List<Player> players;
    private final Deck deck;
    SharedPlayersQueue SQ = null;

    public Game(int numPlayers) {
        this.numPlayers = numPlayers;
        this.players = new ArrayList<>();
        this.deck = new StandardDeck();

        initializePlayers();
    }

    private void initializePlayers() {

        SQ = new SharedPlayersQueue(-1, numPlayers - 1);

        for (int i = 0; i < numPlayers; i++) {
            OldMaidPlayer player = new OldMaidPlayer("Player ", i, SQ);
            players.add(player);
        }
    }

    public void startGame() {

        dealCards();
        playGame();
        reportResults();

    }

    private void firstPairsThrow() {
        List<Thread> pairingThreads = new ArrayList<>();
        for (Player player : players) {
            Thread pairingThread = new Thread(() -> {
                player.formPairs();
            });
            pairingThreads.add(pairingThread);
            pairingThread.start();
        }

        for (Thread pairingThread : pairingThreads) {
            try {
                pairingThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void firstRun() {
        try {
            Thread.sleep(400);
            int size = players.size();
            Player player = players.get(size - 1);
            Thread pairingThread = new Thread(player::StartGameplay);
            pairingThread.start();
            pairingThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void dealCards() {
        int cardsPerPlayer = deck.getDeckSize() / numPlayers;
        int remainingCards = deck.getDeckSize() % numPlayers;

        for (Player player : players) {
            for (int i = 0; i < cardsPerPlayer; i++) {
                Card drawnCard = deck.drawCard();
                if (drawnCard != null) {
                    player.receiveCard(drawnCard);
                }
            }
        }

        if (remainingCards > 0) {
            players.get(0).receiveCard(deck.drawCard());
        }
    }

    private void playGame() {

        firstPairsThrow();

        List<Thread> playerThreads = new ArrayList<>();
        for (Player player : players) {
            Thread thread = new Thread(player);
            playerThreads.add(thread);
            thread.start();
        }

        firstRun();

        for (Thread thread : playerThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void reportResults() {
        List<Thread> pairingThreads = new ArrayList<>();
        for (Player player : players) {
            Thread pairingThread = new Thread(player::reportStatus);
            pairingThreads.add(pairingThread);
            pairingThread.start();
        }

        for (Thread pairingThread : pairingThreads) {
            try {
                pairingThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        int numPlayers = 2;// between 2 and 6
        Game game = new Game(numPlayers);
        game.startGame();
    }
}