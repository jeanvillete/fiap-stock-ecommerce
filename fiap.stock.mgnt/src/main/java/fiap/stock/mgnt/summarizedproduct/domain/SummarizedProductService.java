package fiap.stock.mgnt.summarizedproduct.domain;

import fiap.stock.mgnt.product.domain.Product;

public interface SummarizedProductService {

    SummarizedProduct summarizeProduct(Product product);

    void postSummarizedProductToStockPortal(String loginId, SummarizedProduct summarizedProduct);

}
