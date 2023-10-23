package space.gavinklfong.finance.topology;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import space.gavinklfong.demo.finance.schema.AccountBalance;
import space.gavinklfong.demo.finance.schema.EvaluationResult;
import space.gavinklfong.demo.finance.schema.LoanRequest;
import space.gavinklfong.demo.finance.schema.LoanResponse;
import space.gavinklfong.demo.finance.topology.TransactionSerdes;

import java.math.BigDecimal;
import java.time.Instant;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.compare.ComparableUtils.is;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoanEvaluationTopology {

    public static Topology build() {

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, LoanRequest> loanRequests = builder.stream("loan-requests",
                Consumed.with(Serdes.String(), TransactionSerdes.loanRequest()))
                .peek((key, value) -> log.info("input - key: {}, value: {}", key, value), Named.as("log-input"));

        KTable<String, AccountBalance> accountBalanceTable = builder.stream("account-balances",
                        Consumed.with(Serdes.String(), TransactionSerdes.accountBalance()))
                        .toTable(Named.as("account-balances-table"));

        loanRequests.leftJoin(accountBalanceTable,
                (loanRequest, accountBalance) -> evaluate(loanRequest, accountBalance))
                .peek((key, value) -> log.info("output - key: {}, value: {}", key, value), Named.as("log-output"))
                .to("loan-evaluation-results",
                        Produced.with(Serdes.String(), TransactionSerdes.loanResponse()));

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
