package fiap.stock.mgnt.portalorder.domain;

import fiap.stock.mgnt.order.domain.OrderStatus;

public class PortalOrder {

    private OrderStatus status;

    public PortalOrder(OrderStatus status) {
        this.status = status;
    }

    public OrderStatus getStatus() {
        return status;
    }

}
