package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

/**
 * A custom dialog class for displaying messages, warnings, and errors. This
 * class provides a consistent look and feel for all popup dialogs in the
 * application.
 */
public class CustomDialog extends JDialog {
  private static final long serialVersionUID = -1982278163095427878L;
  private static final int DEFAULT_WIDTH = 400;
  private static final int DEFAULT_HEIGHT = 150;
  private static final int PADDING = 10;

  /**
   * Creates a custom dialog.
   *
   * @param owner   the parent frame
   * @param title   the dialog title
   * @param message the message to display
   * @param type    the message type (ERROR_MESSAGE, WARNING_MESSAGE, or
   *                INFORMATION_MESSAGE)
   */
  public CustomDialog(Frame owner, String title, String message, int type) {
    super(owner, title, true);
    initializeDialog(message, type);
  }

  /**
   * Initializes the dialog with the given message and type.
   *
   * @param message the message to display
   * @param type    the message type (ERROR_MESSAGE, WARNING_MESSAGE, or
   *                INFORMATION_MESSAGE)
   */
  private void initializeDialog(String message, int type) {
    setLayout(new BorderLayout(PADDING, PADDING));

    // Create icon based on message type
    Icon icon = getIconForType(type);

    // Create components
    JPanel contentPanel = createContentPanel(icon, message);
    JPanel buttonPanel = createButtonPanel();

    // Add components to dialog
    add(contentPanel, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);

    // Configure dialog properties
    configureDialog();
  }

  /**
   * Returns the icon for the given message type.
   *
   * @param type the message type
   * @return the icon for the given message type
   */
  private Icon getIconForType(int type) {
    switch (type) {
      case JOptionPane.ERROR_MESSAGE:
        return UIManager.getIcon("OptionPane.errorIcon");
      case JOptionPane.WARNING_MESSAGE:
        return UIManager.getIcon("OptionPane.warningIcon");
      case JOptionPane.INFORMATION_MESSAGE:
        return UIManager.getIcon("OptionPane.informationIcon");
      default:
        return UIManager.getIcon("OptionPane.informationIcon");
    }
  }

  /**
   * Creates the content panel for the dialog.
   *
   * @param icon    the icon to display
   * @param message the message to display
   * @return the content panel
   */
  private JPanel createContentPanel(Icon icon, String message) {
    JPanel contentPanel = new JPanel(new BorderLayout(PADDING, 0));
    contentPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

    // Icon panel
    JLabel iconLabel = new JLabel(icon);
    JPanel iconPanel = new JPanel(new BorderLayout());
    iconPanel.add(iconLabel, BorderLayout.CENTER);

    // Message panel
    JTextArea messageArea = createMessageArea(message);
    JScrollPane scrollPane = new JScrollPane(messageArea);
    scrollPane.setBorder(null);

    contentPanel.add(iconPanel, BorderLayout.WEST);
    contentPanel.add(scrollPane, BorderLayout.CENTER);

    return contentPanel;
  }

  /**
   * Creates the message area for the dialog.
   *
   * @param message the message to display
   * @return the message area
   */
  private JTextArea createMessageArea(String message) {
    JTextArea messageArea = new JTextArea(message);
    messageArea.setWrapStyleWord(true);
    messageArea.setLineWrap(true);
    messageArea.setOpaque(false);
    messageArea.setEditable(false);
    messageArea.setFont(new Font("Dialog", Font.PLAIN, 14));
    messageArea.setBorder(BorderFactory.createEmptyBorder(0, PADDING, 0, 0));

    // Calculate preferred size based on message length
    int preferredWidth = DEFAULT_WIDTH - 100; // Account for icon and padding
    int preferredHeight = Math.min((int) messageArea.getPreferredSize().getHeight(),
        DEFAULT_HEIGHT - 100 // Account for button and padding
    );
    messageArea.setPreferredSize(new Dimension(preferredWidth, preferredHeight));

    return messageArea;
  }

  /**
   * Creates the button panel for the dialog.
   * 
   * @return the button panel 
   */
  private JPanel createButtonPanel() {
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, PADDING, PADDING, PADDING));

    JButton okButton = new JButton("OK");
    okButton.setPreferredSize(new Dimension(100, 30));
    okButton.addActionListener(e -> dispose());
    buttonPanel.add(okButton);

    return buttonPanel;
  }

  /**
   * Configures the dialog properties.
   */
  private void configureDialog() {
    pack();
    setSize(Math.max(DEFAULT_WIDTH, getWidth()), Math.max(DEFAULT_HEIGHT, getHeight()));
    setLocationRelativeTo(getOwner());
    setResizable(false);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  }
}