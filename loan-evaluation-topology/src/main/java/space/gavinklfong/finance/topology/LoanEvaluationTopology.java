package space.gavinklfong.finance.topology;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import space.gavinklfong.demo.finance.schema.*;
import space.gavinklfong.demo.finance.topology.SerdeFactory;

import java.math.BigDecimal;
import java.time.Instant;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.compare.ComparableUtils.is;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoanEvaluationTopology {

    public static Topology build(SerdeFactory serdeFactory) {
        Serde<Account> accountKeySerde = serdeFactory.getSerde(true);
        Serde<AccountBalance> accountBalanceSerde = serdeFactory.getSerde(false);
        Serde<LoanRequest> loanRequestSerde = serdeFactory.getSerde(false);
        Serde<LoanResponse> loanResponseSerde = serdeFactory.getSerde(false);

        StreamsBuilder builder = new StreamsBuilder();

        KStream<Account, LoanRequest> loanRequests = builder.stream("loan-requests",
                Consumed.with(accountKeySerde, loanRequestSerde))
                .peek((key, value) -> log.info("input - key: {}, value: {}", key, value), Named.as("log-input"));

        KTable<Account, AccountBalance> accountBalanceTable = builder.stream("account-balances",
                        Consumed.with(accountKeySerde, accountBalanceSerde))
                        .toTable(Materialized.<Account, AccountBalance, KeyValueStore<Bytes, byte[]>>as("account-balances-table")
                                .withKeySerde(accountKeySerde)
                                .withValueSerde(accountBalanceSerde));

        loanRequests.leftJoin(accountBalanceTable, LoanEvaluationTopology::evaluate)
                .peek((key, value) -> log.info("output - key: {}, value: {}", key, value), Named.as("log-output"))
                .to("loan-evaluation-results",
                        Produced.with(accountKeySerde, loanResponseSerde));

        return builder.build();
    }

    private static LoanResponse evaluate(LoanRequest loanRequest, AccountBalance accountBalance) {
        EvaluationResult result = nonNull(accountBalance)?
                evaluate(loanRequest, accountBalance.getAmount()):
                EvaluationResult.REJECTED;

        return LoanResponse.newBuilder()
                .setRequestId(loanRequest.getRequestId())
                .setAccount(loanRequest.getAccount())
                .setAmount(loanRequest.getAmount())
                .setResult(result)
                .setTimestamp(Instant.now())
                .build();
    }

    private static EvaluationResult evaluate(LoanRequest loanRequest, BigDecimal balance) {
        if (is(balance)
                .greaterThanOrEqualTo(loanRequest.getAmount().multiply(BigDecimal.valueOf(3)))) {
            return EvaluationResult.APPROVED;
        } else if (is(balance)
                .greaterThanOrEqualTo(loanRequest.getAmount().multiply(BigDecimal.valueOf(2)))) {
            return EvaluationResult.REVIEW_NEEDED;
        } else {
            return EvaluationResult.REJECTED;
        }
    }
}
