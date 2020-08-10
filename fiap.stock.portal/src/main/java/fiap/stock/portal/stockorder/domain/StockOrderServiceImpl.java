package fiap.stock.portal.stockorder.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import fiap.stock.portal.order.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
class StockOrderServiceImpl implements StockOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockOrderServiceImpl.class);
    private static final String EXCHANGE_REQUESTED_ORDER = "exchange.requested.order";

    private final Channel rabbitMqChannel;
    private final ObjectMapper objectMapper;

    StockOrderServiceImpl(Channel rabbitMqChannel, ObjectMapper objectMapper) {
        this.rabbitMqChannel = rabbitMqChannel;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void postConstruct() throws IOException {
        rabbitMqChannel.exchangeDeclare(EXCHANGE_REQUESTED_ORDER, BuiltinExchangeType.FANOUT);
    }

    @Override
    public void postOrderToStock(String loginId, Order order) {
        StockOrder stockOrder = new StockOrder(
                order.getCode(),
                order.getProducts()
        );

        try {
            String stockOrderAsString = objectMapper.writeValueAsString(stockOrder);

            LOGGER.debug("Serialized stock order json content [{}]", stockOrderAsString);

            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .contentEncoding(UTF_8.name())
                    .headers(
                            new HashMap<String, Object>(){{
                                put("loginId", loginId);
                            }}
                    )
                    .build();

            rabbitMqChannel.basicPublish(EXCHANGE_REQUESTED_ORDER, "", props, stockOrderAsString.getBytes(UTF_8));

            LOGGER.debug("Stock order published to message broker successfully; [{}]", stockOrderAsString);
        } catch (JsonProcessingException exception) {
            LOGGER.error("Exception raised while serializing stock order instance;", exception);
        } catch (IOException exception) {
            LOGGER.error("Exception raised while publishing stock order to message broker;", exception);
        }
    }

}
