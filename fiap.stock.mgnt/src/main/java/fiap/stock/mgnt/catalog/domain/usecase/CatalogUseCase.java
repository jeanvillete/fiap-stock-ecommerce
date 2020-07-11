package fiap.stock.mgnt.catalog.domain.usecase;

import fiap.stock.mgnt.catalog.domain.Catalog;
import fiap.stock.mgnt.catalog.domain.CatalogService;
import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import org.springframework.stereotype.Component;

@Component
public class CatalogUseCase {

    public static class CatalogPayload {
        Integer id;
        String description;

        public CatalogPayload() {
        }

        public CatalogPayload(Integer id, String description) {
            this.id = id;
            this.description = description;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    private final CatalogService catalogService;

    public CatalogUseCase(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public CatalogPayload insertNewCatalogItem(String loginId, CatalogPayload catalogPayload) throws InvalidSuppliedDataException {
        this.catalogService.validLoginId(loginId);

        this.catalogService.validDescription(catalogPayload.description);

        Catalog catalog = new Catalog(
                loginId,
                catalogPayload.getDescription()
        );

        this.catalogService.insert(catalog);

        return new CatalogPayload(
                catalog.getId(),
                catalog.getDescription()
        );
    }

}
