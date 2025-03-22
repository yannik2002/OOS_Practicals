package org.example;

import bank.*;
import bank.exceptions.*;

import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import com.google.gson.*;

public class Main {
    public static void main(String[] args) throws TransactionAttributeException, IOException, AccountAlreadyExistsException, TransactionAlreadyExistException, AccountDoesNotExistException, TransactionDoesNotExistException {

        /* Testen von Serialisierung/Deserialisierung Praktikum 4:
        PrivateBank bank = new PrivateBank("bank", 0.05, 0.1,
                    "C:\\Users\\ysint\\Desktop\\OOSPraktikum04\\src\\main\\java\\bank\\BankDaten\\");

        bank.createAccount("Fritz");
        Payment paymentFritz = new Payment("23.11.2023", 1000, "Irgendeine Zahlung");
        IncomingTransfer transferFritz1 = new IncomingTransfer("23.11.2023", 1000, "Zahlung an Hans", "Fritz", "Hans");
        IncomingTransfer transferFritz2 = new IncomingTransfer("23.11.2023", 1000, "Zahlung von Heinz", "Heinz", "Fritz");
        bank.addTransaction("Fritz", paymentFritz);
        bank.addTransaction("Fritz", transferFritz1);
        bank.addTransaction("Fritz", transferFritz2);

        bank.createAccount("Klaus");
        Payment paymentKlaus1 = new Payment("23.11.2023", 1000, "Irgendeine Zahlung");
        bank.addTransaction("Klaus", paymentKlaus1);

        System.out.println(bank.toString());*/

        
        //bank.addTransaction("peter", payment);
        //bank.addTransaction("peter", incomingTransfer1);
        //bank.writeAccount("peter");
        //bank.readAccounts();

        /*FileWriter writer = new FileWriter(this.directoryName + "/konto_" + account + ".json");
        List<Transaction> accountliste = accountsToTransactions.get(account);
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(Transaction.class, new CustomSerializerDeserializer()).setPrettyPrinting().create();
        writer.write(gson.toJson(accountliste));
        writer.close();*/



        /* Praktikum 3:
        // in der main versuchen alle exceptions mit try catch konstrukten zu fangen

        System.out.println("<====================================>");
        System.out.println("Exceptions testen: ");

        try {
            PrivateBank TestBank1 = new PrivateBank("TestBank1", 3, 0.5);
        } catch (PaymentInterestException e) {
            // nimmt hier das was ich in Payment unter throw new beim setzen schmeiße als nachricht
            System.out.println(e.getMessage());
        }

        try {
            OutgoingTransfer TestOutgoingTransfer = new OutgoingTransfer("12.11.2023", -1, "TestOutgoingTransfer");
        } catch (TransferAmountException e) {
            System.out.println(e.getMessage());
        }

        try {
            PrivateBank TestBank1 = new PrivateBank("TestBank1", 0.05, 0.1);
            TestBank1.createAccount("TestAccount1");
            TestBank1.createAccount("TestAccount1"); // Account existiert bereits
        } catch (PaymentInterestException | AccountAlreadyExistsException  e) {
            System.out.println(e.getMessage());
        }

        try {
            PrivateBank TestBank1 = new PrivateBank("TestBank1", 0.05, 0.1);
            Payment TestPayment = new Payment("TestPayment", 1000, "Description");
            TestBank1.addTransaction("TestAccount2", TestPayment); // der Account existiert nicht
        } catch (TransferAmountException | PaymentInterestException | TransactionAlreadyExistException |
                 AccountDoesNotExistException | TransactionAttributeException e) {
            System.out.println(e.getMessage());
        }

        try {
            PrivateBank TestBank1 = new PrivateBank("TestBank1", 0.05, 0.1);
            Payment TestPayment = new Payment("TestPayment", 1000, "Description");
            TestBank1.createAccount("TestAccount1");
            TestBank1.addTransaction("TestAccount1", TestPayment);
            TestBank1.addTransaction("TestAccount1", TestPayment);
        } catch (AccountAlreadyExistsException | TransferAmountException | PaymentInterestException | TransactionAlreadyExistException |
                 AccountDoesNotExistException | TransactionAttributeException e) {
            System.out.println(e.getMessage());
        }

        try {
            PrivateBank TestBank1 = new PrivateBank("TestBank1", 0.05, 0.1);
            Payment TestPayment = new Payment("TestPayment", 1000, "Description");
            TestBank1.createAccount("TestAccount1");
            TestBank1.removeTransaction("TestAccount1", TestPayment);
        } catch (AccountAlreadyExistsException | TransferAmountException | PaymentInterestException |
                 AccountDoesNotExistException | TransactionDoesNotExistException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("<====================================>");

        try {
            // TestBank erstellen
            PrivateBank TestBank1 = new PrivateBank("TestBank1", 0.05, 0.1);

            // Transactions erstellen
            IncomingTransfer TestIncomingTransfer = new IncomingTransfer("12.11.2023", 1000, "Beschreibung", "sender", "me");
            OutgoingTransfer TestOutgoingTransfer = new OutgoingTransfer("12.11.2023", 1000, "Beschreibung", "me", "recipient");
            Payment TestPayment = new Payment("23.11.2023", 2000, "TestPayment");

            // Account erstellen
            TestBank1.createAccount("me");
            TestBank1.addTransaction("me", TestIncomingTransfer);
            TestBank1.addTransaction("me", TestOutgoingTransfer);
            TestBank1.addTransaction("me", TestPayment);

            // Ausgeben der Transactions
            System.out.println("Unsortiert über getTransactions:");
            List<Transaction> list_randomOrder = TestBank1.getTransactions("me");
            System.out.println(list_randomOrder.toString());

            System.out.println("Aufsteigend sortiert über getTransactions:");
            List<Transaction> list_ascendingOrder = TestBank1.getTransactionsSorted("me", true);
            System.out.println(list_ascendingOrder.toString());

            System.out.println("Absteigend sortiert über getTransactions:");
            List<Transaction> list_descendingOrder = TestBank1.getTransactionsSorted("me", false);
            System.out.println(list_descendingOrder.toString());

            System.out.println("Nur Transaktionen mit positiven Amounts:");
            List<Transaction> list_positiveAmounts = TestBank1.getTransactionsByType("me", true);
            System.out.println(list_positiveAmounts.toString());

            System.out.println("Nur Transaktionen mit negativen Amounts:");
            List<Transaction> list_negativeAmounts = TestBank1.getTransactionsByType("me", false);
            System.out.println(list_negativeAmounts.toString());

            System.out.println("<====================================>");

            // hier testen von Balance

            // zweite Bank erstellen mit Alt
            PrivateBankAlt TestBankAlt = new PrivateBankAlt("TestBankAlt", 0.05, 0.1);
            TestBankAlt.createAccount("me");
            Transfer TestTransfer1 = new Transfer("12.11.2023", 1000, "Beschreibung", "sender", "me");
            Transfer TestTransfer2 = new Transfer("12.11.2023", 1000, "Beschreibung", "me", "recipient");
            TestBankAlt.addTransaction("me", TestTransfer1);
            TestBankAlt.addTransaction("me", TestTransfer2);
            TestBankAlt.addTransaction("me", TestPayment);

            System.out.println("TestBank1 Balance und TestBankAlt Balance müssen gleich sein:");
            System.out.println(TestBank1.getAccountBalance("me"));
            System.out.println(TestBankAlt.getAccountBalance("me"));

            System.out.println("<====================================>");

            // contains & remove testen
            System.out.println("Aufsteigend sortiert über getTransactions:");
            List<Transaction> listTest = TestBank1.getTransactionsSorted("me", true);
            System.out.println(listTest.toString());

            if (TestBank1.containsTransaction("me", TestPayment)) {
                System.out.println("TestPayment ist in TestBank1 enthalten!");
            }
            Payment lol = new Payment("1.1.1", 1, "desc");
            if (!TestBank1.containsTransaction("me", lol)) {
                System.out.println("TestIncomingTransfer ist NICHT in TestBank1 enthalten!");
            }
            TestBank1.removeTransaction("me", TestPayment);
            System.out.println("Nach dem Löschen von TestPayment:");
            System.out.println(listTest.toString());

            System.out.println("<====================================>");

            // equals testen
            PrivateBank TestBankNoCopy = new PrivateBank("Name", 0.05, 0.1);
            PrivateBank TestBankCopy = new PrivateBank(TestBankNoCopy);
            // was ist mit accountsToTransactions? wird laut Aufgabenstellung nicht kopiert beim Copy Konstruktor

            if (TestBankNoCopy.equals(TestBankCopy)) {
                System.out.println("TestBankNoCopy und TestBankCopy sind gleich.");
            }
            PrivateBank compare = new PrivateBank("name", 0.06, 0.1);
            if (!compare.equals(TestBankNoCopy)) {
                System.out.println("compare und TestBankNoCopy sind NICHT gleich.");
            }


        } catch (TransactionDoesNotExistException | PaymentInterestException | TransferAmountException |
                 AccountAlreadyExistsException | TransactionAlreadyExistException | AccountDoesNotExistException |
                 TransactionAttributeException e) {
            System.out.println(e.getMessage());
        }*/
    }
}
