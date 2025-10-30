package seedu.address.ui;

import javafx.event.EventHandler;
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
    private Scene boundScene = null;

    private final KeyCodeCombination comboShortcutW =
            new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN);
    private final KeyCodeCombination comboEsc =
            new KeyCodeCombination(KeyCode.ESCAPE);
    private final KeyCodeCombination comboAltF4 =
            new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN);

    private final Runnable closeAction = this::onCloseShortcut;

    private final EventHandler<KeyEvent> fallbackFilter = e -> {
        boolean isShortcutW = enableShortcutWClose()
                && e.getCode() == KeyCode.W && e.isShortcutDown();
        boolean isEsc = enableEscClose()
                && e.getCode() == KeyCode.ESCAPE;
        boolean isAltF4 = enableAltF4Close()
                && e.getCode() == KeyCode.F4 && e.isAltDown();

        if (isShortcutW || isEsc || isAltF4) {
            e.consume();
            closeAction.run();
        }
    };

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

    /* ====== per-window toggles (override in subclasses) ====== */

    // Cmd/Ctrl + W
    protected boolean enableShortcutWClose() {
        return true;
    }

    // Esc
    protected boolean enableEscClose() {
        return true;
    }

    // Alt + F4
    protected boolean enableAltF4Close() {
        return true;
    }

    /**
     * Installs platform-aware accelerators and a fallback key filter.
     */
    protected final void installCloseAccelerators() {
        Stage stage = getRoot();

        // Re-bind whenever the Scene changes
        stage.sceneProperty().addListener((obs, oldScene, newScene) -> {
            detachFrom(oldScene);
            attachTo(newScene);
        });

        // Attach now if a Scene is already present; otherwise wait until shown
        if (stage.getScene() != null) {
            attachTo(stage.getScene());
        } else {
            stage.addEventHandler(WindowEvent.WINDOW_SHOWN, e -> {
                if (stage.getScene() != null) {
                    attachTo(stage.getScene());
                }
            });
        }
    }

    /** Attach accelerators to a Scene (idempotent per Scene). */
    private void attachTo(Scene scene) {
        if (scene == null || scene == boundScene) {
            return;
        }

        // Install accelerators
        if (enableShortcutWClose()) {
            scene.getAccelerators().put(comboShortcutW, closeAction);
        }
        if (enableEscClose()) {
            scene.getAccelerators().put(comboEsc, closeAction);
        }
        if (enableAltF4Close()) {
            scene.getAccelerators().put(comboAltF4, closeAction);
        }

        // Fallback filter
        scene.addEventFilter(KeyEvent.KEY_PRESSED, fallbackFilter);

        boundScene = scene;
    }

    /** Detach accelerators from a Scene if we had attached before. */
    private void detachFrom(Scene scene) {
        if (scene == null || scene != boundScene) {
            return;
        }

        // Remove accelerators we added (need the same KeyCodeCombination keys)
        if (enableShortcutWClose()) {
            scene.getAccelerators().remove(comboShortcutW);
        }
        if (enableEscClose()) {
            scene.getAccelerators().remove(comboEsc);
        }
        if (enableAltF4Close()) {
            scene.getAccelerators().remove(comboAltF4);
        }

        scene.removeEventFilter(KeyEvent.KEY_PRESSED, fallbackFilter);
        boundScene = null;
    }
}
