package space.gavinklfong.demo.insurance.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import space.gavinklfong.demo.insurance.config.KafkaConfig;
import space.gavinklfong.demo.insurance.dto.ClaimRequest;
import space.gavinklfong.demo.insurance.dto.Priority;
import space.gavinklfong.demo.insurance.dto.Product;
import space.gavinklfong.demo.insurance.model.ClaimReviewResult;
import space.gavinklfong.demo.insurance.model.Status;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Slf4j
public class PocTest {

    @Test
    void sendNewClaimMessage() throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        KafkaProducer<Object, Object> kafkaProducer = new KafkaProducer<>(props);

        ClaimRequest request = ClaimRequest.builder()
                .id(UUID.randomUUID().toString())
                .customerId(UUID.randomUUID().toString())
                .product(Product.MEDICAL)
                .claimAmount(100d)
                .priority(Priority.HIGH)
                .build();

        ProducerRecord<Object, Object> producerRecord =
                new ProducerRecord<>("claim-submitted", request);

        kafkaProducer.send(producerRecord).get();
    }

    @Test
    void sendClaimUpdateMessage() throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        KafkaProducer<Object, Object> kafkaProducer = new KafkaProducer<>(props);

        ClaimReviewResult result = ClaimReviewResult.builder()
                .customerId(UUID.randomUUID().toString())
                .claimId(UUID.randomUUID().toString())
                .status(Status.APPROVED)
                .product(Product.MEDICAL)
                .claimAmount(150D)
                .remarks("ABCD")
                .build();

        ProducerRecord<Object, Object> producerRecord =
                new ProducerRecord<>("claim-updated", result);

        kafkaProducer.send(producerRecord).get();
    }

    @Test
    void receiveMessage() {

        ObjectMapper objectMapper = new ObjectMapper();

        Properties props = new Properties();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-test-update-consumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("claim-updated"));


        while (true) {
            ConsumerRecords<Object, Object> consumerRecords = consumer.poll(Duration.ofSeconds(5));
            if (consumerRecords.count()==0) {
                continue;
            }

            consumerRecords.forEach(record -> {
//                try {
                try {
                    log.info("key: {}, value: {}, partition: {}, offset: {}",
                             record.key(), objectMapper.readValue((String)record.value(), ClaimReviewResult.class), record.partition(), record.offset());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
            });

            consumer.commitAsync();
            break;
        }
    }
}
