import bank.Payment;
import bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Klasse für Payment und die davon zur Verfügung gestellten Methoden
 */
public class PaymentTest {
    Payment payment1;
    Payment payment2;
    Payment payment3;

    /**
     * Test Objekte, die vor jedem Test erstellt werden
     * @throws TransactionAttributeException
     */
    @BeforeEach
    public void init() throws TransactionAttributeException {
        payment1 = new Payment("24.11.2023", -200, "Beschreibung1", 0.05, 0.1);
        payment2 = new Payment("24.11.2023", 50, "Beschreibung2", 0.05, 0.1);
        payment3 = new Payment("24.11.2023", 50, "Beschreibung3");
    }

    @Test
    public void testConstructor() {
        Assertions.assertEquals(payment1.getDate(), "24.11.2023");
        Assertions.assertEquals(payment1.getAmount(), -200);
        Assertions.assertEquals(payment1.getDescription(), "Beschreibung1");
        Assertions.assertEquals(payment1.getIncomingInterest(), 0.05);
        Assertions.assertEquals(payment1.getOutgoingInterest(), 0.1);

        Assertions.assertEquals(payment3.getDate(), "24.11.2023");
        Assertions.assertEquals(payment3.getAmount(), 50);
        Assertions.assertEquals(payment3.getDescription(), "Beschreibung3");
    }

    @ParameterizedTest
    @ValueSource(doubles = {-10,-0.1,1.1})
    public void testConstructorMitFehler(double failure) {
        assertThrows(TransactionAttributeException.class, ()->{
            Payment paymentF = new Payment("01.01.2022",1000,"Fail",failure,failure);
            Assertions.assertNotEquals(paymentF.getIncomingInterest(), failure);
            Assertions.assertNotEquals(paymentF.getOutgoingInterest(), failure);
        });
    }

    @Test
    public void testCopyConstructor() throws TransactionAttributeException {
        Payment paymentTest = new Payment(payment1);
        Assertions.assertEquals(payment1, paymentTest);
    }

    @Test
    public void testCalculate() {
        Assertions.assertEquals(payment1.calculate(), (-200 + (-200 * 0.1)));
        Assertions.assertEquals(payment2.calculate(), (50 - (50 * 0.05)));
    }

    @Test
    public void testEquals() throws TransactionAttributeException {
        Assertions.assertEquals(payment1, new Payment("24.11.2023", -200, "Beschreibung1", 0.05, 0.1));
    }

    @Test
    public void testToString() {
        Assertions.assertEquals(payment1.toString(), "Date: 24.11.2023 Amount: -220.0 Description: Beschreibung1 incomingInterest: 0.05 outgoingInterest: 0.1");
    }

}
