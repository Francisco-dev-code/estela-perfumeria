package cl.estela.api.web;

import cl.estela.api.dto.OrderResponse;
import cl.estela.api.repo.OrderRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final OrderRepository orders;

    public AdminOrderController(OrderRepository orders) {
        this.orders = orders;
    }

    @GetMapping
    public List<OrderResponse> all() {
        return orders.findAllWithItems().stream().map(OrderResponse::from).toList();
    }
}
