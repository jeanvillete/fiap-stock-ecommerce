package fiap.stock.mgnt.catalog.domain;

import fiap.stock.mgnt.catalog.domain.exception.CatalogNotFoundException;
import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
class CatalogServiceImpl implements CatalogService {

    private static final int MIN_DESCRIPTION_LENGTH = 10;
    private static final int MAX_DESCRIPTION_LENGTH = 50;

    private final CatalogRepository catalogRepository;

    CatalogServiceImpl(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
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
    public void insert(Catalog catalog) {
        catalogRepository.save(catalog);
    }

    @Override
    public Catalog findById(Integer catalogId) throws CatalogNotFoundException {
        return catalogRepository.findById(catalogId)
                .orElseThrow(() -> new CatalogNotFoundException("No catalog could be found for the provided id; " + catalogId));
    }

}
