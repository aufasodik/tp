package seedu.address.ui;

import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://nus-cs2103-ay2526s1.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE = "Refer to the full user guide at: " + USERGUIDE_URL;
    public static final String COMMAND_OVERVIEW = """
                                Commands Overview:

                                add n/NAME [p/PHONE] [e/EMAIL] [a/ADDRESS] [r/REMARK] [s/STATUS] [t/TAG]...
                                    Add a company to Cerebro.
                                    Examples:
                                    add n/Google Inc
                                    add n/Meta p/65432100 e/careers@meta.com s/applied
                                    add n/ByteDance r/Fast-growing s/tech-interview t/tech t/remote-friendly

                                list
                                    Display all companies in Cerebro.

                                edit INDEX [,INDEX]... [fields]
                                edit START-END [fields]
                                    Edit one or more companies.
                                    Examples:
                                    edit 2 n/Meta Platforms s/offered
                                    edit 1,3,5 s/rejected
                                    edit 2-4 s/applied t/tech

                                find KEYWORD [MORE_KEYWORDS]
                                    Search for companies by name.
                                    Examples:
                                    find Google

                                delete INDEX [,INDEX]...
                                delete START-END
                                    Delete one or more companies.
                                    Examples:
                                    delete 2
                                    delete 1,3,5
                                    delete 2-4

                                status INDEX s/STATUS
                                    Update the application status for a company.
                                    valid statuses: to-apply, applied, oa, tech-interview, hr-interview,
                                                    in-process, offered, accepted, rejected
                                    Examples:
                                    status 1 s/tech-interview
                                    status 3 s/rejected
                                    status 5 s/offered

                                remark
                                    Add, edit, or delete remarks for a company.
                                    Usage: remark INDEX r/[REMARK]
                                    Examples:
                                    remark 1 r/Great company culture

                                clear
                                    Clear all companies from Cerebro (irreversible!).

                                metrics
                                    View application status metrics.

                                help
                                    Display this help message.

                                exit
                                    Exit Cerebro.
                                """;


    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    @FXML
    private TextArea commandOverview;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
        commandOverview.setText(COMMAND_OVERVIEW);

        installCloseAccelerators();
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();

        Platform.runLater(() -> {
            if (getRoot().getScene() != null && getRoot().getScene().getRoot() != null) {
                getRoot().getScene().getRoot().requestFocus();
            }
        });
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }

    private void installCloseAccelerators() {
        Stage stage = getRoot();
        Runnable close = this::hide;

        // If the Scene hasn't been attached yet, defer until shown.
        if (stage.getScene() == null) {
            stage.addEventHandler(WindowEvent.WINDOW_SHOWN, e -> installCloseAccelerators());
            return;
        }

        Scene scene = stage.getScene();

        // Platform-aware shortcuts (Cmd on macOS, Ctrl elsewhere)
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN), close);

        // Escape
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.ESCAPE), close);

        // Windows/Linux system close
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN), close);

        // Fallback: if a focused control consumes key events (e.g., TextArea), catch them here.
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            boolean isShortcutW = e.getCode() == KeyCode.W && e.isShortcutDown();
            boolean isEsc = e.getCode() == KeyCode.ESCAPE;
            boolean isAltF4 = e.getCode() == KeyCode.F4 && e.isAltDown();
            if (isShortcutW || isEsc || isAltF4) {
                e.consume();
                close.run();
            }
        });
    }
}
