package space.gavinklfong.demo.insurance.controllers;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import space.gavinklfong.demo.insurance.InsuranceApplication;
import space.gavinklfong.demo.insurance.dto.ClaimRequest;
import space.gavinklfong.demo.insurance.dto.Priority;
import space.gavinklfong.demo.insurance.dto.Product;
import space.gavinklfong.demo.insurance.model.ClaimReviewResult;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
public class ClaimsRestController {

    private final KafkaTemplate<String, ClaimRequest> kafkaTemplate;
    private final KafkaProducer<String, ClaimRequest> kafkaProducer;
    private final KafkaConsumer<String, ClaimReviewResult> kafkaConsumer;

    public ClaimsRestController(KafkaTemplate<String, ClaimRequest> kafkaTemplate,
                                @Value("${spring.kafka.bootstrap-servers}") String kafkaServer) {
        this.kafkaTemplate = kafkaTemplate;
        kafkaProducer = createKafkaProducer(kafkaServer);
        kafkaConsumer = createKafkaConsumer(kafkaServer);
        kafkaConsumer.subscribe(Collections.singletonList("claim-updated"));
    }

    @PostMapping("/claim-2")
    public ResponseEntity<ClaimRequest> generateClaimRequest2() throws ExecutionException, InterruptedException {
        ClaimRequest request = generateClaimRequest();
        kafkaTemplate.send("claim-submitted", request.getCustomerId(), request).get();
        return ResponseEntity.ok().body(request);
    }

    @PostMapping("/claim-1")
    public ResponseEntity<ClaimRequest> generateClaimRequest1() throws ExecutionException, InterruptedException {
        ClaimRequest request = generateClaimRequest();
        ProducerRecord<String, ClaimRequest> producerRecord = new ProducerRecord<>("claim-submitted", request.getCustomerId(), request);
        kafkaProducer.send(producerRecord).get();
        return ResponseEntity.ok().body(request);
    }

    @GetMapping("/claim-results")
    public ResponseEntity<List<ClaimReviewResult>> getClaimResults() {

        List<ClaimReviewResult> results = new ArrayList<>();

        ConsumerRecords<String, ClaimReviewResult> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(3));
        consumerRecords.forEach(consumerRecord -> {
            results.add(consumerRecord.value());
            log.info("received message: {}", consumerRecord);
        });

        return ResponseEntity.ok().body(results);
    }


    private KafkaProducer<String, ClaimRequest> createKafkaProducer(String kafkaServer) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new KafkaProducer<>(props);
    }

    private KafkaConsumer<String, ClaimReviewResult> createKafkaConsumer(String kafkaServer) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-test-consumer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, InsuranceApplication.class.getPackage().getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ClaimReviewResult.class);
        return new KafkaConsumer<>(props);
    }

    private ClaimRequest generateClaimRequest() {
        return ClaimRequest.builder()
                .id(UUID.randomUUID().toString())
                .customerId(UUID.randomUUID().toString())
                .product(Product.MEDICAL)
                .claimAmount(RandomUtils.nextDouble(200, 7000))
                .priority(Priority.HIGH)
                .build();
    }
}
