import bank.*;
import bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.*;

/**
 * Test Klasse für Transfer und die davon zur Verfügung gestellten Methoden
 */
public class TransferTest {
    OutgoingTransfer transfer1;
    IncomingTransfer transfer2;

    @BeforeEach
    public void init() throws TransactionAttributeException {
        transfer1 = new OutgoingTransfer("24.11.2023",200,"Beschreibung1","Yannik","Jemand");
        transfer2 = new IncomingTransfer("24.11.2023",50,"Beschreibung2","KeinPlan","Yannik");
    }

    @Test
    public void testConstructor() {
        Assertions.assertEquals(transfer1.getDate(),"24.11.2023");
        Assertions.assertEquals(transfer1.getAmount(),200);
        Assertions.assertEquals(transfer1.getDescription(),"Beschreibung1");
        Assertions.assertEquals(transfer1.getSender(),"Yannik");
        Assertions.assertEquals(transfer1.getRecipient(),"Jemand");

        Assertions.assertEquals(transfer2.getDate(),"24.11.2023");
        Assertions.assertEquals(transfer2.getAmount(),50);
        Assertions.assertEquals(transfer2.getDescription(),"Beschreibung2");
        Assertions.assertEquals(transfer2.getSender(),"KeinPlan");
        Assertions.assertEquals(transfer2.getRecipient(),"Yannik");
    }

    @Test
    public void testCopyConstructor() throws TransactionAttributeException {
        OutgoingTransfer testTransfer = new OutgoingTransfer(transfer1);
        Assertions.assertEquals(transfer1, testTransfer);
    }

    @Test
    public void calculate() {
        Assertions.assertEquals(transfer1.calculate(),-200);
        Assertions.assertEquals(transfer2.calculate(),50);
    }

    @Test
    public void equalsTest() throws TransactionAttributeException {
        Assertions.assertEquals(transfer1,new OutgoingTransfer("24.11.2023",200,"Beschreibung1","Yannik","Jemand"));
    }

    @Test
    public void toStringTest(){
        Assertions.assertEquals(transfer1.toString(),"Date: 24.11.2023 Amount: -200.0 Description: Beschreibung1 Sender: Yannik Recipient: Jemand");
    }

}
