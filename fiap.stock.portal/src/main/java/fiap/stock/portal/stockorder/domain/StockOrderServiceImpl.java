package fiap.stock.portal.stockorder.domain;

import fiap.stock.portal.common.exception.InvalidSuppliedDataException;
import fiap.stock.portal.order.domain.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
class StockOrderServiceImpl implements StockOrderService {

    private final String stockMgntBaseDomain;

    StockOrderServiceImpl(@Value("${stock.mgnt.host}") String stockMgntBaseDomain) {
        this.stockMgntBaseDomain = stockMgntBaseDomain;
    }

    @Override
    public void postOrderToStock(String loginId, Order order) throws InvalidSuppliedDataException {
        StockOrder stockOrder = new StockOrder(
                order.getCode(),
                order.getProducts()
        );

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<StockOrder> httpEntity = new HttpEntity<>(stockOrder, headers);

        String url = this.stockMgntBaseDomain + "stock/users/" + loginId + "/orders";
        String response = restTemplate.postForObject(
                url,
                httpEntity,
                String.class
        );

        if (Objects.isNull(response)) {
            throw new InvalidSuppliedDataException(
                    "No valid response resulted from posting order to; " + url
            );
        }
    }

}
