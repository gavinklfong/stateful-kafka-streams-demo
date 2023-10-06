
package space.gavinklfong.demo.ecommerce.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import space.gavinklfong.demo.ecommerce.dto.ShoppingOrder;
import space.gavinklfong.demo.ecommerce.dto.ShoppingOrderKey;
import space.gavinklfong.demo.ecommerce.util.ShoppingOrderGenerator;

import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class ShoppingOrderService {

    private static final String TOPIC = "shopping-orders";

    private final KafkaProducer<ShoppingOrderKey, ShoppingOrder> shoppingOrderKafkaProducer;

    public ShoppingOrder sendShoppingOrder() throws ExecutionException, InterruptedException {
        ShoppingOrder payload = ShoppingOrderGenerator.generateRandomShoppingOrder();
        ShoppingOrderKey key = ShoppingOrderKey.builder()
                .orderId(payload.getId())
                .customerId(payload.getCustomerId())
                .build();
        ProducerRecord<ShoppingOrderKey, ShoppingOrder> producerRecord = new ProducerRecord<>(TOPIC, key, payload);
        shoppingOrderKafkaProducer.send(producerRecord).get();
        return payload;
    }
}
