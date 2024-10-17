package control.commands;

import facade.GameFacade;

public class LookAroundCommand implements GameCommand {
    @Override
    public String execute(GameFacade facade) {
        return facade.playerLookAround();
    }
}
