package fiap.stock.mgnt.summarizedproduct.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import fiap.stock.mgnt.common.exception.InvalidSuppliedDataException;
import fiap.stock.mgnt.product.domain.Product;
import fiap.stock.mgnt.product.domain.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
class SummarizedProductServiceImpl implements SummarizedProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SummarizedProductServiceImpl.class);
    private static final String EXCHANGE_UPDATED_PRODUCT = "exchange.updated.product";
    private static final String NAMELESS_ROUTING_KEY = "";

    private final ProductService productService;
    private final Channel rabbitMqChannel;
    private final ObjectMapper objectMapper;

    SummarizedProductServiceImpl(ProductService productService, Channel rabbitMqChannel, ObjectMapper objectMapper) {
        this.productService = productService;
        this.rabbitMqChannel = rabbitMqChannel;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void postConstruct() throws IOException {
        rabbitMqChannel.exchangeDeclare(EXCHANGE_UPDATED_PRODUCT, BuiltinExchangeType.FANOUT);
    }

    @Override
    public SummarizedProduct summarizeProduct(Product product) {
        if (Objects.isNull(product)) {
            throw new IllegalArgumentException("Product is a mandatory parameter for stock portal summarized a product instance.");
        }
        if (Objects.isNull(product.getQuantity()) || Objects.isNull(product.getCode()) || Objects.isNull(product.getCatalog()) || Objects.isNull(product.getPrice())) {
            throw new IllegalStateException("Products instance and its nested properties 'quantity', 'code', 'catalog' and 'price' cannot be neither null nor empty.");
        }

        Integer availableQuantity = productService.availableQuantity(product);

        return new SummarizedProduct(
                product.getCode(),
                product.getCatalog().getDescription(),
                product.getPrice(),
                availableQuantity
        );
    }

    @Override
    public void postSummarizedProductToStockPortal(String loginId, SummarizedProduct summarizedProduct) throws InvalidSuppliedDataException {
        try {
            String summarizedProductAsString = objectMapper.writeValueAsString(summarizedProduct);

            LOGGER.debug("Serialized summarized product json content [{}]", summarizedProductAsString);

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

            rabbitMqChannel.basicPublish(EXCHANGE_UPDATED_PRODUCT, NAMELESS_ROUTING_KEY, props, summarizedProductAsString.getBytes(UTF_8));

            LOGGER.debug("Summarized product published to message broker successfully; [{}]", summarizedProductAsString);
        } catch (JsonProcessingException exception) {
            LOGGER.error("Exception raised while serializing summarized product instance;", exception);
        } catch (IOException exception) {
            LOGGER.error("Exception raised while publishing summarized product to message broker;", exception);
        }
    }

}
