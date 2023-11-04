package space.gavinklfong.finance.service;

import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import space.gavinklfong.demo.finance.schema.Account;
import space.gavinklfong.demo.finance.schema.LoanResponse;

@Component
public class LoanResultSender {

    private final String topic;
    private final KafkaProducer<Account, LoanResponse> kafkaProducer;

    public LoanResultSender(@Value("${loan.output.topic}") String topic, KafkaProducer<Account, LoanResponse> kafkaProducer) {
        this.topic = topic;
        this.kafkaProducer = kafkaProducer;
    }

    @SneakyThrows
    public void send(LoanResponse response) {
        ProducerRecord<Account, LoanResponse> producerRecord = new ProducerRecord<>(topic, buildKey(response), response);
        kafkaProducer.send(producerRecord).get();
    }

    private Account buildKey(LoanResponse loanResponse) {
        return Account.newBuilder()
                .setAccount(loanResponse.getAccount())
                .build();
    }
}
