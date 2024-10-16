package control.commands;

import facade.GameFacade;

public class PickUpItemCommand implements GameCommand {
    private final String itemName;

    public PickUpItemCommand(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String execute(GameFacade facade) {
        try {
            String currentPlayer = facade.getCurrentPlayerName();
            facade.playerPickUpItem(currentPlayer, itemName);
            return String.format("%s picked up %s", currentPlayer, itemName);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return "Failed to pick up item: " + e.getMessage();
        }
    }
}