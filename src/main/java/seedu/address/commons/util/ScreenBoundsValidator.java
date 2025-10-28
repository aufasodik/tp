package seedu.address.commons.util;

import java.awt.Point;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 * Helper functions for validating window positions and dimensions against screen bounds.
 */
public class ScreenBoundsValidator {

    private static final int MINIMUM_VISIBLE_WIDTH = 50;
    private static final int MINIMUM_VISIBLE_HEIGHT = 50;
    private static final double DEFAULT_SAFE_WIDTH = 1024;
    private static final double DEFAULT_SAFE_HEIGHT = 768;

    /**
     * Checks if the given window position is visible on any available screen.
     * A position is considered visible if at least a portion of the window
     * (defined by MINIMUM_VISIBLE_WIDTH x MINIMUM_VISIBLE_HEIGHT) would be
     * displayed within any screen's bounds.
     *
     * @param coordinates The window position to check. If null, returns true (uses default positioning).
     * @param width The window width.
     * @param height The window height.
     * @return true if the position is valid (visible or null), false if completely off-screen.
     */
    public static boolean isPositionVisible(Point coordinates, double width, double height) {
        if (coordinates == null) {
            return true; // null coordinates means use default JavaFX positioning
        }

        // Check if the window is visible on any screen
        for (Screen screen : Screen.getScreens()) {
            Rectangle2D bounds = screen.getVisualBounds();
            if (isVisibleOnScreen(coordinates, width, height, bounds)) {
                return true;
            }
        }

        return false; // Not visible on any screen
    }

    /**
     * Returns a safe position for the window. If the given coordinates are valid
     * (visible on screen), returns them unchanged. If invalid or null, returns null
     * to signal that default JavaFX positioning should be used.
     *
     * @param coordinates The window position to validate.
     * @param width The window width.
     * @param height The window height.
     * @return The original coordinates if valid, or null to use default positioning.
     */
    public static Point getSafePosition(Point coordinates, double width, double height) {
        if (coordinates == null) {
            return null; // Already safe - will use default positioning
        }

        if (isPositionVisible(coordinates, width, height)) {
            return coordinates; // Position is valid, return unchanged
        }

        return null; // Invalid position - return null to trigger default positioning
    }

    /**
     * Checks if the given window dimensions fit within any available screen.
     * Dimensions are considered valid if they are smaller than or equal to
     * at least one screen's available space.
     *
     * @param width The window width.
     * @param height The window height.
     * @return true if the dimensions fit on at least one screen, false otherwise.
     */
    public static boolean areDimensionsValid(double width, double height) {
        for (Screen screen : Screen.getScreens()) {
            Rectangle2D bounds = screen.getVisualBounds();
            if (width <= bounds.getWidth() && height <= bounds.getHeight()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns safe dimensions for the window. If the given dimensions fit on any screen,
     * returns them unchanged. Otherwise, returns safe default dimensions that will fit
     * on most screens.
     *
     * @param width The requested window width.
     * @param height The requested window height.
     * @return An array [width, height] with safe dimensions.
     */
    public static double[] getSafeDimensions(double width, double height) {
        if (areDimensionsValid(width, height)) {
            return new double[]{width, height};
        }

        // Dimensions too large, try to fit within primary screen
        Screen primaryScreen = Screen.getPrimary();
        Rectangle2D primaryBounds = primaryScreen.getVisualBounds();

        double safeWidth = Math.min(width, primaryBounds.getWidth() * 0.9); // 90% of screen width
        double safeHeight = Math.min(height, primaryBounds.getHeight() * 0.9); // 90% of screen height

        // Ensure minimum safe defaults
        safeWidth = Math.max(safeWidth, DEFAULT_SAFE_WIDTH);
        safeHeight = Math.max(safeHeight, DEFAULT_SAFE_HEIGHT);

        // Final check - if still too large, use defaults
        if (safeWidth > primaryBounds.getWidth() || safeHeight > primaryBounds.getHeight()) {
            safeWidth = DEFAULT_SAFE_WIDTH;
            safeHeight = DEFAULT_SAFE_HEIGHT;
        }

        return new double[]{safeWidth, safeHeight};
    }

    /**
     * Checks if a window at the given position would be visible on a specific screen.
     * A window is considered visible if at least MINIMUM_VISIBLE_WIDTH x MINIMUM_VISIBLE_HEIGHT
     * pixels of the window would be within the screen bounds.
     *
     * @param coordinates The window position.
     * @param width The window width.
     * @param height The window height.
     * @param screenBounds The bounds of the screen to check against.
     * @return true if at least part of the window would be visible on this screen.
     */
    private static boolean isVisibleOnScreen(Point coordinates, double width, double height,
                                              Rectangle2D screenBounds) {
        double windowX = coordinates.getX();
        double windowY = coordinates.getY();

        // Calculate the visible region of the window
        double visibleLeft = Math.max(windowX, screenBounds.getMinX());
        double visibleTop = Math.max(windowY, screenBounds.getMinY());
        double visibleRight = Math.min(windowX + width, screenBounds.getMaxX());
        double visibleBottom = Math.min(windowY + height, screenBounds.getMaxY());

        // Calculate visible dimensions
        double visibleWidth = visibleRight - visibleLeft;
        double visibleHeight = visibleBottom - visibleTop;

        // Check if enough of the window is visible
        return visibleWidth >= MINIMUM_VISIBLE_WIDTH && visibleHeight >= MINIMUM_VISIBLE_HEIGHT;
    }
}
