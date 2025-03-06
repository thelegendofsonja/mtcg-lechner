package game.restAPI.controller;

import game.restAPI.handler.SessionHandler;
import java.io.IOException;
import java.io.OutputStream;

public class SessionController {
    public static void loginUser(OutputStream output, String body) throws IOException {
        SessionHandler.handleLogin(output, body);
    }
}
