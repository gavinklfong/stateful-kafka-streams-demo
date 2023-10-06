package space.gavinklfong.demo.ecommerce.util;

import org.apache.commons.lang3.RandomUtils;
import space.gavinklfong.demo.ecommerce.dto.OrderStatus;
import space.gavinklfong.demo.ecommerce.dto.ShoppingOrder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ShoppingOrderGenerator {
    private static final int AMOUNT_MIN = 100;
    private static final int AMOUNT_MAX = 10000;
    private static final int QUANTITY_MIN = 1;
    private static final int QUANTITY_MAX = 5;
    private static final int PRODUCT_MAX = 5;
    private static final LocalDate DATE_MIN = LocalDate.parse("2022-01-01");
    private static final LocalDate DATE_MAX = LocalDate.parse("2022-12-31");

    private ShoppingOrderGenerator() { }

    public static ShoppingOrder generateRandomShoppingOrder() {
        return ShoppingOrder.builder()
                .id(UUID.randomUUID().toString())
                .customerId(UUID.randomUUID().toString())
                .totalAmount(BigDecimal.valueOf(RandomUtils.nextInt(AMOUNT_MIN, AMOUNT_MAX)))
                .products(generateProducts())
                .status(generateOrderStatus())
                .deliveryDate(generateDeliveryDate())
                .build();
    }

    private static Map<String, Integer> generateProducts() {
        int productCount = RandomUtils.nextInt(1, PRODUCT_MAX + 1);
        Map<String,Integer> products = new HashMap<>();
        for (int i = 0; i < productCount; i++) {
            products.put(UUID.randomUUID().toString(), RandomUtils.nextInt(QUANTITY_MIN, QUANTITY_MAX));
        }
        return products;
    }

    private static OrderStatus generateOrderStatus() {
        return OrderStatus.values()[RandomUtils.nextInt(0, OrderStatus.values().length)];
    }

    private static LocalDate generateDeliveryDate() {
        return betweenDate(DATE_MIN, DATE_MAX);
    }

    private static LocalDate betweenDate(LocalDate startInclusive, LocalDate endExclusive) {
        long startEpochDay = startInclusive.toEpochDay();
        long endEpochDay = endExclusive.toEpochDay();
        long randomDay = ThreadLocalRandom
                .current()
                .nextLong(startEpochDay, endEpochDay);

        return LocalDate.ofEpochDay(randomDay);
    }
}
