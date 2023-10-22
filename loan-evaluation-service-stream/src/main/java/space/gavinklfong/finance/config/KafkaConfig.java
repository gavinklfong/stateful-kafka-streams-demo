package space.gavinklfong.finance.config;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import space.gavinklfong.demo.finance.schema.LoanRequest;
import space.gavinklfong.demo.finance.schema.LoanResponse;

import java.util.Properties;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic loanRequestTopic(@Value("${loan.input.topic}") String inputTopic) {
        return TopicBuilder.name(inputTopic).build();
    }

    @Bean
    public NewTopic loanResultTopic(@Value("${loan.output.topic}") String outputTopic) {
        return TopicBuilder.name(outputTopic).build();
    }

    @Bean
    public KafkaProducer<String, LoanRequest> createLoanRequestProducer(@Value("${spring.kafka.bootstrap-servers}") String kafkaServer) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put("schema.registry.url", "http://localhost:8081");
        return new KafkaProducer<>(props);
    }

    @Bean
    public KafkaProducer<String, LoanResponse> createLoanEvaluationResponseProducer(@Value("${spring.kafka.bootstrap-servers}") String kafkaServer) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put("schema.registry.url", "http://localhost:8081");
        return new KafkaProducer<>(props);
    }

    @Bean
    public RecordMessageConverter recordMessageConverter() {
        return new ByteArrayJsonMessageConverter();
    }

}
