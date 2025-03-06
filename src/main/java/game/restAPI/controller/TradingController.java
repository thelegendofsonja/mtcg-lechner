package game.restAPI.controller;

import game.restAPI.handler.TradingHandler;
import java.io.IOException;
import java.io.OutputStream;

public class TradingController {
    public static void getTradings(OutputStream output) throws IOException {
        TradingHandler.handleGetTradings(output);
    }

    public static void createTrading(OutputStream output, String body) throws IOException {
        TradingHandler.handleCreateTrading(output, body);
    }

    public static void deleteTrading(OutputStream output, String body) throws IOException {
        TradingHandler.handleDeleteTrading(output, body);
    }
}
