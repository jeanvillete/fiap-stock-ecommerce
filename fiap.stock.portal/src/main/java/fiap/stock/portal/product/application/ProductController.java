package fiap.stock.portal.product.application;

import fiap.stock.portal.common.exception.InvalidSuppliedDataException;
import fiap.stock.portal.product.domain.exception.ProductNotFoundException;
import fiap.stock.portal.product.domain.usecase.ProductUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("portal/users/{loginId}/products")
public class ProductController {

    private final ProductUseCase productUseCase;

    public ProductController(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String insertNewProduct(@PathVariable("loginId") String loginId, @RequestBody ProductUseCase.ProductPayload productPayload) throws InvalidSuppliedDataException {
        return productUseCase.insertNewProduct(loginId, productPayload);
    }

    @GetMapping
    public List<ProductUseCase.ProductPayload> findAllProducts(@PathVariable("loginId") String loginId) {
        return productUseCase.findAllProducts(loginId);
    }

    @GetMapping("{productCode}")
    public ProductUseCase.ProductPayload findProductByCode(
            @PathVariable("loginId") String loginId,
            @PathVariable("productCode") String productCode) throws ProductNotFoundException {

        return productUseCase.findProductByCode(loginId, productCode);
    }

}
