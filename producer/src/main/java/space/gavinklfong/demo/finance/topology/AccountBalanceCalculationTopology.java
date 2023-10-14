package space.gavinklfong.demo.finance.topology;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import space.gavinklfong.demo.finance.model.AccountBalance;
import space.gavinklfong.demo.finance.model.Transaction;
import space.gavinklfong.demo.finance.model.TransactionKey;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountBalanceCalculationTopology {

    private static final String STATE_STORE = "account-balance-store";

    public static Topology build() {

        StreamsBuilder builder = new StreamsBuilder();

        StoreBuilder<KeyValueStore<String, AccountBalance>> accountBalanceStoreBuilder = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore(STATE_STORE),
                Serdes.String(),
                TransactionSerdes.accountBalance()
        );
        builder.addStateStore(accountBalanceStoreBuilder);

        KStream<TransactionKey, Transaction> source = builder.stream("transactions",
                        Consumed.with(TransactionSerdes.transactionKey(), TransactionSerdes.transaction()))
                        .peek((key, value) -> log.info("input - key: {}, value: {}", key, value), Named.as("log-input"));

        source.process(AccountBalanceCalculator::new, Named.as("account-balance-calculator"), STATE_STORE)
                .flatMapValues(v -> v)
                .selectKey((key, value) -> value.getAccount())
                .peek((key, value) -> log.info("output - key: {}, value: {}", key, value), Named.as("log-output"))
                .to("account-balances",
                        Produced.with(TransactionSerdes.accountBalanceKey(), TransactionSerdes.accountBalance()));

        return builder.build();
    }
}
