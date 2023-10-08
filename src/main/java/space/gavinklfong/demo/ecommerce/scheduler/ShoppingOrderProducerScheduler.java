package space.gavinklfong.demo.ecommerce.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import space.gavinklfong.demo.ecommerce.dto.ShoppingOrder;
import space.gavinklfong.demo.ecommerce.service.ShoppingOrderService;

import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@Component
public class ShoppingOrderProducerScheduler {

    private final ShoppingOrderService shoppingOrderService;

    @Scheduled(fixedRate = 500)
    public void scheduleFixedRateTask() throws ExecutionException, InterruptedException {
        ShoppingOrder shoppingOrder = shoppingOrderService.sendShoppingOrder();
        log.info("Shopping order sent: {}", shoppingOrder);
    }
}
