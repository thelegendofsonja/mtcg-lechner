// TradingHandler
package game.restAPI.handler;

import java.io.IOException;
import java.io.OutputStream;

public class TradingHandler {
    public static void handleGetTradings(OutputStream output) throws IOException {
        String response = "HTTP/1.1 501 Not Implemented\r\n\r\nThis endpoint is not implemented yet.";
        output.write(response.getBytes());
        System.out.println("HTTP/1.1 501 Not Implemented - GET /tradings");
    }

    public static void handleCreateTrading(OutputStream output, String body) throws IOException {
        String response = "HTTP/1.1 501 Not Implemented\r\n\r\nThis endpoint is not implemented yet.";
        output.write(response.getBytes());
        System.out.println("HTTP/1.1 501 Not Implemented - POST /tradings");
    }

    public static void handleDeleteTrading(OutputStream output, String body) throws IOException {
        String response = "HTTP/1.1 501 Not Implemented\r\n\r\nThis endpoint is not implemented yet.";
        output.write(response.getBytes());
        System.out.println("HTTP/1.1 501 Not Implemented - DELETE /tradings/{id}");
    }
}
