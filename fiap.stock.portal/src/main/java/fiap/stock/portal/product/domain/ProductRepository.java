package fiap.stock.portal.product.domain;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findByCode(String code);

}
