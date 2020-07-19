package fiap.stock.mgnt.catalog.application;

import fiap.stock.mgnt.catalog.domain.exception.CatalogConflictException;
import fiap.stock.mgnt.catalog.domain.usecase.CatalogUseCase;
import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("stock/users/{loginId}/catalogs")
public class CatalogController {

    private final CatalogUseCase catalogUseCase;

    public CatalogController(CatalogUseCase catalogUseCase) {
        this.catalogUseCase = catalogUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CatalogUseCase.CatalogPayload insertNewCatalogItem(
            @PathVariable("loginId") String loginId,
            @RequestBody CatalogUseCase.CatalogPayload catalogPayload) throws InvalidSuppliedDataException, CatalogConflictException {

        return catalogUseCase.insertNewCatalogItem(loginId, catalogPayload);
    }

    @GetMapping
    public List<CatalogUseCase.CatalogPayload> catalogList(
            @PathVariable("loginId") String loginId) throws InvalidSuppliedDataException {

        return catalogUseCase.catalogList(loginId);
    }

    @PutMapping("{catalogId}")
    public CatalogUseCase.CatalogPayload updateCatalogItem(
            @PathVariable("loginId") String loginId,
            @PathVariable("catalogId") Integer catalogId,
            @RequestBody CatalogUseCase.CatalogPayload catalogPayload) throws CatalogConflictException, InvalidSuppliedDataException {

        catalogPayload.setId(catalogId);

        return catalogUseCase.updateCatalogItem(loginId, catalogPayload);
    }

}
