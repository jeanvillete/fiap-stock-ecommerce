package fiap.stock.portal.product.domain.usecase;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiap.stock.portal.common.exception.InvalidSuppliedDataException;
import fiap.stock.portal.product.domain.Product;
import fiap.stock.portal.product.domain.ProductService;
import fiap.stock.portal.product.domain.exception.ProductNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductUseCase {

    public static class ProductPayload {
        @JsonProperty
        String code;

        @JsonProperty
        String description;

        @JsonProperty
        BigDecimal price;

        @JsonProperty
        Integer quantity;

        public ProductPayload() {
        }

        public ProductPayload(String code, String description, BigDecimal price) {
            this.code = code;
            this.description = description;
            this.price = price;
        }

        public ProductPayload(String code, String description, BigDecimal price, Integer quantity) {
            this.code = code;
            this.description = description;
            this.price = price;
            this.quantity = quantity;
        }
    }

    private final ProductService productService;

    public ProductUseCase(ProductService productService) {
        this.productService = productService;
    }

    public String insertNewProduct(String loginId, ProductPayload productPayload) throws InvalidSuppliedDataException {
        productService.validLoginId(loginId);

        productService.validCode(productPayload.code);
        productService.validDescription(productPayload.description);
        productService.validPrice(productPayload.price);
        productService.validQuantity(productPayload.quantity);

        Product product = null;
        try {
            product = productService.findByCode(productPayload.code);
        } catch (ProductNotFoundException e) {
            product = new Product();
        }

        product.setLoginId(loginId);
        product.setCode(productPayload.code);
        product.setDescription(productPayload.description);
        product.setPrice(productPayload.price);
        product.setQuantity(productPayload.quantity);

        productService.save(product);

        return product.getId();
    }

    public List<ProductPayload> findAllProducts(String loginId) {
        List<Product> productList = productService.findAll();

        return productList.stream()
                .map(product -> new ProductPayload(
                        product.getCode(),
                        product.getDescription(),
                        product.getPrice()
                ))
                .collect(Collectors.toList());
    }

    public ProductPayload findProductByCode(String loginId, String productCode) throws ProductNotFoundException {
        Product product = productService.findByCode(productCode);

        return new ProductPayload(
                product.getCode(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity()
        );
    }

}
