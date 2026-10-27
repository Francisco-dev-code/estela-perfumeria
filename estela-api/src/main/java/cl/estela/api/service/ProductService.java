package cl.estela.api.service;

import cl.estela.api.domain.Product;
import cl.estela.api.dto.ProductRequest;
import cl.estela.api.repo.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository products;

    public ProductService(ProductRepository products) {
        this.products = products;
    }

    @Transactional(readOnly = true)
    public List<Product> search(String query) {
        String normalized = (query == null || query.isBlank()) ? null : query.trim();
        return products.search(normalized);
    }

    @Transactional(readOnly = true)
    public Product get(Long id) {
        Product product = products.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        if (!product.isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no disponible");
        }
        return product;
    }

    @Transactional
    public Product create(ProductRequest request) {
        Product product = new Product(request.name(), request.brand(), request.description(),
                request.price(), request.stock());
        return products.save(product);
    }

    @Transactional
    public Product update(Long id, ProductRequest request) {
        Product product = products.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        product.setName(request.name());
        product.setBrand(request.brand());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());
        return product;
    }

    @Transactional
    public void deactivate(Long id) {
        Product product = products.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        product.setActive(false);
    }
}
