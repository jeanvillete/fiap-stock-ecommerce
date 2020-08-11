package fiap.stock.portal.order.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import fiap.stock.portal.common.exception.InvalidSuppliedDataException;
import fiap.stock.portal.order.domain.exception.OrderNotFoundException;
import fiap.stock.portal.order.domain.usecase.OrderUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Optional;

@Component
public class OrderInbound implements DeliverCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderInbound.class);
    private static final String EXCHANGE_UPDATED_ORDER = "exchange.updated.order";
    private static final String QUEUE_UPDATED_ORDER = "queue.updated.order";
    private static final String NAMELESS_ROUTING_KEY = "";

    private final OrderUseCase orderUseCase;
    private final Channel rabbitMqChannel;
    private final ObjectMapper objectMapper;

    public OrderInbound(OrderUseCase orderUseCase, Channel rabbitMqChannel, ObjectMapper objectMapper) {
        this.orderUseCase = orderUseCase;
        this.rabbitMqChannel = rabbitMqChannel;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void postConstruct() throws IOException {
        rabbitMqChannel.exchangeDeclare(EXCHANGE_UPDATED_ORDER, BuiltinExchangeType.FANOUT);
        rabbitMqChannel.queueDeclare(QUEUE_UPDATED_ORDER, true, false, false, null);
        rabbitMqChannel.queueBind(QUEUE_UPDATED_ORDER, EXCHANGE_UPDATED_ORDER, NAMELESS_ROUTING_KEY);
        rabbitMqChannel.basicQos(1);

        rabbitMqChannel.basicConsume(QUEUE_UPDATED_ORDER, false, this, consumerTag -> {});
    }

    @Override
    public void handle(String consumerTag, Delivery deliveredMessage) throws IOException {
        AMQP.BasicProperties properties = deliveredMessage.getProperties();

        String contentEncoding = Optional.ofNullable(properties)
                .map(AMQP.BasicProperties::getContentEncoding)
                .orElse("UTF-8");

        String orderCode = Optional.ofNullable(properties)
                .map(AMQP.BasicProperties::getHeaders)
                .map(headers -> headers.get("orderCode"))
                .map(Object::toString)
                .orElseThrow(() ->
                        new IllegalArgumentException("It was not found any orderCode header on the received order message.")
                );

        String loginId = Optional.ofNullable(properties)
                .map(AMQP.BasicProperties::getHeaders)
                .map(headers -> headers.get("loginId"))
                .map(Object::toString)
                .orElseThrow(() ->
                        new IllegalArgumentException("It was not found any loginId header on the received order message.")
                );

        String messageAsText = new String(deliveredMessage.getBody(), contentEncoding);

        LOGGER.debug("'Order status updated' message just arrived [{}]", messageAsText);

        OrderUseCase.OrderPayload orderPayload = objectMapper.readValue(messageAsText, OrderUseCase.OrderPayload.class);

        try {
            this.orderUseCase.updateClientOrderStatus(loginId, orderCode, orderPayload);
        } catch (OrderNotFoundException | InvalidSuppliedDataException exception) {
            LOGGER.error("Checked exception raised on receiving an 'order status updated' message;", exception);
        } finally {
            rabbitMqChannel.basicAck(deliveredMessage.getEnvelope().getDeliveryTag(), false);
        }
    }

}
