package game.restAPI.controller;

import java.io.OutputStream;
import java.io.IOException;

public interface Controller {
    void handleRequest(String method, OutputStream output, String body, String username) throws IOException;
}
