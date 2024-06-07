package ui;
import bank.*;
import bank.exceptions.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Pair;


import java.io.IOException;
import java.net.URL;
import java.util.*;

import static ui.mainview.*;


public class accountview extends controllerviews implements Initializable {
    // die aktiven komponenten textfelder und buttons als instanzvariablen
    @FXML
    private TextFlow textAccount;
    @FXML
    private TextFlow textAccountBalance;
    @FXML
    private ListView<Transaction> listTransactions;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // account label und name setzen
        Text account = new Text("Account: " + selectedAccount);
        account.setFont(Font.font("Arial", FontPosture.REGULAR, 14));
        textAccount.getChildren().add(account);

        // listTransactions updaten
        try {
            updateTransactionList();
        } catch (AccountDoesNotExistException e) {
            e.getStackTrace();
        }
    }

    /**
     * dialog transaction löschen
     * @throws AccountDoesNotExistException
     */
    public void removeTransaction() throws AccountDoesNotExistException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Transaktion löschen");
        alert.setHeaderText("Bist du sicher, dass du die Transaktion löschen willst?");

        Optional<ButtonType> result = alert.showAndWait();

        // abbruch falls nicht ok b
        if ((result.get() == ButtonType.CANCEL)) {
            return;
        }
        try {
            privateBank.removeTransaction(selectedAccount, listTransactions.getSelectionModel().getSelectedItem());
        } catch (AccountDoesNotExistException e) {
            errorDialog("Account existiert nicht!");
            e.printStackTrace();
        } catch (TransactionDoesNotExistException e) {
            errorDialog("Transaktion existiert nicht!");
            throw new RuntimeException(e);
        }

        // am ende vom löschen muss die transactionlist geupdated werden
        updateTransactionList();
    }


    /**
     * transactions und kontostand wird hier aktualisiert
     * @throws AccountDoesNotExistException
     */
    public void updateTransactionList() throws AccountDoesNotExistException {
        // liste der transactions aktualisieren
        listTransactions.setItems(FXCollections.observableArrayList(privateBank.getTransactions(selectedAccount)));

        // text erstellen
        Text accountBalanceLabel = new Text("Kontostand: ");
        // double to String mit zwei nachkommastellen
        Text accountBalanceValue = new Text(String.format("%.2f",privateBank.getAccountBalance(selectedAccount)) + "€");

        accountBalanceLabel.setFont(Font.font("Arial", FontPosture.REGULAR, 14));
        accountBalanceValue.setFont(Font.font("Arial", FontPosture.REGULAR, 14));

        // children clearen, ansonsten wird einfach über die alten werte drübergeschrieben
        textAccountBalance.getChildren().clear();

        // textflow erstellen mit label inklusive value
        TextFlow textFlow = new TextFlow(accountBalanceLabel, accountBalanceValue);
        // zu textAccountBalance hinzufügen
        textAccountBalance.getChildren().add(textFlow);
    }

    /**
     * returned die transactions aufsteigend
     * @throws AccountDoesNotExistException falls account nicht existiert
     */
    public void showAscending() throws AccountDoesNotExistException {
        listTransactions.setItems(FXCollections.observableArrayList(privateBank.getTransactionsSorted(selectedAccount, true)));
    }

    /**
     * returned die transactions absteigend
     * @throws AccountDoesNotExistException falls account nicht existiert
     */
    public void showDescending() throws AccountDoesNotExistException {
        listTransactions.setItems(FXCollections.observableArrayList(privateBank.getTransactionsSorted(selectedAccount, false)));
    }

    /**
     * returned nur die positiven transactions
     * @throws AccountDoesNotExistException falls account nicht existiert
     */
    public void showPositive() throws AccountDoesNotExistException {
        listTransactions.setItems(FXCollections.observableArrayList(privateBank.getTransactionsByType(selectedAccount, true)));
    }

    /**
     * returned nur die negativen transactions
     * @throws AccountDoesNotExistException falls account nicht existiert
     */
    public void showNegative() throws AccountDoesNotExistException {
        listTransactions.setItems(FXCollections.observableArrayList(privateBank.getTransactionsByType(selectedAccount, false)));
    }

    /**
     * umschalten von accountview zu mainview
     * @param event betätigung des buttons "Zurück"
     * @throws IOException
     */
    public void backToAccounts(Event event) throws IOException {
        // konstruktor fxmlloader erzeugt einen neuen fxmllader für fxml datei
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mainview.fxml"));
        // die load methode verarbeitet dann die fxml datei und liefert das entsprechende wurzelobjekt
        Parent root = loader.load();
        myStage.setScene(new Scene(root, 455, 295));
    }


    /**
     * dialog: zuerst auswahl payment oder transfer
     * dann folgt eingabe von der jeweiligen transaction
     * @param event
     * @throws AccountDoesNotExistException
     */
    @FXML
    public void addTransaction(Event event) throws AccountDoesNotExistException {
        // die verschiedenen auswahlmöglichkeiten für den choices dialog anlegen
        List<String> choices = new ArrayList<>();
        choices.add("Payment");
        choices.add("Transfer");

        // choices dialog anlegen
        ChoiceDialog<String> dialogChoice = new ChoiceDialog<>("Payment", choices);
        dialogChoice.setTitle("Transaktion hinzufügen");
        dialogChoice.setHeaderText("Neue Transaktion hinzufügen");
        dialogChoice.setContentText("Payment oder Transfer?");

        // um die response zu bekommen
        Optional<String> choiceResponse = dialogChoice.showAndWait();

        // falls Payment oder Transfer ausgewählt wurden
        if (choiceResponse.isPresent()) {

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Transaktion hinzufügen");
            dialog.setHeaderText("Neue Transaktion hinzufügen");

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            // textfields erstellen
            TextField date = new TextField();
            date.setPromptText("Date");
            TextField amount = new TextField();
            amount.setPromptText("Amount");
            TextField description = new TextField();
            description.setPromptText("Description");

            // textfields zu einem grid mit label hinzufügen
            // die hier sind für Payment und Transfer gleich
            grid.add(new Label("Date:"),0,0);
            grid.add(date,0,0);
            grid.add(new Label("Amount:"), 0, 1);
            grid.add(amount, 0, 1);
            grid.add(new Label("Description:"), 0, 2);
            grid.add(description, 0, 2);

            // hier unterscheidung zwischen Payment und Transfer
            if(choiceResponse.get().equals("Payment")) {
                // falls Payment gewählt wurde
                TextField incoming = new TextField();
                incoming.setPromptText("IncomingInterest");

                TextField outgoing = new TextField();
                outgoing.setPromptText("OutgoingInterest");

                // die textfields für payment zum grid hinzufügen
                grid.add(new Label("IncomingInterest:"),0,3);
                grid.add(incoming,0,3);
                grid.add(new Label("OutgoingInterest:"), 0, 4);
                grid.add(outgoing, 0, 4);


                dialog.getDialogPane().setContent(grid);
                dialogChoice.close();

                // auf schließung des dialogs warten
                Optional<String> result = dialog.showAndWait();

                // wird nur übersprungen, falls nutzer abbricht oder keine auswahl trifft
                if (result.isPresent()) {
                    // falls nicht alle attribute angegeben wurden
                    if (date.getText().isEmpty() ||
                            amount.getText().isEmpty() ||
                            description.getText().isEmpty() ||
                            incoming.getText().isEmpty() ||
                            outgoing.getText().isEmpty()) {
                        errorDialog("Es wurden nicht alle Attribute eingegeben!");

                    } else {
                        // falls alle attribute eingegeben wurden, versuche transaction hinzuzufügen
                        try {
                            privateBank.addTransaction(selectedAccount,
                                    new Payment(date.getText(),
                                            Double.parseDouble(amount.getText()),
                                            description.getText(),
                                            Double.parseDouble(incoming.getText()),
                                            Double.parseDouble(outgoing.getText())));

                        } catch (TransactionAlreadyExistException e) {
                            errorDialog("Transaktion existiert bereits!");
                            e.printStackTrace();
                        } catch (AccountDoesNotExistException e) {
                            errorDialog("Account existiert nicht!");
                            e.printStackTrace();
                        } catch (TransactionAttributeException e) {
                            errorDialog("Interests konnten nicht hinzugefügt werden!");
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                // falls Transfer gewählt wurde
                TextField sender = new TextField();
                sender.setPromptText("Sender");

                TextField recipient = new TextField();
                recipient.setPromptText("Recipient");

                // die textfields für transfer zum grid hinzufügen
                grid.add(new Label("Sender:"),0,3);
                grid.add(sender,0,3);
                grid.add(new Label("Recipient:"), 0, 4);
                grid.add(recipient, 0, 4);

                dialog.getDialogPane().setContent(grid);
                dialogChoice.close();

                // auf schließung des dialogs warten
                Optional<String> result = dialog.showAndWait();

                // wird nur übersprungen, falls nutzer abbricht oder keine auswahl trifft
                if(result.isPresent()) {
                    if(date.getText().isEmpty() ||
                            amount.getText().isEmpty() ||
                            description.getText().isEmpty() ||
                            sender.getText().isEmpty() ||
                            recipient.getText().isEmpty()) {
                        errorDialog("Es wurden nicht alle Attribute eingegeben!");
                    }
                    // ab hier soll transfer hinzugefügt werden, unterscheidung outgoing/incoming notwendig

                    // falls sender = selectedaccount ist, handelt es sich um outgoingtransfer
                    if (sender.getText().equals(selectedAccount)) {
                        try {
                            privateBank.addTransaction(selectedAccount,
                                    new OutgoingTransfer(date.getText(),
                                            Double.parseDouble(amount.getText()),
                                            description.getText(),
                                            sender.getText(),
                                            recipient.getText()));

                        } catch (TransactionAlreadyExistException e) {
                            errorDialog("Transaktion existiert bereits!");
                            e.printStackTrace();
                        } catch (AccountDoesNotExistException e) {
                            errorDialog("Account existiert nicht!");
                            e.printStackTrace();
                        } catch (TransactionAttributeException e) {
                            errorDialog("Interests konnten nicht hinzugefügt werden!");
                            e.printStackTrace();
                        }

                    // ansonsten handelt es sich um incomingtransfer
                    } else if (recipient.getText().equals(selectedAccount)) {
                        try {
                            privateBank.addTransaction(selectedAccount,
                                    new IncomingTransfer(date.getText(),
                                            Double.parseDouble(amount.getText()),
                                            description.getText(),
                                            sender.getText(),
                                            recipient.getText()));

                        } catch (TransactionAlreadyExistException e) {
                            errorDialog("Transaktion existiert bereits!");
                            e.printStackTrace();
                        } catch (AccountDoesNotExistException e) {
                            errorDialog("Account existiert nicht!");
                            e.printStackTrace();
                        } catch (TransactionAttributeException e) {
                            errorDialog("Interests konnten nicht hinzugefügt werden!");
                            e.printStackTrace();
                        }
                    }
                }
            }
            updateTransactionList();
        }
    }
}
