package fiap.stock.mgnt.product.domain;

import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import fiap.stock.mgnt.product.domain.exception.ProductNotFoundException;
import fiap.stock.mgnt.product.domain.exception.ProductUnavailableQuantityException;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    void validLoginId(String loginId) throws InvalidSuppliedDataException;

    void validPrice(BigDecimal price) throws InvalidSuppliedDataException;

    void validQuantity(Integer quantity) throws InvalidSuppliedDataException;

    void validCode(String code) throws InvalidSuppliedDataException;

    String figureOutNewProductCode();

    void save(Product product);

    Integer availableQuantity(Product product);

    void validProductList(List<Product> products) throws InvalidSuppliedDataException;

    void ensureAvailableQuantity(String code, Integer quantity) throws ProductNotFoundException, ProductUnavailableQuantityException;

    Product findByCode(String code) throws ProductNotFoundException;

}
