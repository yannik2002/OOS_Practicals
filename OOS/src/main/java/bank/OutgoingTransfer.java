package bank;

import bank.exceptions.TransactionAttributeException;

/**
 * Unterklasse von Transfer für ausgehende Überweisungen
 */
public class OutgoingTransfer extends Transfer{

    /**
     * Erster Konstruktor der Klasse OutgoingTransfer, setzt nur die ersten drei Klassenattribute
     * @param date        - der zu setzende Wert
     * @param amount      - der zu setzende Wert
     * @param description - der zu setzende Wert
     */
    public OutgoingTransfer(String date, double amount, String description) throws TransactionAttributeException {
        super(date, amount, description);
    }

    /**
     * Zweiter Konstruktor der Klasse OutgoingTransfer, setzt alle Klassenattribute
     * @param date        - der zu setzende Wert
     * @param amount      - der zu setzende Wert
     * @param description - der zu setzende Wert
     * @param sender      - der zu setzende Wert
     * @param recipient   - der zu setzende Wert
     */
    public OutgoingTransfer(String date, double amount, String description, String sender, String recipient) throws TransactionAttributeException {
        super(date, amount, description, sender, recipient);
    }

    /**
     * Einziger Copy Constructor der Klasse OutgoingTransfer
     * @param other - Das zu kopierende Element
     */
    public OutgoingTransfer(Transfer other) throws TransactionAttributeException {
        super(other);
    }

    /**
     * Override der calculate-Methode aus der Oberklasse
     * Falls es sich um einen ausgehenden Transfer handelt (Klasse: OutgoingTransfer)
     * muss Amount negativ behandelt werden
     * @return der gleiche Wert negativ zurückgegeben
     */
    @Override
    public double calculate() {
        return(super.calculate()*-1);
    }

}
