package bank;

import bank.exceptions.TransactionAttributeException;

import java.util.Objects;

public class Payment extends Transaction {

    /**
     * Dieses Attribut gibt die Zinsen (positiver Wert in Prozent, 0 bis 1) an,
     * die bei einer Einzahlung ("Deposit") anfallen
     */
    private double incomingInterest;
    /**
     * Dieses Attribut gibt die Zinsen (positiver Wert in Prozent, 0 bis 1) an,
     * die bei einer Auszahlung ("Withdrawal") anfallen
     */
    private double outgoingInterest;

    /**
     * amount stellt die Geldmenge einer Ein- oder Auszahlung bzw. einer Überweisung dar
     * incomingInterest gibt Zinsen in Prozent zwischen 0 und 1 an, die bei einer Einzahlung anfallen
     * outgoingInterest gibt Zinsen in Prozent zwischen 0 und 1 an, die bei einer Auszahlung anfallen
     * @return Falls amount positiv ist: Wert von incomingInterest prozentual von Amount abziehen
     * Falls amount negativ ist: Wert von outgoingInterest prozentual zu Amount hinzuaddieren
     */
    @Override
    public double calculate() {
        if (amount >= 0) {
            return amount - (amount * incomingInterest);
        } else {
            return amount + (amount * outgoingInterest);
        }
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
        if (incomingInterest >= 0 && incomingInterest <= 1) { //incomingInterest muss zwischen 0 und 1 liegen
            this.incomingInterest = incomingInterest;
        } else { //ansonsten Fehler schmeißen
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

    /** Erstellt ein neues {@link Payment} object mit date, amount und description
     * @param date Zeitpunkt einer Ein- oder Auszahlung bzw. einer Überweisung
     * @param amount Stellt die Geldmenge einer Ein- oder Auszahlung bzw. einer Überweisung dar
     * @param description Dieses Attribut erlaubt zusätzliche Beschreibung des Vorgangs
     */
    public Payment(String date, double amount, String description) throws TransactionAttributeException {
        super(date, amount, description);
    }

    /** Erstellt ein neues {@link Payment} object mit date, amount, description, incomingInterest und outgoingInterest
     * @param date Zeitpunkt einer Ein- oder Auszahlung bzw. einer Überweisung
     * @param amount Stellt die Geldmenge einer Ein- oder Auszahlung bzw. einer Überweisung dar
     * @param description Dieses Attribut erlaubt zusätzliche Beschreibung des Vorgangs
     * @param incomingInterest Dieses Attribut gibt die Zinsen (positiver Wert in Prozent, 0 bis 1) an, die bei einer Einzahlung ("Deposit") anfallen
     * @param outgoingInterest Dieses Attribut gibt die Zinsen (positiver Wert in Prozent, 0 bis 1) an, die bei einer Auszahlung ("Withdrawal") anfallen
     */
    public Payment(String date, double amount, String description, double incomingInterest, double outgoingInterest ) throws
            TransactionAttributeException {
        super(date, amount, description);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
    }

    /**
     * Erstellt ein neues {@link Payment} object nach Vorlage eines anderen {@link Payment} object
     * @param obj das zu kopierende object
     */
    public Payment(Payment obj) throws TransactionAttributeException {
        super(obj.date, obj.amount,obj.description);
        setIncomingInterest(obj.incomingInterest);
        setOutgoingInterest(obj.outgoingInterest);
    }

    /**
     * @return String output des objects
     */
    @Override
    public String toString() {
        return (super.toString() + " incomingInterest: " + incomingInterest +
                " outgoingInterest: " + outgoingInterest);
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Payment payment = (Payment) o;
        return Objects.equals(incomingInterest, payment.incomingInterest) && Objects.equals(outgoingInterest, payment.outgoingInterest);
    }
}