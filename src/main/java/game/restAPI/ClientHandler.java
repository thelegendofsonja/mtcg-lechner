package game.restAPI;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Router router; // Router instance

    public ClientHandler(Socket socket, Router router) {
        this.socket = socket;
        this.router = router;
    }

    @Override
    public void run() {
        try (InputStream input = socket.getInputStream();
             OutputStream output = socket.getOutputStream()) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // Parse the HTTP request
            String requestLine = reader.readLine();
            if (requestLine == null || requestLine.isEmpty()) {
                sendBadRequest(output, "Missing or empty request");
                return;
            }

            System.out.println("Request Line: " + requestLine);

            // Parse the HTTP method and path
            String[] requestParts = requestLine.split(" ");
            if (requestParts.length < 2) {
                sendBadRequest(output, "Malformed request");
                return;
            }
            String method = requestParts[0];
            String path = requestParts[1];

            // Parse headers
            String line;
            int contentLength = 0;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                if (line.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(line.split(": ")[1]);
                }
            }

            // Read the body if present
            StringBuilder body = new StringBuilder();
            if (contentLength > 0) {
                char[] buffer = new char[contentLength];
                reader.read(buffer, 0, contentLength);
                body.append(buffer);
            }

            System.out.println("Body: " + body);

            // Route the request
            if (!router.route(method, path, output, body.toString())) {
                sendNotFound(output, "Endpoint not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendBadRequest(OutputStream output, String message) throws IOException {
        String response = "HTTP/1.1 400 Bad Request\r\n\r\n" + message + "\n";
        output.write(response.getBytes());
        System.out.println("HTTP/1.1 400 Bad Request - " + message);
    }

    private void sendNotFound(OutputStream output, String message) throws IOException {
        String response = "HTTP/1.1 404 Not Found\r\n\r\n" + message + "\n";
        output.write(response.getBytes());
        System.out.println("HTTP/1.1 404 Not Found - " + message);
    }
}
