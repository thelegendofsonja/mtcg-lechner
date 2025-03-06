package game.restAPI.controller;

import game.restAPI.handler.UserHandler;
import java.io.IOException;
import java.io.OutputStream;

public class UserController {
    public static void registerUser(OutputStream output, String body) throws IOException {
        UserHandler.handleUserRegistration(output, body);
    }
}
