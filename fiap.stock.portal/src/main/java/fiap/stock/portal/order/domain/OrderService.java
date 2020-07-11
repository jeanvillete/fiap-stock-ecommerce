package fiap.stock.portal.order.domain;

import fiap.stock.portal.address.domain.Address;
import fiap.stock.portal.common.exception.InvalidSuppliedDataException;
import fiap.stock.portal.order.domain.exception.OrderNotFoundException;
import fiap.stock.portal.product.domain.Product;

import java.util.List;

public interface OrderService {

    void validLoginId(String loginId) throws InvalidSuppliedDataException;

    void validCode(String code) throws InvalidSuppliedDataException;

    void validProductList(List<Product> products) throws InvalidSuppliedDataException;

    void validAddress(Address address) throws InvalidSuppliedDataException;

    String figureOutNewOrderCode();

    void save(Order order);

    void validOrderStatus(OrderStatus status) throws InvalidSuppliedDataException;

    Order findByCode(String code) throws OrderNotFoundException;

    void updateOrderStatus(String orderCode, OrderStatus status) throws OrderNotFoundException;

    List<Order> findAllByLoginId(String loginId);

}
