package fiap.stock.mgnt.portalorder.domain;

import fiap.stock.mgnt.order.domain.Order;

public interface PortalOrderService {

    void postUpdatedOrderStatus(String loginId, Order order);

}
