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
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setIcey(icey);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

