package space.gavinklfong.finance.service;

import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import space.gavinklfong.demo.finance.schema.LoanRequest;
import space.gavinklfong.demo.finance.schema.LoanRequestKey;

@Component
public class LoanSender {

    private final String topic;
    private final KafkaProducer<LoanRequestKey, LoanRequest> kafkaProducer;

    public LoanSender(@Value("${loan.input.topic}") String topic, KafkaProducer<LoanRequestKey, LoanRequest> kafkaProducer) {
        this.topic = topic;
        this.kafkaProducer = kafkaProducer;
    }

    @SneakyThrows
    public void send(LoanRequest request) {
        ProducerRecord<LoanRequestKey, LoanRequest> producerRecord = new ProducerRecord<>(topic, buildKey(request), request);
        kafkaProducer.send(producerRecord).get();
    }

    private LoanRequestKey buildKey(LoanRequest loanRequest) {
        return LoanRequestKey.newBuilder()
                .setAccount(loanRequest.getAccount())
                .build();
    }
}
