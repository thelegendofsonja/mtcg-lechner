package game.restAPI;

import game.restAPI.controller.*;
import game.restAPI.repository.*;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<String, Controller> controllers = new HashMap<>();

    public Router(Map<String, Repository> repositories) {
        controllers.put("/users", new UserController((UserRepository) repositories.get("user")));
        controllers.put("/sessions", new LoginController((UserRepository) repositories.get("user")));
        controllers.put("/packages", new PackagesController((PackageRepository) repositories.get("package")));
        controllers.put("/transactions/packages", new TransactionController((PackageRepository) repositories.get("package")));
        controllers.put("/cards", new CardController((CardRepository) repositories.get("card")));
        controllers.put("/deck", new DeckController((CardRepository) repositories.get("card")));
        controllers.put("/battles", new BattleController((GameRepository) repositories.get("game")));
        controllers.put("/tradings", new TradingController((TradingRepository) repositories.get("trade")));
    }

    public Controller getController(String path) {
        return controllers.get(path);
    }
}
