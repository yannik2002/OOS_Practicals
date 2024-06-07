package bank.exceptions;

/**
 * Exception für den Fall, dass eine Transaktion mit dem Namen schon existiert
 */
public class TransactionAlreadyExistException extends Exception
{
    public TransactionAlreadyExistException(String output) {
        super(output);
    }
}
