package space.gavinklfong.demo.finance.topology;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import space.gavinklfong.demo.finance.model.AccountBalance;
import space.gavinklfong.demo.finance.model.Transaction;
import space.gavinklfong.demo.finance.model.TransactionKey;
import space.gavinklfong.demo.finance.util.AccountBalanceLoader;
import space.gavinklfong.demo.finance.util.TransactionLoader;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class AccountBalanceCalculationTopologyTest {

    private static final String TRANSACTION_CSV = "/transactions-test.csv";
    private static final String ACCOUNT_BALANCE_CSV = "/account-balances-test.csv";

    private TopologyTestDriver testDriver;
    private TestInputTopic<TransactionKey, Transaction> inputTopic;
    private TestOutputTopic<String, AccountBalance> outputTopic;

    @BeforeEach
    void setup() {
        Topology topology = AccountBalanceCalculationTopology.build();

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "account-balance-calculation");

        testDriver = new TopologyTestDriver(topology, props);

        inputTopic = testDriver.createInputTopic("transactions",
                TransactionSerdes.transactionKey().serializer(),
                TransactionSerdes.transaction().serializer());
        outputTopic = testDriver.createOutputTopic("account-balances",
                TransactionSerdes.accountBalanceKey().deserializer(),
                TransactionSerdes.accountBalance().deserializer());
    }

    @AfterEach
    void tearDown() {
        testDriver.close();
    }

    @Test
    void doTest() {
        List<Transaction> transactions = TransactionLoader.loadTransactions(TRANSACTION_CSV);
        List<AccountBalance> accountBalances = AccountBalanceLoader.loadAccountBalances(ACCOUNT_BALANCE_CSV);

        transactions.forEach(t -> inputTopic.pipeInput(constructKey(t), t));

        List<KeyValue<String, AccountBalance>> expectedOutput = accountBalances.stream()
                .map(a -> KeyValue.pair(a.getAccount(), a))
                .toList();

        assertThat(outputTopic.readKeyValuesToList()).containsExactlyElementsOf(expectedOutput);
    }

    private TransactionKey constructKey(Transaction transaction) {
        return TransactionKey.builder()
                .id(UUID.randomUUID())
                .toAccount(transaction.getToAccount())
                .fromAccount(transaction.getFromAccount())
                .build();
    }

}
