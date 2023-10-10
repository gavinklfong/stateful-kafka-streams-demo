
package space.gavinklfong.demo.finance.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import space.gavinklfong.demo.finance.model.Transaction;
import space.gavinklfong.demo.finance.model.TransactionKey;

@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    private final String topic;

    private final KafkaProducer<TransactionKey, Transaction> transactionKafkaProducer;

    @SneakyThrows
    public void sendShoppingOrder(Transaction transaction) {
        ProducerRecord<TransactionKey, Transaction> producerRecord = new ProducerRecord<>(topic, buildKey(transaction), transaction);
        transactionKafkaProducer.send(producerRecord).get();
    }

    private TransactionKey buildKey(Transaction transaction) {
        return TransactionKey.builder()
                .fromAccount(transaction.getFromAccount())
                .toAccount(transaction.getToAccount())
                .build();
    }
}
