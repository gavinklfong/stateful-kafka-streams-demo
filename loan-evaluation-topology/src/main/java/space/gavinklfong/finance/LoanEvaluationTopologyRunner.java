package space.gavinklfong.finance;

import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import space.gavinklfong.finance.topology.LoanEvaluationTopology;

import java.util.Properties;

@Slf4j
public class LoanEvaluationTopologyRunner {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "loan-evaluation-topology");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericAvroSerde.class);
        props.put("schema.registry.url", "http://localhost:8081");

        Topology topology = LoanEvaluationTopology.build();
        log.info("Topology: {}", topology.describe());

        KafkaStreams kafkaStreams = new KafkaStreams(topology, props);
        kafkaStreams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }

}