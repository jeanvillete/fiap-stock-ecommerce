package fiap.stock.mgnt.catalog.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CatalogRepository extends JpaRepository<Catalog, Integer> {
}
