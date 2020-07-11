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

@Component
public class ProductUseCase {

    public static class ProductPayload {

        @JsonProperty("entry_date")
        LocalDateTime entryTime;

        @JsonProperty("catalog_id")
        Integer catalogId;

        String code;
        BigDecimal price;
        Integer quantity;

        public ProductPayload() {
        }

        public ProductPayload(LocalDateTime entryTime, Integer catalogId, String code, BigDecimal price, Integer quantity) {
            this.entryTime = entryTime;
            this.catalogId = catalogId;
            this.code = code;
            this.price = price;
            this.quantity = quantity;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Integer getCatalogId() {
            return catalogId;
        }

        public void setCatalogId(Integer catalogId) {
            this.catalogId = catalogId;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public LocalDateTime getEntryTime() {
            return entryTime;
        }

        public void setEntryTime(LocalDateTime entryTime) {
            this.entryTime = entryTime;
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
        productService.validPrice(productPayload.getPrice());
        productService.validQuantity(productPayload.getQuantity());

        Catalog catalog = catalogService.findById(productPayload.getCatalogId());

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
        summarizedProductService.postToStockPortal(loginId, summarizedProduct);

        return new ProductPayload(
                product.getEntryTime(),
                product.getCatalog().getId(),
                product.getCode(),
                product.getPrice(),
                product.getQuantity()
        );
    }

}
