package bank;

import bank.exceptions.TransactionAttributeException;

import java.util.Objects;

public class Transfer extends Transaction {

    /**
     * Dieses Attribut gibt an, welcher Akteur die Geldmenge, die in amount angegeben wurde, überwiesen hat
     */
    private String sender = "N/A";
    /**
     * Dieses Attribut gibt an, welcher Akteur die Geldmenge, die in amount angegeben wurde, überwiesen bekommen hat
     */
    private String recipient = "N/A";

    /**
     *
     * @return gibt Geldmenge einer Ein- oder Auszahlung bzw. einer Überweisung unverändert zurück
     */
    @Override
    public double calculate() {
        return amount;
    }

    /**
     *
     * @return welcher Akteur die Geldmenge, die in amount angegeben wurde, überwiesen hat
     */
    public String getSender() {
        return this.sender;
    }

    /**
     *
     * @param sender setzt welcher Akteur die Geldmenge, die in amount angegeben wurde, überwiesen hat
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     *
     * @return welcher Akteur die Geldmenge, die in amount angegeben wurde, überwiesen bekommen hat
     */
    public String getRecipient() {
        return this.recipient;
    }
    /**
     *
     * @param recipient setzt welcher Akteur die Geldmenge, die in amount angegeben wurde, überwiesen hat
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    /**
     * amount darf nicht kleiner als 0 sein
     * @param amount setzt Geldmenge einer Ein- oder Auszahlung bzw. einer Überweisung
     */
    @Override
    public void setAmount(double amount) throws TransactionAttributeException {
        if (amount >= 0) {
            this.amount = amount;
        } else {
            throw new TransactionAttributeException("Fehler! Amount muss bei Transferobjekten positiv sein.");
        }
    }

    /** Erstellt ein neues {@link Transfer} object mit date, amount und description
     * @param date Zeitpunkt einer Ein- oder Auszahlung bzw. einer Überweisung
     * @param amount Stellt die Geldmenge einer Ein- oder Auszahlung bzw. einer Überweisung dar
     * @param description Dieses Attribut erlaubt zusätzliche Beschreibung des Vorgangs
     * @return Transfer object
     */
    public Transfer(String date, double amount, String description) throws TransactionAttributeException {
        super(date, amount, description);
    }

    /** Erstellt ein neues {@link Transfer} object mit date, amount, description, sender und recipient
     * @param date Zeitpunkt einer Ein- oder Auszahlung bzw. einer Überweisung
     * @param amount Stellt die Geldmenge einer Ein- oder Auszahlung bzw. einer Überweisung dar
     * @param description Dieses Attribut erlaubt zusätzliche Beschreibung des Vorgangs
     * @param sender Dieses Attribut gibt an, welcher Akteur die Geldmenge, die in amount angegeben wurde, überwiesen hat
     * @param recipient Dieses Attribut gibt an, welcher Akteur die Geldmenge, die in amount angegeben wurde, überwiesen bekommen hat
     * @return Transfer object
     */
    public Transfer(String date, double amount, String description, String sender, String recipient) throws TransactionAttributeException {
        super(date, amount, description);
        setSender(sender);
        setRecipient(recipient);
    }

    /**
     * Erstellt ein neues {@link Transfer} object nach Vorlage eines anderen {@link Transfer} object
     * @param obj das zu kopierende object
     * @return das neu erstellte kopierte object
     */
    public Transfer(Transfer obj) throws TransactionAttributeException {
        super(obj.date, obj.amount,obj.description);
        setSender(obj.sender);
        setRecipient(obj.recipient);
    }

    /**
     * @return String output des objects
     */
    @Override
    public String toString() {
        return (super.toString() + " Sender: " + sender +
                " Recipient: " + recipient);
    }

    /**
     * @param o ist mit this zu vergleichen
     * @return true wenn es sich bei object und this um das gleich object handelt
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Transfer transfer = (Transfer) o;
        return Objects.equals(sender, transfer.sender) && Objects.equals(recipient, transfer.recipient);
    }
}