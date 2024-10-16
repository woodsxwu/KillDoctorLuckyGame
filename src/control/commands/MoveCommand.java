package control.commands;

import facade.GameFacade;

public class MoveCommand implements GameCommand {
    private final String spaceName;

    public MoveCommand(String spaceName) {
        this.spaceName = spaceName;
    }

    @Override
    public String execute(GameFacade facade) {
        try {
            String currentPlayer = facade.getCurrentPlayerName();
            facade.movePlayer(currentPlayer, spaceName);
            return String.format("%s moved to %s", currentPlayer, spaceName);
        } catch (IllegalArgumentException e) {
            return "Invalid move: " + e.getMessage();
        }
    }
}