package icey.ui;

import java.io.IOException;

import icey.Icey;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Icey using FXML.
 */
public class Main extends Application {

    private final Icey icey = new Icey();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            scene.getStylesheets().add(Main.class.getResource("/view/icey-theme.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Icey");
            fxmlLoader.<MainWindow>getController().setIcey(icey);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

