package space.gavinklfong.demo.finance.topology;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;
import space.gavinklfong.demo.finance.model.AccountBalance;
import space.gavinklfong.demo.finance.model.Transaction;
import space.gavinklfong.demo.finance.model.TransactionKey;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Slf4j
public class AccountBalanceCalculator implements Processor<TransactionKey, Transaction, UUID, List<AccountBalance>> {
    private KeyValueStore<String, AccountBalance> accountBalanceStore;

    private ProcessorContext<UUID, List<AccountBalance>> processorContext;

    @Override
    public void init(ProcessorContext<UUID, List<AccountBalance>> processorContext) {
        this.accountBalanceStore = processorContext.getStateStore("account-balance-store");
        this.processorContext = processorContext;
    }

    @Override
    public void process(Record<TransactionKey, Transaction> record) {
        List<AccountBalance> accountBalances = calculateAccountBalance(record.value());
        accountBalances.forEach(accountBalance -> accountBalanceStore.put(accountBalance.getAccount(), accountBalance));

        log.info("AccountBalanceCalculator output = {}", accountBalances);
        processorContext.forward(record.withKey(record.key().getId()).withValue(accountBalances));
    }

    private List<AccountBalance> calculateAccountBalance(Transaction transaction) {
        return switch (transaction.getType()) {
            case DEPOSIT, INTEREST -> List.of(creditAccount(transaction.getToAccount(), transaction.getAmount(), transaction.getTimestamp()));
            case WITHDRAWAL, FEE -> List.of(debitAccount(transaction.getFromAccount(), transaction.getAmount(), transaction.getTimestamp()));
            case TRANSFER -> List.of(creditAccount(transaction.getToAccount(), transaction.getAmount(), transaction.getTimestamp()),
                    debitAccount(transaction.getFromAccount(), transaction.getAmount(), transaction.getTimestamp()));
        };
    }

    private AccountBalance creditAccount(String account, BigDecimal amount, LocalDateTime timestamp) {
        AccountBalance accountBalance = retrieveAccountBalance(account);
        return accountBalance.toBuilder()
                .amount(accountBalance.getAmount().add(amount))
                .timestamp(timestamp)
                .build();
    }

    private AccountBalance debitAccount(String account, BigDecimal amount, LocalDateTime timestamp) {
        AccountBalance accountBalance = retrieveAccountBalance(account);
        return accountBalance.toBuilder()
                .amount(accountBalance.getAmount().subtract(amount))
                .timestamp(timestamp)
                .build();
    }

    private AccountBalance retrieveAccountBalance(String account) {
        AccountBalance accountBalance = accountBalanceStore.get(account);
        return nonNull(accountBalance)? accountBalance :
                AccountBalance.builder()
                        .account(account)
                        .amount(BigDecimal.ZERO)
                        .build();
    }

}