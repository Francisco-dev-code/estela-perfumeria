package cl.estela.api.dto;

import cl.estela.api.domain.Order;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(Long id, String customer, String status, BigDecimal total,
                            Instant createdAt, List<OrderLineResponse> items) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(order.getId(), order.getUser().getEmail(), order.getStatus().name(),
                order.getTotal(), order.getCreatedAt(),
                order.getItems().stream().map(OrderLineResponse::from).toList());
    }
}
