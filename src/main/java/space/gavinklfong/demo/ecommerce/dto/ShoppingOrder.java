package space.gavinklfong.demo.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Value
@Builder
@RequiredArgsConstructor
public class ShoppingOrder {
    String id;
    String customerId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate deliveryDate;
    BigDecimal totalAmount;
    Map<String,Integer> products;
    OrderStatus status;
}
