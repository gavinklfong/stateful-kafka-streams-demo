package space.gavinklfong.demo.finance.topology;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import space.gavinklfong.demo.finance.model.Transaction;
import space.gavinklfong.demo.finance.model.TransactionKey;
import space.gavinklfong.demo.finance.schema.AccountBalance;

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

    private static Map<String, String> getSerDeConfig() {
        return Map.of(JsonDeserializer.TRUSTED_PACKAGES, "space.gavinklfong.demo.finance.model");
    }

    public static SpecificAvroSerde<AccountBalance> accountBalance() {
        SpecificAvroSerde<AccountBalance> serde = new SpecificAvroSerde<>();
        serde.configure(getAvroSerdeConfig(), false);
        return serde;
    }

    private static Map<String, String> getAvroSerdeConfig() {
        return Map.of(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
    }
}
