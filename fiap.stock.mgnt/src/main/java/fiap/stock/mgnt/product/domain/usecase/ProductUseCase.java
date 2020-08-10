package fiap.stock.mgnt.product.domain.usecase;

import com.fasterxml.jackson.annotation.JsonProperty;
import fiap.stock.mgnt.catalog.domain.Catalog;
import fiap.stock.mgnt.catalog.domain.CatalogService;
import fiap.stock.mgnt.catalog.domain.exception.CatalogNotFoundException;
import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import fiap.stock.mgnt.product.domain.Product;
import fiap.stock.mgnt.product.domain.ProductService;
import fiap.stock.mgnt.summarizedproduct.domain.SummarizedProduct;
import fiap.stock.mgnt.summarizedproduct.domain.SummarizedProductService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductUseCase {

    public static class ProductPayload {

        @JsonProperty("entry_date")
        LocalDateTime entryTime;

        @JsonProperty("catalog_id")
        Integer catalogId;

        @JsonProperty
        String code;

        @JsonProperty
        BigDecimal price;

        @JsonProperty
        Integer quantity;

        @JsonProperty
        String description;

        public ProductPayload() {
        }

        public ProductPayload(LocalDateTime entryTime, Integer catalogId, String code, BigDecimal price, Integer quantity) {
            this.entryTime = entryTime;
            this.catalogId = catalogId;
            this.code = code;
            this.price = price;
            this.quantity = quantity;
        }

        public ProductPayload(LocalDateTime entryTime, Integer catalogId, String description, String code, BigDecimal price, Integer quantity) {
            this.entryTime = entryTime;
            this.catalogId = catalogId;
            this.description = description;
            this.code = code;
            this.price = price;
            this.quantity = quantity;
        }

        public void setCatalogId(Integer catalogId) {
            this.catalogId = catalogId;
        }
    }

    private final ProductService productService;
    private final CatalogService catalogService;
    private final SummarizedProductService summarizedProductService;

    public ProductUseCase(ProductService productService, CatalogService catalogService, SummarizedProductService summarizedProductService) {
        this.productService = productService;
        this.catalogService = catalogService;
        this.summarizedProductService = summarizedProductService;
    }

    public ProductPayload addProductToTheCatalog(String loginId, ProductPayload productPayload) throws InvalidSuppliedDataException, CatalogNotFoundException {
        productService.validLoginId(loginId);
        productService.validPrice(productPayload.price);
        productService.validQuantity(productPayload.quantity);

        Catalog catalog = catalogService.findById(productPayload.catalogId);

        String productCode = productService.figureOutNewProductCode();

        Product product = new Product(
                loginId,
                catalog,
                productCode,
                productPayload.price,
                productPayload.quantity,
                LocalDateTime.now()
        );

        productService.save(product);

        SummarizedProduct summarizedProduct = summarizedProductService.summarizeProduct(product);
        summarizedProductService.postSummarizedProductToStockPortal(loginId, summarizedProduct);

        return new ProductPayload(
                product.getEntryTime(),
                product.getCatalog().getId(),
                product.getCode(),
                product.getPrice(),
                product.getQuantity()
        );
    }

    public List<ProductPayload> findAllProducts(String loginId) throws InvalidSuppliedDataException {
        productService.validLoginId(loginId);

        List<Product> productList = productService.findAll();

        return productList.stream()
                .map(product ->
                        new ProductPayload(
                                product.getEntryTime(),
                                product.getCatalog().getId(),
                                product.getCatalog().getDescription(),
                                product.getCode(),
                                product.getPrice(),
                                product.getQuantity()
                        )
                )
                .collect(Collectors.toList());
    }

}
