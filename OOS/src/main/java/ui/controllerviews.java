package ui;

import bank.PrivateBank;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class controllerviews extends Application {

    protected static Stage myStage;
    protected static String selectedAccount;
    protected static PrivateBank privateBank;

    // launch(args) führt zum start von start(Stage stage)
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        myStage = stage;
        // konstruktor fxmlloader erzeugt einen neuen fxmllader für fxml datei
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mainview.fxml"));
        // die load methode verarbeitet dann die fxml datei und liefert das entsprechende wurzelobjekt
        Parent root = loader.load();
        myStage.setTitle("Deine Bank");
        myStage.setScene(new Scene(root, 455, 295));
        myStage.show();
    }

    /**
     * Error Dialog
     * @param headerText gibt die exception an
     */
    public static void errorDialog(String headerText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
}
