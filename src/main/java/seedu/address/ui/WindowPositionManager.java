package seedu.address.ui;

import java.awt.Point;
import java.util.logging.Logger;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.ScreenBoundsValidator;

/**
 * Manages window positioning for JavaFX stages.
 * Handles validation of saved window positions and dimensions, ensuring windows
 * always open in visible screen areas with appropriate sizes.
 */
public class WindowPositionManager {

    private static final Logger logger = LogsCenter.getLogger(WindowPositionManager.class);

    /**
     * Applies GUI settings to the stage with validation.
     * Validates both window position and dimensions. If either is invalid
     * (e.g., off-screen position or window too large for current screens),
     * creates corrected default settings, applies them, and returns the
     * corrected settings to be saved.
     *
     * @param stage The JavaFX stage to configure.
     * @param guiSettings The settings to apply.
     * @return The GuiSettings that were actually applied (original if valid, corrected if invalid).
     */
    public static GuiSettings applyGuiSettings(Stage stage, GuiSettings guiSettings) {
        // Step 1: Validate and correct dimensions
        DimensionResult dimensionResult = validateAndCorrectDimensions(guiSettings);

        // Step 2: Validate and correct position (using corrected dimensions)
        PositionResult positionResult = validateAndCorrectPosition(
                guiSettings.getWindowCoordinates(),
                dimensionResult.width,
                dimensionResult.height
        );

        // Step 3: Apply corrected settings to stage
        applyDimensionsToStage(stage, dimensionResult.width, dimensionResult.height);
        applyPositionToStage(stage, positionResult.position, dimensionResult.width, dimensionResult.height);

        // Step 4: Return corrected settings if any correction was made
        boolean needsCorrection = dimensionResult.wasCorrected || positionResult.wasCorrected;
        if (needsCorrection) {
            GuiSettings correctedSettings = captureGuiSettings(stage);
            logger.info("Window settings corrected and will be saved to preferences.");
            return correctedSettings;
        }

        return guiSettings; // No correction needed, return original
    }

    /**
     * Validates window dimensions and corrects them if necessary.
     *
     * @param guiSettings The GUI settings containing dimensions to validate.
     * @return A DimensionResult containing the validated dimensions and whether correction was needed.
     */
    private static DimensionResult validateAndCorrectDimensions(GuiSettings guiSettings) {
        double width = guiSettings.getWindowWidth();
        double height = guiSettings.getWindowHeight();

        double[] safeDimensions = ScreenBoundsValidator.getSafeDimensions(width, height);

        if (safeDimensions[0] != width || safeDimensions[1] != height) {
            logger.warning("Window dimensions (" + width + "x" + height
                    + ") are too large for available screens. Resizing to ("
                    + safeDimensions[0] + "x" + safeDimensions[1] + ")");
            return new DimensionResult(safeDimensions[0], safeDimensions[1], true);
        }

        return new DimensionResult(width, height, false);
    }

    /**
     * Validates window position and corrects it if necessary.
     *
     * @param position The window position to validate.
     * @param width The window width.
     * @param height The window height.
     * @return A PositionResult containing the validated position and whether correction was needed.
     */
    private static PositionResult validateAndCorrectPosition(Point position, double width, double height) {
        Point safePosition = ScreenBoundsValidator.getSafePosition(position, width, height);

        if (safePosition == null && position != null) {
            logger.warning("Window position (" + position.getX() + ", " + position.getY()
                    + ") is off-screen. Centering window on primary screen.");
            Point centeredPosition = calculateCenterPosition(width, height);
            return new PositionResult(centeredPosition, true);
        } else if (safePosition == null) {
            // No safe position, center by default
            Point centeredPosition = calculateCenterPosition(width, height);
            return new PositionResult(centeredPosition, true);
        }

        return new PositionResult(safePosition, false);
    }

    /**
     * Calculates the center position for a window on the primary screen.
     *
     * @param width The window width.
     * @param height The window height.
     * @return A Point representing the centered position.
     */
    private static Point calculateCenterPosition(double width, double height) {
        Rectangle2D primaryBounds = Screen.getPrimary().getVisualBounds();
        double centerX = primaryBounds.getMinX() + (primaryBounds.getWidth() - width) / 2;
        double centerY = primaryBounds.getMinY() + (primaryBounds.getHeight() - height) / 2;
        return new Point((int) centerX, (int) centerY);
    }

    /**
     * Applies dimensions to the stage.
     *
     * @param stage The stage to configure.
     * @param width The window width.
     * @param height The window height.
     */
    private static void applyDimensionsToStage(Stage stage, double width, double height) {
        stage.setWidth(width);
        stage.setHeight(height);
    }

    /**
     * Applies position to the stage.
     *
     * @param stage The stage to configure.
     * @param position The window position.
     * @param width The window width (for logging purposes).
     * @param height The window height (for logging purposes).
     */
    private static void applyPositionToStage(Stage stage, Point position, double width, double height) {
        stage.setX(position.getX());
        stage.setY(position.getY());
        logger.fine("Window positioned at coordinates: (" + position.getX() + ", " + position.getY() + ")");
    }

    /**
     * Helper class to store dimension validation results.
     */
    private static class DimensionResult {
        final double width;
        final double height;
        final boolean wasCorrected;

        DimensionResult(double width, double height, boolean wasCorrected) {
            this.width = width;
            this.height = height;
            this.wasCorrected = wasCorrected;
        }
    }

    /**
     * Helper class to store position validation results.
     */
    private static class PositionResult {
        final Point position;
        final boolean wasCorrected;

        PositionResult(Point position, boolean wasCorrected) {
            this.position = position;
            this.wasCorrected = wasCorrected;
        }
    }

    /**
     * Captures current GUI settings from the stage.
     * Creates a GuiSettings object representing the current window state
     * (size and position).
     *
     * @param stage The JavaFX stage to capture settings from.
     * @return GuiSettings representing the current window state.
     */
    public static GuiSettings captureGuiSettings(Stage stage) {
        return new GuiSettings(
                stage.getWidth(),
                stage.getHeight(),
                (int) stage.getX(),
                (int) stage.getY()
        );
    }
}
