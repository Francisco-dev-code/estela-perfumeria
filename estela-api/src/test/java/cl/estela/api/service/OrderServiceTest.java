package cl.estela.api.service;

import cl.estela.api.domain.*;
import cl.estela.api.dto.OrderItemRequest;
import cl.estela.api.repo.AppUserRepository;
import cl.estela.api.repo.OrderRepository;
import cl.estela.api.repo.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock AppUserRepository users;
    @Mock ProductRepository products;
    @Mock OrderRepository orders;

    OrderService service;
    AppUser cliente;
    Product perfume;

    @BeforeEach
    void setUp() {
        service = new OrderService(users, products, orders);
        cliente = new AppUser("cliente@estela.cl", "hash", "Cliente", Role.CLIENT);
        perfume = new Product("Ambar", "Estela", "Notas amaderadas", new BigDecimal("10000"), 10);
        lenient().when(users.findByEmail("cliente@estela.cl")).thenReturn(Optional.of(cliente));
    }

    @Test
    void createCalculaTotalYDescuentaStock() {
        when(products.findById(1L)).thenReturn(Optional.of(perfume));
        when(orders.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order order = service.create("cliente@estela.cl", List.of(new OrderItemRequest(1L, 2)));

        assertThat(order.getTotal()).isEqualByComparingTo("20000");
        assertThat(order.getItems()).hasSize(1);
        assertThat(perfume.getStock()).isEqualTo(8);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    void createRechazaStockInsuficienteYNoModificaInventario() {
        when(products.findById(1L)).thenReturn(Optional.of(perfume));

        assertThatThrownBy(() -> service.create("cliente@estela.cl", List.of(new OrderItemRequest(1L, 11))))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Stock insuficiente");
        assertThat(perfume.getStock()).isEqualTo(10);
    }

    @Test
    void createRechazaProductoInexistente() {
        when(products.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create("cliente@estela.cl", List.of(new OrderItemRequest(99L, 1))))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Producto no encontrado");
    }

    @Test
    void cancelReintegraStockYCambiaEstado() {
        Order order = new Order(cliente);
        order.addItem(new OrderItem(perfume, 3, perfume.getPrice()));
        perfume.setStock(7);
        when(orders.findById(5L)).thenReturn(Optional.of(order));

        Order cancelled = service.cancel("cliente@estela.cl", 5L);

        assertThat(cancelled.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        assertThat(perfume.getStock()).isEqualTo(10);
    }

    @Test
    void cancelRechazaPedidoDeOtroCliente() {
        AppUser otro = new AppUser("otro@estela.cl", "hash", "Otro", Role.CLIENT);
        Order order = new Order(otro);
        when(orders.findById(7L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> service.cancel("cliente@estela.cl", 7L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("otro cliente");
    }

    @Test
    void returnRechazaPedidoYaCancelado() {
        Order order = new Order(cliente);
        order.addItem(new OrderItem(perfume, 1, perfume.getPrice()));
        order.setStatus(OrderStatus.CANCELLED);
        when(orders.findById(9L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> service.requestReturn("cliente@estela.cl", 9L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Solo un pedido pagado");
    }
}
