package fiap.stock.mgnt.catalog.domain;

import fiap.stock.mgnt.catalog.domain.exception.CatalogConflictException;
import fiap.stock.mgnt.catalog.domain.exception.CatalogDeletionException;
import fiap.stock.mgnt.catalog.domain.exception.CatalogNotFoundException;
import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import fiap.stock.mgnt.product.domain.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
class CatalogServiceImpl implements CatalogService {

    private static final int MIN_DESCRIPTION_LENGTH = 10;
    private static final int MAX_DESCRIPTION_LENGTH = 50;

    private final CatalogRepository catalogRepository;
    private final ProductService productService;

    CatalogServiceImpl(CatalogRepository catalogRepository, ProductService productService) {
        this.catalogRepository = catalogRepository;
        this.productService = productService;
    }

    @Override
    public void validLoginId(String loginId) throws InvalidSuppliedDataException {
        if (Objects.isNull(loginId)) {
            throw new InvalidSuppliedDataException("LoginId is mandatory on path variable.");
        }

        loginId = loginId.trim();

        if (loginId.isEmpty()) {
            throw new InvalidSuppliedDataException("LoginId is mandatory on path variable.");
        }
    }

    @Override
    public void validDescription(String description) throws InvalidSuppliedDataException {
        if (Objects.isNull(description)) {
            throw new InvalidSuppliedDataException("Argument 'description' is mandatory.");
        }

        description = description.trim();

        if (description.isEmpty()) {
            throw new InvalidSuppliedDataException("Argument 'description' is mandatory.");
        }

        if (description.length() < MIN_DESCRIPTION_LENGTH || description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new InvalidSuppliedDataException(
                    "Argument 'description' must be greater than [" + MIN_DESCRIPTION_LENGTH + "] and less than" +
                            " [" + MAX_DESCRIPTION_LENGTH + "] characters."
            );
        }
    }

    @Override
    public void persist(Catalog catalog) {
        catalogRepository.save(catalog);
    }

    @Override
    public Catalog findById(Integer catalogId) throws CatalogNotFoundException {
        return catalogRepository.findById(catalogId)
                .orElseThrow(() -> new CatalogNotFoundException("No catalog could be found for the provided id; " + catalogId));
    }

    @Override
    public List<Catalog> findAll() {
        return catalogRepository.findAll();
    }

    @Override
    public void validCatalogId(Integer id) throws InvalidSuppliedDataException {
        if (Objects.isNull(id)) {
            throw new InvalidSuppliedDataException("Nested argument catalog 'id' is mandatory.");
        }

        boolean idGreaterThanZero = id > 0;

        if (!idGreaterThanZero) {
            throw new InvalidSuppliedDataException("Nested argument catalog 'id' must be greater than zero.");
        }
    }

    @Override
    public void checkForConflictOnUpdate(Catalog catalog) throws CatalogConflictException {
        Integer countCatalogsByDescriptionExcludingCurrentId = this.catalogRepository.countByDescriptionAndIdNot(
                catalog.getDescription(),
                catalog.getId()
        )
        .orElse(0);

        if (countCatalogsByDescriptionExcludingCurrentId > 0) {
            throw new CatalogConflictException(
                    "Impossible apply update, there's already a catalog item record with this description."
            );
        }
    }

    @Override
    public void checkForConflictOnInsert(Catalog catalog) throws CatalogConflictException {
        Integer countCatalogsByDescription = this.catalogRepository.countByDescription(
                catalog.getDescription()
        )
        .orElse(0);

        if (countCatalogsByDescription > 0) {
            throw new CatalogConflictException(
                    "Impossible insert, there's already a catalog item record with this description."
            );
        }
    }

    @Override
    public void ensureNoProductIsAssociated(Catalog catalog) throws CatalogDeletionException {
        Integer countProductsByCatalogId = this.productService.countByCatalogId(catalog)
                .orElse(0);

        if (countProductsByCatalogId > 0) {
            throw new CatalogDeletionException(
                    "Impossible apply delete for this catalog, cause it has product records associated with it."
            );
        }
    }

    @Override
    public void delete(Catalog catalog) {
        catalogRepository.deleteById(catalog.getId());
    }

    @Override
    public Catalog findById(Catalog catalog) throws CatalogNotFoundException {
        Integer catalogId = catalog.getId();
        return this.catalogRepository.findById(catalogId)
                .orElseThrow(() ->
                        new CatalogNotFoundException("No catalog record could be found for the provided id; " + catalogId)
                );
    }

}
