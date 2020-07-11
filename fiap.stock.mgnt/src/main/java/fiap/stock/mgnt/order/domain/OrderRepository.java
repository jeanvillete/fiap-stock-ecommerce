package fiap.stock.mgnt.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Integer> countByCode(String code);

    Optional<Order> findByCode(String code);

}
