package game.restAPI;

import game.restAPI.controller.Controller;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Router router;

    public ClientHandler(Socket socket, Router router) {
        this.socket = socket;
        this.router = router;
    }

    @Override
    public void run() {
        try (InputStream input = socket.getInputStream();
             OutputStream output = socket.getOutputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {

            System.out.println("[DEBUG] Client connected from " + socket.getInetAddress());

            String requestLine = reader.readLine();
            if (requestLine == null || requestLine.isEmpty()) {
                System.out.println("[DEBUG] Empty request received");
                sendResponse(output, 400, "Bad Request: Empty Request");
                return;
            }

            System.out.println("[DEBUG] Received request line: " + requestLine);

            String[] requestParts = requestLine.split(" ");
            if (requestParts.length < 2) {
                System.out.println("[DEBUG] Invalid request line format");
                sendResponse(output, 400, "Bad Request: Invalid Request Line");
                return;
            }

            String method = requestParts[0];
            String path = requestParts[1];
            System.out.println("[DEBUG] Method: " + method + ", Path: " + path);

            Map<String, String> headers = new HashMap<>();
            int contentLength = 0;
            String username = null;

            // Read headers
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                System.out.println("[DEBUG] Header: " + line);
                String[] headerParts = line.split(": ", 2);
                if (headerParts.length == 2) {
                    headers.put(headerParts[0], headerParts[1]);

                    if (headerParts[0].equalsIgnoreCase("Content-Length")) {
                        try {
                            contentLength = Integer.parseInt(headerParts[1]);
                        } catch (NumberFormatException e) {
                            System.out.println("[DEBUG] Invalid Content-Length");
                            sendResponse(output, 400, "Bad Request: Invalid Content-Length");
                            return;
                        }
                    }

                    if (headerParts[0].equalsIgnoreCase("Authorization")) {
                        username = extractUsernameFromToken(headerParts[1]);
                        System.out.println("[DEBUG] Extracted username: " + username);
                    }
                }
            }

            // Ensure username is provided when required (for authenticated routes)
            boolean requiresAuth = path.startsWith("/cards") || path.startsWith("/deck") || path.startsWith("/battles") || path.startsWith("/packages");
            if (requiresAuth && (username == null || username.isEmpty())) {
                System.out.println("[DEBUG] Unauthorized request: Missing token");
                sendResponse(output, 401, "Unauthorized: Missing or invalid token");
                return;
            }

            // Read body if content length is provided
            String body = "";
            if (contentLength > 0) {
                char[] bodyChars = new char[contentLength];
                int readChars = reader.read(bodyChars, 0, contentLength);
                if (readChars != contentLength) {
                    System.out.println("[DEBUG] Content-Length mismatch: Expected " + contentLength + " but got " + readChars);
                    sendResponse(output, 400, "Bad Request: Content-Length Mismatch");
                    return;
                }
                body = new String(bodyChars);
                System.out.println("[DEBUG] Request body: " + body);
            }

            // **ROUTING LOGIC: Delegate request handling to the appropriate controller**
            Controller controller = router.getController(path);
            if (controller != null) {
                System.out.println("[DEBUG] Routing to controller for path: " + path);
                controller.handleRequest(method, output, body, username);
            } else {
                System.out.println("[DEBUG] No matching controller for path: " + path);
                sendResponse(output, 404, "Not Found: No matching route");
            }

        } catch (IOException e) {
            System.err.println("[ERROR] Error handling client request: " + e.getMessage());
        } finally {
            try {
                socket.close();
                System.out.println("[DEBUG] Client connection closed.");
            } catch (IOException e) {
                System.err.println("[ERROR] Error closing socket: " + e.getMessage());
            }
        }
    }

    private void sendResponse(OutputStream output, int statusCode, String message) throws IOException {
        String reasonPhrase = getReasonPhrase(statusCode);
        String response = "HTTP/1.1 " + statusCode + " " + reasonPhrase + "\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + message.length() + "\r\n\r\n" +
                message;
        output.write(response.getBytes());
        output.flush();
        System.out.println("[DEBUG] Sent response: " + statusCode + " " + reasonPhrase);
    }

    private String getReasonPhrase(int statusCode) {
        return switch (statusCode) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            default -> "Unknown";
        };
    }

    /**
     * Extracts username from token in Authorization header.
     * Expected format: "Bearer {username}-mtcgToken"
     */
    private String extractUsernameFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7).trim();

        if (!token.endsWith("-mtcgToken") || token.length() <= 10) {
            return null;
        }

        return token.substring(0, token.length() - 10);
    }
}
