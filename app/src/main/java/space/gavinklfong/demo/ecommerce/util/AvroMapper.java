package space.gavinklfong.demo.ecommerce.util;

import space.gavinklfong.demo.ecommerce.schema.OrderStatus;
import space.gavinklfong.demo.ecommerce.schema.ShoppingOrder;
import space.gavinklfong.demo.ecommerce.schema.ShoppingOrderKey;

public class AvroMapper {
    public static ShoppingOrder mapToAvro(space.gavinklfong.demo.ecommerce.dto.ShoppingOrder shoppingOrder) {
        return ShoppingOrder.newBuilder()
                .setCustomerId(shoppingOrder.getCustomerId())
                .setId(shoppingOrder.getId())
                .setDeliveryDate(shoppingOrder.getDeliveryDate())
                .setProducts(shoppingOrder.getProducts())
                .setStatus(mapToAvro(shoppingOrder.getStatus()))
                .setTotalAmount(shoppingOrder.getTotalAmount())
                .build();
    }

    public static ShoppingOrderKey mapToAvro(space.gavinklfong.demo.ecommerce.dto.ShoppingOrderKey shoppingOrderKey) {
        return ShoppingOrderKey.newBuilder()
                .setCustomerId(shoppingOrderKey.getCustomerId())
                .setOrderId(shoppingOrderKey.getOrderId())
                .build();
    }

    public static OrderStatus mapToAvro(space.gavinklfong.demo.ecommerce.dto.OrderStatus orderStatus) {
        return OrderStatus.valueOf(orderStatus.name());
    }
}
