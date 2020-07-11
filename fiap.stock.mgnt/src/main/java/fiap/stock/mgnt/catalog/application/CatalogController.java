package fiap.stock.mgnt.catalog.application;

import fiap.stock.mgnt.catalog.domain.usecase.CatalogUseCase;
import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("stock/users/{loginId}/catalogs")
public class CatalogController {

    private final CatalogUseCase catalogUseCase;

    public CatalogController(CatalogUseCase catalogUseCase) {
        this.catalogUseCase = catalogUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CatalogUseCase.CatalogPayload insertNewCatalogItem(@PathVariable("loginId") String loginId, @RequestBody CatalogUseCase.CatalogPayload catalogPayload) throws InvalidSuppliedDataException {
        return catalogUseCase.insertNewCatalogItem(loginId, catalogPayload);
    }

}
