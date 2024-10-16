package control.commands;

import facade.GameFacade;

public class LookAroundCommand implements GameCommand {
    @Override
    public String execute(GameFacade facade) {
        String currentPlayer = facade.getCurrentPlayerName();
        return facade.playerLookAround(currentPlayer);
    }
}
