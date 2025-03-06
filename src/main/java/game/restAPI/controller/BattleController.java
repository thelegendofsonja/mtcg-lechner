package game.restAPI.controller;

import game.restAPI.handler.BattleHandler;
import java.io.IOException;
import java.io.OutputStream;

public class BattleController {
    public static void startBattle(OutputStream output) throws IOException {
        BattleHandler.handleStartBattle(output);
    }
}
