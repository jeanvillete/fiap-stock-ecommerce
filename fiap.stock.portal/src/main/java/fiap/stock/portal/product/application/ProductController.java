package fiap.stock.portal.product.application;

import fiap.stock.portal.product.domain.exception.ProductNotFoundException;
import fiap.stock.portal.product.domain.usecase.ProductUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("portal/users/{loginId}/products")
public class ProductController {

    private final ProductUseCase productUseCase;

    public ProductController(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
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
