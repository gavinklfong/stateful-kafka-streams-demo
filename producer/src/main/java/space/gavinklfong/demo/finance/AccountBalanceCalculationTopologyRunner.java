package space.gavinklfong.demo.finance;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import space.gavinklfong.demo.finance.topology.AccountBalanceCalculationTopology;

import java.util.Properties;

@Slf4j
public class AccountBalanceCalculationTopologyRunner {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "account-balance-calculation");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        Topology topology = AccountBalanceCalculationTopology.build();
        log.info("Topology: {}", topology.describe());

        KafkaStreams kafkaStreams = new KafkaStreams(topology, props);
        kafkaStreams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }
}
