package control;

import facade.GameFacade;
import view.GameView;

public class GuiControllerImpl implements WorldController {

  private GameFacade facade;
  private GameView view;
  
  public GuiControllerImpl(GameFacade facade, GameView view) {
    this.facade = facade;
    this.view = view;
  }
  
  @Override
  public void startGame(int maxTurns) {
    facade.setMaxTurns(maxTurns);
    configureKeyboardListener();
    configureMouseListener();
    configureButtonListener();
  }

  private void configureKeyboardListener() {
    
  }
  
  private void configureMouseListener() {

  }
  
  private void configureButtonListener() {

  }
  
}
