package space.gavinklfong.finance.service;

import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import space.gavinklfong.demo.finance.schema.LoanResponse;

@Component
public class LoanResultSender {

    private final String topic;
    private final KafkaProducer<String, LoanResponse> kafkaProducer;

    public LoanResultSender(@Value("${loan.output.topic}") String topic, KafkaProducer<String, LoanResponse> kafkaProducer) {
        this.topic = topic;
        this.kafkaProducer = kafkaProducer;
    }

    @SneakyThrows
    public void send(LoanResponse response) {
        ProducerRecord<String, LoanResponse> producerRecord = new ProducerRecord<>(topic, response.getAccount(), response);
        kafkaProducer.send(producerRecord).get();
    }
}
