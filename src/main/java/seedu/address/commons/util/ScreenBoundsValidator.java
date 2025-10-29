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
    private static final double DEFAULT_SAFE_WIDTH = 1024; // smallest common desktop resolution
    private static final double DEFAULT_SAFE_HEIGHT = 768; // smallest common desktop resolution
    private static final double SAFE_PERCENTAGE = 0.9; // 90% to ensure fit + maximise visibility

    /**
     * Checks if the given window position is visible on any available screen.
     * A position is considered visible if at least a portion of the window
     * (defined by MINIMUM_VISIBLE_WIDTH x MINIMUM_VISIBLE_HEIGHT) would be
     * displayed within any screen's bounds.
     *
     * @param coordinates The window position to check. Must not be null.
     * @param width The window width.
     * @param height The window height.
     * @return true if the position is visible on any screen, false if null or completely off-screen.
     */
    public static boolean isPositionVisible(Point coordinates, double width, double height) {
        if (coordinates == null) {
            return false; // null coordinates are not visible
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
     * Returns a safe position for the window. If the given coordinates are null or invalid
     * (off-screen), returns null to signal that there is no safe position, and that a
     * corrected position should be calculated. If the coordinates are visible on screen,
     * returns them unchanged.
     * <p>
     * This method is independent and does not assume any default positioning behavior.
     * The caller is responsible for deciding what to do when null is returned.
     *
     * @param coordinates The window position to validate.
     * @param width The window width.
     * @param height The window height.
     * @return The original coordinates if valid and visible, or null if coordinates need correction.
     */
    public static Point getSafePosition(Point coordinates, double width, double height) {
        // Handle null explicitly - null coordinates need correction
        if (coordinates == null) {
            return null;
        }

        // Check if the provided coordinates are visible on any screen
        if (isPositionVisible(coordinates, width, height)) {
            return coordinates; // Position is valid, return as-is
        }

        return null; // Position is off-screen, needs correction
    }

    /**
     * Checks if the given window dimensions are valid.
     * Dimensions are considered valid if they meet the minimum size requirements
     * and fit within at least one available screen's bounds.
     *
     * @param width The window width.
     * @param height The window height.
     * @return true if the dimensions are valid (not too small and fit on at least one screen), false otherwise.
     */
    public static boolean areDimensionsValid(double width, double height) {
        // Check minimum dimension requirements
        if (width < MINIMUM_VISIBLE_WIDTH || height < MINIMUM_VISIBLE_HEIGHT) {
            return false;
        }

        // Check if dimensions fit on at least one screen
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

        // Dimensions invalid, calculate safe dimensions based on primary screen
        Screen primaryScreen = Screen.getPrimary();
        Rectangle2D primaryBounds = primaryScreen.getVisualBounds();

        // 90% of screen width and height to ensure you can see the whole app
        double safeWidth = primaryBounds.getWidth() * SAFE_PERCENTAGE;
        double safeHeight = primaryBounds.getHeight() * SAFE_PERCENTAGE;

        // Calculated safe dimensions must always be valid
        assert areDimensionsValid(safeWidth, safeHeight)
                : "Safe dimensions calculated from primary screen must be valid";

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
