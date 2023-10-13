package space.gavinklfong.demo.finance.topology;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;
import space.gavinklfong.demo.finance.model.TransactionType;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionFilterTopology {

    public static Topology build() {
        StreamsBuilder builder = new StreamsBuilder();
        builder.stream("transactions",
                        Consumed.with(TransactionSerdes.transactionKey(), TransactionSerdes.transaction()))
                .peek((key, value) -> log.info("input - key: {}, value: {}", key, value), Named.as("log-input"))
                .filter((key, value) -> value.getType().equals(TransactionType.TRANSFER), Named.as("transaction-filter"))
                .peek((key, value) -> log.info("output - key: {}, value: {}", key, value), Named.as("log-output"))
                .to("transfer-transactions",
                        Produced.with(TransactionSerdes.transactionKey(), TransactionSerdes.transaction()));

        return builder.build();
    }}
