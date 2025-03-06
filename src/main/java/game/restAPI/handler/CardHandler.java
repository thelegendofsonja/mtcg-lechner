package game.restAPI.handler;

import java.io.IOException;
import java.io.OutputStream;

public class CardHandler {
    public static void handleGetCards(OutputStream output) throws IOException {
        String response = "HTTP/1.1 501 Not Implemented\r\n\r\nThis endpoint is not implemented yet.";
        output.write(response.getBytes());
        System.out.println("HTTP/1.1 501 Not Implemented - GET /cards");
    }
}
