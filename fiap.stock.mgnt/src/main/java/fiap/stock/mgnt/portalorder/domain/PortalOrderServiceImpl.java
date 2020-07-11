package fiap.stock.mgnt.portalorder.domain;

import fiap.stock.mgnt.order.domain.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
class PortalOrderServiceImpl implements PortalOrderService {

    private final String stockPortalBaseDomain;

    PortalOrderServiceImpl(@Value("${stock.portal.host}") String stockPortalBaseDomain) {
        this.stockPortalBaseDomain = stockPortalBaseDomain;
    }

    @Override
    public void postUpdatedOrderStatus(String loginId, Order order) {
        PortalOrder portalOrder = new PortalOrder(order.getStatus());

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PortalOrder> httpEntity = new HttpEntity<>(portalOrder, headers);

        String url = this.stockPortalBaseDomain + "portal/users/" + loginId + "/orders/" + order.getCode();
        restTemplate.put(url, httpEntity);
    }

}
