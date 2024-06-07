package bank;

import bank.exceptions.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;


public class PrivateBank implements Bank {
    /**
     * Dieses Attribut soll den Namen der privaten Bank präsentieren
     */
    protected String name;
    /**
     * Dieses Attribut gibt die Zinsen (positiver Wert in Prozent, 0 bis 1) an,
     * die bei einer Einzahlung ("Deposit") anfallen
     */
    protected double incomingInterest;
    /**
     * Dieses Attribut gibt die Zinsen (positiver Wert in Prozent, 0 bis 1) an,
     * die bei einer Auszahlung ("Withdrawal") anfallen
     */
    protected double outgoingInterest;
    /**
     * Dieses Attribut soll Konten mit
     * Transaktionen verknüpfen, so dass jedem gespeicherten Konto 0 bis n Transaktionen zugeordnet
     * werden können. Der Schlüssel steht für den Namen des Kontos.
     */
    protected Map<String, List<Transaction>> accountsToTransactions = new HashMap<>();
    /**
     * Der Speicherort der Konten bzw. Transaktionen
     */
    public String directoryName;

    public Map<String,List<Transaction>> getAccountsToTransaction(){ return accountsToTransactions; }

    /**
     * Löscht einen Account aus der Bank
     * @param account der zu löschende Account
     * @throws AccountDoesNotExistException falls der zu löschende Account gar nicht existiert
     * @throws IOException falls beim Serialisieren ein Fehler auftritt
     */
    public void deleteAccount(String account) throws AccountDoesNotExistException, IOException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Der Account " + account + " existiert nicht!");
        }

        // account aus der map löschen
        accountsToTransactions.remove(account);

        // das file muss auch noch gelöscht werden
        File file = new File(getDirectoryName() + account + ".json");
        file.delete();
    }

    /**
     * Gibt alle Accounts der Bank zurück
     * @return List der Accounts, die in der Bank hinterlegt sind
     */
    public List<String> getAllAccounts() {
        // keySet gibt die menge (set) aller schlüssel (keys) zurück => alle accounts
        return new ArrayList<>(accountsToTransactions.keySet());
    }

    /**
     * Schreibt Kontodaten in eine Json Datei. Erstellt einen String aus verschiedenen Json Objekten
     * des accounts und öffnet einen Dateischreiber und erstellt eine neue .json Datei abhängig vom Kontonamen.
     * Der String wird in die neu erstellte Datei geschrieben
     * @param account Der Name des Kontos, für das die Daten geschrieben werden sollen
     * @throws IOException Falls ein Fehler beim Schreiben der Datei auftritt
     */
    private void writeAccount(String account) throws IOException {
        try {
            // FileWriter erstellt neue .json Datei bzw. überschreibt diese
            FileWriter fileWriter = new FileWriter(getDirectoryName() + account + ".json");

            // Gson Instanz erstellen, GsonBuilder ist Hilfsklasse von Gson
            // bietet Methoden wie prettyPrinting
            // Registrierung von benutzerdefinierter Serialisierung/Deserialisierung
            Gson serialize = new GsonBuilder()
                    .setPrettyPrinting() // Ausgabe wie vorgegeben
                    .registerTypeAdapter(Transaction.class, new CustomSerializerDeserializer()) // selbst definierte De-/Serialisierung nutzen
                    .create();

            // Erstellt ein Type Object, dass den generischen Typen <List<Transaction>> repräsentiert
            Type typeList = new TypeToken<List<Transaction>>() {}.getType();
            // .toJson ist aus Gson, typeList wird übergeben, um mitzuteilen, dass es sich bei übergebener Liste
            // um Liste von Transaktionen handelt, Liste wird in gson als Array repräsentiert
            String objectAsString = serialize.toJson(accountsToTransactions.get(account), typeList);
            fileWriter.write(objectAsString);
            fileWriter.close();

        } catch(IOException io) {
            throw new IOException(io);
        }
    }


    /**
     * Liest alle Json Dateien ein und fügt alle Transaktionen mit dem Kontonamen zu accountsToTransactions hinzu
     * @throws IOException Wenn ein Fehler beim Lesen der Dateien auftritt
     */
    private void readAccounts() throws IOException {
        // Das Verzeichnis zum Lesen öffnen, in dem die .json Dateien gespeichert sind
        File fileReader = new File(getDirectoryName());
        // Liste aller Files im Verzeichnis in File Array speichern
        File listOfFiles[] = fileReader.listFiles();
        // Name der aktuellen Datei ohne ".json" Endung
        String fileName = "";

        // Gson Instanz erstellen, GsonBuilder ist Hilfsklasse von Gson
        // bietet Methoden wie prettyPrinting
        // Registrierung von benutzerdefinierter Serialisierung/Deserialisierung
        Gson deserialize = new GsonBuilder()
                .registerTypeAdapter(Transaction.class, new CustomSerializerDeserializer())
                .setPrettyPrinting()
                .create();

        // Erstellt ein Type Object, dass den generischen Typen <List<Transaction>> repräsentiert
        Type typeList = new TypeToken<List<Transaction>>() {}.getType();

        // Iteriere über alle Dateien im Verzeichnis
        for(File file : listOfFiles) {
            // Name der aktuellen Datei mit Endung
            fileName = file.getName();
            // Aktuellen file zum Lesen öffnen
            FileReader reader = new FileReader(file);
            // Entferne die ".json" Endung vom fileName
            fileName = fileName.replace(".json","");
            // Deserialisiere das file in eine Liste von Transaction Objekten
            List<Transaction> list = deserialize.fromJson(reader, typeList);
            // die Liste dem account entsprechend in accountsToTransaction einfügen
            accountsToTransactions.put(fileName,list);
            reader.close();
        }
    }


    public String getDirectoryName() {
        return directoryName;
    }
    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Zinsen (positiver Wert in Prozent, 0 bis 1), die bei einer Einzahlung ("Deposit") anfallen
     */
    public double getIncomingInterest() {
        return incomingInterest;
    }
    /**
     * incomingInterest muss zwischen 0 und 1 liegen
     * @param incomingInterest setzt Zinsen (positiver Wert in Prozent, 0 bis 1), die bei einer Einzahlung ("Deposit") anfallen
     */
    public void setIncomingInterest(double incomingInterest) throws TransactionAttributeException {
        if (incomingInterest >= 0 && incomingInterest <= 1) { // incomingInterest muss zwischen 0 und 1 liegen
            this.incomingInterest = incomingInterest;
        } else { // ansonsten Fehler schmeißen
            throw new TransactionAttributeException("Fehler! " + incomingInterest + " liegt nicht zwischen 0 und 1 (in%).");
        }
    }
    /**
     * @return Zinsen (positiver Wert in Prozent, 0 bis 1), die bei einer Auszahlung ("Withdrawal") anfallen
     */
    public double getOutgoingInterest() {
        return this.outgoingInterest;
    }
    /**
     * outgoingInterest muss zwischen 0 und 1 liegen
     * @param outgoingInterest setzt Zinsen (positiver Wert in Prozent, 0 bis 1), die bei einer Auszahlung ("Withdrawal") anfallen
     */
    public void setOutgoingInterest(double outgoingInterest) throws TransactionAttributeException {
        if (outgoingInterest >= 0 && outgoingInterest <= 1) { //incomingInterest muss zwischen 0 und 1 liegen
            this.outgoingInterest = outgoingInterest;
        } else { //ansonsten Fehler schmeißen
            throw new TransactionAttributeException("Fehler! " + outgoingInterest + " liegt nicht zwischen 0 und 1 (in%).");
        }
    }

    /**
     * Konstruktor für die ersten 3 Attribute
     * @param name soll gesetzt werden
     * @param incomingInterest soll gesetzt werden
     * @param outgoingInterest soll gesetzt werden
     * @throws TransactionAttributeException für das Fehlerhafte setzen von incoming/outgoingInterest
     */
    public PrivateBank(String name, double incomingInterest, double outgoingInterest, String directoryName) throws TransactionAttributeException {
        this.setName(name);
        this.setIncomingInterest(incomingInterest);
        this.setOutgoingInterest(outgoingInterest);
        this.setDirectoryName(directoryName);

        // Neu für P4
        try {
            readAccounts();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    /**
     * Copy-Konstruktor für die ersten 3 Attribute
     * @param obj
     * @throws TransactionAttributeException für das Fehlerhafte setzen von incoming/outgoingInterest
     */
    public PrivateBank(PrivateBank obj) throws TransactionAttributeException {
        this.setName(obj.getName());
        this.setIncomingInterest(obj.getIncomingInterest());
        this.setOutgoingInterest(obj.getOutgoingInterest());
        this.setDirectoryName(obj.getDirectoryName());

        // Neu für P4
        try {
            readAccounts();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    /**
     * einfach toString-Methode für die drei Attribute von PrivateBank
     * @return String
     */
    @Override
    public String toString() {
        return "Name: " + getName() +
                " incomingInterest: " + getIncomingInterest() +
                " outgoingInterest: " + getOutgoingInterest() +
                " accountsToTransaction: " + accountsToTransactions;
    }

    /**
     * Vergleicht this mit obj, bei Gleichheit true
     * @param obj mit this zu vergleichen
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PrivateBank)) {
            return false;
        }
        // TypeCast für den Zugriff auf getter
        PrivateBank other = (PrivateBank) obj;
        return (this.getName().equals(other.getName()) &&
                this.getIncomingInterest() == other.getIncomingInterest() &&
                this.getOutgoingInterest() == other.getOutgoingInterest() &&
                this.accountsToTransactions.equals(other.accountsToTransactions));
    }

    /**
     * Adds an account to the bank.
     *
     * @param account the account to be added
     * @throws AccountAlreadyExistsException if the account already exists
     */
    public void createAccount(String account) throws AccountAlreadyExistsException {
        if (accountsToTransactions.containsKey(account)) {
            // falls es schon einen Account mit dem Namen gibt -> AccountAlreadyExistsException
            throw new AccountAlreadyExistsException("Der Account " + account + " existiert bereits!");
        } else {
            // falls nicht, account als Key zu Map hinzufügen mit leerer Transaktionsliste
            List<Transaction> empty_list = new ArrayList<>();
            accountsToTransactions.put(account, empty_list);
        }

        // Neu für P4
        try {
            writeAccount(account);
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    /**
     * Adds an account (with specified transactions) to the bank.
     * Important: duplicate transactions must not be added to the account!
     *
     * @param account      the account to be added
     * @param transactions a list of already existing transactions which should be added to the newly created account
     * @throws AccountAlreadyExistsException    if the account already exists
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    public void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException, AccountDoesNotExistException {
        if (accountsToTransactions.containsKey(account)) {
            // falls es schon einen Account mit dem Namen gibt -> AccountAlreadyExistsException
            throw new AccountAlreadyExistsException("Der Account " + account + " existiert bereits!");
        }
        // leere Liste um den Account in die Map zu übertragen
        List<Transaction> emptyList = new ArrayList<>();
        accountsToTransactions.put(account, emptyList);

        // im Nachgang die transactions über die addTransactions einfügen (dort sind exceptions oben implementiert)
        for (Transaction t : transactions) {
            this.addTransaction(account, t);
        }

        // Neu für P4
        try {
            writeAccount(account);
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    /**
     * Adds a transaction to an already existing account.
     *
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which should be added to the specified account
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    public void addTransaction(String account, Transaction transaction)
            throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {
        if (!(accountsToTransactions.containsKey(account))) {
            // falls der Account nicht in accountsToTransaction enthalten ist -> AccountDoesNotExistException
            throw new AccountDoesNotExistException("Der Account " + account + " existiert nicht!");
        }
        if (containsTransaction(account, transaction)) {
            // falls die Transaktion bereits existiert -> TransactionAlreadyExistsException
            throw new TransactionAlreadyExistException("Transaktion existiert bereits (Beschreibung: "
             + transaction.getDescription() + ")");
        }
        if (transaction instanceof Payment) {
            // im falle von Payments sollen die Attribute der transaction mit den Werten aus PrivateBank überschrieben werden
            Payment payment = (Payment) transaction; // TypeCast um auf setter zugreifen zu können
            payment.setIncomingInterest(this.getIncomingInterest());
            payment.setOutgoingInterest(this.getOutgoingInterest());
        }
        if (transaction instanceof Transfer) {
            if (((Transfer)transaction).getAmount() < 0) {
                // amount darf bei Transferobjekten nicht < 0 sein
                throw new TransactionAttributeException("Amount darf nicht kleiner als Null sein!");
            }
        }
        accountsToTransactions.get(account).add(transaction);

        // Neu für P4
        try {
            writeAccount(account);
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    /**
     * Removes a transaction from an account. If the transaction does not exist, an exception is
     * thrown.
     *
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is removed from the specified account
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionDoesNotExistException if the transaction cannot be found
     */
    public void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException {
        if (!accountsToTransactions.containsKey(account)) {
            // falls der Account nicht in accountsToTransaction enthalten ist -> AccountDoesNotExistException
            throw new AccountDoesNotExistException("Der Account " + account + " existiert nicht!");
        }
        if (!containsTransaction(account, transaction)) {
            // falls die zu löschende transaction nicht existiert
            throw new TransactionDoesNotExistException("Die Transaktion (Beschreibung: " + transaction.getDescription() + ") existiert nicht!");
        }
        // falls keine Exceptions aufgetreten sind -> löschen der transaction vornehmen
        accountsToTransactions.get(account).remove(transaction);

        // Neu für P4
        try {
            writeAccount(account);
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    /**
     * Checks whether the specified transaction for a given account exists.
     *
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction to search/look for
     */
    public boolean containsTransaction(String account, Transaction transaction) {
        if (!accountsToTransactions.containsKey(account)) {
            // falls nicht mal der Account existiert -> false
            return false;
        }
        // ansonsten: Account existiert, heißt man kann die zugehörigen transactions durchlaufen
        // .get(account) gibt hier die zugehörige Liste zu dem Account an
        for (Transaction t : accountsToTransactions.get(account)) {
            if (t.equals(transaction)) {
                // falls eines der Elemente aus der Liste gleich der gesuchten transaction ist
                return true;
            }
        }
        // falls kein gleiches Element gefunden wurde
        return false;
    }

    /**
     * Calculates and returns the current account balance.
     *
     * @param account the selected account
     * @return the current account balance
     */
    public double getAccountBalance(String account) {
        // in PrivateBank, also hier, normal programmieren ->einfach zusammenrechnen der calc amount ohne Unterscheidung
        double res = 0.0;
        if(accountsToTransactions.containsKey(account)) {
            // falls Account existiert
            for (Transaction t : accountsToTransactions.get(account)) {
                // alle transaction objects addieren
                res += t.calculate();
            }
        }
        return res;
    }

    /**
     * Returns a list of transactions for an account.
     *
     * @param account the selected account
     * @return the list of all transactions for the specified account
     */
    public List<Transaction> getTransactions(String account) {
        if (!accountsToTransactions.containsKey(account)) {
            return null;
        }
        // die Liste zum Account returnen
        return accountsToTransactions.get(account);
    }

    /**
     * Returns a sorted list (-> calculated amounts) of transactions for a specific account. Sorts the list either in ascending or descending order
     * (or empty).
     *
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted in ascending or descending order
     * @return the sorted list of all transactions for the specified account
     */
    public List<Transaction> getTransactionsSorted(String account, boolean asc) {
        if(!accountsToTransactions.containsKey(account)) {
            return null;
        }
        // Comparator definiert Vergleichsoperator: hier wird ein Double vergleichen, genauer über die Methode calculate
        accountsToTransactions.get(account).sort(Comparator.comparingDouble(Transaction::calculate));

        if (!asc) {
            // falls descending (absteigend) sortiert werden soll -> reverse
            Collections.reverse(accountsToTransactions.get(account));
        }

        // Neu für P4
        try {
            writeAccount(account);
        } catch (IOException e) {
            e.getStackTrace();
        }

        return accountsToTransactions.get(account);
    }

    /**
     * Returns a list of either positive or negative transactions (-> calculated amounts).
     *
     * @param account  the selected account
     * @param positive selects if positive or negative transactions are listed
     * @return the list of all transactions by type
     */
    public List<Transaction> getTransactionsByType(String account, boolean positive) {
        if (!accountsToTransactions.containsKey(account)) {
            // falls der Account gar nicht existiert
            return null;
        }
        List<Transaction> res = new ArrayList<>();
        if (positive) {
            // nur transactions mit positiven amounts in res speichern
            for (Transaction t : accountsToTransactions.get(account)) {
                // jedes transaction Element durchlaufen, das für diesen Account vorliegt
                if (t.calculate() >= 0) {
                    res.add(t);
                }
            }
        } else {
            // nur transactions mit negativen amounts in res speichern
            for (Transaction t : accountsToTransactions.get(account)) {
                // jedes transaction Element durchlaufen, das für diesen Account vorliegt
                if (t.calculate() < 0) {
                    res.add(t);
                }
            }
        }
        return res;
    }
}
