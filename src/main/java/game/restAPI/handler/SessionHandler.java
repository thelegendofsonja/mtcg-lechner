package game.restAPI.handler;

import java.io.OutputStream;
import java.io.IOException;
import java.util.Map;

public class SessionHandler {
    private static final Map<String, String> users = UserHandler.getUsers();

    public static void handleLogin(OutputStream output, String body) throws IOException {
        String username = null;
        String password = null;

        try {
            // Attempt to parse the body as JSON-like input
            if (body.contains("{") && body.contains("}")) {
                String[] keyValuePairs = body.replace("{", "").replace("}", "").replace("\"", "").split(",");
                for (String pair : keyValuePairs) {
                    String[] keyValue = pair.split(":");
                    if (keyValue.length == 2) {
                        if (keyValue[0].trim().equalsIgnoreCase("username")) {
                            username = keyValue[1].trim();
                        } else if (keyValue[0].trim().equalsIgnoreCase("password")) {
                            password = keyValue[1].trim();
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Log and proceed with error handling
            System.err.println("Failed to parse request body: " + body);
        }

        // Validate input
        if (username == null || password == null) {
            String response = "HTTP/1.1 400 Bad Request\r\nContent-Type: application/json\r\n\r\n" +
                    "{\"error\":\"Missing username or password.\"}";
            output.write(response.getBytes());
            return;
        }

        // Check credentials
        if (!users.containsKey(username)) {
            String response = "HTTP/1.1 401 Unauthorized\r\nContent-Type: application/json\r\n\r\n" +
                    "{\"error\":\"Invalid username or password.\"}";
            output.write(response.getBytes());
            return;
        }

        if (!users.get(username).equals(password)) {
            String response = "HTTP/1.1 401 Unauthorized\r\nContent-Type: application/json\r\n\r\n" +
                    "{\"error\":\"Invalid username or password.\"}";
            output.write(response.getBytes());
            return;
        }

        // Generate token
        String token = username + "-mtcgToken";
        String response = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" +
                "{\"token\":\"" + token + "\"}";
        output.write(response.getBytes());
    }
}
