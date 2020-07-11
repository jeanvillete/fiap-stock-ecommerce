package fiap.stock.mgnt.summarizedproduct.domain;

import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import fiap.stock.mgnt.product.domain.Product;

public interface SummarizedProductService {

    SummarizedProduct summarizeProduct(Product product);

    void postToStockPortal(String loginId, SummarizedProduct summarizedProduct) throws InvalidSuppliedDataException;

}
