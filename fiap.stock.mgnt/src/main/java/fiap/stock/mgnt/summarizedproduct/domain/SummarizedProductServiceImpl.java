package fiap.stock.mgnt.summarizedproduct.domain;

import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import fiap.stock.mgnt.product.domain.Product;
import fiap.stock.mgnt.product.domain.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
class SummarizedProductServiceImpl implements SummarizedProductService {

    private final ProductService productService;
    private final String stockPortalBaseDomain;

    SummarizedProductServiceImpl(ProductService productService, @Value("${stock.portal.host}") String stockPortalBaseDomain) {
        this.productService = productService;
        this.stockPortalBaseDomain = stockPortalBaseDomain;
    }

    @Override
    public SummarizedProduct summarizeProduct(Product product) {
        if (Objects.isNull(product)) {
            throw new IllegalArgumentException("Product is a mandatory parameter for stock portal summarized a product instance.");
        }
        if (Objects.isNull(product.getQuantity()) || Objects.isNull(product.getCode()) || Objects.isNull(product.getCatalog()) || Objects.isNull(product.getPrice())) {
            throw new IllegalStateException("Products instance and its nested properties 'quantity', 'code', 'catalog' and 'price' cannot be neither null nor empty.");
        }

        Integer availableQuantity = productService.availableQuantity(product);

        return new SummarizedProduct(
                product.getCode(),
                product.getCatalog().getDescription(),
                product.getPrice(),
                availableQuantity
        );
    }

    @Override
    public void postToStockPortal(String loginId, SummarizedProduct summarizedProduct) throws InvalidSuppliedDataException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SummarizedProduct> httpEntity = new HttpEntity<>(summarizedProduct, headers);

        String url = this.stockPortalBaseDomain + "portal/users/" + loginId + "/products";
        String identification = restTemplate.postForObject(
                url,
                httpEntity,
                String.class
        );

        if (Objects.isNull(identification)) {
            throw new InvalidSuppliedDataException(
                    "No valid identifier resulted from posting summarized product to; " + url
            );
        }
    }

}
