package bank.exceptions;

/**
 * Exception für den Fall, dass ein Account mit diesem Namen nicht existiert
 */
public class  AccountDoesNotExistException extends Exception
{
    public AccountDoesNotExistException(String output )  {
        super(output);
    }
}
