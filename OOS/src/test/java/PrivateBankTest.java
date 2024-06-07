import bank.*;
import bank.exceptions.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testklasse für Konstruktoren, alle impl. Methoden des Interfaces Bank, equals und toString
 */
public class PrivateBankTest {
    PrivateBank privateBank;
    PrivateBank privateBankTest;
    PrivateBank privateBank2;
    PrivateBank privateBankException;
    List<Transaction> list = new ArrayList<>();

    @BeforeEach
    public void init() throws TransactionAttributeException {
        File file = new File("C:\\Users\\ysint\\OOS\\src\\main\\java\\bank\\myBank\\Yannik.json\\");
        if (file.exists()) { file.delete(); }

        privateBank = new PrivateBank("Sparkasse", 0.05, 0.1,
                "C:\\Users\\ysint\\OOS\\src\\main\\java\\bank\\myBank\\");
        IncomingTransfer transfer1 = new IncomingTransfer("23.11.2023", 1000, "transfer1", "Irgendwer", "Yannik");
        IncomingTransfer transfer2 = new IncomingTransfer("24.11.2023", 500.5, "transfer2", "Noch irgendwer", "Yannik");
        OutgoingTransfer transfer3 = new OutgoingTransfer("25.11.2023", 300.01, "transfer3", "Yannik", "Keine Ahnung");
        list.add(transfer1);
        list.add(transfer2);
        list.add(transfer3);
    }

    @Test
    public void testConstructor() {
        Assertions.assertEquals(privateBank.getName(), "Sparkasse");
        Assertions.assertEquals(privateBank.getIncomingInterest(), 0.05);
        Assertions.assertEquals(privateBank.getOutgoingInterest(), 0.1);
    }

    @Test
    public void testCopyConstructor() throws TransactionAttributeException {
        assertDoesNotThrow(()->{
           privateBankTest = new PrivateBank(privateBank);
        });
        Assertions.assertEquals(privateBank, privateBankTest);
    }

    @Test
    public void testToString() {
        Assertions.assertEquals("Name: Sparkasse incomingInterest: 0.05 outgoingInterest: 0.1 accountsToTransaction: {}", privateBank.toString());
    }

    @Test
    public void testEquals() throws TransactionAttributeException {
        assertDoesNotThrow(()->{
            privateBank2 = new PrivateBank("Andere Bank", 0.07, 0.5,
                    "C:\\Users\\ysint\\OOS\\src\\main\\java\\bank\\myBank\\");
            privateBankTest = new PrivateBank(privateBank);
        });
        Assertions.assertTrue(privateBank.equals(privateBankTest));
        Assertions.assertFalse(privateBank.equals(privateBank2));

        assertThrows(TransactionAttributeException.class, ()->{
            privateBankException = new PrivateBank("Fehler", 10, -5, "");
        });
    }

    @Test
    public void testCreateAccount() {
        assertDoesNotThrow(()->{
            privateBank.createAccount("Yannik");
        });
        Map<String, List<Transaction>> testMap = new HashMap<>();
        testMap.put("Yannik", new ArrayList<>());
        Assertions.assertEquals(privateBank.getAccountsToTransaction(), testMap);

        assertThrows(AccountAlreadyExistsException.class, ()->{
            privateBank.createAccount("Yannik");
        });
    }

    @Test
    public void testCreateAccountWithList() {
        assertDoesNotThrow(()->{
            privateBank.createAccount("Yannik", list);
        });
        Map<String, List<Transaction>> testMap = new HashMap<>();
        testMap.put("Yannik", list);
        Assertions.assertEquals(privateBank.getAccountsToTransaction(), testMap);

        assertThrows(AccountAlreadyExistsException.class, ()->{
           privateBank.createAccount("Yannik", list);
        });
        assertThrows(TransactionAttributeException.class, ()->{
            IncomingTransfer failTransfer = new IncomingTransfer("1.1.1", -1, "", "", "");
            list.add(failTransfer);
            privateBank.createAccount("NeuerName", list);
        });
    }

