package icey.ui;

import icey.Icey;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Icey icey;

    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/User.png"));
    private final Image iceyImage = new Image(this.getClass().getResourceAsStream("/images/Icey.png"));

    /**
     * Initializes the main window controller.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        scrollPane.setFitToWidth(true);
        dialogContainer.setFillWidth(true);
        dialogContainer.setCache(true);
        dialogContainer.setCacheHint(CacheHint.SPEED);
    }

    public void setIcey(Icey d) {
        icey = d;
        dialogContainer.getChildren().add(
                DialogBox.getIceyDialog(icey.getGreeting(), iceyImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Icey's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = icey.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getIceyDialog(response, iceyImage)
        );
        userInput.clear();
        if (icey.isExit()) {
            PauseTransition delay = new PauseTransition(javafx.util.Duration.seconds(1));
            delay.setOnFinished(event -> Platform.exit());
            delay.play();
        }
    }
}
