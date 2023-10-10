package space.gavinklfong.demo.finance.topology;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import space.gavinklfong.demo.finance.model.AccountBalance;
import space.gavinklfong.demo.finance.model.Transaction;
import space.gavinklfong.demo.finance.model.TransactionKey;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionSerdes {

    public static JsonSerde<Transaction> transaction() {
        JsonSerde<Transaction> serde = new JsonSerde<>(Transaction.class);
        serde.configure(getSerDeConfig(), false);
        return serde;
    }

    public static JsonSerde<TransactionKey> transactionKey() {
        JsonSerde<TransactionKey> serde = new JsonSerde<>(TransactionKey.class);
        serde.configure(getSerDeConfig(), true);
        return serde;
    }

    public static JsonSerde<AccountBalance> accountBalance() {
        JsonSerde<AccountBalance> serde = new JsonSerde<>(AccountBalance.class);
        serde.configure(getSerDeConfig(), false);
        return serde;
    }

    public static JsonSerde<String> accountBalanceKey() {
        JsonSerde<String> serde = new JsonSerde<>(String.class);
        serde.configure(getSerDeConfig(), false);
        return serde;
    }

    private static Map<String, String> getSerDeConfig() {
        return Map.of(JsonDeserializer.TRUSTED_PACKAGES, "space.gavinklfong.demo.finance.model");
    }
}
