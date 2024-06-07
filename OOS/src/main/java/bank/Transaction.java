package bank;

import bank.exceptions.TransactionAttributeException;

import java.util.Objects;

public abstract class Transaction implements CalculateBill  {
    /**
     * Zeitpunkt einer Ein- oder Auszahlung bzw. einer Überweisung
     */
    protected String date;
    /**
     * Stellt die Geldmenge einer Ein- oder Auszahlung bzw. einer Überweisung dar
     */
    protected double amount;
    /**
     * Dieses Attribut erlaubt zusätzliche Beschreibung des Vorgangs
     */
    protected String description;

    /**
     *
     * @return Zeitpunkt einer Ein- oder Auszahlung bzw. einer Überweisung
     */
    public String getDate() {
        return date;
    }
    /**
     *
     * @param date setzt Zeitpunkt einer Ein- oder Auszahlung bzw. einer Überweisung
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return Geldmenge einer Ein- oder Auszahlung bzw. einer Überweisung
     */
    public double getAmount() {
        return amount;
    }

    /**
     *
     * @param amount setzt Geldmenge einer Ein- oder Auszahlung bzw. einer Überweisung
     */
    public void setAmount(double amount) throws TransactionAttributeException {
        this.amount = amount;
    }

    /**
     *
     * @return eine zusätzliche Beschreibung des Vorgangs
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description setzt zusätzliche Beschreibung des Vorgangs
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Erstellt ein neues {@link Transaction} object mit drei Attributen
     * @param date Zeitpunkt einer Ein- oder Auszahlung bzw. einer Überweisung
     * @param amount Stellt die Geldmenge einer Ein- oder Auszahlung bzw. einer Überweisung dar
     * @param description Dieses Attribut erlaubt zusätzliche Beschreibung des Vorgangs
     * @return Transaction object
     */
    public Transaction(String date, double amount, String description) throws TransactionAttributeException {
        setDate(date);
        setAmount(amount);
        setDescription(description);
    }

    /**
     * Erstellt ein neues {@link Transaction} object nach Vorlage eines anderen {@link Transaction} object
     * @param obj das zu kopierende object
     * @return das neu erstellte kopierte object
     */
    public Transaction(Transaction obj) throws TransactionAttributeException { //erstellt eine Kopie von object
        setDate(obj.getDate());
        setAmount(obj.getAmount());
        setDescription(obj.getDescription());
    }

    /**
     * @return String output des objects
     */
    @Override
    public String toString() {
        // gemeinsamen Attribute über toString der Oberklasse ausgeben
        return ("Date: " + date + " Amount: " + calculate() +
                " Description: " + description);
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
        Transaction that = (Transaction) o;
        return Objects.equals(date, that.date) && Objects.equals(amount, that.amount) && Objects.equals(description, that.description);
    }
}
