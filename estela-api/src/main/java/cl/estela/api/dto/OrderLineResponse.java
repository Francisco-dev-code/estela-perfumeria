package cl.estela.api.dto;

import cl.estela.api.domain.OrderItem;
import java.math.BigDecimal;

public record OrderLineResponse(String product, int quantity, BigDecimal unitPrice, BigDecimal subtotal) {
    public static OrderLineResponse from(OrderItem item) {
        return new OrderLineResponse(item.getProduct().getName(), item.getQuantity(),
                item.getUnitPrice(), item.subtotal());
    }
}
