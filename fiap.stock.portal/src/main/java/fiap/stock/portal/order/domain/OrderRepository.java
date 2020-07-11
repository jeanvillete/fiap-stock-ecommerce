package fiap.stock.portal.order.domain;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface OrderRepository extends MongoRepository<Order, String> {

    Optional<Order> findByCode(String code);

    Optional<List<Order>> findAllByLoginId(String loginId);

}
