package fiap.stock.portal.stockorder.domain;

import fiap.stock.portal.order.domain.Order;

public interface StockOrderService {

    void postOrderToStock(String loginId, Order order);

}
