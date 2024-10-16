package control.commands;

import facade.GameFacade;

public class AddHumanPlayerCommand implements GameCommand {
    private final String playerName;
    private final String startingSpaceName;
    private final boolean hasMaxItems;
    private final int maxItems;

    public AddHumanPlayerCommand(String playerName, String startingSpaceName, boolean hasMaxItems, int maxItems) {
        this.playerName = playerName;
        this.startingSpaceName = startingSpaceName;
        this.hasMaxItems = hasMaxItems;
        this.maxItems = maxItems;
    }

    @Override
    public String execute(GameFacade facade) {
        try {
            facade.addPlayer(playerName, startingSpaceName, hasMaxItems, maxItems);
            return String.format("Human player %s added successfully", playerName);
        } catch (IllegalArgumentException e) {
            return "Failed to add human player: " + e.getMessage();
        }
    }
}