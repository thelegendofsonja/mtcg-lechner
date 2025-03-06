package game.restAPI.handler;

import java.io.IOException;
import java.io.OutputStream;

public class DeckHandler {
    public static void handleGetDeck(OutputStream output) throws IOException {
        String response = "HTTP/1.1 501 Not Implemented\r\n\r\nThis endpoint is not implemented yet.";
        output.write(response.getBytes());
        System.out.println("HTTP/1.1 501 Not Implemented - GET /deck");
    }

    public static void handleSetDeck(OutputStream output, String body) throws IOException {
        String response = "HTTP/1.1 501 Not Implemented\r\n\r\nThis endpoint is not implemented yet.";
        output.write(response.getBytes());
        System.out.println("HTTP/1.1 501 Not Implemented - PUT /deck");
    }
}
