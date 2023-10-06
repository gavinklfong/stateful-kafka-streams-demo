package space.gavinklfong.demo.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import space.gavinklfong.demo.ecommerce.dto.ShoppingOrder;
import space.gavinklfong.demo.ecommerce.service.ShoppingOrderService;

import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OrderRestController {

    private final ShoppingOrderService shoppingOrderService;

    @PostMapping("/orders")
    public ResponseEntity<ShoppingOrder> generateShoppingOrder() throws ExecutionException, InterruptedException {
        ShoppingOrder payload = shoppingOrderService.sendShoppingOrder();
        return ResponseEntity.ok().body(payload);
    }


}
