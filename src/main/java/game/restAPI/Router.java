package game.restAPI;

import game.restAPI.handler.SessionHandler;
import game.restAPI.handler.UserHandler;
import game.restAPI.handler.CardHandler;
import game.restAPI.handler.BattleHandler;
import game.restAPI.handler.DeckHandler;
import game.restAPI.handler.TradingHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Router {
    // Map of routes to handler methods
    private final Map<String, BiConsumer<OutputStream, String>> routes = new HashMap<>();

    // Register a new route
    public void registerRoute(String method, String path, BiConsumer<OutputStream, String> handler) {
        routes.put(method + " " + path, handler); // Key is "METHOD /path"
    }

    // Resolve a route and call the handler
    public boolean route(String method, String path, OutputStream output, String body) {
        BiConsumer<OutputStream, String> handler = routes.get(method + " " + path);
        if (handler != null) {
            handler.accept(output, body); // Call the handler
            return true;
        }
        return false; // Route not found
    }

    // Configure routes
    public void configureRoutes() {
        // User management
        // User management
        registerRoute("POST", "/users", (output, body) -> {
            try {
                UserHandler.handleUserRegistration(output, body);
            } catch (IOException e) {
                handleInternalServerError(output, e);
            }
        });

        registerRoute("POST", "/sessions", (output, body) -> {
            try {
                SessionHandler.handleLogin(output, body);
            } catch (IOException e) {
                handleInternalServerError(output, e);
            }
        });

        // Card management
        registerRoute("GET", "/cards", (output, body) -> {
            try {
                CardHandler.handleGetCards(output);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Deck management
        registerRoute("GET", "/deck", (output, body) -> {
            try {
                DeckHandler.handleGetDeck(output);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        registerRoute("PUT", "/deck", (output, body) -> {
            try {
                DeckHandler.handleSetDeck(output, body);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Battle
        registerRoute("POST", "/battles", (output, body) -> {
            try {
                BattleHandler.handleStartBattle(output);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Trading
        registerRoute("GET", "/tradings", (output, body) -> {
            try {
                TradingHandler.handleGetTradings(output);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        registerRoute("POST", "/tradings", (output, body) -> {
            try {
                TradingHandler.handleCreateTrading(output, body);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        registerRoute("DELETE", "/tradings/{id}", (output, body) -> {
            try {
                TradingHandler.handleDeleteTrading(output, body);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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
