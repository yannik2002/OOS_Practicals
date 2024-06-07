package bank.exceptions;

/**
 * Exception für den Fall, dass ein Account mit dem gleichen Namen besteht
 */
public class AccountAlreadyExistsException extends Exception
{
    public AccountAlreadyExistsException(String output) {
        super(output);
    }
}
