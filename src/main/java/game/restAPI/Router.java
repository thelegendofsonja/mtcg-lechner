package game.restAPI;

import game.restAPI.controller.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Router {
    private final Map<String, BiConsumer<OutputStream, String>> routes = new HashMap<>();

    public void registerRoute(String method, String path, BiConsumer<OutputStream, String> handler) {
        routes.put(method + " " + path, handler);
    }

    public boolean route(String method, String path, OutputStream output, String body) {
        BiConsumer<OutputStream, String> handler = routes.get(method + " " + path);
        if (handler != null) {
            handler.accept(output, body);
            return true;
        }
        return false;
    }

    public void configureRoutes() {
        // User management
        registerRoute("POST", "/users", this::handleUserRegistration);
        registerRoute("POST", "/sessions", this::handleUserLogin);

        // Card management
        registerRoute("GET", "/cards", this::handleGetCards);

        // Deck management
        registerRoute("GET", "/deck", this::handleGetDeck);
        registerRoute("PUT", "/deck", this::handleSetDeck);

        // Battle
        registerRoute("POST", "/battles", this::handleStartBattle);

        // Trading
        registerRoute("GET", "/tradings", this::handleGetTradings);
        registerRoute("POST", "/tradings", this::handleCreateTrading);
        registerRoute("DELETE", "/tradings/{id}", this::handleDeleteTrading);
    }

    private void handleUserRegistration(OutputStream output, String body) {
        try {
            UserController.registerUser(output, body);
        } catch (IOException e) {
            handleInternalServerError(output, e);
        }
    }

    private void handleUserLogin(OutputStream output, String body) {
        try {
            SessionController.loginUser(output, body);
        } catch (IOException e) {
            handleInternalServerError(output, e);
        }
    }

    private void handleGetCards(OutputStream output, String body) {
        try {
            CardController.getCards(output);
        } catch (IOException e) {
            handleInternalServerError(output, e);
        }
    }

    private void handleGetDeck(OutputStream output, String body) {
        try {
            DeckController.getDeck(output);
        } catch (IOException e) {
            handleInternalServerError(output, e);
        }
    }

    private void handleSetDeck(OutputStream output, String body) {
        try {
            DeckController.setDeck(output, body);
        } catch (IOException e) {
            handleInternalServerError(output, e);
        }
    }

    private void handleStartBattle(OutputStream output, String body) {
        try {
            BattleController.startBattle(output);
        } catch (IOException e) {
            handleInternalServerError(output, e);
        }
    }

    private void handleGetTradings(OutputStream output, String body) {
        try {
            TradingController.getTradings(output);
        } catch (IOException e) {
            handleInternalServerError(output, e);
        }
    }

    private void handleCreateTrading(OutputStream output, String body) {
        try {
            TradingController.createTrading(output, body);
        } catch (IOException e) {
            handleInternalServerError(output, e);
        }
    }

    private void handleDeleteTrading(OutputStream output, String body) {
        try {
            TradingController.deleteTrading(output, body);
        } catch (IOException e) {
            handleInternalServerError(output, e);
        }
    }

    private void handleInternalServerError(OutputStream output, Exception e) {
        try {
            String response = "HTTP/1.1 500 Internal Server Error\r\n\r\n" + e.getMessage() + "\n";
            output.write(response.getBytes());
            System.err.println("500 Internal Server Error - " + e.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
