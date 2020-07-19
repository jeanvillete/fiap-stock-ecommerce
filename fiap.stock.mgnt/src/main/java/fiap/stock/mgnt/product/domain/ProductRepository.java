package fiap.stock.mgnt.product.domain;

import fiap.stock.mgnt.catalog.domain.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByCode(String code);

    Optional<Integer> countByCatalog(Catalog catalog);

}
