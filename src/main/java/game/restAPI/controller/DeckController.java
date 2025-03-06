package game.restAPI.controller;

import game.restAPI.handler.DeckHandler;
import java.io.IOException;
import java.io.OutputStream;

public class DeckController {
    public static void getDeck(OutputStream output) throws IOException {
        DeckHandler.handleGetDeck(output);
    }

    public static void setDeck(OutputStream output, String body) throws IOException {
        DeckHandler.handleSetDeck(output, body);
    }
}
