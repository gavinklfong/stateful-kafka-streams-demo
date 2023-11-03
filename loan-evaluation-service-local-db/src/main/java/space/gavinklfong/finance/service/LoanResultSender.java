package space.gavinklfong.finance.service;

import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import space.gavinklfong.demo.finance.schema.LoanResponse;
import space.gavinklfong.demo.finance.schema.LoanResponseKey;

@Component
public class LoanResultSender {

    private final String topic;
    private final KafkaProducer<LoanResponseKey, LoanResponse> kafkaProducer;

    public LoanResultSender(@Value("${loan.output.topic}") String topic, KafkaProducer<LoanResponseKey, LoanResponse> kafkaProducer) {
        this.topic = topic;
        this.kafkaProducer = kafkaProducer;
    }

    @SneakyThrows
    public void send(LoanResponse response) {
        ProducerRecord<LoanResponseKey, LoanResponse> producerRecord = new ProducerRecord<>(topic, buildKey(response), response);
        kafkaProducer.send(producerRecord).get();
    }

    private LoanResponseKey buildKey(LoanResponse loanResponse) {
        return LoanResponseKey.newBuilder()
                .setAccount(loanResponse.getAccount())
                .build();
    }
}
