package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import model.item.Item;

/**
 * A custom dialog for picking items to attack with, including the default
 * "poke" option.
 */
public class AttackItemDialog extends JDialog implements PickerDialog<String> {
  private static final long serialVersionUID = -1207379582587041058L;
  private static final int MIN_WIDTH = 300;
  private static final int MIN_HEIGHT = 200;
  private static final int BUTTON_WIDTH = 250;
  private static final int BUTTON_HEIGHT = 40;
  private String selectedItemName;

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

  /**
   * Creates a new AttackItemDialog.
   *
   * @param parent the parent dialog
   * @param items  the list of items the player has
   */
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

  /**
   * Creates the button panel for the dialog.
   *
   * @param items the list of items the player has
   * @return the button panel
   */
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

  /**
   * Creates an attack button with the given item name and damage.
   *
   * @param itemName the name of the item
   * @param damage   the damage the item does
   * @return the created button
   */
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

  /**
   * Creates the cancel panel for the dialog.
   *
   * @return the cancel panel
   */
  private JPanel createCancelPanel() {
    JPanel panel = new JPanel();
    panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(e -> dispose());
    panel.add(cancelButton);

    return panel;
  }

  @Override
  public String showDialog() {
    setVisible(true);
    return selectedItemName;
  }
}