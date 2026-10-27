package cl.estela.api.service;

import cl.estela.api.domain.Product;
import cl.estela.api.repo.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock ProductRepository products;
    @InjectMocks ProductService service;

    @Test
    void desactivarProductoLoMarcaInactivo() {
        Product product = new Product("Brisa", "Estela", "Citrico", new BigDecimal("30000"), 4);
        when(products.findById(2L)).thenReturn(Optional.of(product));

        service.deactivate(2L);

        assertThat(product.isActive()).isFalse();
    }

    @Test
    void buscarSinTextoUsaConsultaGeneral() {
        service.search("   ");
        org.mockito.Mockito.verify(products).search(null);
    }
}
