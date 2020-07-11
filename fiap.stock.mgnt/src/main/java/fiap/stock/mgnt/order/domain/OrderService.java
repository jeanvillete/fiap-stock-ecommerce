package fiap.stock.mgnt.order.domain;

import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import fiap.stock.mgnt.order.domain.exception.OrderConflictException;
import fiap.stock.mgnt.order.domain.exception.OrderIsNotWaitingForResponseException;
import fiap.stock.mgnt.order.domain.exception.OrderNotFoundException;
import fiap.stock.mgnt.product.domain.Product;

import java.util.List;

public interface OrderService {

    Integer sumApprovedOrdersForSpecificProduct(Product product);

    void validLoginId(String loginId) throws InvalidSuppliedDataException;

    void validCode(String code) throws InvalidSuppliedDataException;

    void checkForConflictOnInsert(String code) throws OrderConflictException;

    void save(Order order);

    Order findByCode(String code) throws OrderNotFoundException;

    List<Order> findAllOrders();

    void checkOrderIsWaitingForApproval(String orderCode) throws OrderIsNotWaitingForResponseException, OrderNotFoundException;

}
