package space.gavinklfong.demo.finance.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import space.gavinklfong.demo.finance.model.Transaction;
import space.gavinklfong.demo.finance.service.TransactionService;
import space.gavinklfong.demo.finance.util.TransactionLoader;

import java.util.Iterator;

@Slf4j
@RequiredArgsConstructor
@Component
public class TransactionProducerScheduler {
    private static final String CSV_FILE = "/transactions.csv";
    private static final Iterator<Transaction> TRANSACTIONS = TransactionLoader.loadTransactions(CSV_FILE).iterator();

    private final TransactionService transactionService;

    @Scheduled(fixedRateString = "${transaction-producer.fixed-rate}")
    public void scheduleFixedRateTask() {
        if (TRANSACTIONS.hasNext()) {
            Transaction transaction = TRANSACTIONS.next();
            transactionService.sendShoppingOrder(transaction);
            log.info("Transaction sent: {}", transaction);
        }
    }
}
