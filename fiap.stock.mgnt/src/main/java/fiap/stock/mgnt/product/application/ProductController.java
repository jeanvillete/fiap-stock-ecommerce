package fiap.stock.mgnt.product.application;

import fiap.stock.mgnt.catalog.domain.exception.CatalogNotFoundException;
import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import fiap.stock.mgnt.product.domain.usecase.ProductUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("stock/users/{loginId}/catalogs/{catalogId}/products")
public class ProductController {

    private final ProductUseCase productUseCase;

    public ProductController(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductUseCase.ProductPayload addProductToTheCatalog(
            @PathVariable("loginId") String loginId,
            @PathVariable("catalogId") Integer catalogId,
            @RequestBody ProductUseCase.ProductPayload productPayload)
            throws CatalogNotFoundException, InvalidSuppliedDataException {

        productPayload.setCatalogId(catalogId);

        return productUseCase.addProductToTheCatalog(loginId, productPayload);
    }

}
