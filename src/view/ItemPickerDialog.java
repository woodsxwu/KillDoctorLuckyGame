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
 * A custom dialog for picking items from a list.
 */
public class ItemPickerDialog extends JDialog implements PickerDialog<String> {
  private static final long serialVersionUID = 4293822951656010428L;
  private static final int MIN_WIDTH = 300;
  private static final int MIN_HEIGHT = 200;
  private static final int BUTTON_WIDTH = 250;
  private static final int BUTTON_HEIGHT = 40;
  private String selectedItemName;

  /**
   * Creates a new ItemPickerDialog.
   *
   * @param parent the parent frame
   * @param items  the list of items to display
   */
  public ItemPickerDialog(Frame parent, List<Item> items) {
    super(parent, "Pick Up Item", true);
    this.selectedItemName = null;
    initializeDialog(items);
  }

  /**
   * Initializes the dialog.
   *
   * @param items the list of items to display
   */
  private void initializeDialog(List<Item> items) {
    setLayout(new BorderLayout());
    setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));

    // Create button panel
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
   * Creates a panel containing buttons for each item.
   *
   * @param items the list of items to display
   * @return the panel containing buttons
   */
  private JPanel createButtonPanel(List<Item> items) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    for (Item item : items) {
      JButton itemButton = createItemButton(item);
      panel.add(itemButton);
      panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    return panel;
  }

  /**
   * Creates a button for an item.
   *
   * @param item the item to create a button for
   * @return the button
   */
  private JButton createItemButton(Item item) {
    JButton button = new JButton(
        String.format("%s (Damage: %d)", item.getItemName(), item.getDamage()));
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
    button.setMargin(new Insets(5, 15, 5, 15));

    button.addActionListener(e -> {
      selectedItemName = item.getItemName();
      dispose();
    });

    return button;
  }

  /**
   * Creates a panel containing a cancel button.
   *
   * @return the panel containing the cancel button
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