package space.gavinklfong.demo.finance.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import space.gavinklfong.demo.finance.model.Transaction;
import space.gavinklfong.demo.finance.model.TransactionKey;
import space.gavinklfong.demo.finance.service.TransactionService;

@Configuration
@EnableScheduling
public class AppConfig {

    @Bean
    public TransactionService shoppingOrderProducer(@Value("${shopping-order-producer.topic-json}") String topic,
                                                        KafkaProducer<TransactionKey, Transaction> kafkaProducer) {
        return new TransactionService(topic, kafkaProducer);
    }

}
