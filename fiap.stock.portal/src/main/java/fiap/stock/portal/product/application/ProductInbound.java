package fiap.stock.portal.product.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import fiap.stock.portal.common.exception.InvalidSuppliedDataException;
import fiap.stock.portal.product.domain.usecase.ProductUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Optional;

@Component
public class ProductInbound implements DeliverCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductInbound.class);
    private static final String EXCHANGE_UPDATED_PRODUCT = "exchange.updated.product";
    private static final String QUEUE_UPDATED_PRODUCT = "queue.updated.product";
    private static final String NAMELESS_ROUTING_KEY = "";

    private final ProductUseCase productUseCase;
    private final Channel rabbitMqChannel;
    private final ObjectMapper objectMapper;

    public ProductInbound(ProductUseCase productUseCase, Channel rabbitMqChannel, ObjectMapper objectMapper) {
        this.productUseCase = productUseCase;
        this.rabbitMqChannel = rabbitMqChannel;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void postConstruct() throws IOException {
        rabbitMqChannel.exchangeDeclare(EXCHANGE_UPDATED_PRODUCT, BuiltinExchangeType.FANOUT);
        rabbitMqChannel.queueDeclare(QUEUE_UPDATED_PRODUCT, true, false, false, null);
        rabbitMqChannel.queueBind(QUEUE_UPDATED_PRODUCT, EXCHANGE_UPDATED_PRODUCT, NAMELESS_ROUTING_KEY);
        rabbitMqChannel.basicQos(1);

        rabbitMqChannel.basicConsume(QUEUE_UPDATED_PRODUCT, false, this, consumerTag -> {});
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

        LOGGER.debug("Product updated message just arrived [{}]", messageAsText);

        ProductUseCase.ProductPayload productPayload = objectMapper.readValue(messageAsText, ProductUseCase.ProductPayload.class);

        try {
            productUseCase.insertNewProduct(loginId, productPayload);
        } catch (InvalidSuppliedDataException exception) {
            LOGGER.error("Checked exception raised on receiving a product updated message;", exception);
        } finally {
            rabbitMqChannel.basicAck(deliveredMessage.getEnvelope().getDeliveryTag(), false);
        }
    }
}
