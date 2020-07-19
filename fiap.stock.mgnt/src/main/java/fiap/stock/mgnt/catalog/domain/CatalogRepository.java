package fiap.stock.mgnt.catalog.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface CatalogRepository extends JpaRepository<Catalog, Integer> {

    Optional<Integer> countByDescriptionAndIdNot(String description, Integer id);

    Optional<Integer> countByDescription(String description);

}
