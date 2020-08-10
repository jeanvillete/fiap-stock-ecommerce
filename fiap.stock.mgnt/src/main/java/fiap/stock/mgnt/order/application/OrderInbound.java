package fiap.stock.mgnt.order.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import fiap.stock.mgnt.order.domain.exception.OrderConflictException;
import fiap.stock.mgnt.order.domain.usecase.OrderUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Optional;

@Component
public class OrderInbound implements DeliverCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderInbound.class);
    private static final String EXCHANGE_REQUESTED_ORDER = "exchange.requested.order";
    private static final String QUEUE_REQUESTED_ORDER = "queue.requested.order";
    public static final String NAMELESS_ROUTING_KEY = "";

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
        rabbitMqChannel.exchangeDeclare(EXCHANGE_REQUESTED_ORDER, BuiltinExchangeType.FANOUT);
        rabbitMqChannel.queueDeclare(QUEUE_REQUESTED_ORDER, true, false, false, null);
        rabbitMqChannel.queueBind(QUEUE_REQUESTED_ORDER, EXCHANGE_REQUESTED_ORDER, NAMELESS_ROUTING_KEY);
        rabbitMqChannel.basicQos(1);

        rabbitMqChannel.basicConsume(QUEUE_REQUESTED_ORDER, false, this, consumerTag -> {});
    }

    @Override
    public void handle(String consumerTag, Delivery deliveredMessage) throws IOException {
        AMQP.BasicProperties properties = deliveredMessage.getProperties();

        String contentEncoding = Optional.ofNullable(properties)
                .map(AMQP.BasicProperties::getContentEncoding)
                .orElse("UTF-8");

        String loginId = Optional.ofNullable(properties)
                .map(AMQP.BasicProperties::getHeaders)
                .map(headers -> headers.get("loginId"))
                .map(Object::toString)
                .orElseThrow(() ->
                        new IllegalArgumentException("It was not found any loginId header on the received order message.")
                );

        String messageAsText = new String(deliveredMessage.getBody(), contentEncoding);

        LOGGER.debug("New order message just arrived [{}]", messageAsText);

        OrderUseCase.OrderPayload orderPayload = objectMapper.readValue(messageAsText, OrderUseCase.OrderPayload.class);

        try {
            orderUseCase.insertNewOrder(loginId, orderPayload);
        } catch (InvalidSuppliedDataException | OrderConflictException exception) {
            LOGGER.error("Checked exception raised on receiving a new order message;", exception);
        } finally {
            rabbitMqChannel.basicAck(deliveredMessage.getEnvelope().getDeliveryTag(), false);
        }
    }

}
