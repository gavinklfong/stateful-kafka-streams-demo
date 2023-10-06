package space.gavinklfong.demo.ecommerce.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;
import space.gavinklfong.demo.ecommerce.dto.ShoppingOrder;
import space.gavinklfong.demo.ecommerce.dto.ShoppingOrderKey;

import java.util.Properties;

@EnableKafka
@Configuration
public class KafkaConfig {
    @Bean
    public RecordMessageConverter recordMessageConverter() {
        return new ByteArrayJsonMessageConverter();
    }

    @Bean
    public KafkaProducer<ShoppingOrderKey, ShoppingOrder> shoppingOrderKafkaProducer(@Value("${spring.kafka.bootstrap-servers}") String kafkaServer) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new KafkaProducer<>(props);
    }
}
