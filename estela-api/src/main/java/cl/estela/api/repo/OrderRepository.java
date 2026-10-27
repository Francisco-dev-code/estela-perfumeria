package cl.estela.api.repo;

import cl.estela.api.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
        select distinct o
        from Order o
        join fetch o.user
        left join fetch o.items i
        left join fetch i.product
        where o.user.email = :email
        order by o.createdAt desc
        """)
    List<Order> findByCustomer(@Param("email") String email);

    @Query("""
        select distinct o
        from Order o
        join fetch o.user
        left join fetch o.items i
        left join fetch i.product
        order by o.createdAt desc
        """)
    List<Order> findAllWithItems();
}