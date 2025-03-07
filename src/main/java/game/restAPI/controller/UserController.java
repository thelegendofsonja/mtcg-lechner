package game.restAPI.controller;

import game.restAPI.repository.UserRepository;
import java.io.OutputStream;
import java.io.IOException;

public class UserController implements Controller {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void handleRequest(String method, OutputStream output, String body, String username) throws IOException {
        System.out.println("[DEBUG] Handling user request: " + method);

        if (!method.equals("POST")) {
            sendResponse(output, 405, "Method Not Allowed");
            return;
        }

        // Simulate user creation logic
        boolean userCreated = createUser(body);
        if (userCreated) {
            sendResponse(output, 201, "User registered successfully");
        } else {
            sendResponse(output, 409, "User already exists");  // 409 Conflict for duplicate users
        }
    }

    private boolean createUser(String body) {
        // TODO: Implement real user creation logic (check if user already exists)
        return Math.random() > 0.5; // Simulated success/failure
    }

    private void sendResponse(OutputStream output, int statusCode, String message) throws IOException {
        String reasonPhrase = switch (statusCode) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            case 409 -> "Conflict";
            case 500 -> "Internal Server Error";
            default -> "Unknown";
        };

        String response = "HTTP/1.1 " + statusCode + " " + reasonPhrase + "\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + message.length() + "\r\n\r\n" +
                message;
        output.write(response.getBytes());
        output.flush();
        System.out.println("[DEBUG] Sent response: " + statusCode + " " + reasonPhrase);
    }
}
