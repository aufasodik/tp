package seedu.address.ui;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Base class for Stage-based windows that can be closed with keyboard shortcuts.
 * Copies the accelerator logic (SHORTCUT+W, ESC, ALT+F4) and a key-event fallback.
 */
public abstract class ClosableWindow extends UiPart<Stage> {

    protected ClosableWindow(String fxml, Stage root) {
        super(fxml, root);
        installCloseAccelerators();
    }

    /**
     * Default close behavior is to hide the Stage. Subclasses can override if needed.
     */
    protected void onCloseShortcut() {
        getRoot().hide();
    }

    /**
     * Installs platform-aware accelerators and a fallback key filter.
     */
    protected final void installCloseAccelerators() {
        Stage stage = getRoot();
        Runnable close = this::onCloseShortcut;

        // If Scene isn't ready yet, defer until the Stage is shown.
        if (stage.getScene() == null) {
            stage.addEventHandler(WindowEvent.WINDOW_SHOWN, e -> installCloseAccelerators());
            return;
        }

        Scene scene = stage.getScene();

        // SHORTCUT+W (Cmd on macOS, Ctrl on others)
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN), close);

        // Escape
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.ESCAPE), close);

        // Alt+F4 (common on Windows/Linux)
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN), close);

        // Fallback: catch keys even if a focused control consumes them (e.g., TextArea)
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            boolean isShortcutW = e.getCode() == KeyCode.W && e.isShortcutDown();
            boolean isEsc       = e.getCode() == KeyCode.ESCAPE;
            boolean isAltF4     = e.getCode() == KeyCode.F4 && e.isAltDown();

            if (isShortcutW || isEsc || isAltF4) {
                e.consume();
                close.run();
            }
        });
    }
}
