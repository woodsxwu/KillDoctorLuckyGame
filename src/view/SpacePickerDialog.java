package view;

import model.space.Space;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A custom dialog for picking spaces from a list with enhanced scrolling.
 */
public class SpacePickerDialog extends JDialog {
  private String selectedSpaceName;
  private static final int DIALOG_WIDTH = 350;
  private static final int DIALOG_HEIGHT = 400;
  private static final int BUTTON_WIDTH = 300;
  private static final int BUTTON_HEIGHT = 40;
  private static final int SCROLL_SPEED = 16;

  /**
   * Creates a new SpacePickerDialog.
   *
   * @param parent the parent frame
   * @param spaces the list of spaces to display
   */
  public SpacePickerDialog(Frame parent, List<Space> spaces) {
    super(parent, "Choose Starting Space", true);
    this.selectedSpaceName = null;
    initializeDialog(spaces);
  }

  private void initializeDialog(List<Space> spaces) {
    setLayout(new BorderLayout(0, 5));
    setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));

    // Create title label
    JLabel titleLabel = new JLabel("Select Starting Space", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    add(titleLabel, BorderLayout.NORTH);

    // Create button panel
    JPanel buttonPanel = createButtonPanel(spaces);

    // Create scroll pane with custom settings
    JScrollPane scrollPane = new JScrollPane(buttonPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setBorder(null);

    // Customize scroll speed
    scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_SPEED);

    // Add some padding around the scroll pane
    JPanel scrollPaneWrapper = new JPanel(new BorderLayout());
    scrollPaneWrapper.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
    scrollPaneWrapper.add(scrollPane);

    // Add cancel button
    JPanel cancelPanel = createCancelPanel();

    // Add components
    add(scrollPaneWrapper, BorderLayout.CENTER);
    add(cancelPanel, BorderLayout.SOUTH);

    // Configure dialog
    pack();
    setLocationRelativeTo(getOwner());
    setResizable(true);
    setMinimumSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT / 2));
  }

  private JPanel createButtonPanel(List<Space> spaces) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

    // Add space buttons
    for (Space space : spaces) {
      JButton spaceButton = createSpaceButton(space);
      panel.add(spaceButton);
      panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    // Add extra padding at bottom to ensure last button is fully visible
    panel.add(Box.createRigidArea(new Dimension(0, 5)));

    return panel;
  }

  private JButton createSpaceButton(Space space) {
    JButton button = new JButton();

    // Create a panel for the button content
    JPanel buttonContent = new JPanel(new BorderLayout());
    buttonContent.setOpaque(false);

    // Add space name with HTML formatting for better text wrapping
    JLabel nameLabel = new JLabel(
        String.format("<html><center>%s</center></html>", space.getSpaceName()));
    nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
    buttonContent.add(nameLabel, BorderLayout.CENTER);

    button.setLayout(new BorderLayout());
    button.add(buttonContent);

    // Style the button
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
    button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
    button.setMargin(new Insets(5, 15, 5, 15));
    button.setFocusPainted(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Add hover effect
    button.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        button.setBackground(new Color(230, 230, 230));
      }

      public void mouseExited(java.awt.event.MouseEvent evt) {
        button.setBackground(UIManager.getColor("Button.background"));
      }
    });

    button.addActionListener(e -> {
      selectedSpaceName = space.getSpaceName();
      dispose();
    });

    return button;
  }

  private JPanel createCancelPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

    JButton cancelButton = new JButton("Cancel");
    cancelButton.setPreferredSize(new Dimension(100, 30));
    cancelButton.setFocusPainted(false);
    cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    cancelButton.addActionListener(e -> dispose());

    panel.add(cancelButton);

    return panel;
  }

  /**
   * Shows the dialog and returns the selected space name.
   *
   * @return the name of the selected space, or null if cancelled
   */
  public String showDialog() {
    setVisible(true);
    return selectedSpaceName;
  }
}