// BattleHandler
package game.restAPI.handler;

import java.io.IOException;
import java.io.OutputStream;

public class BattleHandler {
    public static void handleStartBattle(OutputStream output) throws IOException {
        String response = "HTTP/1.1 501 Not Implemented\r\n\r\nThis endpoint is not implemented yet.";
        output.write(response.getBytes());
        System.out.println("HTTP/1.1 501 Not Implemented - POST /battles");
    }
}