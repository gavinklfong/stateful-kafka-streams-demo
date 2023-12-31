package space.gavinklfong.demo.finance.topology;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import space.gavinklfong.demo.finance.model.Transaction;
import space.gavinklfong.demo.finance.model.TransactionKey;
import space.gavinklfong.demo.finance.schema.*;

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

    public static SpecificAvroSerde<LoanRequest> loanRequest() {
        SpecificAvroSerde<LoanRequest> serde = new SpecificAvroSerde<>();
        serde.configure(getAvroSerdeConfig(), false);
        return serde;
    }

    public static SpecificAvroSerde<LoanResponse> loanResponse() {
        SpecificAvroSerde<LoanResponse> serde = new SpecificAvroSerde<>();
        serde.configure(getAvroSerdeConfig(), false);
        return serde;
    }

    public static SpecificAvroSerde<AccountBalance> accountBalance() {
        SpecificAvroSerde<AccountBalance> serde = new SpecificAvroSerde<>();
        serde.configure(getAvroSerdeConfig(), false);
        return serde;
    }

    public static SpecificAvroSerde<Account> accountKey() {
        SpecificAvroSerde<Account> serde = new SpecificAvroSerde<>();
        serde.configure(getAvroSerdeConfig(), true);
        return serde;
    }

    public static <T extends SpecificRecord> Serde<T> getSerde(boolean isKey, Map<String, String> config) {
        Serde<T> serde = new SpecificAvroSerde<>();
        serde.configure(config, isKey);
        return serde;
    }

    private static Map<String, String> getAvroSerdeConfig() {
        return Map.of(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
    }
}
