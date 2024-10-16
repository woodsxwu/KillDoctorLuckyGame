package control.commands;

import facade.GameFacade;

public class DisplayPlayerInfoCommand implements GameCommand {
    @Override
    public String execute(GameFacade facade) {
        String currentPlayer = facade.getCurrentPlayerName();
        return facade.getPlayerInfo(currentPlayer);
    }
}