package cl.estela.api.config;

import cl.estela.api.domain.AppUser;
import cl.estela.api.domain.Product;
import cl.estela.api.domain.Role;
import cl.estela.api.repo.AppUserRepository;
import cl.estela.api.repo.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seed(AppUserRepository users, ProductRepository products, PasswordEncoder encoder) {
        return args -> {
            if (!users.existsByEmail("admin@estela.cl")) {
                users.save(new AppUser("admin@estela.cl", encoder.encode("Admin123!"),
                        "Administradora Estela", Role.ADMIN));
            }
            if (!users.existsByEmail("cliente@estela.cl")) {
                users.save(new AppUser("cliente@estela.cl", encoder.encode("Cliente123!"),
                        "Cliente de Prueba", Role.CLIENT));
            }
            if (products.count() == 0) {
                products.saveAll(List.of(
                    new Product("Ambar Nocturno", "Estela", "Notas de ambar, vainilla y madera.", new BigDecimal("49990"), 12),
                    new Product("Brisa de Limon", "Estela", "Citrico fresco para uso diario.", new BigDecimal("32990"), 20),
                    new Product("Jardin Secreto", "Aurea", "Floral blanco con fondo de musgo.", new BigDecimal("45990"), 8),
                    new Product("Cuero y Sal", "Aurea", "Cuero suave con sal marina.", new BigDecimal("58990"), 5),
                    new Product("Vetiver Sereno", "Nordica", "Vetiver, bergamota y cedro.", new BigDecimal("39990"), 15)
                ));
            }
        };
    }
}
