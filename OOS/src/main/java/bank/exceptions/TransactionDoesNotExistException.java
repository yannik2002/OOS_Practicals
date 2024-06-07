package bank.exceptions;

/**
 * Exception für den Fall, dass eine Transaktion mit dem Namen nicht existiert
 */
public class TransactionDoesNotExistException extends Exception
{
    public TransactionDoesNotExistException(String output) {
        super(output);
    }
}
