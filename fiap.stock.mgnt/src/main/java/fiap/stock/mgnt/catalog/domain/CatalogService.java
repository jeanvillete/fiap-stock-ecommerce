package fiap.stock.mgnt.catalog.domain;

import fiap.stock.mgnt.catalog.domain.exception.CatalogConflictException;
import fiap.stock.mgnt.catalog.domain.exception.CatalogDeletionException;
import fiap.stock.mgnt.catalog.domain.exception.CatalogNotFoundException;
import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;

import java.util.List;

public interface CatalogService {

    void validLoginId(String loginId) throws InvalidSuppliedDataException;

    void validDescription(String description) throws InvalidSuppliedDataException;

    void persist(Catalog catalog);

    Catalog findById(Integer catalogId) throws CatalogNotFoundException;

    List<Catalog> findAll();

    void validCatalogId(Integer id) throws InvalidSuppliedDataException;

    void checkForConflictOnUpdate(Catalog catalog) throws CatalogConflictException;

    void checkForConflictOnInsert(Catalog catalog) throws CatalogConflictException;

    void ensureNoProductIsAssociated(Catalog catalog) throws CatalogDeletionException;

    void delete(Catalog catalog);

    Catalog findById(Catalog catalog) throws CatalogNotFoundException;

}
