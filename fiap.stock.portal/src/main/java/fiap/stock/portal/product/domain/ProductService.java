package fiap.stock.portal.product.domain;

import fiap.stock.portal.common.exception.InvalidSuppliedDataException;
import fiap.stock.portal.product.domain.exception.ProductNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    void validLoginId(String loginId) throws InvalidSuppliedDataException;

    void validCode(String code) throws InvalidSuppliedDataException;

    void validDescription(String description) throws InvalidSuppliedDataException;

    void validPrice(BigDecimal price) throws InvalidSuppliedDataException;

    void validQuantity(Integer quantity) throws InvalidSuppliedDataException;

    Product findByCode(String code) throws ProductNotFoundException;

    void save(Product product);

    List<Product> findAll();

}
