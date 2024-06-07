package bank;

import bank.exceptions.TransactionAttributeException;
import com.google.gson.*;

import java.lang.reflect.Type;

public class CustomSerializerDeserializer
        implements JsonSerializer<Transaction>, JsonDeserializer<Transaction> {

    /**
     * Diese Methode serialisiert ein Transaktionsobjekt in ein json Element
     * @param transactionObject Das zu serialisierende Transaktionsobjekt
     * @param type Typ des Quellobjekts (wird hier nicht verwendet)
     * @param context Kontext für Json Serialisierung (wird hier nicht verwendet)
     * @return Ein JsonElement, das das serialisierte Json Objekt repräsentiert
     */
    @Override
    public JsonElement serialize(Transaction transactionObject, Type type, JsonSerializationContext context) {
        // Erstellen von zwei Json Objekten, eines für den äußeren Rahmen und eines für den inneren Inhalt
        JsonObject inner = new JsonObject();
        JsonObject outer = new JsonObject();

        // Überprüfen des Typs der Transaktion und entsprechendes Setzen des Klassennamens im äußeren Rahmen
        if (transactionObject instanceof Payment) {
            outer.addProperty("CLASSNAME", "Payment");
            // Hinzufügen von Eigenschaften spezifisch für die Payment-Klasse im inneren Objekt
            inner.addProperty("incomingInterest", ((Payment) transactionObject).getIncomingInterest());
            inner.addProperty("outgoingInterest", ((Payment) transactionObject).getOutgoingInterest());
        } else if (transactionObject instanceof IncomingTransfer) {
            outer.addProperty("CLASSNAME", "IncomingTransfer");
            // Hinzufügen von Eigenschaften spezifisch für die IncomingTransfer-Klasse im inneren Objekt
            inner.addProperty("sender", ((IncomingTransfer) transactionObject).getSender());
            inner.addProperty("recipient", ((IncomingTransfer) transactionObject).getRecipient());
        } else if (transactionObject instanceof OutgoingTransfer) {
            outer.addProperty("CLASSNAME", "OutgoingTransfer");
            // Hinzufügen von Eigenschaften spezifisch für die OutgoingTransfer-Klasse im inneren Objekt
            inner.addProperty("sender", ((OutgoingTransfer) transactionObject).getSender());
            inner.addProperty("recipient", ((OutgoingTransfer) transactionObject).getRecipient());
        }

        // Gemeinsame Eigenschaften für alle Transaktionstypen im inneren Objekt
        inner.addProperty("date", transactionObject.getDate());
        inner.addProperty("amount", transactionObject.getAmount());
        inner.addProperty("description", transactionObject.getDescription());

        // Hinzufügen des inneren Objekts zum äußeren Objekt
        outer.add("INSTANCE", inner);

        // Das fertige, äußere JsonElement zurückgeben
        return outer;
    }

    /**
     * Diese Methode deserialisiert ein Json Element in ein Transaktionsobjekt
     * @param json Das zu deserialisierende Json Element
     * @param type Der Typ des Zielobjekts (wird hier nicht verwendet)
     * @param context Kontext für Json Deserialisierung (wird hier nicht verwendet)
     * @return Ein Transaktionsobjekt, das aus dem Json Element wiederhergestellt wurde
     */
    @Override
    public Transaction deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
        // Das übergebene Json Element wird in ein Json Objekt umgewandelt
        // JsonElement ist die Basisklasse, JsonObject stellt weitere Methoden zur Verfügung, wie z.B.
        // über get auf den Schlüssel zuzugreifen
        JsonObject obj = (JsonObject) json;

        // Der innere Teil des Json Objects wird extrahiert
        JsonObject inner = (JsonObject) obj.get("INSTANCE");

        // Klassenname aus dem äußeren Teil extrahieren und Anführungszeichen entfernen, Backslash dient nur der Erkennung
        String className = obj.get("CLASSNAME").getAsString();

        // Gemeinsame Eigenschaften werden aus dem inneren Teil extrahiert
        String date = inner.get("date").getAsString();
        double amount = inner.get("amount").getAsDouble();
        String description = inner.get("description").getAsString();

        try {
            // Je nach Klassennamen wird eine entsprechende Transaktion erstellt und zurückgegeben.
            if (className.equals("Payment")) {
                // Holen von Eigenschaften spezifisch für die Payment-Klasse
                double incomingInterest = inner.get("incomingInterest").getAsDouble();
                double outgoingInterest = inner.get("outgoingInterest").getAsDouble();

                // Payment Objekt erstellen und direkt zurückgeben
                return new Payment(date, amount, description, incomingInterest, outgoingInterest);
            } else if (className.equals("OutgoingTransfer")) {
                // Holen von Eigenschaften spezifisch für die Transfer-Klasse
                String sender = inner.get("sender").getAsString();
                String recipient = inner.get("recipient").getAsString();

                // OutgoingTransfer Objekt erstellen und direkt zurückgeben
                return new OutgoingTransfer(date, amount, description, sender, recipient);
            } else {
                // Holen von Eigenschaften spezifisch für die Transfer-Klasse
                String sender = inner.get("sender").getAsString();
                String recipient = inner.get("recipient").getAsString();

                // IncomingTransfer Objekt erstellen und direkt zurückgeben
                return new IncomingTransfer(date, amount, description, sender, recipient);
            }
        } catch (TransactionAttributeException e) {
            e.getStackTrace();
        }
        return null;
    }
}
