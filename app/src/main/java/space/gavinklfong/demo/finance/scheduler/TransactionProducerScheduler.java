package space.gavinklfong.demo.finance.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import space.gavinklfong.demo.finance.model.Transaction;
import space.gavinklfong.demo.finance.service.TransactionService;
import space.gavinklfong.demo.finance.util.TransactionGenerator;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class TransactionProducerScheduler {

    private final TransactionService transactionService;

    @Scheduled(fixedRateString = "${shopping-order-producer.fixed-rate}")
    public void scheduleFixedRateTask() {

        List<String> accounts = List.of("001-00001", "001-00101", "001-00201", "001-00301");

        Transaction transaction = TransactionGenerator.generateRandomTransaction();
        transactionService.sendShoppingOrder(transaction);
//        avroShoppingOrderService.sendShoppingOrder(AvroMapper.mapToAvro(shoppingOrder));
        log.info("Shopping order sent: {}", transaction);
    }
}
