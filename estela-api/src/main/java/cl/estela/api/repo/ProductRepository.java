package cl.estela.api.repo;

import cl.estela.api.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.active = true and " +
           "(:q is null or lower(p.name) like lower(concat('%', :q, '%')) " +
           "or lower(p.brand) like lower(concat('%', :q, '%'))) order by p.name")
    List<Product> search(@Param("q") String q);
}
