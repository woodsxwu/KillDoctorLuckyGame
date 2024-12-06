package view;

import java.awt.event.ActionListener;

/**
 * Interface defining the contract for game setup panel components.
 */
public interface SetupPanel {
  /**
   * Adds a player to the setup panel's display list.
   *
   * @param playerName    the name of the player
   * @param startingSpace the starting space name
   * @param capacity      the item capacity
   * @param isHuman       whether the player is human or computer
   */
  void addPlayerToList(String playerName, String startingSpace, int capacity, boolean isHuman);

  /**
   * Resets the setup panel to its initial state.
   */
  void reset();

  /**
   * Enables or disables the start game button.
   *
   * @param enabled true to enable the button, false to disable it
   */
  void enableStartButton(boolean enabled);

  /**
   * Adds an action listener for panel buttons.
   *
   * @param listener the action listener to add
   */
  void addActionListener(ActionListener listener);
}