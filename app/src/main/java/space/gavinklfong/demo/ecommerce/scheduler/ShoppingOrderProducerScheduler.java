package space.gavinklfong.demo.ecommerce.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import space.gavinklfong.demo.ecommerce.dto.ShoppingOrder;
import space.gavinklfong.demo.ecommerce.service.AvroShoppingOrderService;
import space.gavinklfong.demo.ecommerce.service.JsonShoppingOrderService;
import space.gavinklfong.demo.ecommerce.util.AvroMapper;
import space.gavinklfong.demo.ecommerce.util.ShoppingOrderGenerator;

@Slf4j
@RequiredArgsConstructor
@Component
public class ShoppingOrderProducerScheduler {

    private final AvroShoppingOrderService avroShoppingOrderService;
    private final JsonShoppingOrderService jsonShoppingOrderService;

    @Scheduled(fixedRateString = "${shopping-order-producer.fixed-rate}")
    public void scheduleFixedRateTask() {
        ShoppingOrder shoppingOrder = ShoppingOrderGenerator.generateRandomShoppingOrder();
        jsonShoppingOrderService.sendShoppingOrder(shoppingOrder);
//        avroShoppingOrderService.sendShoppingOrder(AvroMapper.mapToAvro(shoppingOrder));
        log.info("Shopping order sent: {}", shoppingOrder);
    }
}
