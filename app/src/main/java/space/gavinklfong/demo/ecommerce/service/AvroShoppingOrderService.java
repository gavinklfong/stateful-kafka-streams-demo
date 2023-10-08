
package space.gavinklfong.demo.ecommerce.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import space.gavinklfong.demo.ecommerce.schema.ShoppingOrder;
import space.gavinklfong.demo.ecommerce.schema.ShoppingOrderKey;

@Slf4j
@RequiredArgsConstructor
public class AvroShoppingOrderService {

    private final String topic;

    private final KafkaProducer<ShoppingOrderKey, ShoppingOrder> shoppingOrderKafkaProducer;

    @SneakyThrows
    public void sendShoppingOrder(ShoppingOrder shoppingOrder) {
        ProducerRecord<ShoppingOrderKey, ShoppingOrder> producerRecord = new ProducerRecord<>(topic, buildKey(shoppingOrder), shoppingOrder);
        shoppingOrderKafkaProducer.send(producerRecord).get();
    }

    private ShoppingOrderKey buildKey(ShoppingOrder shoppingOrder) {
        return ShoppingOrderKey.newBuilder()
                .setCustomerId(shoppingOrder.getCustomerId())
                .setOrderId(shoppingOrder.getId())
                .build();
    }
}
