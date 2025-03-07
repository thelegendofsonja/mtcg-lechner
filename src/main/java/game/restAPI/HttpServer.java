package game.restAPI;

import game.restAPI.repository.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private final int port;
    private final ExecutorService threadPool;
    private final Router router;

    public HttpServer(int port, int maxThreads) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(maxThreads);

        // ✅ Create the repository map and pass it to Router
        Map<String, Repository> repositories = new HashMap<>();
        repositories.put("user", new UserRepository());
        repositories.put("package", new PackageRepository());
        repositories.put("card", new CardRepository());
        repositories.put("game", new GameRepository());
        repositories.put("trade", new TradingRepository());

        this.router = new Router(repositories);
    }

    private void handleInternalServerError(OutputStream output, Exception e) {
        try {
            String response = "HTTP/1.1 500 Internal Server Error\r\n\r\n" + e.getMessage() + "\n";
            output.write(response.getBytes());
            System.err.println("500 Internal Server Error - " + e.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");
                threadPool.submit(new ClientHandler(clientSocket, router));
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}
