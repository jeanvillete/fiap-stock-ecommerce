package fiap.stock.mgnt.catalog.domain.usecase;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiap.stock.mgnt.catalog.domain.Catalog;
import fiap.stock.mgnt.catalog.domain.CatalogService;
import fiap.stock.mgnt.catalog.domain.exception.CatalogConflictException;
import fiap.stock.mgnt.catalog.domain.exception.CatalogDeletionException;
import fiap.stock.mgnt.catalog.domain.exception.CatalogNotFoundException;
import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CatalogUseCase {

    public static class CatalogPayload {
        @JsonProperty
        Integer id;

        @JsonProperty
        String description;

        public CatalogPayload() {
        }

        public CatalogPayload(Integer id, String description) {
            this.id = id;
            this.description = description;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

    private final CatalogService catalogService;

    public CatalogUseCase(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public CatalogPayload insertNewCatalogItem(String loginId, CatalogPayload catalogPayload) throws InvalidSuppliedDataException, CatalogConflictException {
        this.catalogService.validLoginId(loginId);

        this.catalogService.validDescription(catalogPayload.description);

        Catalog catalog = new Catalog(
                loginId,
                catalogPayload.description
        );

        this.catalogService.checkForConflictOnInsert(catalog);

        this.catalogService.persist(catalog);

        return getCatalogPayload(catalog);
    }

    public List<CatalogPayload> catalogList(String loginId) throws InvalidSuppliedDataException {
        this.catalogService.validLoginId(loginId);

        List<Catalog> catalogList = this.catalogService.findAll();

        return catalogList.stream()
                .map(this::getCatalogPayload)
                .collect(Collectors.toList());
    }

    public CatalogPayload updateCatalogItem(String loginId, CatalogPayload catalogPayload) throws InvalidSuppliedDataException, CatalogConflictException, CatalogNotFoundException {
        this.catalogService.validLoginId(loginId);

        this.catalogService.validCatalogId(catalogPayload.id);

        this.catalogService.validDescription(catalogPayload.description);

        Catalog detachedCatalog = new Catalog(catalogPayload.id);

        Catalog attachedCatalog = this.catalogService.findById(detachedCatalog);
        attachedCatalog.setDescription(catalogPayload.description);

        this.catalogService.checkForConflictOnUpdate(attachedCatalog);

        this.catalogService.persist(attachedCatalog);

        return getCatalogPayload(attachedCatalog);
    }

    public void deleteCatalogItem(String loginId, Integer catalogId) throws InvalidSuppliedDataException, CatalogDeletionException, CatalogNotFoundException {
        this.catalogService.validLoginId(loginId);

        this.catalogService.validCatalogId(catalogId);

        Catalog detachedCatalog = new Catalog(catalogId);

        Catalog attachedCatalog = this.catalogService.findById(detachedCatalog);

        this.catalogService.ensureNoProductIsAssociated(attachedCatalog);

        this.catalogService.delete(attachedCatalog);
    }

    private CatalogPayload getCatalogPayload(Catalog catalog) {
        return new CatalogPayload(
                catalog.getId(),
                catalog.getDescription()
        );
    }
}
