package game.restAPI.controller;

import game.restAPI.repository.CardRepository;  // Since decks are made of cards
import game.restAPI.handler.DeckHandler;
import java.io.OutputStream;
import java.io.IOException;

public class DeckController implements Controller {
    private final CardRepository cardRepository;

    public DeckController(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public void handleRequest(String method, OutputStream output, String body, String username) throws IOException {
        switch (method) {
            case "GET":
                DeckHandler.handleGetDeck(output, username);
                break;
            case "PUT":
                DeckHandler.handleSetDeck(output, body, username);
                break;
            default:
                output.write("HTTP/1.1 405 Method Not Allowed\r\n\r\n".getBytes());
        }
    }
}
