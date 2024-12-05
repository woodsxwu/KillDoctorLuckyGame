package view;

import model.item.Item;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A custom dialog for picking items to attack with, including the default
 * "poke" option.
 */
public class AttackItemDialog extends JDialog {
  private String selectedItemName;
  private static final int MIN_WIDTH = 300;
  private static final int MIN_HEIGHT = 200;
  private static final int BUTTON_WIDTH = 250;
  private static final int BUTTON_HEIGHT = 40;

  /**
   * Creates a new AttackItemDialog.
   *
   * @param parent the parent frame
   * @param items  the list of items the player has
   */
  public AttackItemDialog(Frame parent, List<Item> items) {
    super(parent, "Choose Attack Item", true);
    this.selectedItemName = null;
    initializeDialog(items);
  }

  private void initializeDialog(List<Item> items) {
    setLayout(new BorderLayout());
    setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));

    // Create main button panel
    JPanel buttonPanel = createButtonPanel(items);

    // Create scroll pane
    JScrollPane scrollPane = new JScrollPane(buttonPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBorder(null);

    // Add cancel button
    JPanel cancelPanel = createCancelPanel();

    // Add components
    add(scrollPane, BorderLayout.CENTER);
    add(cancelPanel, BorderLayout.SOUTH);

    // Configure dialog
    pack();
    setLocationRelativeTo(getOwner());
  }

  private JPanel createButtonPanel(List<Item> items) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Add "poke" as the first option
    JButton pokeButton = createAttackButton("poke", 1);
    panel.add(pokeButton);
    panel.add(Box.createRigidArea(new Dimension(0, 5)));

    // Add inventory items
    for (Item item : items) {
      JButton itemButton = createAttackButton(item.getItemName(), item.getDamage());
      panel.add(itemButton);
      panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    return panel;
  }

  private JButton createAttackButton(String itemName, int damage) {
    JButton button = new JButton(String.format("%s (Damage: %d)", itemName, damage));
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
    button.setMargin(new Insets(5, 15, 5, 15));

    button.addActionListener(e -> {
      selectedItemName = itemName;
      dispose();
    });

    return button;
  }

  private JPanel createCancelPanel() {
    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(e -> dispose());
    panel.add(cancelButton);

    return panel;
  }

  /**
   * Shows the dialog and returns the selected item name.
   *
   * @return the name of the selected item (including "poke"), or null if
   *         cancelled
   */
  public String showDialog() {
    setVisible(true);
    return selectedItemName;
  }
}