package space.gavinklfong.demo.ecommerce.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ShoppingOrderKey {
    String customerId;
    String orderId;
}
