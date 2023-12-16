package space.gavinklfong.demo.finance.topology;

import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Serde;

import java.util.Map;

@RequiredArgsConstructor
public class SerdeFactory {
    private final String getSchemaRegistryUrl;
    private final SchemaRegistryClient schemaRegistryClient;

    public <T extends SpecificRecord> Serde<T> getSerde(boolean isKey) {
        Serde<T> serde = new SpecificAvroSerde<>(schemaRegistryClient);
        serde.configure(Map.of(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, getSchemaRegistryUrl), isKey);
        return serde;
    }
}
