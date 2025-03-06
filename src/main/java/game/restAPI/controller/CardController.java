package game.restAPI.controller;

import game.restAPI.handler.CardHandler;
import java.io.IOException;
import java.io.OutputStream;

public class CardController {
    public static void getCards(OutputStream output) throws IOException {
        CardHandler.handleGetCards(output);
    }
}
