package space.gavinklfong.finance.service;

import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import space.gavinklfong.demo.finance.schema.LoanRequest;

@Component
public class LoanSender {

    private final String topic;
    private final KafkaProducer<String, LoanRequest> kafkaProducer;

    public LoanSender(@Value("${loan.input.topic}") String topic, KafkaProducer<String, LoanRequest> kafkaProducer) {
        this.topic = topic;
        this.kafkaProducer = kafkaProducer;
    }

    @SneakyThrows
    public void send(LoanRequest request) {
        ProducerRecord<String, LoanRequest> producerRecord = new ProducerRecord<>(topic, request.getAccount(), request);
        kafkaProducer.send(producerRecord).get();
    }
}
