package cl.estela.api.service;

import cl.estela.api.domain.*;
import cl.estela.api.dto.OrderItemRequest;
import cl.estela.api.repo.AppUserRepository;
import cl.estela.api.repo.OrderRepository;
import cl.estela.api.repo.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class OrderService {

    private final AppUserRepository users;
    private final ProductRepository products;
    private final OrderRepository orders;

    public OrderService(AppUserRepository users, ProductRepository products, OrderRepository orders) {
        this.users = users;
        this.products = products;
        this.orders = orders;
    }

    @Transactional
    public Order create(String email, List<OrderItemRequest> requestedItems) {
        AppUser user = users.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no valido"));

        if (requestedItems == null || requestedItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El pedido debe tener al menos un producto");
        }

        Order order = new Order(user);
        for (OrderItemRequest requested : requestedItems) {
            Product product = products.findById(requested.productId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
            if (!product.isActive()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Producto no disponible: " + product.getName());
            }
            if (requested.quantity() > product.getStock()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Stock insuficiente para " + product.getName());
            }
            product.setStock(product.getStock() - requested.quantity());
            order.addItem(new OrderItem(product, requested.quantity(), product.getPrice()));
        }
        return orders.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> listMine(String email) {
        return orders.findByCustomer(email);
    }

    @Transactional
    public Order cancel(String email, Long orderId) {
        Order order = ownedOrder(email, orderId);
        if (order.getStatus() != OrderStatus.PAID) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Solo un pedido pagado puede cancelarse");
        }
        restock(order);
        order.setStatus(OrderStatus.CANCELLED);
        return order;
    }

    @Transactional
    public Order requestReturn(String email, Long orderId) {
        Order order = ownedOrder(email, orderId);
        if (order.getStatus() != OrderStatus.PAID) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Solo un pedido pagado puede devolverse");
        }
        restock(order);
        order.setStatus(OrderStatus.RETURNED);
        return order;
    }

    private Order ownedOrder(String email, Long orderId) {
        Order order = orders.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
        if (!order.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El pedido pertenece a otro cliente");
        }
        return order;
    }

    private void restock(Order order) {
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
        }
    }
}
