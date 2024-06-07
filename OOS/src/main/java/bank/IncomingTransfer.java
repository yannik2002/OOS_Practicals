package bank;

import bank.exceptions.TransactionAttributeException;

/**
 * Unterklasse von Transfer für eingehende Überweisungen
 */
public class IncomingTransfer extends Transfer{

    /**
     * Konstruktor der Klasse IncomingTransfer, setzt die ersten drei Klassenattribute
     * nutzt über den Konstruktor der Oberklasse
     * @param date        - der zu setzende Wert
     * @param amount      - der zu setzende Wert
     * @param description - der zu setzende Wert
     */
    public IncomingTransfer(String date, double amount, String description) throws TransactionAttributeException {
        super(date, amount, description);
    }

    /**
     * Konstruktor der Klasse IncomingTransfer, setzt alle Klassenattribute
     * nutzt über den Konstruktor der Oberklasse
     * @param date        - der zu setzende Wert
     * @param amount      - der zu setzende Wert
     * @param description - der zu setzende Wert
     * @param sender      - der zu setzende Wert
     * @param recipient   - der zu setzende Wert
     */
    public IncomingTransfer(String date, double amount, String description, String sender, String recipient) throws TransactionAttributeException {
        super(date, amount, description, sender, recipient);
    }

    /**
     * Einziger Copy Constructor der Klasse IncomingTransfer
     * @param other - Das zu kopierende Element
     */
    public IncomingTransfer(Transfer other) throws TransactionAttributeException {
        super(other);
    }

    /*
     * in IncomingInterest braucht man die calculate-Methode der Oberklasse nicht zu
     * überschreiben, da eine Zahlung auf das eigene Konto immer positiv ist
     */
}
