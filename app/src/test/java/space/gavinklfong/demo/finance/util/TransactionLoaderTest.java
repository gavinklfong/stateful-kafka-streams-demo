package space.gavinklfong.demo.finance.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import space.gavinklfong.demo.finance.model.Transaction;

import java.util.List;

@Slf4j
class TransactionLoaderTest {

    @Test
    void loadCSV() {
        List<Transaction> transactions = TransactionLoader.loadTransactions();
        transactions.forEach(t -> log.info("{}", t));
    }
}
