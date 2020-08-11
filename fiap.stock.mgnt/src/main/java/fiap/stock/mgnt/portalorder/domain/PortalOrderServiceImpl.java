package fiap.stock.mgnt.portalorder.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import fiap.stock.mgnt.order.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
class PortalOrderServiceImpl implements PortalOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PortalOrderServiceImpl.class);
    private static final String EXCHANGE_UPDATED_ORDER = "exchange.updated.order";
    private static final String NAMELESS_ROUTING_KEY = "";

    private final Channel rabbitMqChannel;
    private final ObjectMapper objectMapper;

    PortalOrderServiceImpl(Channel rabbitMqChannel, ObjectMapper objectMapper) {
        this.rabbitMqChannel = rabbitMqChannel;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void postConstruct() throws IOException {
        rabbitMqChannel.exchangeDeclare(EXCHANGE_UPDATED_ORDER, BuiltinExchangeType.FANOUT);
    }

    @Override
    public void postUpdatedOrderStatus(String loginId, Order order) {
        try {
            PortalOrder portalOrder = new PortalOrder(order.getStatus());

            String portalOrderAsString = objectMapper.writeValueAsString(portalOrder);

            LOGGER.debug("Serialized portal order json content [{}]", portalOrderAsString);

            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .contentEncoding(UTF_8.name())
                    .headers(
                            new HashMap<String, Object>(){{
                                put("loginId", loginId);
                                put("orderCode", order.getCode());
                            }}
                    )
                    .build();

            rabbitMqChannel.basicPublish(EXCHANGE_UPDATED_ORDER, NAMELESS_ROUTING_KEY, props, portalOrderAsString.getBytes(UTF_8));

            LOGGER.debug("Portal order published to message broker successfully; [{}]", portalOrderAsString);
        } catch (JsonProcessingException exception) {
            LOGGER.error("Exception raised while serializing portal order instance;", exception);
        } catch (IOException exception) {
            LOGGER.error("Exception raised while publishing portal order to message broker;", exception);
        }
    }

}
