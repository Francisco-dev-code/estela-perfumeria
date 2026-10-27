package cl.estela.api.web;

import cl.estela.api.dto.OrderRequest;
import cl.estela.api.dto.OrderResponse;
import cl.estela.api.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody OrderRequest request, Principal principal) {
        return OrderResponse.from(service.create(principal.getName(), request.items()));
    }

    @GetMapping("/mine")
    public List<OrderResponse> mine(Principal principal) {
        return service.listMine(principal.getName()).stream().map(OrderResponse::from).toList();
    }

    @PostMapping("/{id}/cancel")
    public OrderResponse cancel(@PathVariable Long id, Principal principal) {
        return OrderResponse.from(service.cancel(principal.getName(), id));
    }

    @PostMapping("/{id}/return")
    public OrderResponse requestReturn(@PathVariable Long id, Principal principal) {
        return OrderResponse.from(service.requestReturn(principal.getName(), id));
    }
}