    @Test
    public void testAddTransactions() throws TransactionAttributeException {
        IncomingTransfer testTransfer = new IncomingTransfer("24.11.2023", 50, "Beschreibung", "Jemand", "Yannik");
        assertDoesNotThrow(()->{
            privateBank.createAccount("Yannik");
            privateBank.addTransaction("Yannik", testTransfer);
            Assertions.assertEquals(privateBank.toString(), "Name: Sparkasse incomingInterest: 0.05 outgoingInterest: 0.1 accountsToTransaction: {Yannik=[Date: 24.11.2023 Amount: 50.0 Description: Beschreibung Sender: Jemand Recipient: Yannik]}");
        });
        assertThrows(TransactionAlreadyExistException.class, ()->{
            privateBank.addTransaction("Yannik", testTransfer);
        });
        assertThrows(AccountDoesNotExistException.class, ()->{
           privateBank.addTransaction("ExistiertNicht", testTransfer);
        });
    }

    @Test
    public void testRemoveTransactions() throws TransactionAttributeException {
        IncomingTransfer testTransfer = new IncomingTransfer("24.11.2023", 50, "Beschreibung", "Jemand", "Yannik");
        assertDoesNotThrow(()->{
            privateBank.createAccount("Yannik");
            privateBank.addTransaction("Yannik", testTransfer);
            Assertions.assertEquals(privateBank.toString(), "Name: Sparkasse incomingInterest: 0.05 outgoingInterest: 0.1 accountsToTransaction: {Yannik=[Date: 24.11.2023 Amount: 50.0 Description: Beschreibung Sender: Jemand Recipient: Yannik]}");
            privateBank.removeTransaction("Yannik", testTransfer);
            Assertions.assertEquals(privateBank.toString(), "Name: Sparkasse incomingInterest: 0.05 outgoingInterest: 0.1 accountsToTransaction: {Yannik=[]}");
        });
        assertThrows(TransactionDoesNotExistException.class, ()->{
           privateBank.removeTransaction("Yannik", testTransfer);
        });
    }

    @Test
    public void testContainsTransactions() throws TransactionAttributeException {
        IncomingTransfer testTransfer = new IncomingTransfer("24.11.2023", 50, "Beschreibung", "Jemand", "Yannik");
        assertDoesNotThrow(()->{
            privateBank.createAccount("Yannik");
            privateBank.addTransaction("Yannik", testTransfer);
            Assertions.assertEquals(privateBank.toString(), "Name: Sparkasse incomingInterest: 0.05 outgoingInterest: 0.1 accountsToTransaction: {Yannik=[Date: 24.11.2023 Amount: 50.0 Description: Beschreibung Sender: Jemand Recipient: Yannik]}");
        });
        Assertions.assertTrue(privateBank.containsTransaction("Yannik", testTransfer));
        Assertions.assertFalse(privateBank.containsTransaction("Yannik", new OutgoingTransfer("", 1, "")));
    }

    @Test
    public void testGetAccountBalance() throws TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException {
        privateBank.createAccount("Yannik", list);
        Assertions.assertEquals(privateBank.getAccountBalance("Yannik"), 1200.49);
    }

    @Test
    public void testGetTransactions() throws TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException {
        privateBank.createAccount("Yannik", list);
        Assertions.assertEquals(privateBank.getTransactions("Yannik"), list);
    }

    @Test
    public void testGetTransactionsSorted() throws TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException {
        privateBank.createAccount("Yannik", list);
        list.sort(Comparator.comparingDouble(Transaction::calculate));
        Assertions.assertEquals(privateBank.getTransactionsSorted("Yannik", true), list);
    }

    @Test
    public void testGetTransactionsByType() throws TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException {
        privateBank.createAccount("Yannik", list);
        List<Transaction> resList = new ArrayList<>();
        // alle positiven calculate() ergebnisse in resList speichern
        for (Transaction t : list) {
            if (t.calculate() >= 0) {
                resList.add(t);
            }
        }
        Assertions.assertEquals(privateBank.getTransactionsByType("Yannik", true), resList);
    }

    @AfterEach
    public void de_serializeTest() throws TransactionAttributeException, IOException, TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException {
        PrivateBank testBank = new PrivateBank("Sparkasse", 0.05, 0.1,
                "C:\\Users\\ysint\\OOS\\src\\main\\java\\bank\\myBank");
        Assertions.assertEquals(privateBank, testBank);
    }
}
