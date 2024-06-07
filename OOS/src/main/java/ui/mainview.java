package ui;

import bank.PrivateBank;
import bank.exceptions.AccountAlreadyExistsException;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionAttributeException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.Event;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class mainview extends controllerviews implements Initializable {
    // die aktiven komponenten textfelder und buttons als instanzvariablen
    @FXML
    private ListView<String> listAccounts;
    @FXML
    ButtonBar addButton;
    @FXML
    MenuItem delete;
    @FXML
    MenuItem choose;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            privateBank = new PrivateBank("My Bank", 0.1, 0.1,
                    "C:\\Users\\ysint\\OOS\\src\\main\\java\\bank\\myBank\\");
        } catch (TransactionAttributeException e) {
            e.getStackTrace();
        }
        updatePrivateBank();
    }

    /**
     * listAccounts aktualisieren
     */
    public void updatePrivateBank() {
        listAccounts.setItems(FXCollections.observableArrayList(privateBank.getAllAccounts()));
    }

    /**
     * event zum hinzufügen eines accounts
     * @param event
     */
    @FXML
    public void addAccount(Event event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Account hinzufügen");
        dialog.setHeaderText("Möchtest du einen neuen Account hinzufügen?");
        dialog.setContentText("Name des Accounts:");

        // um die response zu bekommen
        Optional<String> result = dialog.showAndWait();

        // falls der dialog abgebrochen wird, returned result.isPresent() false
        if(result.isPresent()) {
            if(result.get().equals("")) {
                errorDialog("Es wurde keine Eingabe getätigt!");
                return;
            }
            // account einfügen, falls er nicht schon existiert
            try {
                privateBank.createAccount(result.get());
            } catch (AccountAlreadyExistsException e) {
                errorDialog("Der Account existiert bereits!");
                e.printStackTrace();
            }
        }
        // falls hier ein neuer account eingefügt wurde, muss listAccounts geupdated werden
        updatePrivateBank();
    }

    /**
     * event zum löschen eines accounts
     * @param event
     */
    @FXML
    public void removeAccount(Event event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Account löschen");
        alert.setHeaderText("Bist du sicher, dass Du den Account löschen möchtest?");

        // um die response zu bekommen
        Optional<ButtonType> result = alert.showAndWait();

        // falls cancel betätigt wurde abbruch
        if((result.get() == ButtonType.CANCEL)){
            return;
        }
        // bei OK eingabe wird account deleted
        try {
            privateBank.deleteAccount(listAccounts.getSelectionModel().getSelectedItem());
        } catch (AccountDoesNotExistException | IOException e) {
            errorDialog("Account existiert nicht!");
            e.printStackTrace();
        }
        // falls hier ein account deleted wurde, muss listTransactions geupdated werden
        updatePrivateBank();
    }

    /**
     * bereitet accountview for und lädt entsprechende fxml
     * @param event
     * @throws IOException
     */
    public void switchToAccountview(Event event) throws IOException, AccountDoesNotExistException {
        // falls nichts in der listview ausgewählt wurde
        if (listAccounts.getSelectionModel().getSelectedItem() == null) {
            throw new AccountDoesNotExistException("Account existiert nicht!");
        }

        // den selected account merken
        selectedAccount = listAccounts.getSelectionModel().getSelectedItem();

        // konstruktor fxmlloader erzeugt einen neuen fxmllader für fxml datei
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("accountview.fxml"));
        // die load methode verarbeitet dann die fxml datei und liefert das entsprechende wurzelobjekt
        Parent root = loader.load();
        myStage.setScene(new Scene(root, 455, 295));
    }
}