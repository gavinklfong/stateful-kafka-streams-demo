package space.gavinklfong.demo.ecommerce.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import space.gavinklfong.demo.ecommerce.dto.ShoppingOrder;
import space.gavinklfong.demo.ecommerce.dto.ShoppingOrderKey;
import space.gavinklfong.demo.ecommerce.service.AvroShoppingOrderService;
import space.gavinklfong.demo.ecommerce.service.JsonShoppingOrderService;

@Configuration
@EnableScheduling
public class AppConfig {

    @Bean
    public JsonShoppingOrderService jsonShoppingOrderProducer(@Value("${shopping-order-producer.topic-json}") String topic,
                                                          KafkaProducer<ShoppingOrderKey, ShoppingOrder> kafkaProducer) {
        return new JsonShoppingOrderService(topic, kafkaProducer);
    }

    @Bean
    public AvroShoppingOrderService avroShoppingOrderProducer(@Value("${shopping-order-producer.topic-avro}") String topic,
                                                          KafkaProducer<space.gavinklfong.demo.ecommerce.schema.ShoppingOrderKey,
                                                                  space.gavinklfong.demo.ecommerce.schema.ShoppingOrder> kafkaProducer) {
        return new AvroShoppingOrderService(topic, kafkaProducer);
    }
}
