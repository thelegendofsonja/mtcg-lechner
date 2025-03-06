package game.model;

import java.util.List;

public class User {
    private String username;
    private String password;  // This should be encrypted in real implementations
    private int coins = 20;   // Initial coins for buying packages
    private List<Card> stack; // All cards owned by the user
    private Deck deck;        // The user's selected deck

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public List<Card> getStack() {
        return stack;
    }

    public void setStack(List<Card> stack) {
        this.stack = stack;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", coins=" + coins +
                ", stack=" + stack +
                ", deck=" + deck +
                '}';
    }
}
