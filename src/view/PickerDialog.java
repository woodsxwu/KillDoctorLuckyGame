package view;

/**
 * Interface for picker dialog components that allow selection from a list of
 * options.
 * 
 * @param <T> The type of value being returned by the dialog
 */
public interface PickerDialog<T> {
  /**
   * Shows the dialog and returns the selected value.
   *
   * @return the selected value, or null if cancelled/closed
   */
  T showDialog();

  /**
   * Makes the dialog visible or invisible.
   *
   * @param visible true to make the dialog visible, false to hide it
   */
  void setVisible(boolean visible);

  /**
   * Disposes of the dialog, freeing up system resources.
   */
  void dispose();
}