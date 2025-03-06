package game.restAPI;

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
             OutputStream output = socket.getOutputStream()) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String requestLine = reader.readLine();
            if (requestLine == null || requestLine.isEmpty()) {
                sendResponse(output, 400, "Bad Request: Empty Request");
                return;
            }

            String[] requestParts = requestLine.split(" ");
            if (requestParts.length < 2) {
                sendResponse(output, 400, "Bad Request: Invalid Request Line");
                return;
            }

            String method = requestParts[0];
            String path = requestParts[1];
            Map<String, String> headers = new HashMap<>();
            String line;
            int contentLength = 0;

            while (!(line = reader.readLine()).isEmpty()) {
                String[] headerParts = line.split(": ", 2);
                if (headerParts.length == 2) {
                    headers.put(headerParts[0], headerParts[1]);
                    if (headerParts[0].equalsIgnoreCase("Content-Length")) {
                        contentLength = Integer.parseInt(headerParts[1]);
                    }
                }
            }

            StringBuilder body = new StringBuilder();
            if (contentLength > 0) {
                char[] bodyChars = new char[contentLength];
                reader.read(bodyChars, 0, contentLength);
                body.append(bodyChars);
            }

            router.route(method, path, output, body.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendResponse(OutputStream output, int statusCode, String message) throws IOException {
        String response = "HTTP/1.1 " + statusCode + " OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + message.length() + "\r\n\r\n" +
                message;
        output.write(response.getBytes());
        output.flush();
    }
}
