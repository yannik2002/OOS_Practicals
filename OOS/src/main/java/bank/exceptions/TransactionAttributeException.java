package bank.exceptions;

/**
 * Exception für den Fall, dass ein Attribut fehlerhaft gesetzt wird
 */
public class TransactionAttributeException extends Exception
{
    public TransactionAttributeException(String output) {
        super(output);
    }
}